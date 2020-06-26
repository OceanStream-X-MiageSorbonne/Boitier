package oceanbox.system.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import oceanbox.propreties.SystemPropreties;
import oceanbox.utils.loggers.RemoteLogger;

/**
 * Cette classe permet de lancer les téléchargements des vidéos qui sont sur un
 * serveur FTP
 */
public class RecupVideoFromServer {
	
	private RemoteLogger logger;
	private Set<Integer> videosFiles;
	private String cheminDistant;
	private String prefixeNomVideo;
	private String suffixeNomVideo;
	private String cheminLocal;
	private static FTPSClient ftpsClient;
	
	/*
	public RecupVideoFromServer() {
		setVideoRegex();
		setVideosFiles();
	}
	*/
	
	// ************************* Implémentation du Singleton *************************************
	private static RecupVideoFromServer INSTANCE = null;

	private RecupVideoFromServer() {
		logger = new RemoteLogger("ftpLogger", SystemPropreties.getPropretie("ftpLogFileName"));
		setVideoRegex();
		setVideosFiles();
	}
	
	public static RecupVideoFromServer getInstance() {
		if(INSTANCE == null) INSTANCE = new RecupVideoFromServer();
		return INSTANCE;
	}
	//*************************************************************************************
	

	public void uploadFtpLogFile() {
		ftpsClient = FtpsConnectionHandler.ftpsConnection(logger);
		logger.uploadLogFileOnServer(ftpsClient);
		FtpsConnectionHandler.ftpsDeconnection(logger);
	}

	/**
	 * Cette méthode télécharge la video ayant le numéro souhaité
	 * 
	 * @param numVideo : le numéro de la vidéo à télécharger
	 */
	public void ftpsDownloadFile(int numVideo) {
		ftpsClient = FtpsConnectionHandler.ftpsConnection(logger);

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
			FtpsConnectionHandler.ftpsDeconnection(logger);
			
		} catch (IOException e) {
			// LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ftpsClient.isConnected()) {
				// On ferme la connexion FTP
				FtpsConnectionHandler.ftpsDeconnection(logger);
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
		ftpsClient = FtpsConnectionHandler.ftpsConnection(logger);
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
		prefixeNomVideo = "25-6-2020_";

		// Le vrai préfixe du nom des prochaines vidéos est celui ci-dessous
		//prefixeNomVideo = LocalDateTime.now().plusDays(1).getDayOfMonth() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getYear() + "_";
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
