package desireMapApplicationPackage.dataBasePackage;

public class DBSQLiteConstantsMaster extends GeneralDBConstantsMaster{
	public String createViewAges = "create view if not exists AGES AS" +
			" SELECT login, (strftime('%Y', 'now') - strftime('%Y', birth))" +
			" - (strftime('%m-%d', 'now') < strftime('%m-%d', birth)) AS age" +
			" from INFO";

	public String enableForeignKeys = "PRAGMA FOREIGN_KEYS = ON;";
	public String createTableUSERS = "CREATE TABLE IF NOT EXISTS USERS(userID Integer PRIMARY KEY AUTOINCREMENT NOT NULL, login String UNIQUE NOT NULL, password String NOT NULL)";
	public String createTableINFO = "CREATE TABLE IF NOT EXISTS INFO(" +
			"login String UNIQUE NOT NULL REFERENCES users(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"name String NOT NULL, " +
			"sex Char NOT NULL, " +
			"birth String NOT NULL, " +
			"rating Integer" +
			");";
	public String createTableDESIRES_MAIN = "CREATE TABLE IF NOT EXISTS DESIRES_MAIN(" +
			"desireID String PRIMARY KEY NOT NULL," +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"desireDescription String NOT NULL, " +
			"category Integer NOT NULL, " +
			"latitude Real NOT NULL, " +
			"longitude Real NOT NULL, " +
			"tile String NOT NULL, " +
			"time String NOT NULL, " +
			"likesAmount INT NOT NULL" +
			");";

	public String createTableDESIRES_SPORT = "CREATE TABLE IF NOT EXISTS DESIRES_SPORT(" +
			"desireID String PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"sportName String NOT NULL, " +
			"advantages Integer NOT NULL" +
			");";
	public String createTableDESIRES_DATING = "CREATE TABLE IF NOT EXISTS DESIRES_DATING(" +
			"desireID String PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"partnerSex Char NOT NULL, " +
			"partnerAgeFrom Integer NOT NULL, " +
			"partnerAgeTo Integer NOT NULL " +
			"CHECK(partnerAgeFrom <= partnerAgeTo)" +
			");";
	public String createTableMESSAGES = "CREATE TABLE IF NOT EXISTS MESSAGES(" + 
			"messageID String PRIMARY KEY NOT NULL," +
			"sender String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"receiver String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"messageText String NOT NULL, " +
			"status Integer NOT NULL, "+
			"time String NOT NULL);";

	public  String createTableONLINE_DEVICES  =  "CREATE table IF NOT EXISTS ONLINE_DEVICES ( " +
			"owner String UNIQUE NOT NULL REFERENCES USERS(login), " +
			"ID String UNIQUE NOT NULL)";

	public String createTableDESIRE_HISTORY  =  "CREATE table IF NOT EXISTS DESIRE_HISTORY ( " +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"desireDescription String NOT NULL, " +
			"category Integer NOT NULL, " +
			"latitude Real NOT NULL, " +
			"longitude Real NOT NULL, " +
			"actionTime String NOT NULL " +
			");";

	public String createTriggerOnDeletingDesire = "CREATE trigger IF NOT EXISTS desireDeleted " +
			"AFTER DELETE " +
			"ON DESIRES_MAIN " +
			"BEGIN" +
			"     INSERT INTO DESIRE_HISTORY VALUES (OLD.login, OLD.desireDescription, OLD.category, OLD.latitude, OLD.longitude, datetime('now'));" +
			"END";

	public String createTableUSERS_COMING_HISTORY = "CREATE TABLE IF NOT EXISTS USERS_COMING_HISTORY (" +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE," +
			"ID String NOT NULL," +
			"action Char NOT NULL," +
			"actionTime String NOT NULL" +
			");"
			;
	public String createTriggerOnLoggingIn = "CREATE trigger IF NOT EXISTS loggedIn " +
			"AFTER INSERT " +
			"ON ONLINE_DEVICES " +
			"BEGIN" +
			"     INSERT OR REPLACE INTO USERS_COMING_HISTORY VALUES (NEW.owner, NEW.ID , '+', datetime('now'));" +
			"END";

	public String createTriggerOnExiting = "CREATE trigger IF NOT EXISTS exited AFTER DELETE ON ONLINE_DEVICES " +
			"	  BEGIN" +
			"	    INSERT OR REPLACE INTO USERS_COMING_HISTORY VALUES (OLD.owner, OLD.ID , '-', datetime('now'));" +
			"	  END;";


	public String cleanOnline = "delete from ONLINE_DEVICES";
}
