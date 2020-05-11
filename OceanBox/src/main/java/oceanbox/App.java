package oceanbox;

import oceanbox.controler.AbstractControler;
import oceanbox.controler.Controler;
import oceanbox.model.AbstractModel;
import oceanbox.model.Model;

import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Cette classe contient la méthode main qui lance l'application
 */
public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		SystemPropreties.initProperties();
		ClientPropreties.initProperties();

		// DatabaseLoader.setPropertiesFromDatabase();

		// Modifiez la ligne ci-dessous si vous avez un autre path ou nom de video
		SystemPropreties.setPropertie("videoPath", "/Users/abdelbenamara/Movies/OceanBox/");

		// Les 5 properties ci-dessous influent directement sur l'application
		ClientPropreties.setPropertie("downloadHour", "18:32:00");
		ClientPropreties.setPropertie("heureDeReveil", "08:30:00");
		ClientPropreties.setPropertie("infos", "true");
		ClientPropreties.setPropertie("activateStandby", "true");
		ClientPropreties.setPropertie("timeBeforeStandby", "00:00:05");

		// TODO MVC
		AbstractModel model = new Model();
		@SuppressWarnings("unused")
		AbstractControler controler = new Controler(model);
	}
}
