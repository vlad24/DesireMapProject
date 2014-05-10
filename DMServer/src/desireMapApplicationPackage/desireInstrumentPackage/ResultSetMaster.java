package desireMapApplicationPackage.desireInstrumentPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

	public DesireContentDating getDatingContentFromCurrentRow(ResultSet resultRows){
		try {
			return new DesireContentDating(
					resultRows.getString("login"),
					resultRows.getString("desireID"),
					resultRows.getString("desireDescription"),
					new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude")),
					new SimpleDateFormat("HHHH-MM-DD hh:mm:ss").parse(resultRows.getString("time")),
					resultRows.getInt("likesAmount"),
					resultRows.getString("partnerSex").charAt(0),
					resultRows.getInt("partnerAgeFrom"),
					resultRows.getInt("partnerAgeTo")
					                       );
		} catch (Exception parseError) {
			System.out.println(parseError.getMessage());
			System.out.println("-Parsing from DB Time to Java time failed");
			return null;
		}
	}

	public DesireContentSport getSportContentFromCurrentRow(ResultSet resultRows){
		try {
			return new DesireContentSport(
					resultRows.getString("login"),
					resultRows.getString("desireID"),
					resultRows.getString("desireDescription"),
					new Coordinates(resultRows.getDouble("latitude"), resultRows.getDouble("longitude")),
					new SimpleDateFormat("HHHH-MM-DD hh:mm:ss").parse(resultRows.getString("time")),
					resultRows.getInt("likesAmount"),
					resultRows.getString("sportName"),
					resultRows.getString("advantages")
		                                  );
		} catch (Exception parseError) {
			System.out.println(parseError.getMessage());
			System.out.println("-Error at getting sportContent");
			return null;
		}
	}

	public DesireSet convertToDesireSetAndClose(ResultSet resultRows, int categoryCode){
		try{
			int counter = 0;
			System.out.println("$ Convertion from DBResultSet to DesireSet...");
			DesireSet toSend = new DesireSet();
			switch(categoryCode){
			case (CodesMaster.Categories.DatingCode):{
				System.out.println("$ Convertion to DatingSet...");
				while(resultRows.next())
				{
					counter++;
					try{
						toSend.dArray.add(getDatingContentFromCurrentRow(resultRows));
					}
					catch(Exception error){
						System.out.println("-Problems in DesireSetConverter(Dating) : " + error.getMessage());
						break;
					}
				}
				System.out.println("$ Convertion completed. " + counter + " rows converted.");
				return toSend;
			}
			case(CodesMaster.Categories.SportCode):{
				System.out.println("$ Convertion to SportSet...");
				while(resultRows.next())
				{
					counter++;
					try{
						toSend.dArray.add(getSportContentFromCurrentRow(resultRows));
					}
					catch(Exception error){
						System.out.println("$ -Problems in DesireSetConverter(Sport) : " + error.getMessage());
						break;
					}
				}
				System.out.println("$ Convertion completed. " + counter + " rows converted.");
				return toSend;
			}
			default:{
				System.out.println("$ Category undefined (?)");
				return null;
			}
			}
		}
		catch(Exception error){
			System.out.println("$ -Error at Covering to DesireSet : " + error.getMessage());
			return null;
		}
		finally{
			try {
				resultRows.close();
				System.out.println("$ ResultSet closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public MainData getMainDataFromCurrentRow(ResultSet resultRows) throws SQLException{
		return new MainData(
				resultRows.getString("login"),
				resultRows.getString("name"),
				resultRows.getString("sex").charAt(0),
				resultRows.getString("birth"),
				resultRows.getInt("rating")
				           );
	}

	public MainData convertToMainDataAndClose(ResultSet resultRows) throws SQLException {
		try {
			resultRows.next();
			return getMainDataFromCurrentRow(resultRows);
		} catch (SQLException error) {
			System.out.println("-Problems in convertion to Main Data" + error.getMessage());
			return null;
		}
		finally{
			resultRows.close();
		}
	}

	public SatisfySet convertToSatisfySetAndClose(ResultSet resultRows, int categoryCode){
		try{
			System.out.println("$ Convertion from DBResultSet to SatisfySet...");
			SatisfySet toSend = new SatisfySet();
			int convertedRowsCounter = 0;
			switch(categoryCode){
			case (CodesMaster.Categories.DatingCode):{
				while(resultRows.next())
				{
					if (resultRows.getString("likeTime") != null){
						toSend.likedByUser.add(resultRows.getString("desireID"));
					}
					toSend.dTree.insertData(getDatingContentFromCurrentRow(resultRows));
					toSend.desireAuthors.put(resultRows.getString("login"), getMainDataFromCurrentRow(resultRows));
					convertedRowsCounter++;
				}
				System.out.println("$ Convertion completed : " + convertedRowsCounter + " rows converted. ");
				if (convertedRowsCounter == 0){
					System.out.println("$ Empty set.");
					return null;
				}
				else{
					System.out.println("$ Normal set is returned.");
					return toSend;
				}
				
			}
			case(CodesMaster.Categories.SportCode):{
				
				while(resultRows.next())
				{
					if (resultRows.getString("likeTime") != null){
						toSend.likedByUser.add(resultRows.getString("desireID"));
					}
					toSend.dTree.insertData(getSportContentFromCurrentRow(resultRows));
					toSend.desireAuthors.put(resultRows.getString("login"), getMainDataFromCurrentRow(resultRows));
					convertedRowsCounter++;
				}
				System.out.println("$ Convertion completed : " + convertedRowsCounter + " rows converted. ");
				if (convertedRowsCounter == 0){
					System.out.println("$ Empty set.");
					return null;
				}
				else{
					System.out.println("$ Normal set is returned.");
					return toSend;
				}
				
			}
			default:{
				
				System.out.println("$ Category undefined (?)");
				return null;
				
			}
			}
		}
		catch(Exception error){
			System.out.println("$ - Some problems in converting to SatisfySet : " + error.getMessage());
			return null;
		}
		finally{
			try {
				resultRows.close();
				System.out.println("$ ResultSet closed");
			} catch (SQLException e) {
				System.out.println("$ Cannot close ResulSet");
				e.printStackTrace();
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
				ClientMessage message = new ClientMessage(set.getString("messageID"), set.getString("sender"), set.getString("receiver"), set.getString("messageText"));
				ArrayList<ClientMessage> list = new ArrayList<ClientMessage>();
				list.add(message);
				while (set.next()){
					message = new ClientMessage(set.getString("messageID"), set.getString("sender"), set.getString("receiver"), set.getString("messageText"));
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
