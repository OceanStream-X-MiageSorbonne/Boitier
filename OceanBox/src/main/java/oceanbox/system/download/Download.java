package oceanbox.system.download;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import oceanbox.propreties.ClientPropreties;

import oceanbox.system.Contenu;
import oceanbox.system.bdd.DatabaseLoader;
import oceanbox.system.ftp.RecupVideoFromServer;

import oceanbox.videoplayer.Video;
import oceanbox.videoplayer.VideosInfos;

public class Download {

	private Timer timeToDownload;
	private VideosInfos objectVideosInfo;
	private Map<Integer, Video> videosInfos;
	private Contenu contenu;

	public Download(Contenu contenu) {
		this.contenu = contenu;
	}

	public void initDownload() {
		timeToDownload = new Timer();
		timeToDownload.schedule(new DownloadTask(), initTimeBeforeDownload());
	}

	private Date initTimeBeforeDownload() {

		objectVideosInfo = new VideosInfos();

		long total = objectVideosInfo.getTotalDurationOfVideos();

		LocalDateTime ldt = LocalDateTime.now();

		String[] times = ClientPropreties.getPropertie("wakingHour").split(":");

		int hour = Integer.parseInt(times[0]);
		int minutes = Integer.parseInt(times[1]);
		int seconds = Integer.parseInt(times[2]);

		ldt = LocalDateTime.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth(), hour, minutes, seconds);

		ldt = ldt.plus((long) ((((24.0 * 3600.0) / total) - 1) * total), ChronoUnit.SECONDS);

		try {
			ClientPropreties.setPropertie("nextDownloadTime",
					Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()).toString());
			DatabaseLoader.setNextDownloadTime();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	private class DownloadTask extends TimerTask {

		@Override
		public void run() {
			
			DatabaseLoader.setPropertiesFromDatabase();
			
			RecupVideoFromServer serverStuff = new RecupVideoFromServer();
			videosInfos = objectVideosInfo.getVideosInfos();

			int n = 0;
			for (int i : serverStuff.getVideosFiles()) {

				n = i;
				int[] infosCurrentVideo = contenu.getInfosCurrentVideo(videosInfos, contenu.repereForDiffusion(),
						false);
				if (infosCurrentVideo[0] <= i)
					if (i <= videosInfos.size())
						try {
							Thread.sleep((videosInfos.get(i).getDuration() - infosCurrentVideo[1]) * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

				serverStuff.ftpDownloadFile(i);
				System.out.println(i);
			}

			for (int j = n + 1; j < videosInfos.size(); j++) {

				serverStuff.deleteLocalOldFile(j);
			}
			
			initDownload();
		}
	}
}
