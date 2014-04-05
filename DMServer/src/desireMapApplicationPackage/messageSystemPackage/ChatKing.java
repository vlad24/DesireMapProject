package desireMapApplicationPackage.messageSystemPackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		
	private int insertMessageIntoDB(Message message) throws Exception {
		try (Statement stat = DesireInstrument.getAccessToDesireBase().createStatement()){
			System.out.println("+Inserting into DB");
			int id = MessageIDGenerator.getInstance().getNextID();
			System.out.println("INSERT INTO MESSAGES VALUES (" + id + ", '" + message.receiver + "','" + message.sender + "','" + message.text +"', 0, dateTime('now') );");
			stat.execute("INSERT INTO MESSAGES VALUES (" + id + ", '" + message.receiver + "','" + message.sender + "','" + message.text +"', 0, dateTime('now') );");
			System.out.println("+Message is inserted");
			System.out.println("Status of the message is set to `fresh`");
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void postMessage(Message message){
		try{
			int newID = insertMessageIntoDB(message);
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
			onlineObservers.remove(leavingThread.getUserName());
		}
		System.out.println("+" + leavingThread.getUserName() + " has been removed from online talkers");
	}
	
	public void deliverMessage(int newID, String receiverThreadName, Message message) throws Exception{
		DesireThread receiver = getTalker(receiverThreadName);
		if (receiver != null){
			System.out.println("... Take your messages, " + receiverThreadName);
			receiver.takeMessages(message);
			System.out.println("... " + receiverThreadName + " : I've taken!");
			try (Statement stat = DesireInstrument.getAccessToDesireBase().createStatement()){
				stat.executeUpdate("UPDATE MESSAGES SET STATUS = 2 WHERE (messageID = " + newID + ");");
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
