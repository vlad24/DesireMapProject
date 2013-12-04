package dataBasePackage;
import java.sql.*;

public abstract class DataBase {
	
	public abstract void connectToBase() throws SQLException, ClassNotFoundException;
	public abstract void disconnect();
	public abstract Connection getConnection();
	protected Connection connection;
	protected String driver;
	protected String baseName;
	public Boolean turnedOn;
	//protected abstract void createSimpleTable(String name, String[] columns, String[] specifiers) throws SQLException;
	
}