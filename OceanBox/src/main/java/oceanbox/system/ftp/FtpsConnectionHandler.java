package oceanbox.system.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import oceanbox.propreties.SystemPropreties;

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
	 * Cette méthode permet de se connecter au serveur FTP via une connexion
	 * sécurisée (FTPS)
	 */
	public static FTPSClient ftpsConnection() {
		// ftpsClient = new FTPSClient();

		if (!FTPS_CLIENT.isConnected()) {
			try {
				FTPS_CLIENT.connect(FTP_IP, FTP_PORT);
				FTPS_CLIENT.login(FTP_USER, FTP_PWD);
				FTPS_CLIENT.execPBSZ(0);
				FTPS_CLIENT.execPROT("P");
				FTPS_CLIENT.enterLocalPassiveMode();
				FTPS_CLIENT.setFileType(FTP.BINARY_FILE_TYPE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return FTPS_CLIENT;
	}

	/**
	 * Cette méthode permet de se déconnecter du serveur FTP
	 */
	public static void ftpsDeconnection() {
		try {
			FTPS_CLIENT.logout();
			FTPS_CLIENT.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FTPSClient getFtpsClient() {
		return FTPS_CLIENT;
	}

}
