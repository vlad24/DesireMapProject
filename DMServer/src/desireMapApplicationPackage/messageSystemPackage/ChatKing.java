package desireMapApplicationPackage.messageSystemPackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.UserSet;

public class ChatKing {

	private ChatKing(DesireInstrument newInstrument){
		instrument = newInstrument;
	}
	
	private static ChatKing chatKing;
	private static DesireInstrument instrument;

	public static ChatKing getInstance(){
		if (chatKing != null){
			return chatKing;
		}
		else{
			chatKing = new ChatKing(DesireInstrument.getInstance());
			return chatKing;
		}
	}

	private String getMessageID(ClientMessage clientMessage){
		System.out.println("...Producing the id for the message...");
		StringBuilder builder = new StringBuilder();
		builder.append("m:");
		builder.append(clientMessage.sender);
		builder.append(builder.hashCode() + ":");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY:MM:dd:HH:mm:ss");
		builder.append(sdf.format(calendar.getTime()));
		String id = builder.toString();
		return id;
	}

	private void insertMessageIntoDB(String messageID, ClientMessage message, int status) throws Exception {
		System.out.println("...Inserting the message with " + status + "status...");
		instrument.insertMessageIntoDB(messageID, message, status);
	}

	public void postMessage(ClientMessage clientMessage){
		try{
			System.out.println("...Trying to convert the message to Server Message(google format)...");
			ServerMessage sMessage = convertToServerMessage(clientMessage);
			if (sMessage != null){
				System.out.println("...Server message is ready,posting...");
				Poster.post(sMessage);
				System.out.println("...Posted, inserting into application DB...");
				insertMessageIntoDB(getMessageID(clientMessage), clientMessage, 1);
				System.out.println("...Inserted...");
			}
			else{
				System.out.println("...Convertion is not succesful, not posted yet, inserting into application DB...");
				insertMessageIntoDB(getMessageID(clientMessage), clientMessage, -1);
				System.out.println("...No appealing to google servers...");
			}
		}
		catch(Exception error){
			System.out.println("-Some problems at posting/converting");
			System.out.println(error.getMessage());
		}

	}

	private ServerMessage convertToServerMessage(ClientMessage clientMessage) throws SQLException {
		System.out.println("...Converting to server message");
		System.out.println("Sender : " + clientMessage.sender + ", Receiver : " + clientMessage.receiver);
		String messageReceiverID = instrument.getOnlineReceiverIDAtDB(clientMessage.receiver);
		ServerMessage sMessage = new ServerMessage();
		if(messageReceiverID != null){
			System.out.println("+ID, corresponding to the receiver found");
			sMessage.addReceiver(messageReceiverID);
			sMessage.fillServerMessage(getMessageID(clientMessage), clientMessage.sender, clientMessage.text);
			return sMessage;
		}
		else{
			System.out.println("-No IDs corresponding to the receiver found");
			return null;
		}
	}

	public void getUndeliveredMessagesForThread(DesireThread thread) throws SQLException {
		System.out.println("...Trying to deliver unread messages");
		MessageSet messageSet = instrument.getAndMarkUndeliveredMessagesForUserAtDB(thread.getUserName());
		System.out.println("...Undelivered messages fetched");
		int deliveredMessagesAmount = 0;
		int index = 0;
		System.out.println("...If set is not empty, go!");
		if (messageSet == null){
			System.out.println("...Empty set");
		}
		else{
			for (index = 0; index < messageSet.mSet.size(); index++){
				ServerMessage sMessage = new ServerMessage();
				sMessage.addReceiver(thread.getDeviceID());
				sMessage.fillServerMessage(messageSet.mSet.get(index).messageID, messageSet.mSet.get(index).receiver,  messageSet.mSet.get(index).text);
				if (thread.isRunning){
					Poster.post(sMessage);
					System.out.println("+ Posted. Message ¹ " + index + " has been delivered");
				}
				else{
					System.out.println("+ Not Posted. Message ¹ " + index + ". User exited.");
					System.out.println("...some of messages haven't been posted");
					deliveredMessagesAmount = index - 1;
					break;
				}
				instrument.unmarkAllDeliveredMessages(thread.getUserName());
			}
			while (index < messageSet.mSet.size()){
				instrument.unmarkCurrentUndeliveredMessage(messageSet, index);
				System.out.println("+Message ¹ " + index + " rolled back into sending");
				index++;
			}
			
		}
		System.out.println("Report ::");
		System.out.println("...Messagses : " + messageSet.mSet.size());
		System.out.println("...Posted messagses : " + deliveredMessagesAmount);
		System.out.println("...Unposted messagses : " + (messageSet.mSet.size() - deliveredMessagesAmount));
	}

	public MessageSet getMessagesByCryteria(MessageDeliverPack pack) throws Exception {
		System.out.println("...Trying to deliver some old messages");
		return instrument.getOldMessagesForUserAtDB(pack.from, pack.to, pack.hoursRadius);
	}

	public UserSet getUsersTalkedToAtChat(DesireThread thread) throws Exception {
		System.out.println("...Trying to get users ever chated to");
		return instrument.getUsersTalkedToAtDB(thread.getUserName());
	}
	
}