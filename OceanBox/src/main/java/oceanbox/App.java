package oceanbox;

import java.io.FileNotFoundException;
import java.io.IOException;

import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

import oceanbox.system.Contenu;
import oceanbox.system.download.Download;

public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		SystemPropreties.initProperties();
		ClientPropreties.initProperties();
		// DatabaseLoader.setPropertiesFromDatabase();

		// Modifiez la ligne ci-dessous si vous avez un autre path ou nom de video
		//SystemPropreties.setPropertie("videoPath", "/Users/abdelbenamara/Movies/OceanBox/");
		//SystemPropreties.setPropertie("videoPath", "/Users/daekc/Desktop/video/");
		SystemPropreties.setPropertie("videoPath", "/home/pi/OceanBox/video/");
		
		// Les 5 properties ci-dessous influent directement sur l'application
		ClientPropreties.setPropertie("heureDeReveil", "08:00:00");
		ClientPropreties.setPropertie("activateStandby", "false");
		ClientPropreties.setPropertie("timeBeforeStandby", "00:00:20");

		Contenu c = new Contenu();
		Download d = new Download(c);
		d.initDownload();
		c.initVideos();
	}
}
