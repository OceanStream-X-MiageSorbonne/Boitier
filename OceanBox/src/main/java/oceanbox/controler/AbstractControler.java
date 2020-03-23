package oceanbox.controler;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import oceanbox.model.AbstractModel;
import oceanbox.view.Alerte;
import oceanbox.view.info.Barre_info;
import oceanbox.view.info.BasicInfo;

public abstract class AbstractControler {

	protected AbstractModel model;
	protected Stage stage;
	protected PauseTransition pauseBeforeClose = new PauseTransition(Duration.seconds(20));
	protected EventHandler<ActionEvent> closeApp = event -> {
		stage.close();
	};

	protected Label closeInfoControler;
	protected PauseTransition pauseBeforeCloseAlert = new PauseTransition(Duration.seconds(14));
	protected EventHandler<ActionEvent> closeAlert = event -> {
		closeInfoControler = new Alerte();
		model.notifyObserver(closeInfoControler, true);
	};

	protected VBox infoControler;
	protected PauseTransition pauseBeforeRemoveInfo = new PauseTransition(Duration.seconds(5));
	protected EventHandler<ActionEvent> infoRemove = event -> {
		model.notifyObserver(infoControler, false);
	};
	protected PauseTransition pauseBeforeShowUpInfo = new PauseTransition(Duration.seconds(8));
	protected EventHandler<ActionEvent> basicInfoShowUp = event -> {
		infoControler = new Barre_info(new BasicInfo("Ceci est une information qui apparaît à l'écran"));
		model.notifyObserver(infoControler, true);
		pauseBeforeRemoveInfo.play();
	};

	public AbstractControler(Stage stage, AbstractModel model) {
		this.model = model;
		this.stage = stage;
		control(20);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			model.notifyObserver(closeInfoControler, false);
			control(20);
		});
	}

	public abstract void control(int max);
}
