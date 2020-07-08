package oceanbox.system.download;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import oceanbox.ReglagesDeTest;
import oceanbox.propreties.ClientPropreties;
import oceanbox.system.Contenu;
import oceanbox.system.bdd.DatabaseLoader;
import oceanbox.system.ftp.RecupVideoFromServer;

import oceanbox.videoplayer.Video;
import oceanbox.videoplayer.VideosInfos;

/**
 * Cette classe gère le téléchargement quotidien des vidéos
 */
public class Download {

	private Timer timeToDownload;
	private VideosInfos objectVideosInfo;
	private Map<Integer, Video> videosInfos;
	private Contenu contenu;
	private RecupVideoFromServer serverStuff;

	public Download(Contenu contenu) {
		this.contenu = contenu;
	}

	/**
	 * Cette méthode initialise le Timer qui déclenchera le téléchargement
	 */
	public void initDownload() {
		timeToDownload = new Timer();
		// timeToDownload.schedule(new DownloadTask(), initTimeBeforeDownload());
		// ----------------------------------------------------------------------
		objectVideosInfo = new VideosInfos();
		long total = objectVideosInfo.getTotalDurationOfVideos();
		if (total == 0)
			timeToDownload.schedule(new DownloadTask(),
					Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		else
			timeToDownload.schedule(new DownloadTask(), Date.from(LocalDateTime.now()
					.plusSeconds(total - contenu.repereForDiffusion()).atZone(ZoneId.systemDefault()).toInstant()));
	}

	/**
	 * Cette méthode calcule la Date du prochain téléchargement
	 * 
	 * @return : la Date du prochain téléchargement
	 */
	@SuppressWarnings("unused")
	private Date initTimeBeforeDownload() {

		objectVideosInfo = new VideosInfos();

		long total = objectVideosInfo.getTotalDurationOfVideos();

		LocalDateTime ldt = LocalDateTime.now();

		String[] times = ClientPropreties.getPropretie("wakingHour").split(":");

		int hour = Integer.parseInt(times[0]);
		int minutes = Integer.parseInt(times[1]);
		int seconds = Integer.parseInt(times[2]);

		ldt = LocalDateTime.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth() + 1, hour, minutes, seconds);

		ldt = ldt.minus((long) total, ChronoUnit.SECONDS);

		try {
			ClientPropreties.setPropretie("nextDownloadTime",
					Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()).toString());
			DatabaseLoader.setNextDownloadTime();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Cette classe lance le téléchargement dans un Thread à part
	 */
	private class DownloadTask extends TimerTask {

		@Override
		public void run() {
			objectVideosInfo = new VideosInfos();
			DatabaseLoader.setPropretiesFromDatabase();
			// Ce qui est ci-dessous évite les conflits en testant sur un ordinateur
			try {
				ReglagesDeTest.initPersonalSettings();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ---------------------------------------------------------------------

			serverStuff = RecupVideoFromServer.getInstance();
			videosInfos = objectVideosInfo.getVideosInfos();

			serverStuff.setVideoRegex();

			if (videosInfos.isEmpty()) {
				System.out.println(">>>>>>> Début du 1er téléchargement de l'application");

				serverStuff.setPrefixeNomVideo("2-1-2000_");
				serverStuff.setVideosFiles();

				for (int i : serverStuff.getVideosFiles()) {
					serverStuff.ftpsDownloadFile(i);
				}
				
				contenu.setFirstTime(false);
				contenu.initInfosOfVideo();
				
			} else {
				serverStuff.setVideosFiles();

				if (serverStuff.getPrefixeNomVideo().equals(videosInfos.get(1).getDate() + "_")) {
					System.out.println(">>>>>>> J+1 déjà en mémoire");
					initDownload();
					return;
				}

				System.out.println(">>>>>>> Début du téléchargement J+1");

				int n = 0;
				int[] infosCurrentVideo;
				for (int i : serverStuff.getVideosFiles()) {
					n = i;
					infosCurrentVideo = contenu.getInfosCurrentVideo(videosInfos, contenu.repereForDiffusion(), false);
					if (infosCurrentVideo[0] <= i)
						if (i <= videosInfos.size())
							try {
								Thread.sleep((videosInfos.get(i).getDuration() - infosCurrentVideo[1]) * 1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

					serverStuff.ftpsDownloadFile(i);
				}

				if (n > 0) {
					for (int j = n + 1; j <= videosInfos.size(); j++) {
						serverStuff.deleteLocalOldFile(j);
					}
				}
			}

			System.out.println(">>>>>>> Fin du téléchargement");

			serverStuff.uploadFtpLogFile();

			initDownload();
		}
	}
}
