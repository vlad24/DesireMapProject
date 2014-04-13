package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.dataBaseConstantsPackage.dbConstantsMaster;
import desireMapApplicationPackage.dataBasePackage.DataBaseSQLite;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class DesireInstrument {
	private static DataBaseSQLite desireDataBase;
	public static CategoryTube tube;
	private static HashMap<Integer,String> dataBaseSuffixes;
	public static ResultSetMaster rsMaster;
	//--
	static {
		System.out.println("...LOADING DESIRE INSTRUMENT");
		desireDataBase = new DataBaseSQLite(dbConstantsMaster.mainDBAdress);
		System.out.println("+MAIN DB IS LOADED !!!");
		try {
			desireDataBase.connectToBase();
			Statement creator = desireDataBase.getConnection().createStatement();
			System.out.println("...ENABLING FOREIGN KEYS");
			creator.execute(dbConstantsMaster.enableForeignKeys);
			System.out.println("...CREATING TABLES IN MAIN DB");
			creator.execute(dbConstantsMaster.createTableUSERS);
			System.out.println("+USERS - DONE");
			creator.execute(dbConstantsMaster.crateTableINFO);
			System.out.println("+INFO - DONE");
			creator.execute(dbConstantsMaster.createTableDESIRES_MAIN);
			System.out.println("+MAIN DES - DONE");
			creator.execute(dbConstantsMaster.createTableDESIRES_SPORT);
			System.out.println("+SPORT - DONE");
			creator.execute(dbConstantsMaster.createTableDESIRES_DATING);
			System.out.println("+DATING - DONE");
			creator.execute(dbConstantsMaster.createTableMESSAGES);
			System.out.println("+MESSAGES - DONE");
			creator.close();
			tube = new CategoryTube();
			System.out.println("+ADDERS - DONE");
			dataBaseSuffixes = new HashMap<Integer, String>();
			dataBaseSuffixes.put(CodesMaster.Categories.SportCode, dbConstantsMaster.sportTableSuffix);
			dataBaseSuffixes.put(CodesMaster.Categories.DatingCode, dbConstantsMaster.datingTableSuffix);
			System.out.println("+HashMapSuffixes - DONE !!!");
			rsMaster = new ResultSetMaster();
			System.out.println("+Initialization - DONE !!!");
		} catch (ClassNotFoundException | SQLException error) {
			System.out.println("-Instrument hasn't managed to initialize the base !!!");
			error.printStackTrace();
		}
	}
	
	public static Connection getAccessToDesireBase(){
		return desireDataBase.getConnection();
	}
	
	public static void turnOffTheBase(){
		desireDataBase.disconnect();
	}
	
	private static String getLikeString(HashSet<String> tiles){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(String tile : tiles){
			System.out.println("+Tile : " + tile);
			builder.append("(tile LIKE '" + tile + "%') OR ");
		}
		int currentLength = builder.length();
		builder.delete(currentLength - 4, currentLength);
		builder.append(") ");
		return builder.toString();
	}
	
	private static String getNotLikeString(HashSet<String> tiles){
		if (tiles != null){
			StringBuilder builder = new StringBuilder("");
			builder.append("(");
			for(String tile : tiles){
				System.out.println("-Tile : " + tile);
				builder.append("(tile NOT LIKE '" + tile + "%') OR ");
			}
			int currentLength = builder.length();
			builder.delete(currentLength - 4, currentLength);
			builder.append(") AND ");
			return builder.toString();
		}
		else
			return "";
	}
	
	public static void registerAtDB(RegistrationData regData) throws Exception {
		String login = regData.login;
		String password = regData.password;
		String name = regData.name;
		char sex = regData.sex;
		String birth = regData.birth;
    	try(Statement inserter = DesireInstrument.getAccessToDesireBase().createStatement()){
    		inserter.execute("INSERT INTO USERS(login, password) VALUES('" + login + "', '" + password + "')");                
    		inserter.execute("INSERT INTO INFO (login, name, sex, birth, rating) VALUES('" + login + "', '" + name + "', '" + sex + "', '" + birth + "' , 0)");
    	}
    	catch (Exception error){
    		System.out.println("-SQL error at register");
    		throw error;
    	}  
	}
	
	public static void logInAtDB(String login, String password) throws Exception{  
		try(Statement selector = DesireInstrument.getAccessToDesireBase().createStatement()){
			System.out.println("...Trying to find the user");
			ResultSet setAfterSelection = selector.executeQuery("SELECT * FROM USERS WHERE login = '" + login + "' AND password = '" + password + "'");
			System.out.println("...Selection completed, lets's see");
			if (setAfterSelection.next()){
				System.out.println("+Success");
			}
			else{
				System.out.println("-Fail");
				throw new Exception("-Nothing found in database");
			}
		}
		catch (SQLException error){
			System.out.println("-SQL error at logging in");
			throw error;
		}
	}
	
	public static String addDesireAtDB(AddPack pack) throws Exception {
			StringBuilder builder = new StringBuilder();
			String user = pack.desireContent.login;
			Calendar calendar = Calendar.getInstance();
    		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    		builder.append("d:");
    		builder.append(user);
    		builder.append(sdf.format(calendar.getTime()));
    		String thisDesireID = builder.toString();
			System.out.println("+ID has been generated :" + thisDesireID);
			tube.tryToAdd(thisDesireID, pack);
			return thisDesireID;
	}
	
	//loaded tiles can be null
	public static SatisfySet getSatisfiersAtDB(String desireID, Integer categoryCode, HashSet<String> tilesToScan, HashSet<String> tilesToIgnore){
		if (categoryCode != null){
			String suffix = dataBaseSuffixes.get(categoryCode);
			String tileLikeCondition = getLikeString(tilesToScan);
			String tileNotLikeCondition = getNotLikeString(tilesToIgnore);
			String timeCondition = "(time > datetime('now', '-120 hours')) ";
			try(Statement selector = getAccessToDesireBase().createStatement()){
				String fullQuery = "SELECT * FROM DESIRES_MAIN inner join DESIRES"+ suffix + " using (desireID) WHERE desireID = " + desireID + ";";
				ResultSet centerSet = selector.executeQuery(fullQuery);
				switch(categoryCode){
				case(CodesMaster.Categories.SportCode):{
					System.out.println("Sport query is gonna be launched");
					String sport = centerSet.getString("sport");
					Integer adv = centerSet.getInt("advantages");
					centerSet.close();
					String sportQuery = "SELECT * FROM ((DESIRES_SPORT inner join DESIRES_MAIN using (desireID)) inner join INFO using (login)) WHERE " + tileNotLikeCondition + tileLikeCondition + "AND (" + timeCondition + ") AND ABS(advantages - " + adv +") < 2) AND (sport LIKE '%" + sport + "%');";
					ResultSet satSet =  selector.executeQuery(sportQuery);
					System.out.println(sportQuery);
					SatisfySet result = rsMaster.convertToSatisfySetAndClose(satSet, categoryCode);
					return result;
				}
				case(CodesMaster.Categories.DatingCode):{
					System.out.println("Dating query is gonna be launched");
					char pSex = centerSet.getString("partnerSex").charAt(0);
					Integer pAge = centerSet.getInt("partnerAge");
					centerSet.close();
					String datingQuery = "SELECT * FROM ((DESIRES_DATING inner join DESIRES_MAIN using (desireID)) inner join INFO using (login)) WHERE" + tileNotLikeCondition + tileLikeCondition + " AND (" + timeCondition + ") AND (sex = '" + pSex + "');";
					System.out.println(datingQuery);
					ResultSet satSet =  selector.executeQuery(datingQuery);
					SatisfySet result = rsMaster.convertToSatisfySetAndClose(satSet, categoryCode);
					return result;
				}
				default:{
					return null;
				}
				}

			}
			catch(Exception error){
				System.out.println("-Error at get satisfiers : " + error.getMessage());
				return null;
			}
		}
		else{
			System.out.println("-Category is wrong");
			return null;
		}
	}
	
	public static DesireSet getPersonalDesiresAtDB(String login, int searchCategory) throws Exception{
		String category = dataBaseSuffixes.get(searchCategory);
		try (Statement selector = getAccessToDesireBase().createStatement()){
			System.out.println("select * from (DESIRES" + category +" inner join DESIRES_MAIN using (desireID)) as DESIRES inner join USERS using (login) " +
					"where USERS.LOGIN = '" + login + "' AND datetime(TIME) > datetime('now', '-5 hours')");
			ResultSet rsDesires = selector.executeQuery("select * from (DESIRES" + category +" inner join DESIRES_MAIN using (desireID)) as DESIRES inner join USERS using (login) " +
					"where USERS.LOGIN = '" + login + "' AND datetime(TIME) > datetime('now', '-5 hours')");
			DesireSet result = rsMaster.convertToDesireSetAndClose(rsDesires, searchCategory);
			return result;
		} catch (SQLException error){
			System.out.println("-SQL error at getPersonalDesires");
			throw error;
		}
	}
	
	public static MainData getInfoAtDB(String login) throws Exception{
		try(Statement selector = getAccessToDesireBase().createStatement()){
			ResultSet info = selector.executeQuery("select USERS.LOGIN, NAME, SEX, BIRTH, RATING from USERS inner join INFO using (LOGIN) where USERS.LOGIN = '" + login + "';");
			MainData data = DesireInstrument.rsMaster.convertToMainDataAndClose(info);
		    return data;
		}
		catch(Exception error){
			System.out.println("-SQL error at get info");
			throw error;
		}
	}

	public static void deleteAtDB(DeletePack delPack) throws Exception{
		String setString = delPack.contents;
		System.out.println("delete from DESIRES_MAIN where desireID in (" + setString + ");");
		try(Statement deleter = getAccessToDesireBase().createStatement()){
			deleter.execute("delete from DESIRES_MAIN where desireID in (" + setString + ");");
		}
		catch(Exception error){
			System.out.println("-SQL error at deleting");
			System.out.println(error.getMessage());
			throw error;
		}
	}

	public static Integer getCategoryTableByID(String desireID) throws SQLException {
		try(Statement selector = getAccessToDesireBase().createStatement()){
			String catQuery = "SELECT category FROM DESIRES_MAIN WHERE desireID = " + desireID + ";";
			ResultSet catSet = selector.executeQuery(catQuery);
			if (catSet.next()){
				Integer categoryCode = catSet.getInt("category");
				catSet.close();
				return categoryCode;
			}
			else {
				return null;
			}
		}
	}



}
