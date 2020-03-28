package oceanbox.view;

import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import oceanbox.Recup_video;
import oceanbox.propreties.ClientPropreties;

/**
 * Cette classe contient la vidéo qui sera affichée à l'écran
 */
public class Contenu extends BorderPane {

	private MediaPlayer video;

	public Contenu(Stage stage, String fileName) {

		this.video = new MediaPlayer(new Recup_video(fileName).getVideo());

		video.setOnReady(() -> {
			video.stop();
			video.setStartTime(repereForDiffusion());
			video.play();
		});

		Timeline diffusion = timelineForDiffusion();

		MediaView mediaView = new MediaView(video);
		mediaView.setFitWidth(stage.getWidth());
		mediaView.setFitHeight(stage.getHeight());

		this.getChildren().add(mediaView);

		diffusion.play();
	}

	/**
	 * Cette méthode permet d'initialiser le temps auquel débute la vidéo en
	 * fonction de l'heure de réveil définie dans les propriétés du client
	 * 
	 * @return la temps relatif en secondes auquel doit débuter la vidéo
	 */
	public Duration repereForDiffusion() {

		int timeVideo = (int) video.getMedia().getDuration().toSeconds();

		int currently = (LocalDateTime.now().getHour() * 3600) + (LocalDateTime.now().getMinute() * 60)
				+ LocalDateTime.now().getSecond();

		String[] times = ClientPropreties.getPropertie("heureDeReveil").split(":");

		int base = (Integer.parseInt(times[0]) * 3600) + (Integer.parseInt(times[1]) * 60) + Integer.parseInt(times[2]);

		return Duration.seconds((currently - base) % timeVideo);
	}

	/**
	 * Cette méthode permet de spécifier les paramètres de la timeline dans laquelle
	 * est jouée la vidéo
	 * 
	 * @return la timeline qui diffuse la vidéo
	 */
	public Timeline timelineForDiffusion() {

		KeyFrame update = new KeyFrame(Duration.seconds(0.5), event -> {
			video.setAutoPlay(true);
			video.setOnEndOfMedia(() -> {
				video.setStartTime(Duration.ZERO);
				video.seek(Duration.ZERO);
				video.play();
			});
		});

		Timeline diffusion = new Timeline(update);

		return diffusion;
	}
}
