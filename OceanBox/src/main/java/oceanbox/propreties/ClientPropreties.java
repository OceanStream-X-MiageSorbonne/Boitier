package oceanbox.propreties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Cette classe gère les informations relatives au client écrites dans le
 * fichier de propriété du même nom que la classe
 */
public class ClientPropreties {

	protected static Properties props;
	protected static Map<String, String> defaultProperties;
	protected static String path = "ClientPropreties.propreties";

	/**
	 * Cette méthode initialise des propriétés par défaut pour créer les champs
	 * nécessaires et ne pas laisser vides
	 */
	private static void initDefaultProperties() {

		defaultProperties = new HashMap<String, String>();

		// User settings
		defaultProperties.put("userName", "-1");
		defaultProperties.put("userName", "name");
		defaultProperties.put("userType", "type");

		// Video settings
		defaultProperties.put("videoStream", "default");
		defaultProperties.put("wakingHour", "06:00:00");

		// Standby settings
		defaultProperties.put("activateStandby", "true");
		defaultProperties.put("timeBeforeStandby", "00:10:00");

		// Download settings
		defaultProperties.put("nextDownloadTime", "UNKNOWN");

	}

	/**
	 * Cette méthode initialise le fichier de propriétés
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void initProperties() throws FileNotFoundException, IOException {

		props = new Properties();

		if (!propretiesFileExist()) {
			createProperties();
			try (OutputStream writer = new FileOutputStream(path)) {
				props.store(writer, null);
			}

		} else {
			try (InputStream input = new FileInputStream(path)) {
				props.load(input);
			}
		}
	}

	/**
	 * Cette méthode permet de savoir si le fichier de propriétés voulu existe
	 * 
	 * @return : true s'il existe, false sinon
	 */
	private static boolean propretiesFileExist() {

		File f = new File(path);

		return f.exists();
	}

	/**
	 * Cette méthode supprime le fichier de propriété
	 */
	public static void deletePropertiesFile() {

		if (propretiesFileExist()) {
			File f = new File(path);
			f.delete();
		}
	}

	/**
	 * Cette méthode crée le fichier et y écrit les propriétés par défaut
	 */
	private static void createProperties() {

		initDefaultProperties();

		for (String p : defaultProperties.keySet()) {
			props.setProperty(p, defaultProperties.get(p));
		}
	}

	/**
	 * Cette méthode récupère la propriété passée en paramètre
	 * 
	 * @param key : le nom (sensible à la casse) de la propriété voulue
	 * @return : la valeur de la propriété
	 */
	public static String getPropertie(String key) {
		return (String) props.get(key);
	}

	/**
	 * Cette méthode modifie la valeur de la propriété ciblée
	 * 
	 * @param key   : le nom (sensible à la casse) de la propriété ciblée
	 * @param value : la nouvelle valeur de la propriété
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void setPropertie(String key, String value) throws FileNotFoundException, IOException {

		props.setProperty(key, value);
		try (OutputStream writer = new FileOutputStream(path)) {
			props.store(writer, null);
		}
	}
}
