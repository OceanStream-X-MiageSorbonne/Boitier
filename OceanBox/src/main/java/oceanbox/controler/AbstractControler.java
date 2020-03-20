package oceanbox.controler;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
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
	protected Label closeInfoControler;
	protected PauseTransition alertBeforeClose = new PauseTransition(Duration.seconds(14));
	protected EventHandler<ActionEvent> closeAlert = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			model.notifyObserver(closeInfoControler, true);
		}
	};

	public AbstractControler(Stage stage, AbstractModel model) {
		this.model = model;
		this.stage = stage;
		control(20);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				model.notifyObserver(closeInfoControler, false);
				control(20);
			}
		});
	}
	
	public void setCloseInfoControler(Label closeInfoControler) {
		this.closeInfoControler = closeInfoControler;
	}

	public abstract void control(int max);
}
