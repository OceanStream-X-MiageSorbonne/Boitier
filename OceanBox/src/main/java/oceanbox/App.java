package oceanbox;

import oceanbox.controler.AbstractControler;
import oceanbox.controler.Controler;
import oceanbox.model.AbstractModel;
import oceanbox.model.Model;
import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;
import oceanbox.view.Lecteur_video;

import java.io.FileNotFoundException;
import java.io.IOException;

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
		AbstractControler controler = new Controler(primaryStage, model);
		Lecteur_video lecteur = new Lecteur_video(primaryStage, controler, SystemPropreties.getPropertie("videoName"));
		model.addObserver(lecteur);

		Scene primaryScene = new Scene(lecteur);
		primaryScene.setCursor(Cursor.NONE);
		primaryStage.setScene(primaryScene);
		lecteur.getTimer().start();
		primaryStage.show();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		SystemPropreties.initProperties();
		ClientPropreties.initProperties();
		
		SystemPropreties.setPropertie("videoPath", "/Users/daekc/OneDrive/Bureau/video/");
		SystemPropreties.setPropertie("videoName", "video-test.mp4");
		
		Application.launch(args);
	}
}
