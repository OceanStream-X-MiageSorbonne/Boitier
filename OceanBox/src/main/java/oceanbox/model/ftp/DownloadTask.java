package oceanbox.model.ftp;

import java.util.TimerTask;

import oceanbox.controler.AbstractControler;
import oceanbox.videoplayer.Video;

public class DownloadTask extends TimerTask {

	private AbstractControler controler;
	@SuppressWarnings("unused")
	private Video videoPlaying;
	
	public DownloadTask(AbstractControler controler) {
		this.controler = controler;
		this.videoPlaying = controler.getContenu().getVideoPlaying();
	}

	@Override
	public void run() {
		controler.setDownload(!controler.isDownload());
		System.out.println(controler.isDownload());
		//TODO: Donwload event 
	}
}
