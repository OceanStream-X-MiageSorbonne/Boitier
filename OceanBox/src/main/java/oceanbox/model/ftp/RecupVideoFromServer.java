package oceanbox.model.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oceanbox.propreties.SystemPropreties;

public class RecupVideoFromServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(RecupVideoFromServer.class);
	private static FTPSClient ftpsClient;

	private RecupVideoFromServer() {
	    throw new IllegalStateException("FTP Connection & Download class");
	}
	
	/**
	 * Connexion FTP
	 */
	public static void ftpConnection() {

		//String host = SystemPropreties.getPropertie("ftpIP");
		//String login = SystemPropreties.getPropertie("ftpUser");
		//String mdp = SystemPropreties.getPropertie("ftpPasswd");
		//int port = SystemPropreties.getPropertie("ftpPort");
		String host = "37.187.107.122";
		String login = "ocean_ftp"; 
		String mdp = "Stream2020";
		int port= 21;
		
		ftpsClient = new FTPSClient();
		
		try {
			ftpsClient.connect(host, port);
			ftpsClient.login(login, mdp);
			ftpsClient.execPBSZ(0);
			ftpsClient.execPROT("P");
			ftpsClient.addProtocolCommandListener(new PrintCommandListener((PrintStream) LOGGER));
			ftpsClient.enterLocalPassiveMode();
			ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
			LOGGER.info("FTP Connection OK");
		} catch (IOException e) {
			LOGGER.error("FTP Connection KO");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Déconnexion FTP
	 */
	public static void ftpDeconnection() {
		try {
			ftpsClient.logout();
			ftpsClient.disconnect();
			LOGGER.info("FTP Deconnection OK");
		} catch (IOException e) {
			LOGGER.error("FTP Deconnection KO");
			e.printStackTrace();
		}
	}
	

	public static void ftpDownloadFile() {
		
		String cheminDistant = SystemPropreties.getPropertie("ftpVideoPath");
		String cheminLocal = SystemPropreties.getPropertie("videoPath");
		
		int nbPaquets = -1;
		
		try {									
			// On récupère le dossier où se trouve les paquets pour tous les télécharger
			// dans le dossier local
			FTPFile[] paquets = ftpsClient.listFiles(cheminDistant);
			nbPaquets = paquets.length;
						
			for(int i = 0 ; i < nbPaquets ; i++) {
				// Nom du paquet courant sur le serveur
				String nomPaquet = paquets[i].getName();
				
				// On récupére le fichier local ...
				File fichierlocal = new File(cheminLocal+nomPaquet);
				// ... on le crée s'il n'existe pas
				fichierlocal.createNewFile();
				
				OutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(fichierlocal, false));
				
				LOGGER.info(" *** Début du téléchargement du fichier : " + nomPaquet + " ***");
				ftpsClient.retrieveFile(cheminDistant+nomPaquet, outputStream);
				LOGGER.info(" *** Fin du téléchargement du fichier : " + nomPaquet + " ***");
				
				// Téléchargement du paquet terminé, on ferme les flux
				outputStream.close();
			}
			
			// On stock le nombre de paquets sur le serveur
			SystemPropreties.setPropertie("nbPaquets", String.valueOf(nbPaquets));
			
			// On ferme la connexion FTP
			RecupVideoFromServer.ftpDeconnection();
						
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ftpsClient.isConnected()) {
				// On ferme la connexion FTP
				RecupVideoFromServer.ftpDeconnection();
			}
		}
	}
	

}

