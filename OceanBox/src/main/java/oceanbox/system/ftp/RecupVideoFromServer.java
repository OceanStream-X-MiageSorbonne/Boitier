package oceanbox.system.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

//import org.apache.log4j.Logger;
import oceanbox.propreties.SystemPropreties;

public class RecupVideoFromServer {

	// private final static Logger LOGGER =
	// Logger.getLogger(RecupVideoFromServer.class.getName());;
	private FTPSClient ftpsClient;
	private Set<Integer> videosFiles;
	private String cheminDistant;
	private String prefixeNomVideo;
	private String suffixeNomVideo;
	private String cheminLocal;

	public RecupVideoFromServer() {
//		// Initialisation du Logger
//		Appender fh = null;
//		try {
//			fh = new FileAppender(new SimpleLayout(), SystemPropreties.getPropertie("relativeLogPath"));
//			LOGGER.addAppender(fh);
//			fh.setLayout(new SimpleLayout());
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		setVideoRegex();
		setVideosFiles();
	}

	/**
	 * Connexion FTP
	 */
	private void ftpConnection() {

		String host = SystemPropreties.getPropertie("ftpIP");
		String login = SystemPropreties.getPropertie("ftpUser");
		String mdp = SystemPropreties.getPropertie("ftpPasswd");
		String port = SystemPropreties.getPropertie("ftpPort");

		ftpsClient = new FTPSClient();

		try {
			ftpsClient.connect(host, Integer.valueOf(port));
			ftpsClient.login(login, mdp);
			ftpsClient.execPBSZ(0);
			ftpsClient.execPROT("P");
			// LOGGER.info("FTP Connection OK");
			ftpsClient.addProtocolCommandListener(new PrintCommandListener(
					new PrintWriter(new FileOutputStream(SystemPropreties.getPropertie("relativeLogPath")))));
			ftpsClient.enterLocalPassiveMode();
			ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			// LOGGER.error("FTP Connection KO");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Déconnexion FTP
	 */
	private void ftpDeconnection() {
		try {
			ftpsClient.logout();
			ftpsClient.disconnect();
			// LOGGER.info("FTP Deconnection OK");
		} catch (IOException e) {
			// LOGGER.error("FTP Deconnection KO");
			e.printStackTrace();
		}
	}

	public void ftpDownloadFile(int numVideo) {
		if (!ftpsClient.isConnected()) {
			ftpConnection();
		}
		String nomVideoVoulu = prefixeNomVideo + numVideo + suffixeNomVideo;
		try {
			File fichierlocal = new File(cheminLocal + nomVideoVoulu);
			fichierlocal.createNewFile();

			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fichierlocal, false));

//			LOGGER.info(" *** Début du téléchargement du fichier : " + nomVideoVoulu + " ***");
			ftpsClient.retrieveFile(cheminDistant + nomVideoVoulu, outputStream);
//			LOGGER.info(" *** Fin du téléchargement du fichier : " + nomVideoVoulu + " ***");

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

	public void deleteLocalOldFile(int numVideo) {

		// Suppression de l'ancien paquet
		for (String nomVideo : new File(cheminLocal).list()) {
			if (!nomVideo.startsWith(prefixeNomVideo) && nomVideo.endsWith(numVideo + suffixeNomVideo))
				new File(cheminLocal + nomVideo).delete();
		}
	}

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

	private void setVideoRegex() {
		cheminDistant = SystemPropreties.getPropertie("ftpVideoPath");
		cheminLocal = SystemPropreties.getPropertie("videoPath");
		suffixeNomVideo = ".mp4";
		prefixeNomVideo = "19-4-2020_";
		// prefixeNomVideo = LocalDateTime.now().getDayOfMonth() + "-" +
		// LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getYear() +
		// "_";

	}

	public Set<Integer> getVideosFiles() {
		return videosFiles;
	}
}
