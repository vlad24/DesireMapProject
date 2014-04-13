package desireMapApplicationPackage.messageSystemPackage;

import java.io.Serializable;

public class Message implements Serializable{
	public Message(String newSender,String newReceiver,  String newText, String newID){
		sender = newSender;
		receiver = newReceiver;
		text = newText;
		id = newID;
	}
	public final String sender;
	public final String receiver;
	public final String text;
	public  String id;
}
