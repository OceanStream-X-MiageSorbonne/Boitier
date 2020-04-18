package oceanbox.controler;

import java.util.List;

import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

import oceanbox.model.AbstractModel;
import oceanbox.propreties.ClientPropreties;
import oceanbox.view.Veille;

/**
 * Cette classe implémente les méthodes control(), controlInfo() et
 * controlVeille()
 */
public class Controler extends AbstractControler {

	public Controler() {
		super();
	}

	public Controler(Stage stage, AbstractModel model) {
		super(stage, model);
	}

	@Override
	public void control() {

		veille = new Veille();

		pauseBeforeClose.setOnFinished(null);
		pauseBeforeCloseAlert.setOnFinished(null);

		if (ClientPropreties.getPropertie("activateStandby").equals("true")) {

			pauseBeforeClose = new PauseTransition(Duration.seconds(secondsBeforeClose));
			pauseBeforeClose.setOnFinished(this.closeApp);

			pauseBeforeCloseAlert = new PauseTransition(Duration.seconds(secondsBeforeClose - 6));
			pauseBeforeCloseAlert.setOnFinished(this.closeAlert);

			pauseBeforeClose.play();
			pauseBeforeCloseAlert.play();
		}

		// -----------------------------------------------------------------------------------------

		pauseBeforeShowUpInfo.setOnFinished(null);

		if (ClientPropreties.getPropertie("infos").equals("true") && infoControler == null) {

			pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(10));
			pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);
			pauseBeforeShowUpInfo.play();
		}
	}

	@Override
	public void controlInfo() {

		if (infoControler.getBandeauDeroulant().nbCharInInfo() == 0) {

			model.notifyObserver(infoControler, false);

		} else {

			List<PauseTransition> defilement = infoControler.getBandeauDeroulant().getDefilement();

			defilement.get(defilement.size() - 1).setOnFinished(event -> {

				model.notifyObserver(infoControler, false);

				infoControler = null;

				pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(10));
				pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);
				pauseBeforeShowUpInfo.play();
			});

			defilement.get(0).play();
		}
	}

	@Override
	public void controlVeille() {

		pauseBeforeClose.setOnFinished(null);
		pauseBeforeCloseAlert.setOnFinished(null);
		pauseBeforeShowUpInfo.setOnFinished(null);

		infoControler = null;
	}
}
