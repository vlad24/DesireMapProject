package desireMapApplicationPackage.dataBasePackage;

public class DBPostgresConstantsMaster{
	public  String createTableUSERS = "CREATE TABLE IF NOT EXISTS USERS(login text UNIQUE NOT NULL, password text NOT NULL)";
	public  String createTableINFO = "CREATE TABLE IF NOT EXISTS INFO(" +
			"login text UNIQUE NOT NULL REFERENCES users(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"name text NOT NULL, " +
			"sex Char NOT NULL, " +
			"birth text NOT NULL, " +
			"rating integer" +
			");";
	public  String createTableDESIRES_MAIN = "CREATE TABLE IF NOT EXISTS DESIRES_MAIN(" +
			"desireID text PRIMARY KEY NOT NULL," +
			"login text NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"desireDescription text NOT NULL, " +
			"category Integer NOT NULL, " +
			"latitude Real NOT NULL, " +
			"longitude Real NOT NULL, " +
			"tile text NOT NULL, " +
			"time text NOT NULL" +
			");";

	public  String createTableDESIRES_SPORT = "CREATE TABLE IF NOT EXISTS DESIRES_SPORT(" +
			"desireID text PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"sportName text NOT NULL, " +
			"advantages Integer NOT NULL" +
			");";
	public  String createTableDESIRES_DATING = "CREATE TABLE IF NOT EXISTS DESIRES_DATING(" +
			"desireID text PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"partnerSex Char NOT NULL, " +
			"partnerAge Integer NOT NULL " +
			"CHECK(partnerAge > 0)" +
			");";
	public  String createTableMESSAGES = "CREATE TABLE IF NOT EXISTS MESSAGES(" + 
			"messageID text PRIMARY KEY NOT NULL," +
			"sender text NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"receiver text NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"messageText text NOT NULL, " +
			"status Integer NOT NULL, "+
			"time text NOT NULL);";

	public  String createTableONLINE_DEVICES  =  "CREATE table IF NOT EXISTS ONLINE_DEVICES ( " +
			"owner text UNIQUE NOT NULL REFERENCES USERS(login), " +
			"ID text UNIQUE NOT NULL)";

	public  String createTableDESIRE_HISTORY  =  "CREATE table IF NOT EXISTS DESIRE_HISTORY ( " +
			"login text NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"desireDescription text NOT NULL, " +
			"category Integer NOT NULL, " +
			"latitude Real NOT NULL, " +
			"longitude Real NOT NULL, " +
			"actionTime text NOT NULL " +
			");";

	public  String createTriggerOnDeletingDesire = "CREATE trigger IF NOT desireDeleted " +
			"AFTER DELETE " +
			"ON DESIRES_MAIN " +
			"BEGIN" +
			"     INSERT INTO DESIRE_HISTORY VALUES (OLD.login, OLD.desireDescription, OLD.category, OLD.latitude, OLD.longitude, datetime('now'));" +
			"END";

	public  String createTableUSERS_COMING_HISTORY = "CREATE TABLE IF NOT EXISTS USERS_COMING_HISTORY (" +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE," +
			"ID String NOT NULL," +
			"action Char NOT NULL," +
			"actionTime String NOT NULL" +
			");"
			;
	public  String createTriggerOnLoggingIn = "CREATE trigger IF NOT EXISTS loggedIn " +
			"AFTER INSERT " +
			"ON ONLINE_DEVICES " +
			"BEGIN" +
			"     INSERT OR REPLACE INTO USERS_COMING_HISTORY VALUES (NEW.owner, NEW.ID , '+', datetime('now'));" +
			"END";

	public  String createTriggerOnExiting = "CREATE trigger IF NOT EXISTS exited AFTER DELETE ON ONLINE_DEVICES " +
			"	  BEGIN" +
			"	    INSERT OR REPLACE INTO USERS_COMING_HISTORY VALUES (OLD.owner, OLD.ID , '-', datetime('now'));" +
			"	  END;";
}
