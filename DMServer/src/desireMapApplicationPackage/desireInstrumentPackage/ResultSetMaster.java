package desireMapApplicationPackage.desireInstrumentPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;


import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.MainData;


public class ResultSetMaster {
	
	public DesireContentDating getDatingContentFromCurrentRow(ResultSet resultRows) throws SQLException{
		String login = resultRows.getString("login");
		int desireID = resultRows.getInt("desireID");
		String desDes = resultRows.getString("desireDescription");
		Coordinates coord = new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude"));
		//String time = resultRows.getString("time");
		char partnerSex = resultRows.getString("partnerSex").charAt(0);
		int partnerAge = resultRows.getInt("partnerAge");
		DesireContentDating content = new DesireContentDating(login, desireID, desDes, coord, partnerSex, partnerAge);
		return content;
	}
	
	public DesireContentSport getSportContentFromCurrentRow(ResultSet resultRows) throws SQLException{
		String login = resultRows.getString("login");
		int desireID = resultRows.getInt("desireID");
		String desDes = resultRows.getString("desireDescription");
		Coordinates coord = new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude"));
		//String time = resultRows.getString("time");
		String sport = resultRows.getString("sportName");
		int advantages = resultRows.getInt("advantages");
		DesireContentSport content = new DesireContentSport(login, desireID, desDes, coord, sport, advantages);
		return content;
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
					toSend.dTree.insertData(content);
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
					toSend.dTree.insertData(content);
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

	public Deque<Message> convertToQueueOfMessagesAndClose(ResultSet set){
		try {
			if (!(set.next())){
				System.out.println("+Empty result set");
				return null;
			}
			else{
				Message mes = new Message(set.getString("receiver"), set.getString("sender"), set.getString("messageText"));
				Deque<Message> deque = new LinkedList<Message>();
				deque.add(mes);
				while (set.next()){
					mes = new Message(set.getString("receiver"), set.getString("sender"), set.getString("messageText"));
					deque.add(mes);
				}
				return deque;
			}
		} catch (SQLException e) {
			System.out.println("-Could not convert to queue of messages");
			e.printStackTrace();
			return null;
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
}
