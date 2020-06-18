package oceanbox.system.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import oceanbox.propreties.SystemPropreties;
import oceanbox.utils.loggers.RemoteLogger;

/**
 * Cette classe permet de lancer les téléchargements des vidéos qui sont sur un
 * serveur FTP
 */
public class RecupVideoFromServer {

	/**
	 * CONSTANTES
	 */
	private static final String FTP_IP = SystemPropreties.getPropretie("ftpIP");
	private static final String FTP_USER = SystemPropreties.getPropretie("ftpUser");
	private static final String FTP_PWD = SystemPropreties.getPropretie("ftpPassword");
	private static final int FTP_PORT = Integer.parseInt(SystemPropreties.getPropretie("ftpPort"));
	
	/**
	 * VARIABLES
	 */
	private RemoteLogger logger = new RemoteLogger("ftpLogger", SystemPropreties.getPropretie("ftpLogFileName"));
	private FTPSClient ftpsClient;
	private Set<Integer> videosFiles;
	private String cheminDistant;
	private String prefixeNomVideo;
	private String suffixeNomVideo;
	private String cheminLocal;

	public RecupVideoFromServer() {
		setVideoRegex();
		setVideosFiles();
	}

	/**
	 * Cette méthode permet de se connecter au serveur FTP
	 */
	private void ftpConnection() {
		ftpsClient = new FTPSClient();

		try {
			ftpsClient.connect(FTP_IP, FTP_PORT);
			ftpsClient.login(FTP_USER, FTP_PWD);
			ftpsClient.execPBSZ(0);
			ftpsClient.execPROT("P");
			logger.log(Level.INFO, "FTP Connection OK");
			/*
			ftpsClient.addProtocolCommandListener(new PrintCommandListener(
					new PrintWriter(new FileOutputStream(SystemPropreties.getPropretie("FtpLogPath")))));
			*/
			ftpsClient.enterLocalPassiveMode();
			ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FTP Connection KO");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de se déconnecter du serveur FTP
	 */
	private void ftpDeconnection() {
		// Avant de fermer la connexion au serveur, on va upload le fichier log ftp
		// A revoir car ça fait planter le téléchargement après une vidéo
		// logger.uploadLogFileOnServer(ftpsClient);
		
		try {
			ftpsClient.logout();
			ftpsClient.disconnect();
			logger.log(Level.INFO, "FTP Déconnexion OK");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FTP Déconnexion KO");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode télécharge la video ayant le numéro souhaité
	 * 
	 * @param numVideo : le numéro de la vidéo à télécharger
	 */
	public void ftpDownloadFile(int numVideo) {

		if (!ftpsClient.isConnected()) {
			ftpConnection();
		}
		
		String nomVideoVoulu = prefixeNomVideo + numVideo + suffixeNomVideo;
		try {
			File fichierlocal = new File(cheminLocal + nomVideoVoulu);
			System.out.println(fichierlocal);
			fichierlocal.createNewFile();

			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fichierlocal, false));

			System.out.println(" *** Début du téléchargement du fichier : " + nomVideoVoulu + " ***");
			logger.log(Level.INFO, " *** Début du téléchargement du fichier : " + nomVideoVoulu + " ***");
			ftpsClient.retrieveFile(cheminDistant + nomVideoVoulu, outputStream);
			logger.log(Level.INFO, " *** Fin du téléchargement du fichier : " + nomVideoVoulu + " ***");
			System.out.println(" *** Fin du téléchargement du fichier : " + nomVideoVoulu + " ***");

			// Téléchargement du paquet terminé, on ferme les flux
			outputStream.close();

			deleteLocalOldFile(numVideo);

			// On ferme la connexion FTP
			ftpDeconnection();

		} catch (

		IOException e) {
			// LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ftpsClient.isConnected()) {
				// On ferme la connexion FTP
				ftpDeconnection();
			}
		}
	}

	/**
	 * Cette méthode supprime la video ayant le numéro souhaité
	 * 
	 * @param numVideo : le numéro de la vidéo à supprimer
	 */
	public void deleteLocalOldFile(int numVideo) {
		// Suppression de l'ancien paquet en local
		for (String nomVideo : new File(cheminLocal).list()) {
			if (!nomVideo.startsWith(prefixeNomVideo) && nomVideo.endsWith(numVideo + suffixeNomVideo))
				new File(cheminLocal + nomVideo).delete();
		}
	}

	/**
	 * Cette méthode initialise le Set " videosFiles " avec les numéros des vidéos à
	 * télécharger sur le serveur
	 */
	private void setVideosFiles() {
		ftpConnection();
		videosFiles = new TreeSet<Integer>();
		try {
			for (FTPFile paquet : ftpsClient.listFiles(cheminDistant)) {
				if (paquet.getName().startsWith(prefixeNomVideo)) {
					videosFiles.add(Integer.parseInt(
							paquet.getName().substring(prefixeNomVideo.length(), prefixeNomVideo.length() + 1)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode initialise les informations nécessaires au téléchargement des
	 * vidéos du jour suivant
	 */
	private void setVideoRegex() {
		cheminDistant = SystemPropreties.getPropretie("ftpVideoPath");
		cheminLocal = SystemPropreties.getPropretie("videoPath");
		suffixeNomVideo = ".mp4";
		prefixeNomVideo = "19-4-2020_";

		// Le vrai préfixe du nom des prochaines vidéos est celui ci-dessous
		// prefixeNomVideo = LocalDateTime.now().getDayOfMonth() + "-" +
		// LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getYear() +
		// "_";
	}

	/**
	 * Cette méthode renvoie le Set qui contient les numéros des vidéos à
	 * télécharger
	 * 
	 * @return : le Set de numéros des vidéos
	 */
	public Set<Integer> getVideosFiles() {
		return videosFiles;
	}
}
