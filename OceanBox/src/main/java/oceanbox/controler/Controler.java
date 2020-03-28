package oceanbox.controler;

import java.util.List;

import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

import oceanbox.model.AbstractModel;
import oceanbox.propreties.ClientPropreties;

/**
 * Cette classe implémente les méthodes control() et controlInfo()
 */
public class Controler extends AbstractControler {

	public Controler(Stage stage, AbstractModel model) {
		super(stage, model);
	}

	@Override
	public void control() {

		pauseBeforeClose.setOnFinished(null);
		pauseBeforeCloseAlert.setOnFinished(null);
		pauseBeforeShowUpInfo.setOnFinished(null);

		if (ClientPropreties.getPropertie("activateStandby").equals("true")) {

			pauseBeforeClose = new PauseTransition(Duration.seconds(secondsBeforeClose));
			pauseBeforeClose.setOnFinished(this.closeApp);

			pauseBeforeCloseAlert = new PauseTransition(Duration.seconds(secondsBeforeClose - 6));
			pauseBeforeCloseAlert.setOnFinished(this.closeAlert);

			pauseBeforeClose.play();
			pauseBeforeCloseAlert.play();
		}

		if (ClientPropreties.getPropertie("infos").equals("true")) {

			pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(10));
			pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);
			pauseBeforeShowUpInfo.play();
		}
	}

	@Override
	public void controlInfo() {

		List<PauseTransition> defilement = infoControler.getBandeauDeroulant().getDefilement();

		defilement.get(defilement.size() - 1).setOnFinished(event -> {

			model.notifyObserver(infoControler, false);
		});
		
		defilement.get(0).play();
	}
}
