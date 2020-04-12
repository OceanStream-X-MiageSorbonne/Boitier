package oceanbox.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe contient les vidéos qui seront affichées à l'écran
 */
public class Contenu extends BorderPane {

	private List<Media> videos = new ArrayList<Media>();
	private Iterator<Media> it;
	private int[] durationOfVideo = new int[Integer.valueOf(SystemPropreties.getPropertie("nbPaquets"))];
	private int totalDurationOfVideo = 0;
	private MediaView mediaView = new MediaView();

	public Contenu(int secondsForTest) {
		// Ceci est un constructeur qui n'est utile que pour les tests unitaires
		this.totalDurationOfVideo = secondsForTest;
	}

	public Contenu(Stage stage) {

		initVideos();

		mediaView.setFitWidth(stage.getWidth());
		mediaView.setFitHeight(stage.getHeight());

		this.getChildren().add(mediaView);
	}

	/**
	 * Cette méthode permet de récupérer les différents paquets dans le répertoire
	 * local et de planifier la lecture des videos les unes après les autres
	 */
	public void initVideos() {

		videos = new ArrayList<Media>();
		totalDurationOfVideo = 0;

		for (int i = 1; i <= durationOfVideo.length; i++) {
			String regex = i + ".mp4";
			Media video = new Recup_video(regex).getVideo();

			if (i != durationOfVideo.length) {
				MediaPlayer player = new MediaPlayer(video);
				int[] pos = { i - 1 };
				player.setOnReady(() -> {
					durationOfVideo[pos[0]] = (int) player.getMedia().getDuration().toSeconds();
					totalDurationOfVideo += durationOfVideo[pos[0]];
				});
			}

			videos.add(video);
		}

		MediaPlayer player = new MediaPlayer(videos.get(videos.size() - 1));
		player.setOnReady(() -> {
			durationOfVideo[durationOfVideo.length - 1] = (int) player.getMedia().getDuration().toSeconds();
			totalDurationOfVideo += durationOfVideo[durationOfVideo.length - 1];

			timelineForDiffusion().play();
		});
	}

	/**
	 * Cette méthode permet d'initialiser le temps auquel débute la vidéo en
	 * fonction de l'heure de réveil définie dans les propriétés du client
	 * 
	 * @return la temps relatif en secondes auquel doit débuter la diffusion
	 */
	public int repereForDiffusion() {

		int currently = (LocalDateTime.now().getHour() * 3600) + (LocalDateTime.now().getMinute() * 60)
				+ LocalDateTime.now().getSecond();

		String[] times = ClientPropreties.getPropertie("heureDeReveil").split(":");

		int base = (Integer.parseInt(times[0]) * 3600) + (Integer.parseInt(times[1]) * 60) + Integer.parseInt(times[2]);

		return (currently - base) % totalDurationOfVideo;
	}

	/**
	 * Cette méthode permet de spécifier les paramètres de la timeline dans laquelle
	 * sont jouées les vidéos
	 * 
	 * @return la timeline qui diffuse les vidéos
	 */
	public Timeline timelineForDiffusion() {

		it = videos.iterator();

		int[] start = { repereForDiffusion() };

		for (int timeVideo : durationOfVideo) {
			while (start[0] > timeVideo) {
				start[0] -= timeVideo;
				it.next();
			}
		}

		KeyFrame updates = new KeyFrame(Duration.seconds(0.5), event -> {
			customPlay(it.next(), start[0]);
		});

		return new Timeline(updates);
	}

	/**
	 * Cette méthode gère la lecture des vidéos les unes après les autres
	 * 
	 * @param nextVideo
	 */
	private void customPlay(Media nextVideo, int begin) {
		Media media = nextVideo;
		MediaPlayer player = new MediaPlayer(media);
		player.setStartTime((begin < 0) ? Duration.ZERO : Duration.seconds(begin));
		mediaView.setMediaPlayer(player);
		player.play();
		player.setOnEndOfMedia(() -> {
			player.stop();
			player.setStartTime(Duration.ZERO);
			if (it.hasNext())
				customPlay(it.next(), -1);
			else
				initVideos();
		});
	}
}
