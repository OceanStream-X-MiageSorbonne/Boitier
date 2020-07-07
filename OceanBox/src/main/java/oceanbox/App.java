package oceanbox;

import java.io.FileNotFoundException;
import java.io.IOException;

import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

import oceanbox.system.Contenu;
import oceanbox.system.bdd.DatabaseLoader;
import oceanbox.system.download.Download;

/**
 * Cette classe est le point d'entrée de l'application
 */
public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		// On initialise les fichiers de propriétés puis on les complète avec les
		// informations qui sont sur la base de données
		SystemPropreties.initPropreties();
		ClientPropreties.initPropreties();
		DatabaseLoader.setPropretiesFromDatabase();

		Contenu c = new Contenu();
		Download d = new Download(c);
		d.initDownload();
		c.initVideos();
		
	}
}
