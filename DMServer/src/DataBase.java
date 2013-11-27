import java.sql.*;

public abstract class DataBase {
	
	public abstract void connectToBase() throws SQLException, ClassNotFoundException;
	public abstract void disconnect();
	public abstract Connection getConnection();
	protected Connection connection;
	protected String driver;
	protected String baseName;
	protected Boolean turnedOn;
	
}