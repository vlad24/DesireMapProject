package desireMapApplicationPackage.dataBasePackage;

public class DBSQLiteConstantsMaster extends GeneralDBConstantsMaster{
	public final String createViewAges = "create view if not exists AGES AS" +
			" SELECT login, (strftime('%Y', 'now') - strftime('%Y', birth))" +
			" - (strftime('%m-%d', 'now') < strftime('%m-%d', birth)) AS age" +
			" from INFO";

	public final String enableForeignKeys = "PRAGMA FOREIGN_KEYS = ON;";
	public final String createTableUSERS = "CREATE TABLE IF NOT EXISTS USERS(userID Integer PRIMARY KEY AUTOINCREMENT NOT NULL, login String UNIQUE NOT NULL, password String NOT NULL)";
	public final String createTableINFO = "CREATE TABLE IF NOT EXISTS INFO(" +
			"login String UNIQUE NOT NULL REFERENCES users(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"name String NOT NULL, " +
			"sex Char NOT NULL, " +
			"birth String NOT NULL, " +
			"rating Integer" +
			");";
	public final String createTableDESIRES_MAIN = "CREATE TABLE IF NOT EXISTS DESIRES_MAIN(" +
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

	public final String createTableDESIRES_SPORT = "CREATE TABLE IF NOT EXISTS DESIRES_SPORT(" +
			"desireID String PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"sportName String NOT NULL, " +
			"advantages Integer NOT NULL" +
			");";
	public final String createTableDESIRES_DATING = "CREATE TABLE IF NOT EXISTS DESIRES_DATING(" +
			"desireID String PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"partnerSex Char NOT NULL, " +
			"partnerAgeFrom Integer NOT NULL, " +
			"partnerAgeTo Integer NOT NULL " +
			"CHECK(partnerAgeFrom <= partnerAgeTo)" +
			");";
	public final String createTableMESSAGES = "CREATE TABLE IF NOT EXISTS MESSAGES(" + 
			"messageID String PRIMARY KEY NOT NULL," +
			"sender String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"receiver String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"messageText String NOT NULL, " +
			"status Integer NOT NULL, "+
			"time String NOT NULL);";

	public  String createTableONLINE_DEVICES  =  "CREATE table IF NOT EXISTS ONLINE_DEVICES ( " +
			"owner String UNIQUE NOT NULL REFERENCES USERS(login), " +
			"ID String UNIQUE NOT NULL)";

	public final String createTableDESIRES_CONTROL  =  "CREATE table IF NOT EXISTS DESIRES_CONTROL ( " +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"desireDescription String NOT NULL, " +
			"category Integer NOT NULL, " +
			"latitude Real NOT NULL, " +
			"longitude Real NOT NULL, " +
			"time String NOT NULL " +
			");";

	public final String createTriggerOnDeletingDesire = "CREATE trigger IF NOT EXISTS desireDeleted " +
			"AFTER DELETE " +
			"ON DESIRES_MAIN " +
			"BEGIN" +
			"     INSERT INTO DESIRES_CONTROL VALUES (OLD.login, OLD.desireDescription, OLD.category, OLD.latitude, OLD.longitude, datetime('now'));" +
			"END";

	public final String createTableUSERS_COMING_CONTROL = "CREATE TABLE IF NOT EXISTS USERS_COMING_CONTROL (" +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE," +
			"ID String NOT NULL," +
			"action Char NOT NULL," +
			"actionTime String NOT NULL" +
			");"
			;
	public final String createTriggerOnLoggingIn = "CREATE trigger IF NOT EXISTS loggedIn " +
			"AFTER INSERT " +
			"ON ONLINE_DEVICES " +
			"BEGIN" +
			"     INSERT OR REPLACE INTO USERS_COMING_CONTROL VALUES (NEW.owner, NEW.ID , '+', datetime('now'));" +
			"END";

	public final String createTriggerOnExiting = "CREATE trigger IF NOT EXISTS exited AFTER DELETE ON ONLINE_DEVICES " +
			"	  BEGIN" +
			"	    INSERT OR REPLACE INTO USERS_COMING_CONTROL VALUES (OLD.owner, OLD.ID , '-', datetime('now'));" +
			"	  END;";


	public final String cleanOnline = "delete from ONLINE_DEVICES";
	
	public final String createTableLIKES_CONTROL  =  "CREATE table IF NOT EXISTS LIKES_CONTROL ( " +
			"desireID String NOT NULL REFERENCES DESIRES_MAIN(desireID), " +
			"login String NOT NULL REFERENCES USERS(login)," +
			"likeTime String NOT NULL," +
			"UNIQUE(desireID, login))";
	
	public final String createTriggerOnLike = "CREATE TRIGGER IF NOT EXISTS liked AFTER INSERT ON LIKES_CONTROL " +
			"	  BEGIN" +
			"	    UPDATE DESIRES_MAIN SET likesAmount = (likesAmount + 1) WHERE (desireID = NEW.desireID);" +
			"       UPDATE INFO SET rating = (rating + 1) WHERE (login = (select login from DESIRES_MAIN where (desireID = NEW.desireID)));" +
			"	  END;";
	
	public final String createTriggerOnDislike = "CREATE trigger IF NOT EXISTS disliked AFTER DELETE ON LIKES_CONTROL " +
			"	  BEGIN" +
			"	     UPDATE DESIRES_MAIN SET likesAmount = (likesAmount - 1) WHERE desireID = OLD.desireID;" +
			"        UPDATE INFO SET rating = (rating + 1) WHERE (login = (select login from DESIRES_MAIN where (desireID = NEW.desireID)));" + 
			"	  END;";
	

}
