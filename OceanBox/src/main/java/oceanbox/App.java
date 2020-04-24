package oceanbox;

import oceanbox.controler.AbstractControler;
import oceanbox.controler.Controler;
import oceanbox.model.AbstractModel;
import oceanbox.model.Model;
import oceanbox.model.bdd.DatabaseLoader;
import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;
import oceanbox.view.Lecteur_video;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Cette classe contient la méthode main qui lance l'application
 */
public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("A real ocean");
		primaryStage.setFullScreen(true);

		AbstractModel model = new Model();
		AbstractControler controler = new Controler(primaryStage, model);
		Lecteur_video lecteur = new Lecteur_video(controler);
		model.addObserver(lecteur);

		Scene primaryScene = new Scene(lecteur);
		primaryScene.setCursor(Cursor.NONE);
		primaryStage.setScene(primaryScene);
		primaryStage.show();

		// Pour le développement on supprime les properties à chaque fermeture de
		// l'application
		primaryStage.setOnHiding(event -> {
			ClientPropreties.deletePropertiesFile();
			SystemPropreties.deletePropertiesFile();
		});
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		SystemPropreties.initProperties();
		ClientPropreties.initProperties();

		DatabaseLoader.setPropertiesFromDatabase();

		// Modifiez la ligne ci-dessous si vous avez un autre path ou nom de video
		SystemPropreties.setPropertie("videoPath", "/Users/abdelbenamara/Movies/OceanBox/");

		// Les 5 properties ci-dessous influent directement sur l'application
		ClientPropreties.setPropertie("downloadHour", "18:32:00");
		ClientPropreties.setPropertie("heureDeReveil", "08:30:00");
		ClientPropreties.setPropertie("infos", "true");
		ClientPropreties.setPropertie("activateStandby", "true");
		ClientPropreties.setPropertie("timeBeforeStandby", "00:05:00");

		Application.launch(args);
	}
}
