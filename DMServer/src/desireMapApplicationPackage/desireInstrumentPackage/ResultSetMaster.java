package desireMapApplicationPackage.desireInstrumentPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;

import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.userDataPackage.MainData;


public class ResultSetMaster {
	public DesireSet convertToDesireSetAndClose(ResultSet resultRows, int categoryCode) throws SQLException{
		System.out.println("$ Convertion from DBResultSet to DesireSet");
		DesireSet toSend = new DesireSet();
		switch(categoryCode){
		case (CodesMaster.Categories.DatingCode):{
			while(resultRows.next())
			{
				try{
					String login = resultRows.getString("login");
					int desireID = resultRows.getInt("desireID");
					String desDes = resultRows.getString("desireDescription");
					//String tag = resultRows.getString("tag");
					Coordinates coord = new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude"));
					//String time = resultRows.getString("time");
					char partnerSex = resultRows.getString("partnerSex").charAt(0);
					int partnerAge = resultRows.getInt("partnerAge");
					DesireContent content = new DesireContentDating(login, desireID, desDes, coord, partnerSex, partnerAge);
					toSend.dSet.add(content);
				}
				catch(SQLException outOfBound){
					System.out.println("-Problems in DesireSetConverter");
					break;
				}
			}
			resultRows.close();
			System.out.println("$ Convertion completed");
			return toSend;
		}
		case(CodesMaster.Categories.SportCode):{
			while(resultRows.next())
			{
				try{
					String login = resultRows.getString("login");
					int desireID = resultRows.getInt("desireID");
					String desDes = resultRows.getString("desireDescription");
					//String tag = resultRows.getString("tag");
					Coordinates coord = new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude"));
					String time = resultRows.getString("time");
					String sport = resultRows.getString("sportName");
					int advantages = resultRows.getInt("advantages");
					DesireContent content = new DesireContentSport(login, desireID, desDes, coord, sport, advantages);
					toSend.dSet.add(content);
				}
				catch(SQLException outOfBound){
					System.out.println("-Problems in DesireSetConverter");
					break;
				}
			}
			resultRows.close();
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

	public MainData convertToMainDataAndClose(ResultSet info) throws SQLException {
		try {
			info.next();
			MainData data = new MainData(info.getString(1), info.getString(2), info.getString(3).charAt(0), info.getString(4), info.getInt(5));
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally{
			info.close();
		}
	    		
	}
	
}
