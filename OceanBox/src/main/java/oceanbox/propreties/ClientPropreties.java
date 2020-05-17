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

public class ClientPropreties {

	protected static Properties props;
	protected static Map<String, String> defaultProperties;
	protected static String path = "ClientPropreties.propreties";

	private static void initDefaultProperties() {

		defaultProperties = new HashMap<String, String>();

		// User settings
		defaultProperties.put("userName", "x");
		defaultProperties.put("userType", "x");

		// Video settings
		defaultProperties.put("VideoFlux", "default");
		defaultProperties.put("heureDeReveil", "06:00:00");
		defaultProperties.put("nextDownloadTime", "UNKNOWN");
		
		// Standby settings
		defaultProperties.put("activateStandby", "true");
		defaultProperties.put("timeBeforeStandby", "00:10:00");

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
