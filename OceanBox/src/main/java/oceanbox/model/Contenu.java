package oceanbox.model;

import java.time.LocalDateTime;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import oceanbox.controler.AbstractControler;

import oceanbox.propreties.ClientPropreties;

import oceanbox.videoplayer.JOmxPlayer;
import oceanbox.videoplayer.Video;
import oceanbox.videoplayer.VideosInfos;

/**
 * Cette classe contient les vidéos qui seront affichées à l'écran
 */
public class Contenu {

	private Map<Integer, Video> videosInfos;
	private Iterator<Integer> timelineIterator;
	private int totalDurationOfVideo;
	private AbstractControler controler;
	private Video videoPlaying;
	private JOmxPlayer videoPlayer;
	private Process processPlayer;
	private int start;
	private boolean showing;
	private Timer timeBeforeVeille;

	public Contenu(int secondsForTest) {
		// Ceci est un constructeur qui n'est utile que pour les tests unitaires
		this.totalDurationOfVideo = secondsForTest;
	}

	public Contenu(AbstractControler controler) {

		this.controler = controler;
		totalDurationOfVideo = 0;
		videoPlayer = new JOmxPlayer();
		timeBeforeVeille = new Timer();
		start = -1;
		initVideos();
	}

	private class VeilleTask extends TimerTask {
		@Override
		public void run() {
			stopDiffusion();
		}
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

		showing = true;

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

		timeBeforeVeille.schedule(new VeilleTask(), controler.getSecondsBeforeClose() * 1000);

		customPlay(videosInfos.get(startIndice), start);
	}

	/**
	 * Cette méthode gère la lecture des vidéos les unes après les autres
	 * 
	 * @param nextVideo
	 */
	private void customPlay(Video nextVideo, int begin) {

		System.out.println(begin);

		videoPlaying = nextVideo;

		processPlayer = videoPlayer.playVLC(nextVideo.getPath(), begin);
		try {
			processPlayer.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (showing) {
			if (timelineIterator.hasNext())
				customPlay(videosInfos.get(timelineIterator.next()), 0);
			else
				initVideos();
		} else {
			while (!showing) {
				Thread t = new Thread(() -> {
					@SuppressWarnings("resource")
					Scanner sc = new Scanner(System.in);
					sc.next();
					showing = true;
				});
				t.run();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			start = -1;
			initVideos();
		}

		// TODO réadapter le pattern observer
//		if (controler.isSleep()) {
//			controler.getModel().notifyObserver(controler.getVeille(), false);
//			controler.sleepMode(false);
//		}
	}

	public void stopDiffusion() {

		start = -1;
		showing = false;
		processPlayer.destroy();
	}

	public int getTotalDurationOfVideo() {
		return totalDurationOfVideo;
	}

	public Video getVideoPlaying() {
		return videoPlaying;
	}
}
