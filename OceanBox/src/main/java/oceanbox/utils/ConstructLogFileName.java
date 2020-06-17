package oceanbox.utils;

import java.time.LocalDateTime;

import oceanbox.propreties.SystemPropreties;

public class ConstructLogFileName {

	private static final String FILE_EXTENSION = ".txt";
	
	private ConstructLogFileName() {
	    throw new IllegalStateException("Utility class");
	  }
	
	public static String getFtpLogFileName(String prefixe) {
		String numRasp = SystemPropreties.getPropretie("oceanBoxNumber");
		String currentDate = LocalDateTime.now().getDayOfMonth()+"-"+LocalDateTime.now().getMonthValue()+"-"+LocalDateTime.now().getYear();
		String fileName = prefixe+numRasp+"_"+currentDate+FILE_EXTENSION;
		System.out.println("FTP : " + fileName);
		return fileName;
	}
	
	public static String getDbLogFileName(String prefixe) {
		String fileName = prefixe+FILE_EXTENSION;
		System.out.println("DB : " + fileName);
		return fileName;
	}
}
