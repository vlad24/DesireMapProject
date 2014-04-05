package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.dataBaseConstantsPackage.dbConstantsMaster;
import desireMapApplicationPackage.dataBasePackage.DataBaseSQLite;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
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
			System.out.println("+DATING - DONE");
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
	
	public static void addDesireAtDB(DesireContent content) throws Exception {
			int thisDesireID = DesireIDGeneratorSingleton.getInstance().getNextID();
			System.out.println("ID has been generated");
			tube.tryToAdd(thisDesireID, content);
	}
	
	public static SatisfySet getSatisfiersAtDB(){
		// TODO !
		return null;
	}
	
	//! can replace to SQLException only
	public static DesireSet getPersonalDesiresAtDB(String login, Cryteria cryteria) throws Exception{
		String category = dataBaseSuffixes.get(cryteria.searchCategory);
		Integer timeDelta = cryteria.timeRadius;
		//timeDelta = 2100000;
		try (Statement selector = getAccessToDesireBase().createStatement()){
			System.out.println("select * from (DESIRES" + category +" inner join DESIRES_MAIN using (desireID)) as DESIRES inner join USERS using (login) " +
					"where USERS.LOGIN = '" + login + "' AND datetime(TIME) > datetime('now', '" + timeDelta.toString() + " hours')");
			ResultSet rsDesires = selector.executeQuery("select * from (DESIRES" + category +" inner join DESIRES_MAIN using (desireID)) as DESIRES inner join USERS using (login) " +
					"where USERS.LOGIN = '" + login + "' AND datetime(TIME) > datetime('now', '-" + timeDelta.toString() + " hours')");
			DesireSet result = rsMaster.convertToDesireSetAndClose(rsDesires, cryteria.searchCategory);
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


}
