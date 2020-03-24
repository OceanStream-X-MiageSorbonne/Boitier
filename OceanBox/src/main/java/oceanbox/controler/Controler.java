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
	public void control() {
		this.pauseBeforeClose.setOnFinished(null);
		this.pauseBeforeCloseAlert.setOnFinished(null);
		this.pauseBeforeShowUpInfo.setOnFinished(null);

		this.pauseBeforeClose = new PauseTransition(Duration.seconds(20));
		this.pauseBeforeClose.setOnFinished(this.closeApp);

		this.pauseBeforeCloseAlert = new PauseTransition(Duration.seconds(14));
		this.pauseBeforeCloseAlert.setOnFinished(this.closeAlert);

		this.pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(8));
		this.pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);

		this.pauseBeforeClose.play();
		this.pauseBeforeCloseAlert.play();
		this.pauseBeforeShowUpInfo.play();
	}

	@Override
	public void controlInfo() {
		PauseTransition[] defilement = new PauseTransition[120];
		for (int i = 0; i < 120; i++) {
			defilement[i] = new PauseTransition(Duration.seconds(0.05));
			if (i == 119)
				defilement[i].setOnFinished(event -> {
					infoControler.getbDeroulant().setHvalue(infoControler.getbDeroulant().getHvalue() + 0.01);
					model.notifyObserver(infoControler, false);
				});
			else if (i < 10) {
				int pos[] = { i };
				defilement[i].setOnFinished(event -> {
					defilement[pos[0] + 1].play();
				});
			} else {
				int pos[] = { i };
				defilement[i].setOnFinished(event -> {
					infoControler.getbDeroulant().setHvalue(infoControler.getbDeroulant().getHvalue() + 0.01);
					defilement[pos[0] + 1].play();
				});
			}
		}
		defilement[0].play();
	}
}
