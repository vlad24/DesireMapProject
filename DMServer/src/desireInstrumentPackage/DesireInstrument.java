package desireInstrumentPackage;

import java.sql.*;
import dataBasePackage.DataBaseSQLite;
import desiresPackage.Desire;

public class DesireInstrument {
	
	protected State stateObject;
	private static DataBaseSQLite desireDataBase;
	//--
	static {
		desireDataBase = new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/desireMapMain.db");
		System.out.println("MAIN BD IS LOADED");
		try {
			desireDataBase.connectToBase();
			Statement creator = desireDataBase.getConnection().createStatement();
			//Creating table of logins-passwords (id information)
			creator.execute("CREATE TABLE IF NOT EXISTS USERS(LOGIN TEXT PRIMARY KEY, PASSWORD TEXT NOT NULL)");
			//Creating table of login-name-sex-birth (personal information)
			creator.execute("CREATE TABLE IF NOT EXISTS INFO(LOGIN TEXT PRIMARY KEY, NAME TEXT NOT NULL, SEX TEXT NOT NULL, BIRTH TEXT NOT NULL)");
			//Creating table of login-desire (DESIRES) ((TEMPORARY))
			creator.execute("CREATE TABLE IF NOT EXISTS DESIRES(LOGIN TEXT NOT NULL, DESIRE TEXT NOT NULL,"
					+ " TAG TEXT NOT NULL, LATITUDE REAL NOT NULL, LONGITUDE REAL NOT NULL , TIME TEXT NOT NULL)");
			creator.execute("CREATE TABLE IF NOT EXISTS DESIRES_SPORT(LOGIN TEXT NOT NULL, DESIRE TEXT NOT NULL,"
					+ " TAG TEXT NOT NULL, LATITUDE REAL NOT NULL, LONGITUDE REAL NOT NULL, TIME TEXT NOT NULL )");
			creator.execute("CREATE TABLE IF NOT EXISTS DESIRES_DATING (LOGIN TEXT NOT NULL, DESIRE TEXT NOT NULL,"
					+ " TAG TEXT NOT NULL, LATITUDE REAL NOT NULL, LONGITUDE REAL NOT NULL, TIME TEXT NOT NULL )");
			creator.close();
		} catch (ClassNotFoundException | SQLException error) {
			error.printStackTrace();
		}
	}
	
	protected static Connection getAccessToDesireBase(){
		return desireDataBase.getConnection();
	}
	
	public static void turnOffTheBase(){
		desireDataBase.disconnect();
	}
	
	public DesireInstrument(){
		stateObject = new StateStart(this);
	}
	
	public void register(String login, String password, String name, String sex, String birthdate) throws Exception {
		stateObject.register(login, password, name, sex, birthdate);
	}
	
	public void logIn(String login, String password) throws Exception{
		stateObject.logIn(login, password);
	}
	
	public void addDesire(Desire desire) throws Exception {
		stateObject.addDesire(desire);
	}
	
	public ResultSet getDesires(String login, String category) throws Exception{
		return stateObject.getDesires(login, category);
	}
	
	public ResultSet getInfo(String login) throws Exception{
		return stateObject.getInfo(login);
	}
	
	public ResultSet getSatisfiersToday(Desire desire, String radius) throws Exception{
		return stateObject.getSatisfiersToday(desire, radius);
	}
	
	public void exit(){
		stateObject.exit();
	}
	
}
