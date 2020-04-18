package oceanbox.model;

import java.util.TimerTask;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.util.Duration;

import oceanbox.controler.AbstractControler;

import oceanbox.model.ftp.RecupVideoFromServer;

public class Telechargement extends TimerTask {

	private MediaView mediaViewBonus;
	private RecupVideoFromServer recuperation;

	public Telechargement(AbstractControler controler) {

		String regex = "bonus.mp4";
		Media video = new Recup_video(regex).getVideo();
		MediaPlayer player = new MediaPlayer(video);
		mediaViewBonus = new MediaView(player);
		player.play();
		player.setOnEndOfMedia(() -> {
			player.stop();
			player.setStartTime(Duration.ZERO);
			player.play();
		});

		recuperation = new RecupVideoFromServer(this, controler);
	}

	@Override
	public void run() {
		recuperation.ftpDownloadFile();
	}

	public MediaView getMediaViewBonus() {
		return mediaViewBonus;
	}
}
