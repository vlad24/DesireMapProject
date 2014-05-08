package desireMapApplicationPackage.dataBasePackage;
import java.sql.*;

public class DataBaseSQLite extends DataBase{
	public DBSQLiteConstantsMaster constantsMaster;
	/////////
	public DataBaseSQLite(String name) {
		driver = "org.sqlite.JDBC";
		baseName = name;
		connection = null;
		constantsMaster = new DBSQLiteConstantsMaster();
		System.out.println("+Constants master is created");
	}

	public void connectToBase() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		connection = DriverManager.getConnection("jdbc:sqlite:" + baseName);
		turnedOn = true;
		System.out.println("+Connected to base " + baseName);
	}

	@Override
	public void init() throws SQLException {
		Statement creator = connection.createStatement();
		System.out.println("...ENABLING FOREIGN KEYS");
		creator.execute(constantsMaster.enableForeignKeys);
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
		creator.execute(constantsMaster.createTableDESIRES_CONTROL);
		System.out.println("+DESIRES_CONTROL - DONE");
		creator.execute(constantsMaster.createTriggerOnDeletingDesire);
		System.out.println("+DELETEING TRIGGER - DONE");
		creator.execute(constantsMaster.createTableUSERS_COMING_CONTROL);
		System.out.println("+USERS_COMING_HISTORY - DONE");
		creator.execute(constantsMaster.createTriggerOnLoggingIn);
		System.out.println("+LOGGING TRIGGER - DONE");
		creator.execute(constantsMaster.createTriggerOnExiting);
		System.out.println("+EXITING TRIGGER - DONE");
		creator.execute(constantsMaster.cleanOnline);
		System.out.println("+ONLINE CLEANING - DONE");
		creator.execute(constantsMaster.createTableLIKES_CONTROL);
		System.out.println("+LIKES_CONTROL - DONE");
		creator.execute(constantsMaster.createTriggerOnLike);
		System.out.println("+LIKES TRIGGER - DONE");
		creator.execute(constantsMaster.createTriggerOnDislike);
		System.out.println("+DISLIKES TRIGGER - DONE");
		creator.execute(constantsMaster.createViewAges);
		System.out.println("+VIEW AGES - DONE");
		creator.execute(constantsMaster.createViewDesiresDatingExact);
		System.out.println("+VIEW DesiresDatingExact - DONE");
		creator.execute(constantsMaster.createViewDesiresSportExact);
		System.out.println("+VIEW DesiresSportExact - DONE");
		creator.execute(constantsMaster.createViewDesiresDatingExactInfo);
		System.out.println("+VIEW DesiresDatingExactInfo - DONE");
		creator.execute(constantsMaster.createViewDesiresSportExactInfo);
		System.out.println("+VIEW DesiresSportExactInfo - DONE");
		creator.close();
	}

}
