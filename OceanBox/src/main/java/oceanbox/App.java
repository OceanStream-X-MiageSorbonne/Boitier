package oceanbox;

import oceanbox.controler.AbstractControler;
import oceanbox.controler.Controler;
import oceanbox.model.AbstractModel;
import oceanbox.model.Model;
import oceanbox.view.Lecteur_video;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("A real ocean");
		primaryStage.setFullScreen(true);

		AbstractModel model = new Model();
		AbstractControler controler = new Controler(model);
		Lecteur_video lecteur = new Lecteur_video(primaryStage, controler, "video-test.mp4");
		model.addObserver(lecteur);

		Scene primaryScene = new Scene(lecteur);
		primaryStage.setScene(primaryScene);
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE)
					primaryStage.close();
			}
		});
		primaryStage.show();
		lecteur.getVideo().play();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
