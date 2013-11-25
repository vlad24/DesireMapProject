import java.sql.*;

public class DataBaseSQLite extends DataBase{

	public DataBaseSQLite(String name) {
		driver = "org.sqlite.JDBC";
		baseName = name;
		connection = null;
	}

	public void connectToBase() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		connection = DriverManager.getConnection("jdbc:sqlite:" + baseName);
		turnedOn = true;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void disconnect(){
		if (connection != null){
			try {
				connection.close();
				turnedOn = false;
			} catch (SQLException error) {
				System.out.println("DB can't be closed");
			}
		}
	}
}
