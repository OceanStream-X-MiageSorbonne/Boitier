package oceanbox.view;

import oceanbox.Recup_video;
import oceanbox.controler.AbstractControler;
import oceanbox.observer.Observer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Lecteur_video extends StackPane implements Observer {

	private AbstractControler controler;
	private MediaPlayer video;
	private Label timeInfo = new Label();
	private Thread timer;

	public Lecteur_video(Stage stage, AbstractControler controler, String fileName) {
		this.controler = controler;
		BorderPane container = new BorderPane();

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
		this.video.setAutoPlay(true);
		this.video.setOnEndOfMedia(new Runnable() {
			public void run() {
				video.setStartTime(Duration.ZERO);
				video.seek(Duration.ZERO);
				video.play();
			}
		});

		MediaView mediaView = new MediaView(this.video);
		mediaView.setFitWidth(stage.getWidth());
		mediaView.setFitHeight(stage.getHeight());

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE)
					stage.close();
			}
		});

		container.getChildren().add(mediaView);
		this.getChildren().add(container);

		this.timeInfo.setFont(new Font(40));
		this.timeInfo.setTextAlignment(TextAlignment.RIGHT);
		this.timeInfo.setTextFill(Color.WHITE);
		this.timeInfo.setPadding(new Insets(30));
		StackPane.setAlignment(this.timeInfo, Pos.BOTTOM_RIGHT);

		this.timer = new Thread(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy" + "\n" + "HH:mm:ss");
				int sec = 0;
				while (sec < 5) {
					try {
						Thread.sleep(1000);
						sec++;
					} catch (InterruptedException ex) {
					}
					final String time = dt.format(new Date());
					Platform.runLater(() -> {
						timeInfo.setText(time);
					});
				}
				timeInfo.setManaged(false);
				timeInfo.setVisible(false);
			}
		});

		this.getChildren().add(this.timeInfo);
		
		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				getChildren().remove(controler.getCloseInfo());
				controler.control(20);
			}
		});
	}

	public MediaPlayer getVideo() {
		return video;
	}

	public Thread getTimer() {
		return timer;
	}

	@Override
	public void update() {
		this.getChildren().add(this.controler.getCloseInfo());
	}
}
