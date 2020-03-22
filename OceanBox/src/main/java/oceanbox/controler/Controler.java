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
		this.pauseBeforeCloseAlert.setOnFinished(null);
		this.pauseBeforeShowUpInfo.setOnFinished(null);
		this.pauseBeforeRemoveInfo.setOnFinished(null);

		this.pauseBeforeClose = new PauseTransition(Duration.seconds(20));
		this.pauseBeforeClose.setOnFinished(this.closeApp);

		this.pauseBeforeCloseAlert = new PauseTransition(Duration.seconds(14));
		this.pauseBeforeCloseAlert.setOnFinished(this.closeAlert);

		this.pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(8));
		this.pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);
		
		this.pauseBeforeRemoveInfo = new PauseTransition(Duration.seconds(5));
		this.pauseBeforeRemoveInfo.setOnFinished(this.infoRemove);
		
		this.pauseBeforeClose.play();
		this.pauseBeforeCloseAlert.play();
		this.pauseBeforeShowUpInfo.play();
	}
}
