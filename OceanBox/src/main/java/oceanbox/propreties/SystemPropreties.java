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
 * Cette classe gère les informations relatives à la configuration personnalisée
 * du programme écrites dans le fichier de propriété du même nom que la classe
 */
public class SystemPropreties {

	protected static Properties props;
	protected static Map<String, String> defaultProperties;
	protected static String path = "SystemPropreties.propreties";

	/**
	 * Cette méthode initialise des propriétés par défaut pour créer les champs
	 * nécessaires et ne pas laisser vides
	 */
	private static void initDefaultPropreties() {

		defaultProperties = new HashMap<String, String>();

		// OceanBox settings
		defaultProperties.put("oceanBoxNumber", "0");
		defaultProperties.put("oceanBoxIP", "0.0.0.0");

		// FTP settings
		defaultProperties.put("ftpUser", "ocean_ftp");
		defaultProperties.put("ftpIP", "37.187.107.122");
		defaultProperties.put("ftpPassword", "Stream2020");
		defaultProperties.put("ftpPort", "21");
		defaultProperties.put("ftpVideoPath", "/default_video/");

		// Database settings
		defaultProperties.put("dbUser", "ocean_bdd");
		defaultProperties.put("dbIP", "37.187.107.122");
		defaultProperties.put("dbPassword", "OceanBox2020");
		defaultProperties.put("dbPort", "3306");

		// Video settings
		defaultProperties.put("videoPath", "/home/pi/OceanBox/video/");

		// Log settings
		defaultProperties.put("ftpLogPath", "ftpLogFile.log");
		defaultProperties.put("dbLogPath", "dbLogFile.txt");
		
		//CMDs
		defaultProperties.put("mediaInfoCMD", "/usr/bin/mediainfo");
		defaultProperties.put("vlcCMD", "/usr/bin/vlc");
		
		//defaultProperties.put("remoteLogPath", "\\logs\\");
		//defaultProperties.put("localLogPath", "/OceanBox/src/main/resources/properties/");
		//defaultProperties.put("localLogPath", "");
		//defaultProperties.put("ftpLogFileName", ConstructLogFileName.getFtpLogFileName("logFtpRasp", defaultProperties.get("oceanBoxNumber")));
		//defaultProperties.put("dbLogFileName", ConstructLogFileName.getDbLogFileName("dbLogFile"));
		//defaultProperties.put("dbLogPath", "");
		//defaultProperties.put("ftpLogPath","");

	}

	/**
	 * Cette méthode initialise le fichier de propriétés
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void initPropreties() throws FileNotFoundException, IOException {

		props = new Properties();

		if (!propretiesFileExist()) {
			createPropreties();
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
	public static void deletePropretiesFile() {

		if (propretiesFileExist()) {
			File f = new File(path);
			f.delete();
		}
	}

	/**
	 * Cette méthode crée le fichier et y écrit les propriétés par défaut
	 */
	private static void createPropreties() {

		initDefaultPropreties();

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
	public static String getPropretie(String key) {
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
	public static void setPropretie(String key, String value) throws FileNotFoundException, IOException {

		props.setProperty(key, value);
		try (OutputStream writer = new FileOutputStream(path)) {
			props.store(writer, null);
		}
	}
}
