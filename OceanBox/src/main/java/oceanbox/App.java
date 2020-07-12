package oceanbox;

import java.io.FileNotFoundException;
import java.io.IOException;

import oceanbox.system.Content;
import oceanbox.system.Download;
import oceanbox.utils.OceanLogger;
import oceanbox.utils.bdd.DatabaseLoader;
import oceanbox.utils.propreties.ClientProperties;
import oceanbox.utils.propreties.SystemProperties;

/**
 * Cette classe est le point d'entrée de l'application
 */
public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// On initialise les fichiers de propriétés puis on les complète avec les
		// informations qui sont sur la base de données
		SystemProperties.initProperties();
		ClientProperties.initProperties();

		// Ce qui est ci-dessous évite les conflits en testant sur un ordinateur
		ReglagesDeTest.initPersonalSettings();
		// ---------------------------------------------------------------------

		OceanLogger.initLogger();
		DatabaseLoader.setPropretiesFromDatabase();

		// Ce qui est ci-dessous évite les conflits en testant sur un ordinateur
		ReglagesDeTest.initPersonalSettings();
		// ---------------------------------------------------------------------

		Content c = new Content();
		Download d = new Download(c);
		d.initDownload();
		c.initVideos();
	}
}
