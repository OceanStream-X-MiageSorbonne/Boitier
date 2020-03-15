package oceanbox.view;

import oceanbox.Recup_video;
import oceanbox.controler.AbstractControler;
import oceanbox.observer.Observer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Lecteur_video extends BorderPane implements Observer {

	MediaPlayer video;

	public Lecteur_video(Stage stage, AbstractControler controler, String fileName) {
		this.video = new MediaPlayer(new Recup_video(fileName).getVideo());
		this.video.setOnEndOfMedia(new Runnable() {
			public void run() {
				stage.close();
			}
		});

		MediaView mediaView = new MediaView(this.video);
		mediaView.setFitWidth(stage.getWidth());
		mediaView.setFitHeight(stage.getHeight());

		this.getChildren().add(mediaView);

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE)
					stage.close();
			}
		});
	}

	public MediaPlayer getVideo() {
		return video;
	}

	@Override
	public void update(String time) {
		// TODO Auto-generated method stub
	}
}
