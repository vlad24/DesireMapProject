package desireMapApplicationPackage.messageSystemPackage;

import java.io.Serializable;

public class Message implements Serializable{
	public Message(String newReceiver, String newSender, String newText){
		sender = newSender;
		receiver = newReceiver;
		text = newText;
	}
	public final String sender;
	public final String receiver;
	public final String text;
}
