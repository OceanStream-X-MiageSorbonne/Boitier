package oceanbox;

import oceanbox.controler.AbstractControler;
import oceanbox.controler.Controler;
import oceanbox.model.AbstractModel;
import oceanbox.model.Model;
import oceanbox.view.Lecteur_video;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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
		primaryScene.setCursor(Cursor.NONE);
		primaryStage.setScene(primaryScene);
		primaryStage.show();
		lecteur.getVideo().play();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
