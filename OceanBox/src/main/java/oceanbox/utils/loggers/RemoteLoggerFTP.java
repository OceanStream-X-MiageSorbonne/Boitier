package oceanbox.utils.loggers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.net.ftp.FTPClient;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe permet de créer et de remplir un fichier de log distant. NB : Ce
 * fichier se situe par défaut sur le serveur au chemin suivant : "/logs/"
 *
 */
public class RemoteLoggerFTP {

	private static final String FTP_IP = SystemPropreties.getPropretie("ftpIP");
	private static final String FTP_USER = SystemPropreties.getPropretie("ftpUser");
	private static final String FTP_PWD = SystemPropreties.getPropretie("ftpPassword");
	private static final int FTP_PORT = Integer.parseInt(SystemPropreties.getPropretie("ftpPort"));
	
	private static LocalLogger logger;
	private FileInputStream fis;
	private String localFileName, remoteFileName;
	private FTPClient ftpClient;

	/**
	 * On initialise notre logger local pour le compléter au fil des diffusions
	 * vidéos Il sera ensuite uploadé comme expliqué en commentaire de la fonction
	 * "uploadLogFileOnServer()" ci-dessous
	 * 
	 * @param loggerName
	 */
	public RemoteLoggerFTP(String loggerName, String fileName) {
		logger = new LocalLogger(loggerName, fileName);
		remoteFileName = SystemPropreties.getPropretie("remoteLogPath")
				+ SystemPropreties.getPropretie("ftpLogFileName");
		localFileName = SystemPropreties.getPropretie("localLogPath") + SystemPropreties.getPropretie("ftpLogFileName");
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
	public void uploadLogFileOnServer() {
		ftpConnection();
		
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
				if(ftpClient.storeFile(remoteFileName, fis)) {
					logger.log(Level.INFO, "Stockage fichier sur serveur OK");
					System.out.println("Stockage fichier sur serveur OK !!!!!");
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
		
		ftpDeconnection();

		//logger.deleteLocalLogFile(file);
	}
	
	/**
	 * Cette méthode permet de se connecter au serveur FTP
	 * via 
	 */
	private FTPClient ftpConnection() {
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(FTP_IP, FTP_PORT);
			ftpClient.login(FTP_USER, FTP_PWD);
			logger.log(Level.INFO, "FTP Connection OK");
			System.out.println("FTP Connection OK");
		} catch (IOException e) {
			System.out.println("FTP Connection NOT OK");
			logger.log(Level.SEVERE, "FTP Connection NOT OK");
			e.printStackTrace();
		}
		return ftpClient;
	}

	/**
	 * Cette méthode permet de se déconnecter du serveur FTP
	 */
	private void ftpDeconnection() {
		if(ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
				logger.log(Level.INFO, "FTP Déconnexion OK");
				System.out.println("FTP Déconnexion OK");
			} catch (IOException e) {
				System.out.println("FTP Déconnexion NOT OK");
				logger.log(Level.SEVERE, "FTP Déconnexion NOT OK");
				e.printStackTrace();
			}
		}
	}
	
}
