package desireMapApplicationPackage.dataBasePackage;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBasePostgreSQL extends DataBase{
	
	public DBPostgresConstantsMaster constantsMaster;
	String user;
	String password;
	///////////
	
	public DataBasePostgreSQL(String name, String newUser, String newPassword) {
		user = newUser;
		password = newPassword;
		driver = "org.postgresql.Driver";
		baseName = name;
		connection = null;
	}

	public void connectToBase() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + baseName, user, password);
		turnedOn = true;
		System.out.println("+Connected to base " + baseName);
	}

	@Override
	public void init() throws SQLException {
		Statement creator = connection.createStatement();
		System.out.println("...CREATING TABLES IN MAIN DB");
		creator.execute(constantsMaster.createTableUSERS);
		System.out.println("+USERS - DONE");
		creator.execute(constantsMaster.createTableINFO);
		System.out.println("+INFO - DONE");
		creator.execute(constantsMaster.createTableDESIRES_MAIN);
		System.out.println("+MAIN DES - DONE");
		creator.execute(constantsMaster.createTableDESIRES_SPORT);
		System.out.println("+SPORT - DONE");
		creator.execute(constantsMaster.createTableDESIRES_DATING);
		System.out.println("+DATING - DONE");
		creator.execute(constantsMaster.createTableMESSAGES);
		System.out.println("+MESSAGES - DONE");
		creator.execute(constantsMaster.createTableONLINE_DEVICES);
		System.out.println("+IDs - DONE");
		creator.execute(constantsMaster.createTableDESIRE_HISTORY);
		System.out.println("+DESIRE_HISTORY - DONE");
		creator.execute(constantsMaster.createTriggerOnDeletingDesire);
		System.out.println("+DELETEING TRIGGER - DONE");
		creator.execute(constantsMaster.createTableUSERS_COMING_HISTORY);
		System.out.println("+USERS_COMING_HISTORY - DONE");
		creator.execute(constantsMaster.createTriggerOnLoggingIn);
		System.out.println("+LOGGING TRIGGER - DONE");
		creator.execute(constantsMaster.createTriggerOnExiting);
		System.out.println("+EXITING TRIGGER - DONE");
		creator.close();
	}

}
