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

public class Contenu {

	private BorderPane container = new BorderPane();
	private MediaPlayer video;

	public Contenu(Stage stage, String fileName) {

		this.video = new MediaPlayer(new Recup_video(fileName).getVideo());

		LocalDateTime currently = LocalDateTime.now();
		if (currently.getSecond() == 0 || currently.getSecond() == 18 || currently.getSecond() == 35
				|| currently.getSecond() == 52)
			this.video.setStartTime(Duration.ZERO);
		else {
			int repere = 0;
			if (currently.getSecond() > 52)
				repere = 52;
			else if (currently.getSecond() > 35)
				repere = 35;
			else if (currently.getSecond() > 18)
				repere = 18;
			this.video.setStartTime(Duration.seconds(currently.getSecond() - repere));
		}

		KeyFrame update = new KeyFrame(Duration.seconds(0.5), event -> {
			this.video.setAutoPlay(true);
			this.video.setOnEndOfMedia(new Runnable() {
				public void run() {
					video.setStartTime(Duration.ZERO);
					video.seek(Duration.ZERO);
					video.play();
				}
			});
		});
		Timeline diffusion = new Timeline(update);
		diffusion.play();

		MediaView mediaView = new MediaView(this.video);
		mediaView.setFitWidth(stage.getWidth());
		mediaView.setFitHeight(stage.getHeight());

		container.getChildren().add(mediaView);
	}

	public BorderPane getContainer() {
		return container;
	}
}
