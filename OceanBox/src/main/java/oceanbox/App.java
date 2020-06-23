package oceanbox;

import java.io.FileNotFoundException;
import java.io.IOException;

import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

import oceanbox.system.Contenu;
import oceanbox.system.bdd.DatabaseLoader;
import oceanbox.system.download.Download;
import oceanbox.system.ftp.RecupVideoFromServer;

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
		//SystemPropreties.setPropretie("videoPath", "/Users/daekc/Desktop/video/");
		SystemPropreties.setPropretie("videoPath", "/home/mathieuridet/Videos/OceanBox/");
		//SystemPropreties.setPropretie("videoPath", "/home/pi/OceanBox/video/");


		// Les properties ci-dessous influent directement sur l'application
		ClientPropreties.setPropretie("wakingHour", "08:30:00");
		ClientPropreties.setPropretie("activateStandby", "true");
		ClientPropreties.setPropretie("timeBeforeStandby", "00:00:20");
		
		//SystemPropreties.setPropretie("VlcCMD", "/Applications/VLC.app/Contents/MacOS/VLC");
		SystemPropreties.setPropretie("VlcCMD", "/usr/bin/vlc");
		
		//SystemPropreties.setPropretie("MediaInfoCMD", "/usr/local/bin/mediainfo");
		SystemPropreties.setPropretie("MediaInfoCMD", "/usr/bin/mediainfo");

		// -----------------------------------------------------------------------------------

		Contenu c = new Contenu();
		Download d = new Download(c);
		d.initDownload();
		c.initVideos();
		
	}
}
