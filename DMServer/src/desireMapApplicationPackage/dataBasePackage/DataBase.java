package desireMapApplicationPackage.dataBasePackage;
import java.sql.*;

public abstract class DataBase {
	
	public abstract void connectToBase() throws SQLException, ClassNotFoundException;
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
	
	public Connection getConnection(){
		return connection;
	}
	
//	public DBConstantsMaster getConstantsMaster(){
//		return constantsMaster;
//	}
	
	public abstract void init() throws SQLException;
	/////////
	//protected DBConstantsMaster constantsMaster;
	protected Connection connection;
	protected String driver;
	protected String baseName;
	public Boolean turnedOn;
	
}