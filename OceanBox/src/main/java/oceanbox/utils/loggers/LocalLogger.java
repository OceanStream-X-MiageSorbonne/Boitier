package oceanbox.utils.loggers;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe représente un objet qui permet de créer et de remplir un fichier
 * local de log. NB : Ce fichier se situe par défaut sous "/OceanBox/" (au même
 * endroit que les fichiers propriétés (ClientPropreties.propreties &
 * SystemPropreties.propreties).
 *
 */
public class LocalLogger {

	private static Logger logger;
	private static FileHandler fh;

	/**
	 * Dans le constructeur on initialise le Logger ainsi que son handler.
	 * 
	 * @param fileName
	 * @param loggerName
	 */
	public LocalLogger(String loggerName, String fileName) {
		File file = new File(fileName);

		/**
		 * Si le fichier de log précédent possède le même nom n'a pas été supprimé, on
		 * le supprime maintenant pour en recréer un nouveau
		 */
		if (file.exists()) {
			System.out.println("DELETE : " + fileName);
			file.delete();
		}

		logger = Logger.getLogger(loggerName);
		String path = SystemPropreties.getPropretie("localLogPath").equalsIgnoreCase("unknown") ? "" : SystemPropreties.getPropretie("localLogPath");
		try {
			fh = new FileHandler(path + fileName, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addHandler(fh);
		logger.setUseParentHandlers(false);
	}

	/**
	 * Cette méthode permet d'ajouter un commentaire/log dans le fichier de log en
	 * précisant le niveau du message (Info, Severe, etc..).
	 * 
	 * @param msgLevel
	 * @param msg
	 */
	public void log(Level msgLevel, String msg) {
		fh.setLevel(msgLevel);
		logger.log(msgLevel, msg);
	}

	public void deleteLocalLogFile(File file) {
		if (file.delete())
			logger.log(Level.INFO, "Suppression du fichier local OK");
		else
			logger.log(Level.WARNING, "Suppression du fichier local NOT OK");
	}

	/**
	 * Cette méthode permet de récupérer le logger de la classe.
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}

}
