package desireMapApplicationPackage.messageSystemPackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Deque;
import java.util.HashMap;

import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;

public class ChatKing {
	public HashMap<String,DesireThread> onlineObservers;
	private static ChatKing chatKing;
	
	public static ChatKing getInstance(){
		if (chatKing != null){
			return chatKing;
		}
		else{
			chatKing = new ChatKing();
			return chatKing;
		}

	}
	private ChatKing(){
		onlineObservers = new HashMap<String, DesireThread>();
	}
	private DesireThread getTalker(String receiverName){
		try{ 
			DesireThread talker = onlineObservers.get(receiverName);
			return talker;
		}catch(Exception error){
			return null;
		}
	}
		
	private String getMessageID(Message message){
		StringBuilder builder = new StringBuilder();
		builder.append("m:");
		builder.append(message.sender);
		builder.append(":" + builder.hashCode() + ":");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		builder.append(sdf.format(calendar.getTime()));
		String id = builder.toString();
		return id;
	}
	private String insertMessageIntoDB(Message message, boolean fresh) throws Exception {
		if (fresh){
			String id = getMessageID(message);
			message.id = id;
			try (Statement stat = DesireInstrument.getAccessToDesireBase().createStatement()){
				System.out.println("ID :" + id);
				System.out.println("+Inserting into DB");
				System.out.println("INSERT INTO MESSAGES VALUES ('" + id + "', '" + message.receiver + "','" + message.sender + "','" + message.text +"', 0, dateTime('now') );");
				stat.execute("INSERT INTO MESSAGES VALUES ('" + id + "', '" + message.receiver + "','" + message.sender + "','" + message.text +"', 0, dateTime('now') );");
				System.out.println("+Message is inserted");
				System.out.println("Status of the message is set to `fresh`");
				return id;
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		else{
			System.out.println("+Saving undeliverd messages");
			try (Statement stat = DesireInstrument.getAccessToDesireBase().createStatement()){
				System.out.println("INSERT INTO MESSAGES VALUES ('" + message.id + "', '" + message.receiver + "','" + message.sender + "','" + message.text +"', 0, dateTime('now') );");
				stat.execute("UPDATE MESSAGES SET status = 0 WHERE ( messageID = '" + message.id + "');");
				return null;
			}
		}

	}

	public void postMessage(Message message, boolean fresh){
		try{
			System.out.println("Receiver : " + message.receiver);
			System.out.println("Sender :  " + message.sender);
			String newID = insertMessageIntoDB(message, fresh);
			System.out.println("+Searching to a receiver thread : " + message.receiver);
			DesireThread receiver = getTalker(message.receiver);
			if (receiver != null){
				System.out.println("+Found. Trying to give to messages " + message.receiver);
				deliverMessage(newID, message.receiver, message);
				System.out.println("+Given");
			}
			else{
				System.out.println("+Receiver is not online, messages saved");
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("-Error at post message");
		}
	}

	public void registerThread(DesireThread newThread, boolean deliver){
		String threadName = newThread.getUserName();
		synchronized(this){
			onlineObservers.put(threadName, newThread);
			System.out.println("+" + threadName + " has been added to online talkers");
		}
		if (deliver){
			try{
				System.out.println("...Trying to deliver new messages to " + threadName);
				deliverAllMessages(threadName);
			}
			catch(Exception error){
				error.printStackTrace();
				System.out.println("-Could not upload new messages");
			}
		}
	}
	
	public void unregisterThread(DesireThread leavingThread){
		synchronized(this){
			for (Message m : leavingThread.localMessages){
				try {
					insertMessageIntoDB(m, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			onlineObservers.remove(leavingThread.getUserName());
		}
		System.out.println("+" + leavingThread.getUserName() + " has been removed from online talkers");
	}
	
	public void deliverMessage(String newID, String receiverThreadName, Message message) throws Exception{
		DesireThread receiver = getTalker(receiverThreadName);
		if (receiver != null){
			System.out.println("... Take your messages, " + receiverThreadName);
			receiver.takeMessages(message);
			System.out.println("... " + receiverThreadName + " : I've taken!");
			try (Statement stat = DesireInstrument.getAccessToDesireBase().createStatement()){
				stat.executeUpdate("UPDATE MESSAGES SET STATUS = 2 WHERE (messageID = '" + newID + "');");
				System.out.println("Status of the message is updated to `captured`");
			}
		}
	}

	public void deliverAllMessages(String receiverThreadName) throws Exception{
		synchronized(this){
			try(Statement stat = DesireInstrument.getAccessToDesireBase().createStatement()){
				System.out.println("SELECT receiver, sender, messageText FROM MESSAGES WHERE" +
						" (receiver = '" + receiverThreadName + "') "
						+ "AND (STATUS = 0) ORDER BY TIME DESC");
				ResultSet set = stat.executeQuery("SELECT receiver, sender, messageText FROM MESSAGES WHERE" +
						" (receiver = '" + receiverThreadName + "') "
						+ "AND (STATUS = 0) ORDER BY TIME DESC");
				System.out.println("+Result set is formed");		
				Deque<Message> deque = DesireInstrument.rsMaster.convertToQueueOfMessagesAndClose(set);
				System.out.println("+Queue is formed");
				if (deque != null){
					stat.executeUpdate("UPDATE MESSAGES SET STATUS = 1 WHERE (receiver = '" + receiverThreadName + "') AND (STATUS = 0)");
					System.out.println("+Status of the selected messages is updated to `fetched`");
					DesireThread getter = getTalker(receiverThreadName);
					if (getter != null){
						System.out.println("... Take your new messages, " + receiverThreadName);
						getter.takeMessages(deque);
						System.out.println("... " + receiverThreadName + " : I've taken!");
						stat.executeUpdate("UPDATE MESSAGES SET STATUS = 2 WHERE (receiver = '" + receiverThreadName + "') AND (STATUS = 1)");
						System.out.println("+Status of the messages is updated to `captured`");
					}
					else{
						System.out.println("-Thread ran away");
						stat.executeUpdate("UPDATE MESSAGES SET STATUS = 0 WHERE (receiver = '" + receiverThreadName + "') AND (STATUS = 1)");
						System.out.println("+Status of the messages is updated back to `fresh`");
					}
				}
				else{
					System.out.println("+Empty queue - no new messages");
				}
			}catch(Exception error){
				throw error;
			}
		}
	}
	
}
