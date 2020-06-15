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

		// -----------------------------------------------------------------------------------

		// Modifiez la ligne ci-dessous pour aller chercher les videos au bon endroit
		//SystemPropreties.setPropretie("videoPath", "/Users/abdelbenamara/Movies/OceanBox/");
		// SystemPropreties.setPropretie("videoPath", "/Users/daekc/Desktop/video/");
		SystemPropreties.setPropretie("videoPath", "/home/mathieuridet/Videos/OceanBox/");

		// Le vrai chemin vers les vidéos sur raspberry est celui ci-dessous
		// SystemPropreties.setPropretie("videoPath", "/home/pi/OceanBox/video/");

		// Les properties ci-dessous influent directement sur l'application
		ClientPropreties.setPropretie("wakingHour", "08:30:00");
		ClientPropreties.setPropretie("activateStandby", "false");
		ClientPropreties.setPropretie("timeBeforeStandby", "00:00:20");

		// -----------------------------------------------------------------------------------

		// L'objet Contenu permet de gérer les vidéos à l'écran
		Contenu c = new Contenu();

		// L'objet Download permet de gérer les téléchargements des vidéos en arrière
		// plan sans perturber la diffusion continue du Contenu
		Download d = new Download(c);

		// On initialise le téléchargement d'abord
		d.initDownload();
		// car la diffusion se fait de manière récursive à l'infini
		c.initVideos();
	}
}
