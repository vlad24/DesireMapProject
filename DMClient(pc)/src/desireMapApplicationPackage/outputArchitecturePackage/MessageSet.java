package desireMapApplicationPackage.outputArchitecturePackage;

import java.io.Serializable;
import java.util.Queue;

import desireMapApplicationPackage.messageSystemPackage.Message;

public class MessageSet implements Serializable{
	public MessageSet(Queue<Message> newMessages){
		newMessages = messages;
	}
	public Queue<Message> messages;
}
