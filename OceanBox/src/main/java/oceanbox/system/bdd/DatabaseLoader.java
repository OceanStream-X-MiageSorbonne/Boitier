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
	private static final String USER = SystemPropreties.getPropertie("dbUser");
	private static final String PASS = SystemPropreties.getPropertie("dbPassword");
	private static final String HOST = SystemPropreties.getPropertie("dbIP");
	private static final String PORT = SystemPropreties.getPropertie("dbPort");
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
	public static void setPropertiesFromDatabase() {

		dbConnection();

		try {

			stmt = conn.createStatement();

			InetAddress IP = InetAddress.getLocalHost();
			SystemPropreties.setPropertie("oceanBoxIP", IP.getHostAddress());

			preparedStatement = conn.prepareStatement("UPDATE config SET oceanBoxIP = ? WHERE oceanBoxNumber = ?");
			preparedStatement.setString(1, SystemPropreties.getPropertie("oceanBoxIP"));
			preparedStatement.setString(2, SystemPropreties.getPropertie("oceanBoxNumber"));
			preparedStatement.executeUpdate();

			ResultSet resultat = stmt.executeQuery("SELECT * FROM db, ftp");
			while (resultat.next()) {
				SystemPropreties.setPropertie("dbUser", resultat.getString("dbUser"));
				SystemPropreties.setPropertie("dbIP", resultat.getString("dbIP"));
				SystemPropreties.setPropertie("dbPassword", resultat.getString("dbPassword"));
				SystemPropreties.setPropertie("dbPort", resultat.getString("dbPort"));
				SystemPropreties.setPropertie("ftpUser", resultat.getString("ftpUser"));
				SystemPropreties.setPropertie("ftpIP", resultat.getString("ftpIP"));
				SystemPropreties.setPropertie("ftpPassword", resultat.getString("ftpPassword"));
				SystemPropreties.setPropertie("ftpPort", resultat.getString("ftpPort"));
				SystemPropreties.setPropertie("ftpVideoPath", resultat.getString("ftpVideoPath"));
			}

			preparedStatement = conn
					.prepareStatement("SELECT * FROM config, client WHERE oceanBoxNumber = ? AND userId = idClient");
			preparedStatement.setString(1, SystemPropreties.getPropertie("oceanBoxNumber"));
			resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
				ClientPropreties.setPropertie("userId", resultat.getString("idClient"));
				ClientPropreties.setPropertie("userName", resultat.getString("userName"));
				ClientPropreties.setPropertie("userType", resultat.getString("userType"));
				ClientPropreties.setPropertie("videoStream", resultat.getString("videoStream"));
				ClientPropreties.setPropertie("wakingHour", resultat.getString("wakingHour"));
				ClientPropreties.setPropertie("nextDownloadTime", resultat.getString("nextDownloadTime"));
				ClientPropreties.setPropertie("activateStandby", resultat.getString("activateStandby"));
				ClientPropreties.setPropertie("timeBeforeStandby", resultat.getString("timeBeforeStandby"));

				SystemPropreties.setPropertie("videoPath", resultat.getString("videoPath"));
				SystemPropreties.setPropertie("relativeLogPath", resultat.getString("relativeLogPath"));
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
			preparedStatement.setString(1, ClientPropreties.getPropertie("nextDownloadTime"));
			preparedStatement.setString(2, SystemPropreties.getPropertie("oceanBoxNumber"));
			preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.SEVERE, null, ex);
		}

		dbDeconnexion();
	}
}
