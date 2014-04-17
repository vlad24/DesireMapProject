package desireMapApplicationPackage.outputSetPackage;

import java.io.Serializable;
import java.util.Queue;

import desireMapApplicationPackage.messageSystemPackage.Message;

public class MessageSet implements Serializable{
	public MessageSet(Queue<Message> newMessages){
		messages = newMessages;
	}
	public Queue<Message> messages;
}
