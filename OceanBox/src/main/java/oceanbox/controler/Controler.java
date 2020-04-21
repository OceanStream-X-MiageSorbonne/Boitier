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

	private List<PauseTransition> defilementInfo;

	public Controler() {
		super();
	}

	public Controler(Stage stage, AbstractModel model) {
		super(stage, model);
	}

	@Override
	public void control() {

		veille = new Veille();

		pauseBeforeVeille.setOnFinished(null);
		pauseBeforeVeilleAlert.setOnFinished(null);

		if (ClientPropreties.getPropertie("activateStandby").equals("true")) {

			pauseBeforeVeille = new PauseTransition(Duration.seconds(secondsBeforeClose));
			pauseBeforeVeille.setOnFinished(this.veilleApp);

			pauseBeforeVeilleAlert = new PauseTransition(Duration.seconds(secondsBeforeClose - 6));
			pauseBeforeVeilleAlert.setOnFinished(this.veilleAlert);

			pauseBeforeVeille.play();
			pauseBeforeVeilleAlert.play();
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

			defilementInfo = infoControler.getBandeauDeroulant().getDefilement();

			defilementInfo.get(defilementInfo.size() - 1).setOnFinished(event -> {

				model.notifyObserver(infoControler, false);

				infoControler = null;

				pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(10));
				pauseBeforeShowUpInfo.setOnFinished(this.basicInfoShowUp);
				pauseBeforeShowUpInfo.play();
			});

			defilementInfo.get(0).play();
		}
	}

	@Override
	public void controlVeille() {

		pauseBeforeVeille.setOnFinished(null);
		pauseBeforeVeilleAlert.setOnFinished(null);
		pauseBeforeShowUpInfo.setOnFinished(null);

		defilementInfo = null;
		infoControler = null;
	}
}
