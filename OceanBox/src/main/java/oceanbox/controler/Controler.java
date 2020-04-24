package oceanbox.controler;

import javafx.animation.PauseTransition;

import javafx.stage.Stage;

import javafx.util.Duration;

import oceanbox.model.AbstractModel;

import oceanbox.propreties.ClientPropreties;

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

		if (ClientPropreties.getPropertie("activateStandby").equals("true")) {

			pauseBeforeVeille = new PauseTransition(Duration.seconds(secondsBeforeClose));
			pauseBeforeVeille.setOnFinished(this.veilleApp);

			pauseBeforeVeilleAlert = new PauseTransition(Duration.seconds(secondsBeforeClose - 6));
			pauseBeforeVeilleAlert.setOnFinished(this.veilleAlert);

			pauseBeforeVeille.play();
			pauseBeforeVeilleAlert.play();
		}

		// -----------------------------------------------------------------------------------------

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

			defilementInfo = infoControler.getBandeauDeroulant().getDefilement();

			defilementInfo.getLast().setOnFinished(event -> {

				model.notifyObserver(infoControler, false);

				infoControler = null;

				pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(10));
				pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);
				pauseBeforeShowUpInfo.play();
			});

			defilementInfo.getFirst().play();
		}
	}

	@Override
	public void controlVeille() {

		pauseBeforeVeille.setOnFinished(null);
		pauseBeforeVeilleAlert.setOnFinished(null);
		pauseBeforeShowUpInfo.setOnFinished(null);

		pauseBeforeVeille = null;
		pauseBeforeVeilleAlert = null;
		pauseBeforeShowUpInfo = null;

		defilementInfo = null;
		infoControler = null;
	}
}
