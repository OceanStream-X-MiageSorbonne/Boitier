package oceanbox.system.bdd;

import java.io.IOException;

import java.net.InetAddress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.logging.Level;
import java.util.logging.Logger;

import oceanbox.propreties.ClientPropreties;
import oceanbox.propreties.SystemPropreties;

/**
 * Cette classe récupère les informations dans la base de données et les écrit
 * dans les fichiers de propriétés
 */
public class DatabaseLoader {

	// JDBC driver's name
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// Database credentials
	private static final String USER = SystemPropreties.getPropretie("dbUser");
	private static final String PASS = SystemPropreties.getPropretie("dbPassword");
	private static final String HOST = SystemPropreties.getPropretie("dbIP");
	private static final String PORT = SystemPropreties.getPropretie("dbPort");
	private static Statement stmt = null;
	private static PreparedStatement preparedStatement = null;
	private static Connection conn = null;

	/**
	 * Cette méthode permet de se connecter à la base de données
	 */
	private static void dbConnection() {

		try {

			try {
				Class.forName(JDBC_DRIVER);
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Driver Loaded Successfully");

			} catch (ClassNotFoundException ex) {
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Driver Failed To Load");
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, ex.getMessage());
			}

			// Open a connection
			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Connecting to a selected database...");

			conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/oceandatabase", USER, PASS);

			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Connected database successfully...");

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de se déconnecter de la base de données
	 */
	private static void dbDeconnexion() {

		try {

			conn.close();

		} catch (SQLException ex) {
			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Cette méthode permet de mettre à jour les fichiers de propriétés à partir des
	 * informations stockées dans la base de données. A noter que l'on met à jour
	 * l'IP locale stockée dans la base de données à chaque appel de cette méthode
	 */
	public static void setPropretiesFromDatabase() {

		dbConnection();

		try {

			stmt = conn.createStatement();

			InetAddress IP = InetAddress.getLocalHost();
			SystemPropreties.setPropretie("oceanBoxIP", IP.getHostAddress());

			preparedStatement = conn.prepareStatement("UPDATE config SET oceanBoxIP = ? WHERE oceanBoxNumber = ?");
			preparedStatement.setString(1, SystemPropreties.getPropretie("oceanBoxIP"));
			preparedStatement.setString(2, SystemPropreties.getPropretie("oceanBoxNumber"));
			preparedStatement.executeUpdate();

			ResultSet resultat = stmt.executeQuery("SELECT * FROM db, ftp");
			while (resultat.next()) {
				SystemPropreties.setPropretie("dbUser", resultat.getString("dbUser"));
				SystemPropreties.setPropretie("dbIP", resultat.getString("dbIP"));
				SystemPropreties.setPropretie("dbPassword", resultat.getString("dbPassword"));
				SystemPropreties.setPropretie("dbPort", resultat.getString("dbPort"));
				SystemPropreties.setPropretie("ftpUser", resultat.getString("ftpUser"));
				SystemPropreties.setPropretie("ftpIP", resultat.getString("ftpIP"));
				SystemPropreties.setPropretie("ftpPassword", resultat.getString("ftpPassword"));
				SystemPropreties.setPropretie("ftpPort", resultat.getString("ftpPort"));
				SystemPropreties.setPropretie("ftpVideoPath", resultat.getString("ftpVideoPath"));
			}

			preparedStatement = conn
					.prepareStatement("SELECT * FROM config, client WHERE oceanBoxNumber = ? AND userId = idClient");
			preparedStatement.setString(1, SystemPropreties.getPropretie("oceanBoxNumber"));
			resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
				ClientPropreties.setPropretie("userId", resultat.getString("idClient"));
				ClientPropreties.setPropretie("userName", resultat.getString("userName"));
				ClientPropreties.setPropretie("userType", resultat.getString("userType"));
				ClientPropreties.setPropretie("videoStream", resultat.getString("videoStream"));
				ClientPropreties.setPropretie("wakingHour", resultat.getString("wakingHour"));
				ClientPropreties.setPropretie("nextDownloadTime", resultat.getString("nextDownloadTime"));
				ClientPropreties.setPropretie("activateStandby", resultat.getString("activateStandby"));
				ClientPropreties.setPropretie("timeBeforeStandby", resultat.getString("timeBeforeStandby"));

				SystemPropreties.setPropretie("videoPath", resultat.getString("videoPath"));
				SystemPropreties.setPropretie("relativeLogPath", resultat.getString("relativeLogPath"));
			}

		} catch (SQLException | IOException ex) {
			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.SEVERE, null, ex);
		}

		dbDeconnexion();
	}

	/**
	 * Cette méthode permet de mettre à jour dans la base de données la valeur du
	 * prochain moment où le téléchargement des vidéos sera effectué
	 */
	public static void setNextDownloadTime() {

		dbConnection();

		try {

			stmt = conn.createStatement();

			preparedStatement = conn
					.prepareStatement("UPDATE config SET nextDownloadTime = ? WHERE oceanBoxNumber = ?");
			preparedStatement.setString(1, ClientPropreties.getPropretie("nextDownloadTime"));
			preparedStatement.setString(2, SystemPropreties.getPropretie("oceanBoxNumber"));
			preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.SEVERE, null, ex);
		}

		dbDeconnexion();
	}
}
