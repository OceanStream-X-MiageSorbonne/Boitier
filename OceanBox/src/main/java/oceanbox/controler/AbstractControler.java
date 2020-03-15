package oceanbox.controler;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import oceanbox.model.AbstractModel;

public abstract class AbstractControler {

	protected AbstractModel model;
	protected Stage stage;
	protected PauseTransition closeTimer;
	protected EventHandler<ActionEvent> end = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			stage.close();
		}
	};

	public AbstractControler(Stage stage, AbstractModel model) {
		this.model = model;
		this.stage = stage;
		this.closeTimer = new PauseTransition(Duration.seconds(20));
		this.closeTimer.setOnFinished(end);
		this.closeTimer.play();
	}

	public AbstractModel getModel() {
		return model;
	}

	public PauseTransition getCloseTimer() {
		return closeTimer;
	}

	public abstract void control(int max);
}
