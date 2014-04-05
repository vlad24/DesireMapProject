package desireMapApplicationPackage.dataBaseConstantsPackage;


public final class dbConstantsMaster {
	public final static String mainDBAdress = "C://Databases/desireMapMain.db";
	public final static String sportTableSuffix = "_SPORT";
	public final static String datingTableSuffix = "_DATING";
	public final static String enableForeignKeys = "PRAGMA FOREIGN_KEYS = ON;";
	public final static String createTableUSERS = "CREATE TABLE IF NOT EXISTS USERS(userID Integer PRIMARY KEY AUTOINCREMENT NOT NULL, login String UNIQUE NOT NULL, password String NOT NULL)";
	public final static String crateTableINFO = "CREATE TABLE IF NOT EXISTS INFO(" +
			"login String UNIQUE NOT NULL REFERENCES users(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"name String NOT NULL, " +
			"sex Char NOT NULL, " +
			"birth String NOT NULL, " +
			"rating Integer" +
			");";
	public final static String createTableDESIRES_MAIN = "CREATE TABLE IF NOT EXISTS DESIRES_MAIN(" +
			"desireID Integer PRIMARY KEY NOT NULL," +
			"login String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"desireDescription String NOT NULL, " +
			"tag String NOT NULL, " +
			"latitude REAL NOT NULL, " +
			"longitude REAL NOT NULL, " +
			"time String NOT NULL" +
			");";
	
	public final static String createTableDESIRES_SPORT = "CREATE TABLE IF NOT EXISTS DESIRES_SPORT(" +
			"desireID Integer PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"sportName String NOT NULL, " +
			"advantages Integer NOT NULL" +
			");";
	public final static String createTableDESIRES_DATING = "CREATE TABLE IF NOT EXISTS DESIRES_DATING(" +
			"desireID Integer PRIMARY KEY NOT NULL REFERENCES DESIRES_MAIN(desireID) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"partnerSex Char NOT NULL, " +
			"partnerAge Integer NOT NULL " +
			"CHECK(partnerAge > 0)" +
			");";
	public final static String createTableMESSAGES = "CREATE TABLE IF NOT EXISTS MESSAGES(" + 
			"messageID Integer PRIMARY KEY NOT NULL," +
			"receiver String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"sender String NOT NULL REFERENCES USERS(login) ON DELETE CASCADE ON UPDATE CASCADE, " +
			"messageText String NOT NULL, " +
			"status Integer, " +
			"time String NOT NULL);";
}
