package oceanbox.system.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import oceanbox.propreties.SystemPropreties;
import oceanbox.system.ftp.FtpsConnectionHandler;;
/**
 * Cette classe permet de lancer les téléchargements des vidéos qui sont sur un
 * serveur FTP
 */
public class RecupVideoFromServer {
	

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
		setVideoRegex();
		setVideosFiles();
	}
	
	public static RecupVideoFromServer getInstance() {
		if(INSTANCE == null) INSTANCE = new RecupVideoFromServer();
		return INSTANCE;
	}
	//*************************************************************************************

	/**
	 * Cette méthode télécharge la video ayant le numéro souhaité
	 * 
	 * @param numVideo : le numéro de la vidéo à télécharger
	 */
	public void ftpsDownloadFile(int numVideo) {
		ftpsClient = FtpsConnectionHandler.ftpsConnection();

		String nomVideoVoulu = prefixeNomVideo + numVideo + suffixeNomVideo;
		try {
			File fichierlocal = new File(cheminLocal + nomVideoVoulu);
			fichierlocal.createNewFile();

			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fichierlocal, false));

			System.out.println(" *** D�but du t�l�chargement : " + nomVideoVoulu + " ***");
			ftpsClient.retrieveFile(cheminDistant + nomVideoVoulu, outputStream);
			System.out.println(" *** Fin du t�l�chargement du fichier : " + nomVideoVoulu + " ***");

			// Téléchargement du paquet terminé, on ferme les flux
			outputStream.close();
			
			deleteLocalOldFile(numVideo);

			// On ferme la connexion FTP
			FtpsConnectionHandler.ftpsDeconnection();
			
		} catch (IOException e) {
			// LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ftpsClient.isConnected()) {
				// On ferme la connexion FTP
				FtpsConnectionHandler.ftpsDeconnection();
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
			if (!nomVideo.startsWith(prefixeNomVideo) && nomVideo.endsWith(numVideo + suffixeNomVideo)) {
				System.out.println(" *** Suppression de : " + cheminLocal + nomVideo + " ***");
				(new File(cheminLocal + nomVideo)).delete();
			}
			
		}
	}

	/**
	 * Cette méthode initialise le Set " videosFiles " avec les numéros des vidéos à
	 * télécharger sur le serveur
	 */
	private void setVideosFiles() {
		ftpsClient = FtpsConnectionHandler.ftpsConnection();
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
		//prefixeNomVideo = "25-6-2020_";
		prefixeNomVideo = LocalDateTime.now().plusDays(1).getDayOfMonth() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getYear() + "_";
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

	public String getPrefixeNomVideo() {
		return prefixeNomVideo;
	}
}
