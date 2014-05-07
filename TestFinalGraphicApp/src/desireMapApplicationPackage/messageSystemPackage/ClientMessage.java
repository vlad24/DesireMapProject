package desireMapApplicationPackage.messageSystemPackage;

import java.io.Serializable;

public class ClientMessage implements Serializable{
	public ClientMessage(String newMessageID, String newSender, String newReceiver, String newText){
		sender = newSender;
		receiver = newReceiver;
		text = newText;
		messageID = newMessageID;
	}
	public final String sender;
	public final String receiver;
	public final String text;
	public String messageID;
}
