package desireMapApplicationPackage.dataBasePackage;
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
		System.out.println("+Connected to base " + baseName);
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void disconnect(){
		if (connection != null){
			try {
				connection.close();
				turnedOn = false;
				System.out.println("+Disconnected from base " + baseName);
				
			} catch (SQLException error) {
				System.out.println("-DB can't be closed");
			}
		}
		else{
			System.out.println("+No connection found but already closed");
		}
	}

}
