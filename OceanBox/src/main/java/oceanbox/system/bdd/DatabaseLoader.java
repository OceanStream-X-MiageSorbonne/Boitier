package oceanbox.system.bdd;

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

public class DatabaseLoader {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	// Database credentials
	private static final String USER = SystemPropreties.getPropertie("dbUser");
	static final String PASS = SystemPropreties.getPropertie("dbPasswd");
	static final String HOST = SystemPropreties.getPropertie("dbIP");
	static final String PORT = SystemPropreties.getPropertie("dbPort");
	private static Statement stmt = null;
	private static PreparedStatement preparedStatement = null;
	private static Connection conn = null;

	public static void setPropertiesFromDatabase() {
		try {
			String forName = "com.mysql.cj.jdbc.Driver";
			try {
				Class.forName(forName);
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Driver Loaded Successfully");

			} catch (ClassNotFoundException ex) {
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Driver Failed To Load Successfully");
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, ex.getMessage());
			}

			// STEP 3: Open a connection
			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Connecting to a selected database...");
			conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/oceandatabase", USER, PASS);

			Logger.getLogger(DatabaseLoader.class.getName()).log(Level.INFO, "Connected database successfully...");

			stmt = conn.createStatement();
			ResultSet resultat = stmt.executeQuery("SELECT * FROM ftptable, dbtable");
			while (resultat.next()) {
				String ftpIP = resultat.getString("ftpIP");
				String ftpUser = resultat.getString("ftpUser");
				String ftpPasswd = resultat.getString("ftpPasswd");
				String ftpPort = resultat.getString("ftpPort");
				String dbIP = resultat.getString("dbIP");
				String dbUser = resultat.getString("dbUser");
				String dbPasswd = resultat.getString("dbPasswd");
				String dbPort = resultat.getString("dbPort");
				String videoPath = resultat.getString("videoPath");
				String ftpVideoPath = resultat.getString("ftpVideoPath");
				
				SystemPropreties.setPropertie("ftpIP", ftpIP);
				SystemPropreties.setPropertie("ftpUser", ftpUser);
				SystemPropreties.setPropertie("ftpPasswd", ftpPasswd);
				SystemPropreties.setPropertie("ftpPort", ftpPort);
				SystemPropreties.setPropertie("ftpVideoPath", videoPath);
				SystemPropreties.setPropertie("dbIP", dbIP);
				SystemPropreties.setPropertie("dbUser", dbUser);
				SystemPropreties.setPropertie("dbPasswd", dbPasswd);
				SystemPropreties.setPropertie("dbPort", dbPort);
				SystemPropreties.setPropertie("ftpVideoPath", ftpVideoPath);

			}
			preparedStatement = conn.prepareStatement(
					"SELECT * FROM clientsproperties, oceanboxproperties WHERE oceanBoxNumber= ?");
			preparedStatement.setString(1, SystemPropreties.getPropertie("oceanBoxNumber"));
			resultat = preparedStatement.executeQuery();
			resultat.next();

			String VideoFlux = resultat.getString("VideoFlux");
			String userName = resultat.getString("userName");
			String userType = resultat.getString("userType");
			String activateStandby = resultat.getString("activateStandby");
			String timeBeforeStandby = resultat.getString("timeBeforeStandby");
			String heureDeReveil = resultat.getString("heureDeReveil");
			
			ClientPropreties.setPropertie("VideoFlux", VideoFlux);
			ClientPropreties.setPropertie("userName", userName);
			ClientPropreties.setPropertie("userType", userType);
			ClientPropreties.setPropertie("activateStandby", activateStandby);
			ClientPropreties.setPropertie("timeBeforeStandby", timeBeforeStandby);
			ClientPropreties.setPropertie("heureDeReveil", heureDeReveil);

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				Logger.getLogger(DatabaseLoader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
