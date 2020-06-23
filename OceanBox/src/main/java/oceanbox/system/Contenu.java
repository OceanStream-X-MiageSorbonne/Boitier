package oceanbox.system;

import java.time.LocalDateTime;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import oceanbox.propreties.ClientPropreties;
import oceanbox.veille.Veille;
//import oceanbox.veille.VeilleDMV;
import oceanbox.veille.VeilleScanner;
//import oceanbox.videoplayer.JOmxPlayer;
import oceanbox.videoplayer.JVlcPlayer;
import oceanbox.videoplayer.Video;
import oceanbox.videoplayer.VideoPlayer;
import oceanbox.videoplayer.VideosInfos;

/**
 * Cette classe gère les vidéos qui seront affichées à l'écran
 */
public class Contenu {

	private VideosInfos objectVideosInfo;
	private Map<Integer, Video> videosInfos;
	private Iterator<Integer> timelineIterator;
	private int totalDurationOfVideo;
	private VideoPlayer videoPlayer;
	private Process processPlayer;
	private int diffusionStart;
	private Veille veille;

	public Contenu() {
		veille = new VeilleScanner(this);
		videoPlayer = new JVlcPlayer();
		diffusionStart = -1;
	}

	/**
	 * Cette méthode permet de récupérer les différentes vidéos dans le répertoire
	 * local et récupère la durée totale cumulée des vidéos
	 */
	public void initVideos() {

		objectVideosInfo = new VideosInfos();
		videosInfos = objectVideosInfo.getVideosInfos();

		totalDurationOfVideo = objectVideosInfo.getTotalDurationOfVideos();

		initDiffusion();
	}

	/**
	 * Cette méthode permet d'initialiser le temps auquel débute la vidéo en
	 * fonction de l'heure de réveil définie dans les propriétés du client
	 * 
	 * @return le temps relatif cumulé en secondes auquel doit débuter la diffusion
	 */
	public int repereForDiffusion() {

		int currently = (LocalDateTime.now().getHour() * 3600) + (LocalDateTime.now().getMinute() * 60)
				+ LocalDateTime.now().getSecond();

		String[] times = ClientPropreties.getPropretie("wakingHour").split(":");

		int base = (Integer.parseInt(times[0]) * 3600) + (Integer.parseInt(times[1]) * 60) + Integer.parseInt(times[2]);

		if (base > currently)
			return ((currently - base) % totalDurationOfVideo) + totalDurationOfVideo;
		return (currently - base) % totalDurationOfVideo;
	}

	/**
	 * Cette méthode permet de récupérer le numéro de la vidéo en lecture ainsi que
	 * la valeur en secondes à laquelle sa diffusion a débuté
	 * 
	 * @param videosMap   : la Map d'un objet " objectVideosInfo "
	 * @param start       : le temps relatif cumulé depuis l'heure de réveil
	 * @param fromContenu : vaut true si la méthode est appelé depuis Contenu
	 * 
	 * @return un tableau de 2 valeurs : la 1ère est le numéro de la vidéo en
	 *         lecture, l'autre est le temps en secondes auquel débute sa diffusion
	 */
	public int[] getInfosCurrentVideo(Map<Integer, Video> videosMap, int start, boolean fromContenu) {

		Iterator<Integer> iterator = videosMap.keySet().iterator();

		int timeVideo;
		int startIndice = 1;
		while (iterator.hasNext()) {
			startIndice = iterator.next();
			if (fromContenu)
				timelineIterator.next();
			timeVideo = videosMap.get(startIndice).getDuration();
			if (start >= timeVideo) {
				start -= timeVideo;
			} else {
				break;
			}
		}

		int[] res = { startIndice, start };

		return res;
	}

	/**
	 * Cette méthode permet de spécifier les paramètres de la " timeline " dans
	 * laquelle sont jouées les vidéos
	 */
	public void initDiffusion() {

		if (diffusionStart < 0) {
			diffusionStart = repereForDiffusion();
		} else
			diffusionStart = 0;

		timelineIterator = videosInfos.keySet().iterator();

		int[] infosCurrentVideo = getInfosCurrentVideo(videosInfos, diffusionStart, true);

		customPlay(videosInfos.get(infosCurrentVideo[0]), infosCurrentVideo[1]);
	}

	/**
	 * Cette méthode gère la lecture des vidéos les unes après les autres
	 * 
	 * @param nextVideo : la vidéo à diffuser
	 * @param begin     : le temps de la vidéo auquel sa diffusion doit débuter
	 */
	private void customPlay(Video nextVideo, int begin) {
		processPlayer = videoPlayer.play(nextVideo.getPath(), begin);
		try {
			processPlayer.waitFor(((nextVideo.getDuration() - begin) * 1000) - 500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (!veille.isSleepMode()) {
			if (timelineIterator.hasNext())
				customPlay(videosInfos.get(timelineIterator.next()), 0);
			else
				initVideos();
		} else {
			while (veille.isSleepMode()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			diffusionStart = -1;
			initVideos();
		}

	}

	/**
	 * Cette méthode stop la diffusion à l'écran
	 */
	public void stopDiffusion() {
		diffusionStart = -1;
		videoPlayer.stopPlayerProcess();
	}

	/**
	 * Cette méthode renvoie la durée totale cumulée des videos du répertoire locale
	 * 
	 * @return : le temps cumulé en secondes
	 */
	public int getTotalDurationOfVideo() {
		return totalDurationOfVideo;
	}
}
