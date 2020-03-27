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
		defaultProperties.put("ftpIP", "ftpIP");
		defaultProperties.put("ftpUser", "ftpUser");
		defaultProperties.put("ftpPasswd", "ftpPasswd");
		defaultProperties.put("ftpPort", "ftpPort");
		defaultProperties.put("downloadHour", "4");

		// Database settings
		defaultProperties.put("dbIP", "dbIP");
		defaultProperties.put("dbUser", "dbUser");
		defaultProperties.put("dbPasswd", "dbPasswd");
		defaultProperties.put("dbPort", "dbPort");

		// OceanBox settings
		defaultProperties.put("oceanBoxNumber", "oceanBoxNumber");
		defaultProperties.put("oceanBoxIP", "0.0.0.0");
		
		// Video settings 
		defaultProperties.put("videoPath", "/Users/abdelbenamara/Movies/OceanBox/");
		defaultProperties.put("videoName", "video-test.mp4");

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
