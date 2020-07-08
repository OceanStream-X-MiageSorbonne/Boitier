package oceanbox.utils.loggers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.net.ftp.FTPSClient;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe permet de créer et de remplir un fichier de log distant. NB : Ce
 * fichier se situe par défaut sur le serveur au chemin suivant : "/logs/"
 *
 */
public class RemoteLogger {
	
	private static LocalLogger logger;
	private FileInputStream fis;
	private String localFileName, remoteFileName;

	/**
	 * On initialise notre logger local pour le compléter au fil des diffusions
	 * vidéos Il sera ensuite uploadé comme expliqué en commentaire de la fonction
	 * "uploadLogFileOnServer()" ci-dessous
	 * 
	 * @param loggerName
	 */
	public RemoteLogger(String loggerName, String fileName) {
		logger = new LocalLogger(loggerName, fileName);
		String remotePath = SystemPropreties.getPropretie("remoteLogPath").equalsIgnoreCase("unknown") ? "/logs/" : SystemPropreties.getPropretie("remoteLogPath");
		String localPath = SystemPropreties.getPropretie("localLogPath").equalsIgnoreCase("unknown") ? "" : SystemPropreties.getPropretie("localLogPath");
		remoteFileName = remotePath + fileName;
		localFileName = localPath + fileName;
	}

	/**
	 * Cette méthode permet d'ajouter un commentaire/log dans le fichier de log en
	 * précisant le niveau du message (Info, Severe, etc..)
	 * 
	 * @param msgLevel
	 * @param msg
	 */
	public void log(Level level, String msg) {
		// handler.publish(new LogRecord(msgLevel, msg));
		logger.log(level, msg);
	}

	/**
	 * Cette méthode permet d'uploader le fichier de log sur le serveur ftp tous les
	 * jours lors du dernier cycle de diffusion avant l'heure de réveil
	 * préalablement programmée
	 */
	public void uploadLogFileOnServer(FTPSClient ftpsClient) {		
		File file = new File(localFileName);
		// Create an InputStream of the local file to be uploaded
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Fichier à uploader introuvable " + localFileName);
			e.printStackTrace();
		}
		// Store file on server
		if (fis != null) {
			try {
				boolean test = ftpsClient.storeFile(remoteFileName, fis);
				System.out.println(test);
				if(test) {
					logger.log(Level.INFO, "Stockage fichier sur serveur OK");
					System.out.println("Stockage fichier " + remoteFileName + " sur serveur OK");
				} else {
					logger.log(Level.INFO, "Stockage fichier sur serveur NOT OK");
					System.out.println("Stockage fichier sur serveur NOT OK");
				}
			} catch (IOException e) {
				logger.log(Level.WARNING, "Stockage fichier sur serveur NOT OK");
				e.printStackTrace();
			}
		} else {
			logger.log(Level.WARNING, "Stockage fichier sur serveur impossible");
			System.out.println("Stockage fichier sur serveur impossible " + remoteFileName);
		}
		
		try {
			if (fis != null) fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//logger.deleteLocalLogFile(file);
	}	
}
