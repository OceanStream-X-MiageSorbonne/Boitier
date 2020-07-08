package oceanbox;

import java.io.FileNotFoundException;
import java.io.IOException;

import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

public class ReglagesDeTest {

	public static void initPersonalSettings() throws FileNotFoundException, IOException {
		// -----------------------------------------------------------------------------------

		// Modifiez la ligne ci-dessous pour aller chercher les videos au bon endroit
		SystemPropreties.setPropretie("videoPath", "/Users/abdelbenamara/Movies/OceanBox/");
		// SystemPropreties.setPropretie("videoPath", "/Users/daekc/Desktop/video/");
		// SystemPropreties.setPropretie("videoPath", "/home/mathieuridet/Videos/OceanBox/");
		// SystemPropreties.setPropretie("videoPath", "/home/pi/OceanBox/video/");

		// Les properties ci-dessous influent directement sur l'application
		ClientPropreties.setPropretie("wakingHour", "08:30:00");
		ClientPropreties.setPropretie("activateStandby", "true");
		ClientPropreties.setPropretie("timeBeforeStandby", "00:00:20");

		SystemPropreties.setPropretie("vlcCMD", "/Applications/VLC.app/Contents/MacOS/VLC");
		// SystemPropreties.setPropretie("vlcCMD", "/usr/bin/vlc");

		SystemPropreties.setPropretie("mediaInfoCMD", "/usr/local/bin/mediainfo");
		// SystemPropreties.setPropretie("mediaInfoCMD", "/usr/bin/mediainfo");

		// -----------------------------------------------------------------------------------
	}

}
