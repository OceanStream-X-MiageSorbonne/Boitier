package oceanbox.utils.loggers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe représente un objet qui permet de créer et de remplir un fichier local de log.
 * NB : Ce fichier se situe par défaut sous "/OceanBox/" (au même endroit que les fichiers propriétés (ClientPropreties.propreties & SystemPropreties.propreties).
 * @author mathieuridet
 *
 */
public class LocalLogger {

	private static Logger logger;
	private static FileHandler fh;
	
	/**
	 * Dans le constructeur on initialise le Logger ainsi que son handler.
	 * @param fileName
	 * @param loggerName
	 */
	public LocalLogger(String loggerName, String fileName) {
		//System.out.println("******** " + fileName);
		logger = Logger.getLogger(loggerName);
		try {
			fh=new FileHandler(SystemPropreties.getPropretie("localLogPath")+fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addHandler(fh);
		logger.setUseParentHandlers(false);
	}
	
	/**
	 * Cette méthode permet d'ajouter un commentaire/log dans le fichier de log 
	 * en précisant le niveau du message (Info, Severe, etc..).
	 * @param msgLevel
	 * @param msg
	 */
	public void log(Level msgLevel, String msg) {
		fh.setLevel(msgLevel);
		logger.log(msgLevel, msg);
	}
	
	/**
	 * Cette méthode permet de récupérer le logger de la classe.
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}
	
}
