package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.dataBasePackage.DataBase;
import desireMapApplicationPackage.dataBasePackage.DataBaseSQLite;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.AndroidData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class DesireInstrument {
	//---------------------------------------------------------------//
	//Singleton implementation
	private static DesireInstrument uniqueInstrument;
	//Different databases and implementations for them
	private InstrumentImplementation implementationObject;
	protected DataBase desireDataBase;
	//Common parsers,handlers and constant-maps
	protected ResultSetMaster rsMaster;
	protected CategoryTube tube;
	protected HashMap<Integer,String> dataBaseSuffixes;
	//---------------------------------------------------------------//

	public static void prepareInstrument(){
		uniqueInstrument = new DesireInstrument();
	}

	private DesireInstrument(){
		System.out.println("...LOADING DESIRE INSTRUMENT");
		//DB setting here!!!!!!!!!!!!!!
		/*desireDataBase = new DataBasePostgreSQL("desireMapTest");*/
		desireDataBase = new DataBaseSQLite("C:/Databases/desireMapMain.db");
		System.out.println("+MAIN DB IS LOADED !!!");
		implementationObject = new InstrumentImplementationSQLite(this);
		System.out.println("+Implementation to the particular instrument is set");
		implementationObject.initInstrument();
		System.out.println("+ImplementationObject has inited the base");
	}
	
	public static DesireInstrument getInstance(){
		if (uniqueInstrument != null){
			return uniqueInstrument;
		}
		else{
			//Insurance that null won't be returned
			uniqueInstrument = new DesireInstrument();
			return uniqueInstrument;
		}
	}
	public static void throwAwayAndCleanBase(){
		if (uniqueInstrument != null){
			uniqueInstrument.turnOffTheBase();
		}
	}

	public Connection getAccessToDesireBase(){
		return implementationObject.getAccessToDesireBase();
	}

	public void turnOffTheBase(){
		implementationObject.turnOffTheBase();
	}

	public  void registerAtDB(RegistrationData regData) throws Exception {
		implementationObject.registerAtDB(regData);
	}

	public  void handleAndroidData(String login, AndroidData androidData) throws Exception{
		implementationObject.handleAndroidData(login, androidData);
	}

	public  void logInAtDB(String login, String password) throws Exception{  
		implementationObject.logInAtDB(login, password);
	}

	public  String addDesireAtDB(AddPack pack) throws Exception {
		return implementationObject.addDesireAtDB(pack);
	}

	//loaded tiles can be null
	public  SatisfySet getSatisfiersAtDB(String desireID, Integer categoryCode, HashSet<String> tilesToScan, HashSet<String> tilesToIgnore){
		return implementationObject.getSatisfiersAtDB(desireID, categoryCode, tilesToScan, tilesToIgnore);
	}

	public  DesireSet getPersonalDesiresAtDB(String login, int searchCategory) throws Exception{
		return implementationObject.getPersonalDesiresAtDB(login, searchCategory);
	}

	public MainData getInfoAtDB(String login) throws Exception{
		return implementationObject.getInfoAtDB(login);
	}

	public  void deleteAtDB(DeletePack delPack) throws Exception{
		implementationObject.deleteAtDB(delPack);
	}

	public  Integer getCategoryTableByID(String desireID) throws SQLException {
		return implementationObject.getCategoryTableByID(desireID);
	}

	public  void cleanBaseOnExit(String userName, String deviceID) {
		try (Statement deleter = getAccessToDesireBase().createStatement()){
			implementationObject.cleanBaseOnExit(userName, deviceID);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public void insertMessageIntoDB(String messageID, ClientMessage message, int status) throws Exception {
		implementationObject.insertMessageIntoDB(messageID, message, status);
	}

	public MessageSet getAndMarkUndeliveredMessagesForUserAtDB(String login) throws SQLException{
		return implementationObject.getUndeliveredMessageForUserAtDB(login);
	}

	public void unmarkCurrentUndeliveredMessage(MessageSet set, int index) {
		implementationObject.makeCurrentMessageFresh(set, index);
	}

	public String getOnlineReceiverIDAtDB(String receiver) {
		return implementationObject.getOnlineReceiverIDAtDB(receiver);
	}

	public MessageSet getOldMessagesForUserAtDB(String from, String to, int hoursRadius) throws Exception {
		return implementationObject.getOldMessagesForUserAtDB(from, to, hoursRadius);
	}

	public void unmarkAllDeliveredMessages(String userName) {
		implementationObject.unmarkAllDeliveredMessages(userName);
	}

	public UserSet getUsersTalkedToAtDB(String userName) throws Exception {
		return implementationObject.getUsersTalkedToAtDB(userName);
	}

	public void likeDesireAtDB(String desireID, boolean isLiked){
		implementationObject.likeDesireAtDB(desireID, isLiked);
	}


}
