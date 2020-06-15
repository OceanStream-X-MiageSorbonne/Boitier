package oceanbox.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLogger {

	private static Logger logger;
	private static FileHandler fh;
	
	public FileLogger(String fileName, String loggerName) {
		logger = Logger.getLogger(loggerName);
		try {
			fh=new FileHandler(fileName);
			logger.addHandler(fh);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.setUseParentHandlers(false);
	}
	
	public void log(Level msgLevel, String msg) {
		fh.setLevel(msgLevel);
		logger.log(msgLevel, msg);
	}
	
	public Logger getLogger() {
		return logger;
	}
	
}
