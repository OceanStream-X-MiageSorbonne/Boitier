package oceanbox.controler;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import oceanbox.model.AbstractModel;

public abstract class AbstractControler {

	protected AbstractModel model;
	protected Stage stage;
	protected PauseTransition pauseBeforeClose = new PauseTransition(Duration.seconds(20));
	protected EventHandler<ActionEvent> closeApp = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			stage.close();
		}
	};
	protected Label closeInfo = new Label();
	protected PauseTransition alertBeforeClose = new PauseTransition(Duration.seconds(14));
	protected EventHandler<ActionEvent> closeAlert = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			model.notifyObserver();
			Thread closeTimer = new Thread(new Runnable() {
				@Override
				public void run() {
					closeInfo.setManaged(true);
					closeInfo.setVisible(true);
					int sec = 6;
					while (sec > 0) {
						try {
							Thread.sleep(1000);
							sec--;
						} catch (InterruptedException ex) {
						}
						String sentence = "secondes";
						if (sec == 1) {
							sentence = "Inactivité : le boîtier va s'éteindre dans 1 seconde";
							final String time = sentence;
							Platform.runLater(() -> {
								closeInfo.setText(time);
							});
						} else if (sec == 0) {
							sentence = "";
							final String time = sentence;
							Platform.runLater(() -> {
								closeInfo.setManaged(false);
								closeInfo.setVisible(false);
								closeInfo.setText(time);
							});
						} else {
							sentence = "Inactivité : le boîtier va s'éteindre dans " + sec + " secondes";
							final String time = sentence;
							Platform.runLater(() -> {
								closeInfo.setText(time);
							});
						}
					}
					closeInfo.setManaged(false);
					closeInfo.setVisible(false);
				}
			});
			closeTimer.start();
		}
	};

	public AbstractControler(Stage stage, AbstractModel model) {
		this.model = model;
		this.stage = stage;
		this.closeInfo.setFont(new Font(40));
		this.closeInfo.setTextAlignment(TextAlignment.CENTER);
		this.closeInfo.setTextFill(Color.WHITE);
		this.closeInfo.setPadding(new Insets(30));
		StackPane.setAlignment(this.closeInfo, Pos.CENTER);
		control(20);
	}

	public AbstractModel getModel() {
		return model;
	}

	public Label getCloseInfo() {
		return closeInfo;
	}

	public abstract void control(int max);
}
