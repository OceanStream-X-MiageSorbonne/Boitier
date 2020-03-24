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
		PauseTransition[] defilement = new PauseTransition[110];
		for (int i = 0; i < 110; i++) {
			defilement[i] = new PauseTransition(Duration.seconds(0.05));
			if (i == 109)
				defilement[i].setOnFinished(event -> {
					infoControler.getbDeroulant().setHvalue(infoControler.getbDeroulant().getHvalue() + 0.01);
					model.notifyObserver(infoControler, false);
				});
			else {
				int pos[] = {i};
				defilement[i].setOnFinished(event -> {
					infoControler.getbDeroulant().setHvalue(infoControler.getbDeroulant().getHvalue() + 0.01);
					defilement[pos[0] + 1].play();
				});
			}
		}
		defilement[0].play();
	}
}
