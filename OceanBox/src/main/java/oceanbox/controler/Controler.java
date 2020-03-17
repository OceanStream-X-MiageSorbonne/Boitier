package oceanbox.controler;

import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import javafx.util.Duration;
import oceanbox.model.AbstractModel;

public class Controler extends AbstractControler {

	public Controler(Stage stage, AbstractModel model) {
		super(stage, model);
	}

	@Override
	public void control(int max) {
		this.pauseBeforeClose.setOnFinished(null);
		this.alertBeforeClose.setOnFinished(null);

		this.pauseBeforeClose = new PauseTransition(Duration.seconds(20));
		this.pauseBeforeClose.setOnFinished(this.closeApp);

		this.alertBeforeClose = new PauseTransition(Duration.seconds(14));
		this.alertBeforeClose.setOnFinished(this.closeAlert);

		this.pauseBeforeClose.play();
		this.alertBeforeClose.play();
	}
}
