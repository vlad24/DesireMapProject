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
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.dataBasePackage.DataBaseSQLite;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.AndroidData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public abstract class InstrumentImplementation{

	protected DesireInstrument owner;
	//---------------------------------------------------------//
	public InstrumentImplementation(DesireInstrument newOwner){
		owner = newOwner;
	}

	//These methods are implemented in different ways in different drivers
	public abstract void initInstrument();
	public abstract void makeCurrentMessageFresh(MessageSet set, int index);

	public Connection getAccessToDesireBase(){
		return owner.desireDataBase.getConnection();
	}

	public void turnOffTheBase(){
		owner.desireDataBase.disconnect();
	}

	public String getLikeString(HashSet<String> tiles){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(String tile : tiles){
			System.out.println("+Tile : " + tile);
			builder.append("(tile LIKE '" + tile + "%') OR ");
		}
		int currentLength = builder.length();
		builder.delete(currentLength - 4, currentLength);
		builder.append(") ");
		System.out.println(builder.toString());
		return builder.toString();
	}

	public String getNotLikeString(HashSet<String> tiles){
		if (tiles != null){
			StringBuilder builder = new StringBuilder("");
			builder.append("(");
			for(String tile : tiles){
				System.out.println("-Tile : " + tile);
				builder.append("(tile NOT LIKE '" + tile + "%') AND ");
			}
			int currentLength = builder.length();
			builder.delete(currentLength - 4, currentLength);
			builder.append(") AND ");
			return builder.toString();
		}
		else
			return "";
	}

	public String getDesireID(AddPack pack){
		StringBuilder builder = new StringBuilder();
		String user = pack.desireContent.login;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		builder.append("d:");
		builder.append(user + ":");
		builder.append(builder.hashCode() + ":");
		builder.append(sdf.format(calendar.getTime()));
		String thisDesireID = builder.toString();
		return thisDesireID;
	}

	public Integer getCategoryTableByID(String desireID) throws SQLException {
		try(Statement selector = getAccessToDesireBase().createStatement()){
			String catQuery = "SELECT category FROM DESIRES_MAIN WHERE desireID = '" + desireID + "';";
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
	////////
	public void registerAtDB(RegistrationData regData) throws Exception {
		String login = regData.login;
		String password = regData.password;
		String name = regData.name;
		char sex = regData.sex;
		String birth = regData.birth;
		try(Statement inserter = getAccessToDesireBase().createStatement()){
			inserter.execute("INSERT INTO USERS(login, password) VALUES('" + login + "', '" + password + "')");                
			inserter.execute("INSERT INTO INFO (login, name, sex, birth, rating) VALUES('" + login + "', '" + name + "', '" + sex + "', '" + birth + "' , 0)");
		}
		catch (Exception error){
			System.out.println("-SQL error at register");
			throw error;
		}  
	}


	public void logInAtDB(String login, String password) throws Exception{ 
		System.out.println("...trying to log in");
		try(Statement logIner = getAccessToDesireBase().createStatement()){
			System.out.println("...Trying to find the user");
			ResultSet setAfterSelection = logIner.executeQuery("SELECT * FROM USERS WHERE login = '" + login + "' AND password = '" + password + "'");
			System.out.println("...Selection completed, lets's see");
			if (setAfterSelection.next()){
				System.out.println("+Log in -- Success");
			}
			else{
				throw new Exception("-User hasn't been found");
			}
		}
		catch (SQLException error){
			System.out.println("-SQL error at logging in");
			throw error;
		}
	}

	public void handleAndroidData(String login, AndroidData androidData) throws Exception{
		Statement androider = getAccessToDesireBase().createStatement();
		androider.executeUpdate("INSERT OR ABORT INTO ONLINE_DEVICES VALUES('" + login + "', '" + androidData.regID + "')");
		System.out.println("+device id -- Success(inserted or replaced)");
	}

	public String addDesireAtDB(AddPack pack) throws Exception {
		String thisDesireID = getDesireID(pack);
		System.out.println("+ID has been generated :" + thisDesireID);
		owner.tube.tryToAdd(thisDesireID, pack);
		return thisDesireID;
	}

	public SatisfySet getSatisfiersAtDB(String desireID, Integer categoryCode, HashSet<String> tilesToScan, HashSet<String> tilesToIgnore){
		if (categoryCode != null){
			String suffix = owner.dataBaseSuffixes.get(categoryCode);
			String tileLikeCondition = getLikeString(tilesToScan);
			String tileNotLikeCondition = getNotLikeString(tilesToIgnore);
			String timeCondition = "(time > datetime('now', '-120 hours')) ";
			try(Statement selector = getAccessToDesireBase().createStatement()){
				String fullQuery = "SELECT * FROM DESIRES_MAIN inner join DESIRES"+ suffix + " using (desireID) WHERE desireID = '" + desireID + "';";
				ResultSet centerSet = selector.executeQuery(fullQuery);
				switch(categoryCode){
				case(CodesMaster.Categories.SportCode):{
					System.out.println("Sport query is gonna be launched");
					String login = centerSet.getString("login");
					String sport = centerSet.getString("sport");
					String adv = centerSet.getString("advantages");
					centerSet.close();
					String sportQuery = "SELECT * FROM ((DESIRES_SPORT inner join DESIRES_MAIN using (desireID)) inner join INFO using (login)) WHERE " + 
							tileNotLikeCondition + tileLikeCondition +
							" AND (" + timeCondition + ") " + 
							" AND (advantages LIKE %" + adv + "%) " + 
							" AND (sport LIKE '%" + sport + "%') " + 
							" AND (login != '" + login + 
							"');";
					System.out.println(sportQuery);
					ResultSet satSet =  selector.executeQuery(sportQuery);
					SatisfySet result = owner.rsMaster.convertToSatisfySetAndClose(satSet, categoryCode);
					return result;
				}
				case(CodesMaster.Categories.DatingCode):{
					System.out.println("Dating query is gonna be launched");
					String login = centerSet.getString("login");
					char pSex = centerSet.getString("partnerSex").charAt(0);
					Integer pAgeFrom = centerSet.getInt("partnerAgeFrom");
					Integer pAgeTo = centerSet.getInt("partnerAgeTo");
					centerSet.close();
					String datingQuery = "SELECT * FROM (((DESIRES_DATING inner join DESIRES_MAIN using (desireID)) inner join INFO using (login)) inner join AGES using(login)) WHERE" +
							tileNotLikeCondition + tileLikeCondition +
							" AND (" + timeCondition + ") " + 
							" AND (sex = '" + pSex + "')" + 
							" AND (login != '" + login + "')" +
							" AND (age BETWEEN " + pAgeFrom +" AND " + pAgeTo + ");";
					System.out.println(datingQuery);
					ResultSet satSet =  selector.executeQuery(datingQuery);
					SatisfySet result = owner.rsMaster.convertToSatisfySetAndClose(satSet, categoryCode);
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

	public DesireSet getPersonalDesiresAtDB(String login, int searchCategory) throws Exception{
		String category = owner.dataBaseSuffixes.get(searchCategory);
		try (Statement selector = getAccessToDesireBase().createStatement()){
			System.out.println("select * from (DESIRES" + category +" inner join DESIRES_MAIN using (desireID)) as DESIRES inner join USERS using (login) " +
					"where USERS.LOGIN = '" + login + "' AND datetime(TIME) > datetime('now', '-5 hours')");
			ResultSet rsDesires = selector.executeQuery("select * from (DESIRES" + category +" inner join DESIRES_MAIN using (desireID)) as DESIRES inner join USERS using (login) " +
					"where USERS.LOGIN = '" + login + "' AND datetime(TIME) > datetime('now', '-5 hours')");
			DesireSet result = owner.rsMaster.convertToDesireSetAndClose(rsDesires, searchCategory);
			return result;
		} catch (SQLException error){
			System.out.println("-SQL error at getPersonalDesires");
			throw error;
		}
	}

	public MainData getInfoAtDB(String login) throws Exception{
		try(Statement selector = getAccessToDesireBase().createStatement()){
			ResultSet info = selector.executeQuery("select USERS.LOGIN, NAME, SEX, BIRTH, RATING from USERS inner join INFO using (LOGIN) where USERS.LOGIN = '" + login + "';");
			MainData data = owner.rsMaster.convertToMainDataAndClose(info);
			return data;
		}
		catch(Exception error){
			System.out.println("-SQL error at get info");
			throw error;
		}
	}

	public void deleteAtDB(DeletePack delPack) throws Exception{
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


	public void cleanBaseOnExit(String userName, String deviceID) {
		try (Statement deleter = getAccessToDesireBase().createStatement()){
			String delString = "delete from ONLINE_DEVICES WHERE ID = '" + deviceID + "';";
			deleter.executeUpdate(delString);
			System.out.println(userName + " with '" + deviceID + "' has been deleted from ONLINE_DEVICES");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public void insertMessageIntoDB(String messageID, ClientMessage message, int status) throws Exception {
		System.out.println("...Stroring the message in the DB");
		try (Statement stat = getAccessToDesireBase().createStatement()){
			System.out.println("+ID of the message : " + messageID);
			String query = "INSERT INTO MESSAGES VALUES ('" + messageID + "', '" + message.sender + "','" + message.receiver + "','" + message.text + "'," + status + ", dateTime('now') );";
			System.out.println(query);
			stat.execute(query);
			System.out.println("+Message is inserted");
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public MessageSet getUndeliveredMessageForUserAtDB(String login) throws SQLException{
		try(Statement selector = owner.getAccessToDesireBase().createStatement()){
			String markQuery = "update MESSAGES set STATUS = 0 where (RECEIVER ='" + login + "') AND (STATUS = -1);";
			String selectQuery = "select * from MESSAGES where RECEIVER ='" + login + "' AND (STATUS = 0);";
			System.out.println(markQuery);
			selector.executeUpdate(markQuery);
			System.out.println(selectQuery);
			ResultSet set = selector.executeQuery(selectQuery);
			return owner.rsMaster.convertToMessageSetAndClose(set);
		
		}
		catch(Exception error){
			System.out.println("-Can't get undelivered message _ at instrImpl");
			throw error;
		}
	}


	public void unmarkAllDeliveredMessages(String login) {
		try(Statement updater = owner.getAccessToDesireBase().createStatement()){
			String markQuery = "update MESSAGES set STATUS = 1 where (RECEIVER ='" + login + "') AND (STATUS = 0);";
			System.out.println(markQuery);
			updater.executeUpdate(markQuery);
		} catch (SQLException error) {
			error.printStackTrace();
		}
	}

	public String getOnlineReceiverIDAtDB(String messageReceiver) {
		try(Statement selector = getAccessToDesireBase().createStatement()){
			String query = "select ID from ONLINE_DEVICES where OWNER ='" +  messageReceiver + "';";
			System.out.println(query);
			ResultSet set = selector.executeQuery(query);
			if (set.next()){
				String receiverID = set.getString("ID");
				set.close();
				return receiverID;
			}
			else{
				return null;
			}
			
		} catch (SQLException error) {
			error.printStackTrace();
			return null;

		}

	}

	public MessageSet getOldMessagesForUserAtDB(String from, String to, int hoursRadius) throws Exception {
		try(Statement selector = owner.getAccessToDesireBase().createStatement()){
			String selectQuery = "select * from MESSAGES where (RECEIVER ='" + to + "') AND (SENDER = '" + from + "') AND time < datetime('now', '-" +hoursRadius + " hours' );";
			System.out.println(selectQuery);
			ResultSet set = selector.executeQuery(selectQuery);
			MessageSet messageSet = owner.rsMaster.convertToMessageSetAndClose(set);
			return messageSet;
		}
		catch(Exception error){
			System.out.println("-Can't get undelivered message _ at instrImpl");
			throw error;
		}
	}

	public UserSet getUsersTalkedToAtDB(String userName) throws Exception {
		try(Statement selector = owner.getAccessToDesireBase().createStatement()){
			String selectQuery = "select distinct RECEIVER from MESSAGES where (SENDER = '" + userName + "');";
			System.out.println(selectQuery);
			ResultSet set = selector.executeQuery(selectQuery);
			UserSet userSet = owner.rsMaster.convertToUserSetAndClose(set);
			return userSet;
		}
		catch(Exception error){
			System.out.println("-Can't get users chated with _ at instrImpl");
			throw error;
		}
	}

	public void likeDesireAtDB(String desireID, boolean isLiked) {
		char sign = '+';
		if (!isLiked){
			sign = '-';
		}
			try(Statement selector = owner.getAccessToDesireBase().createStatement()){
				String updateQuery = "update DESIRES_MAIN SET likesAmount = (likesAmount " + sign +  " 1) where desireID = " + desireID + ");";
				System.out.println(updateQuery);
				System.out.println("...executing");
				int amount = selector.executeUpdate(updateQuery);
				System.out.println("...Rows affected : " + amount);
			}
			catch(Exception error){
				System.out.println("-Can't like, nobody cares _ at instrImpl");
			}
		}


}