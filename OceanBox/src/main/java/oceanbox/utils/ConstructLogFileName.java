package oceanbox.utils;

import java.time.LocalDateTime;

public class ConstructLogFileName {

	private static final String FILE_EXTENSION = ".txt";
	
	private ConstructLogFileName() {
	    throw new IllegalStateException("Utility class");
	  }
	
	public static String getFtpLogFileName(String prefixe, String numRasp) {
		String currentDate = LocalDateTime.now().getDayOfMonth()+"-"+LocalDateTime.now().getMonthValue()+"-"+LocalDateTime.now().getYear();
		String fileName = prefixe+numRasp+"_"+currentDate+FILE_EXTENSION;
		return fileName;
	}
	
	public static String getDbLogFileName(String prefixe) {
		String currentDate = LocalDateTime.now().getDayOfMonth()+"-"+LocalDateTime.now().getMonthValue()+"-"+LocalDateTime.now().getYear();
		String fileName = prefixe+"_"+currentDate+FILE_EXTENSION;
		return fileName;
	}
}
