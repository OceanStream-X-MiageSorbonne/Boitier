package oceanbox.model;

import java.time.LocalDateTime;

import java.util.Iterator;
import java.util.Map;

import oceanbox.controler.AbstractControler;

import oceanbox.propreties.ClientPropreties;
import oceanbox.videoplayer.Video;

/**
 * Cette classe contient les vidéos qui seront affichées à l'écran
 */
public class Contenu {

	private Map<Integer, Video> videosInfos;
	private Iterator<Integer> timelineIterator;
	private int totalDurationOfVideo = 0;
	private AbstractControler controler;

	public Contenu(int secondsForTest) {
		// Ceci est un constructeur qui n'est utile que pour les tests unitaires
		this.totalDurationOfVideo = secondsForTest;
	}

	public Contenu(AbstractControler controler) {

		this.controler = controler;

		initVideosInfos();
	}

	/**
	 * Cette méthode permet de récupérer les différents paquets dans le répertoire
	 * local et de planifier la lecture des videos les unes après les autres
	 */
	public void initVideosInfos() {

		// TODO calcul de la somme des durées des vidéos + appel de VideosInfos etc...
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
	// TODO il faut peut-être changer le type de la méthode, void a été mis au hasard
	public void initDiffusion() {

		timelineIterator = videosInfos.keySet().iterator();

		int start = repereForDiffusion();

		int timeVideo;
		int startIndice;
		while (timelineIterator.hasNext()) {
			startIndice = timelineIterator.next();
			timeVideo = videosInfos.get(startIndice).getDuration();
			if (start >= timeVideo) {
				start -= timeVideo;
			} else {
				break;
			}
		}

		// TODO peut-être appeler customPlay(videosInfos.get(startIndice), start)
	}

	/**
	 * Cette méthode gère la lecture des vidéos les unes après les autres
	 * 
	 * @param nextVideo
	 */
	@SuppressWarnings("unused")
	private void customPlay(Video nextVideo, int begin) {

		// TODO 
		// Pseudo code : 
		// à la fin de chaque video, lire la video suivante, sinon initVideos
		// faire une boucle récursive pour optimiser le bouclage infini
		
		// TODO peut-être implémenter ici le process etc...
//		JOmxPlayer player =  new JOmxPlayer();
//
//		Process processPlayer = player.play(vInfos.getVideosInfos().get(1).getPath(), "0");
		// TODO l'événement qui permet de passer en veille ou non 
//		try {
//			processPlayer.waitFor();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		if (controler.isSleep()) {
			controler.getModel().notifyObserver(controler.getVeille(), false);
			controler.sleepMode(false);
		}
	}

	public void stopDiffusion() {
		
		// TODO peut-être process.stop()
	}

	public int getTotalDurationOfVideo() {
		return totalDurationOfVideo;
	}
}
