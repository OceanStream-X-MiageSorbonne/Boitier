package oceanbox.utils.loggers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.net.ftp.FTPSClient;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe permet de créer et de remplir un fichier de log distant.
 * NB : Ce fichier se situe par défaut sur le serveur au chemin suivant : "/logs/"
 * @author mathieuridet
 *
 */
public class RemoteLogger {

	private static LocalLogger logger;
	private FileInputStream fis;
	private String localFileName, remoteFileName;
	
	/**
	 * On initialise notre logger local pour le compléter au fil des diffusions vidéos
	 * Il sera ensuite uploadé comme expliqué en commentaire de la fonction "uploadLogFileOnServer()" ci-dessous
	 * @param loggerName
	 */
	public RemoteLogger(String loggerName, String fileName) {
		logger = new LocalLogger(loggerName, fileName);
		remoteFileName = SystemPropreties.getPropretie("remoteLogPath")+SystemPropreties.getPropretie("ftpLogFileName");
	}
	
	/**
	 * Cette méthode permet d'ajouter un commentaire/log dans le fichier de log 
	 * en précisant le niveau du message (Info, Severe, etc..)
	 * @param msgLevel
	 * @param msg
	 */
	public void log(Level level, String msg) {
		//handler.publish(new LogRecord(msgLevel, msg));
		logger.log(level, msg);
	}
	
	/**
	 * Cette méthode permet d'uploader le fichier de log
	 * sur le serveur ftp tous les jours lors du dernier cycle
	 * de diffusion avant l'heure de réveil préalablement programmée
	 */
	public void uploadLogFileOnServer(FTPSClient ftpsClient) {			
		// Create an InputStream of the local file to be uploaded
		try {
			fis = new FileInputStream(new File(localFileName));
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Fichier à uploader introuvable");
			System.out.println("Fichier à uploader introuvable");
			e.printStackTrace();
		} finally {
				try {
					if(fis != null)	fis.close();
					ftpsClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		// Store file on server
		try {
			ftpsClient.storeFile(remoteFileName, fis);
			logger.log(Level.INFO, "Stockage fichier sur serveur OK");
			System.out.println("Stockage fichier sur serveur OK");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Stockage fichier sur serveur KO");
			e.printStackTrace();
		}
		
	}
	
}
