package oceanbox.system.ftp;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import oceanbox.propreties.SystemPropreties;
import oceanbox.utils.loggers.RemoteLogger;

/**
 * Cette classe permet de se connecter ou de se déconnecter du serveur FTP.
 *
 */
public class FtpsConnectionHandler {

	private static final String FTP_IP = SystemPropreties.getPropretie("ftpIP");
	private static final String FTP_USER = SystemPropreties.getPropretie("ftpUser");
	private static final String FTP_PWD = SystemPropreties.getPropretie("ftpPassword");
	private static final int FTP_PORT = Integer.parseInt(SystemPropreties.getPropretie("ftpPort"));
	private static final FTPSClient FTPS_CLIENT = new FTPSClient();

	/**
	 * Cette méthode permet de se connecter au serveur FTP
	 * via une connexion sécurisée (FTPS)
	 */
	public static FTPSClient ftpsConnection(RemoteLogger logger) {
		//ftpsClient = new FTPSClient();

		if(!FTPS_CLIENT.isConnected()) {
			try {
				FTPS_CLIENT.connect(FTP_IP, FTP_PORT);
				FTPS_CLIENT.login(FTP_USER, FTP_PWD);
				FTPS_CLIENT.execPBSZ(0);
				FTPS_CLIENT.execPROT("P");
				FTPS_CLIENT.enterLocalPassiveMode();
				FTPS_CLIENT.setFileType(FTP.BINARY_FILE_TYPE);
				logger.log(Level.INFO, "FTPS Connection OK");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "FTPS Connection NOT OK");
				e.printStackTrace();
			}
		}
		return FTPS_CLIENT;
	}
	
	/**
	 * Cette méthode permet de se déconnecter du serveur FTP
	 */
	public static void ftpsDeconnection(RemoteLogger logger) {
		try {
			FTPS_CLIENT.logout();
			FTPS_CLIENT.disconnect();
			logger.log(Level.INFO, "FTPS Déconnexion OK");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FTPS Déconnexion NOT OK");
			e.printStackTrace();
		}
	}

	public FTPSClient getFtpsClient() {
		return FTPS_CLIENT;
	}
	
	
}
