package desireMapApplicationPackage.outputSetPackage;

import java.io.Serializable;
import java.util.ArrayList;

import desireMapApplicationPackage.messageSystemPackage.ClientMessage;

public class MessageSet implements Serializable{
	public ArrayList<ClientMessage> mSet;
	
	public MessageSet(ArrayList<ClientMessage> newMessageSet){
		mSet = newMessageSet;
	}
}
