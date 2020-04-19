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

public class SystemPropreties {

	protected static Properties props;
	protected static Map<String, String> defaultProperties;
	protected static String path = "SystemProperties.propreties";

	private static void initDefaultProperties() {

		defaultProperties = new HashMap<String, String>();

		// FTP settings
		defaultProperties.put("ftpIP", "37.187.107.122");
		defaultProperties.put("ftpUser", "ocean_ftp");
		defaultProperties.put("ftpPasswd", "Stream2020");
		defaultProperties.put("ftpPort", "21");

		defaultProperties.put("ftpVideoPath", "/default_video/");

		// Log settings
		defaultProperties.put("relativeLogPath", "allAboutFtp.log");

		// Database settings
		defaultProperties.put("dbIP", "37.187.107.122");
		defaultProperties.put("dbUser", "ocean_bdd");
		defaultProperties.put("dbPasswd", "OceanBox2020");
		defaultProperties.put("dbPort", "3306");

		// OceanBox settings
		defaultProperties.put("oceanBoxNumber", "1");
		defaultProperties.put("oceanBoxIP", "0.0.0.0");

		// Video settings
		defaultProperties.put("videoPath", "/home/pi/");
	}

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

	private static boolean propretiesFileExist() {

		File f = new File(path);

		if (!f.exists())
			return false;
		return true;
	}

	public static void deletePropertiesFile() {

		if (propretiesFileExist()) {
			File f = new File(path);
			f.delete();
		}
	}

	private static void createProperties() {

		initDefaultProperties();

		for (String p : defaultProperties.keySet()) {
			props.setProperty(p, defaultProperties.get(p));
		}
	}

	public static String getPropertie(String key) {
		return (String) props.get(key);
	}

	public static void setPropertie(String key, String value) throws FileNotFoundException, IOException {

		props.setProperty(key, value);
		try (OutputStream writer = new FileOutputStream(path)) {
			props.store(writer, null);
		}

	}

}
