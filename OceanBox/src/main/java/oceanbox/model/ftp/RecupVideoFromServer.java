package oceanbox.model.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import javafx.application.Platform;

import javafx.scene.layout.BorderPane;

import oceanbox.controler.AbstractControler;

import oceanbox.model.Contenu;
import oceanbox.model.Telechargement;

import oceanbox.propreties.SystemPropreties;

public class RecupVideoFromServer {

	private final static Logger LOGGER = Logger.getLogger(RecupVideoFromServer.class.getName());;
	private FTPSClient ftpsClient;
	private AbstractControler controler;
	private Telechargement telechargement;

	public RecupVideoFromServer(Telechargement telechargement, AbstractControler controler) {
		this.controler = controler;
		this.telechargement = telechargement;

		// Initialisation du Logger
		Appender fh = null;
		try {
			fh = new FileAppender(new SimpleLayout(), SystemPropreties.getPropertie("relativeLogPath"));
			LOGGER.addAppender(fh);
			fh.setLayout(new SimpleLayout());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			LOGGER.info("FTP Connection OK");
			ftpsClient.addProtocolCommandListener(new PrintCommandListener(
					new PrintWriter(new FileOutputStream(SystemPropreties.getPropertie("relativeLogPath")))));
			ftpsClient.enterLocalPassiveMode();
			ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			LOGGER.error("FTP Connection KO");
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
			LOGGER.info("FTP Deconnection OK");
		} catch (IOException e) {
			LOGGER.error("FTP Deconnection KO");
			e.printStackTrace();
		}
	}

	public void ftpDownloadFile() {

		// Si le boîtier n'est pas en veille alors on réinitialise le contenu à l'écran
		Platform.runLater(() -> {
			controler.setDownload(true);
			if (!controler.isSleep()) {
				BorderPane remplacement = new BorderPane();
				remplacement.setCenter(telechargement.getMediaViewBonus());
				controler.getModel().notifyObserver(controler.getVeille(), false);
				controler.setInfoControler(null);
				controler.getModel().notifyObserver(remplacement, true);
				controler.control();
			}
		});

		ftpConnection();

		String cheminDistant = SystemPropreties.getPropertie("ftpVideoPath");
		String cheminLocal = SystemPropreties.getPropertie("videoPath");

		String prefixeNomVideo = LocalDateTime.now().getDayOfMonth() + "-" + LocalDateTime.now().getMonthValue() + "-"
				+ LocalDateTime.now().getYear() + "_";
		String suffixeNomVideo = ".mp4";

		try {
			// On récupère le dossier où se trouve les paquets pour tous les télécharger
			// dans le dossier local
			List<FTPFile> videosFiles = new ArrayList<FTPFile>();
			for (FTPFile paquet : ftpsClient.listFiles(cheminDistant)) {
				if (paquet.getName().startsWith(prefixeNomVideo)) {
					videosFiles.add(paquet);
					System.out.println(paquet.getName());
				}
			}

			if (videosFiles.isEmpty())
				System.out.println("Aucun nom fichier ne commence par : " + prefixeNomVideo);

			int numVideo = 0;
			for (FTPFile v : videosFiles) {
				numVideo++;

				// Nom du paquet courant sur le serveur
				String nomPaquet = v.getName();

				// On s'occupe que des vidéos du jour, pas celle du jour suivant
				String nomVideoVoulu = prefixeNomVideo + numVideo + suffixeNomVideo;

				if (nomPaquet.equals(nomVideoVoulu)) {
					// On récupère le fichier local ...
					File fichierlocal = new File(cheminLocal + nomPaquet);
					// ... on le crée s'il n'existe pas
					fichierlocal.createNewFile();

					OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fichierlocal, false));

					LOGGER.info(" *** Début du téléchargement du fichier : " + nomPaquet + " ***");
					ftpsClient.retrieveFile(cheminDistant + nomPaquet, outputStream);
					LOGGER.info(" *** Fin du téléchargement du fichier : " + nomPaquet + " ***");

					// Téléchargement du paquet terminé, on ferme les flux
					outputStream.close();

					// Suppression de l'ancien paquet

					for (String nomVideo : new File(cheminLocal).list()) {
						if (!nomVideo.startsWith(prefixeNomVideo) && nomVideo.endsWith(numVideo + suffixeNomVideo))
							new File(cheminLocal + nomVideo).delete();
					}
				}
			}

			// On ferme la connexion FTP
			ftpDeconnection();

			// Si le boîtier n'est pas en veille alors on réinitialise le contenu à l'écran
			Platform.runLater(() -> {
				if (!controler.isSleep()) {
					controler.getModel().notifyObserver(controler.getVeille(), false);
					controler.getModel().notifyObserver(new Contenu(controler), true);
					controler.setInfoControler(null);
					controler.control();
				}
				controler.setDownload(false);
			});

		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ftpsClient.isConnected()) {
				// On ferme la connexion FTP
				ftpDeconnection();
			}
		}
	}
}
