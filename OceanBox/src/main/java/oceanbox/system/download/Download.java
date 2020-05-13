package oceanbox.system.download;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import oceanbox.propreties.ClientPropreties;
import oceanbox.system.Contenu;
import oceanbox.videoplayer.Video;
import oceanbox.videoplayer.VideosInfos;

public class Download {

	private Timer timeToDownload;
	private VideosInfos objectVideosInfo;
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

		int total = objectVideosInfo.getTotalDurationOfVideos();

		LocalDateTime ldt = LocalDateTime.now();

		String[] times = ClientPropreties.getPropertie("heureDeReveil").split(":");

		int hour = Integer.parseInt(times[0]);
		int minutes = Integer.parseInt(times[1]);
		int seconds = Integer.parseInt(times[2]);

		ldt = LocalDateTime.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth(), hour, minutes, seconds);

		// heureDeReveil + ((24 % total) - 1) * total)
		ldt = ldt.plus((((24 * 3600) % total) - 1) * total, ChronoUnit.SECONDS);

		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	private class DownloadTask extends TimerTask {
		
		private Video videoPlaying;
		
		@Override
		public void run() {
			// TODO
			videoPlaying = contenu.getVideoPlaying();
		}
	}
}
