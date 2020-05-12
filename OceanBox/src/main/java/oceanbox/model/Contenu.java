package oceanbox.model;

import java.time.LocalDateTime;

import java.util.Iterator;
import java.util.Map;

import oceanbox.propreties.ClientPropreties;
import oceanbox.veille.Veille;
import oceanbox.veille.VeilleScanner;
import oceanbox.videoplayer.JVlcPlayer;
import oceanbox.videoplayer.Video;
import oceanbox.videoplayer.VideoPlayer;
import oceanbox.videoplayer.VideosInfos;

/**
 * Cette classe contient les vidéos qui seront affichées à l'écran
 */
public class Contenu {

	private Map<Integer, Video> videosInfos;
	private Iterator<Integer> timelineIterator;
	private int totalDurationOfVideo;
	private Video videoPlaying;
	private VideoPlayer videoPlayer;
	private Process processPlayer;
	private int start;
	private Veille veille;

	public Contenu() {
		veille = new VeilleScanner(this);
		videoPlayer = new JVlcPlayer();
		totalDurationOfVideo = 0;
		start = -1;
	}

	/**
	 * Cette méthode permet de récupérer les différents paquets dans le répertoire
	 * local et de planifier la lecture des videos les unes après les autres
	 */
	public void initVideos() {

		videosInfos = new VideosInfos().getVideosInfos();

		for (int i : videosInfos.keySet()) {
			totalDurationOfVideo += videosInfos.get(i).getDuration();
		}
		
		initDiffusion();
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
	// TODO il faut peut-être changer le type de la méthode, void a été mis au
	// hasard
	public void initDiffusion() {

		timelineIterator = videosInfos.keySet().iterator();

		if (start < 0)
			start = repereForDiffusion();
		else
			start = 0;

		int timeVideo;
		int startIndice = 1;
		while (timelineIterator.hasNext()) {
			startIndice = timelineIterator.next();
			timeVideo = videosInfos.get(startIndice).getDuration();
			if (start >= timeVideo) {
				start -= timeVideo;
			} else {
				break;
			}
		}


		customPlay(videosInfos.get(startIndice), start);
	}

	/**
	 * Cette méthode gère la lecture des vidéos les unes après les autres
	 * 
	 * @param nextVideo
	 */
	private void customPlay(Video nextVideo, int begin) {

		videoPlaying = nextVideo;

		processPlayer = videoPlayer.play(nextVideo.getPath(), begin);
		try {
			processPlayer.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (!veille.getSleepMode()){
			if (timelineIterator.hasNext())
				customPlay(videosInfos.get(timelineIterator.next()), 0);
			else
				initVideos();
		} else {
			while(veille.getSleepMode()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			start = -1;
			initVideos();
		}

	}

	public void stopDiffusion() {
		start = -1;
		videoPlayer.stopPlayerProcess();
	}

	public int getTotalDurationOfVideo() {
		return totalDurationOfVideo;
	}

	public Video getVideoPlaying() {
		return videoPlaying;
	}
	
}
