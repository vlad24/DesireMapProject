package desireMapApplicationPackage.desireInstrumentPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.MainData;


public class ResultSetMaster {

	public DesireContentDating getDatingContentFromCurrentRow(ResultSet resultRows) throws SQLException{
		String login = resultRows.getString("login");
		String desireID = resultRows.getString("desireID");
		String desDes = resultRows.getString("desireDescription");
		Coordinates coordinates = new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude"));
		char partnerSex = resultRows.getString("partnerSex").charAt(0);
		int partnerAgeFrom = resultRows.getInt("partnerAgeFrom");
		int partnerAgeTo = resultRows.getInt("partnerAgeTo");
		String dbTime = resultRows.getString("time");
		SimpleDateFormat formater = new SimpleDateFormat("HHHH-MM-DD hh:mm:ss");
		try {
			Date date = formater.parse(dbTime);
			DesireContentDating content = new DesireContentDating(login, desireID, desDes, coordinates, date ,partnerSex, partnerAgeFrom, partnerAgeTo);
			return content;
		} catch (ParseException parseError) {
			System.out.println(parseError.getMessage());
			System.out.println("-Parsing from DB Time to Java time failed");
			return null;
		}
	}

	public DesireContentSport getSportContentFromCurrentRow(ResultSet resultRows) throws SQLException{
		String login = resultRows.getString("login");
		String desireID = resultRows.getString("desireID");
		String desDes = resultRows.getString("desireDescription");
		Coordinates coordinates = new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude"));
		String sport = resultRows.getString("sportName");
		String advantages = resultRows.getString("advantages");
		String dbTime = resultRows.getString("time");
		SimpleDateFormat formater = new SimpleDateFormat("HHHH-MM-DD hh:mm:ss");
		try {
			Date date = formater.parse(dbTime);
			DesireContentSport content = new DesireContentSport(login, desireID, desDes, coordinates, date, sport, advantages);
			return content;
		} catch (ParseException parseError) {
			System.out.println(parseError.getMessage());
			System.out.println("-Parsing from DB Time to Java time failed");
			return null;
		}
	}

	public DesireSet convertToDesireSetAndClose(ResultSet resultRows, int categoryCode) throws SQLException{
		int counter = 0;
		System.out.println("$ Convertion from DBResultSet to DesireSet");
		DesireSet toSend = new DesireSet();
		switch(categoryCode){
		case (CodesMaster.Categories.DatingCode):{
			while(resultRows.next())
			{
				counter++;
				try{
					DesireContentDating content = getDatingContentFromCurrentRow(resultRows);
					toSend.dArray.add(content);
				}
				catch(SQLException outOfBound){
					System.out.println("-Problems in DesireSetConverter");
					break;
				}
			}
			resultRows.close();
			System.out.println(counter);
			System.out.println("$ Convertion completed");
			return toSend;
		}
		case(CodesMaster.Categories.SportCode):{
			while(resultRows.next())
			{
				counter++;
				try{
					DesireContentSport content = getSportContentFromCurrentRow(resultRows);
					toSend.dArray.add(content);
				}
				catch(SQLException outOfBound){
					System.out.println("-Problems in DesireSetConverter");
					break;
				}
			}
			resultRows.close();
			System.out.println(counter);
			System.out.println("$ Convertion completed");
			return toSend;
		}
		default:{
			resultRows.close();
			System.out.println("$ Category undefined");
			return null;
		}
		}
	}


	public MainData getMainDataFromCurrentRow(ResultSet resultRows) throws SQLException{
		MainData data = new MainData(resultRows.getString("login"), resultRows.getString("name"), resultRows.getString("sex").charAt(0), resultRows.getString("birth"), resultRows.getInt("rating"));
		return data;
	}

	public MainData convertToMainDataAndClose(ResultSet resultRows) throws SQLException {
		try {
			resultRows.next();
			MainData data = getMainDataFromCurrentRow(resultRows);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally{
			resultRows.close();
		}

	}

	public SatisfySet convertToSatisfySetAndClose(ResultSet resultRows, int categoryCode) throws Exception {
		boolean empty = true;
		System.out.println("$ Convertion from DBResultSet to SatisfySet");
		SatisfySet toSend = new SatisfySet();
		switch(categoryCode){
		case (CodesMaster.Categories.DatingCode):{
			while(resultRows.next())
			{
				empty = false;
				try{
					DesireContentDating content = getDatingContentFromCurrentRow(resultRows);
					String author = resultRows.getString("login");
					MainData authorInfo = getMainDataFromCurrentRow(resultRows);
					toSend.dTree.insertData(content);
					toSend.desireAuthors.put(author, authorInfo);
				}
				catch(SQLException outOfBound){
					System.out.println("-Problems in DesireSetConverter");
					resultRows.close();
					throw new Exception();
				}
			}
			System.out.println("$ Convertion completed");
			if (empty){
				System.out.println("$ Empty set");
				return null;
			}
			else{
				return toSend;
			}
		}
		case(CodesMaster.Categories.SportCode):{
			while(resultRows.next())
			{
				empty = false;
				try{
					DesireContentSport content = getSportContentFromCurrentRow(resultRows);
					String author = resultRows.getString("login");
					MainData authorInfo = getMainDataFromCurrentRow(resultRows);
					toSend.dTree.insertData(content);
					toSend.desireAuthors.put(author, authorInfo);
				}
				catch(SQLException outOfBound){
					System.out.println("-Problems in DesireSetConverter");
					break;
				}
			}
			resultRows.close();
			System.out.println("$ Convertion completed");
			if (empty){
				return null;
			}
			else{
				return toSend;
			}
		}
		default:{
			resultRows.close();
			System.out.println("$ Category undefined");
			return null;
		}
		}

	}

	public MessageSet convertToMessageSetAndClose(ResultSet set) {
		try {
			if (!(set.next())){
				System.out.println("+Empty result set");
				set.close();
				return null;
			}
			else{
				ClientMessage message = new ClientMessage(set.getString("sender"), set.getString("receiver"), set.getString("messageText"));
				ArrayList<ClientMessage> list = new ArrayList<ClientMessage>();
				list.add(message);
				while (set.next()){
					message = new ClientMessage(set.getString("sender"), set.getString("receiver"), set.getString("messageText"));
					list.add(message);
				}
				set.close();
				System.out.println("...Closing set");
				return new MessageSet(list);
			}
		} catch (SQLException error) {
			System.out.println("-Could not convert to message set and close set");
			error.printStackTrace();
			return null;
		}
	}

	public UserSet convertToUserSetAndClose(ResultSet set) {
		try {
			if (!(set.next())){
				System.out.println("+Empty result set");
				set.close();
				return null;
			}
			else{
				ArrayList<String> list = new ArrayList<String>();
				String receiver = set.getString("receiver");
				list.add(receiver);
				while (set.next()){
					receiver = set.getString("receiver");
					list.add(receiver);
				}
				set.close();
				System.out.println("...Closing set");
				return new UserSet(list);
			}
		} catch (SQLException error) {
			System.out.println("-Could not convert to user set and close set");
			error.printStackTrace();
			return null;
		}
	}

}
