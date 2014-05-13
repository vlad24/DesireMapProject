package logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Deque;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.ExitPack;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageSendPack;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.ShowInfoPack;
import desireMapApplicationPackage.actionQueryObjectPackage.ShowPersonalDesiresPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;


public class Client {

	private static String TAG = "Client";

	private static String server = "5.19.236.176";
	//	private static String server = "192.168.1.227";
	private final static int port = 9252;
	private static Socket socket;
	private static ObjectInputStream in;
	//	private static PrintWriter out;
	private static ObjectOutputStream out;
	private static boolean socketIsEmpty;
	private static String clientName;

	static{
		socketIsEmpty = true;
	}

	public static void changeIP(String ipString){
		server = ipString;
	}

	public static String getIP(){
		return server;
	}

	private static void initializeClient() throws Exception{
		Log.d(TAG, "trying to make socket");
		socket = new Socket(InetAddress.getByName(server), port);
		Log.d(TAG, "created socket");
		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());
	}


	public static void setName(String name){
		clientName = name;
	}

	public static String getName(){
		return clientName;
	}

	public static boolean isCorrect(String userString){
		if(!userString.isEmpty()){
			Pattern p1 = Pattern.compile("^.*/.*$");
			Pattern p2 = Pattern.compile("^.*_.*$");
			Matcher m1 = p1.matcher(userString);
			Matcher m2 = p2.matcher(userString);
			return !(m1.matches()||m2.matches());
		}
		return false;
	}

	public static boolean isCorrectOnServer(){
		try {
			Log.d(TAG, "Try to receive boolean");
			boolean correct = in.readBoolean();
			Log.d(TAG, "received "+correct);
			return correct;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean sendLogin(String log, String password) throws Exception{
		Log.d(TAG, "entered in sendLogin()");
		if(isCorrect(log)&&isCorrect(password)){		
			if(socketIsEmpty){
				initializeClient();
				Log.d(TAG, "finished initializeClient()");
				socketIsEmpty = false;
			}
			LoginData logData = new LoginData(log, password);
			LoginPack logPack = new LoginPack(logData);
			out.writeObject(logPack);
			out.reset();
			return isCorrectOnServer();
		}
		return false;
	}


	public static boolean sendRegistration(String log, String password, String name, char male, String birthdate) throws Exception{
		Log.d(TAG, "entered in sendRegistration()");
		if(socketIsEmpty){
			initializeClient();
			Log.d(TAG, "finished initializeClient()");
			socketIsEmpty = false;
		}

		if(isCorrect(name)&&isCorrect(birthdate)){
			RegistrationData regData = new RegistrationData(log, password, name, male, birthdate);
			RegistrationPack regPack = new RegistrationPack(regData);
			Log.d( TAG, "before sending regData");
			out.writeObject(regPack);
			out.reset();
			Log.d(TAG, "after sending regData");
			return isCorrectOnServer();
		}
		return false;
	}
	
	
	public static void sendMessage(String newMessageText, String receiver) throws IOException{
		Log.d(TAG, "try to sendMessage()");
		Message newMessage = new Message(clientName, receiver, newMessageText);
		MessageSendPack newMessagePack = new MessageSendPack(newMessage);
		out.writeObject(newMessagePack);
		out.reset();
	}


	public static boolean sendDesire(DesireContent newContent) throws Exception{
		//		if(isCorrect(desireString)&&(latitude!=0)&&(longitude!=0)){
		Log.d(TAG, "try to sendDesire()");
		AddPack addPack = new AddPack(newContent, "100312");
		out.writeObject(addPack);
		out.reset();
		return isCorrectOnServer();
	}

	
	public static Deque<Message> getMessages() throws Exception{
		MessageDeliverPack deliverPack = new MessageDeliverPack();
		Deque<Message> messageSet;
		out.writeObject(deliverPack);
		out.reset();
		messageSet = (Deque<Message>) in.readObject();
		return messageSet;
	}

	public static SatisfySet getSatisfyDesires(String desireID, HashSet<String> tiles) throws Exception{
		SatisfyPack satisfyPack = new SatisfyPack(desireID, tiles);
		SatisfySet set;
		out.writeObject(satisfyPack);
		out.reset();
		Log.d(TAG, "Try to receive DesireSet");
		set = (SatisfySet) in.readObject();
		return set;		
	}

	public static DesireSet getPersonalDesires(int category) throws Exception{
		ShowPersonalDesiresPack desiresPack = new ShowPersonalDesiresPack(category);
		DesireSet set;
		out.writeObject(desiresPack);
		out.reset();
		Log.d(TAG, "Try to receive DesireSet");
		set = (DesireSet) in.readObject();
		return set;		
	}

	public static MainData getPersonalInfo() throws Exception{
		ShowInfoPack infoPack = new ShowInfoPack();
		MainData infoData = null;
		out.writeObject(infoPack);
		out.reset();
		Log.d(TAG, "Try to receive MainData");
		if(isCorrectOnServer())
			infoData = (MainData) in.readObject();
		return infoData;
	}
	

	public static boolean deleteDesires(DeletePack delPack){
		try {
			out.writeObject(delPack);
			out.reset();
			return isCorrectOnServer();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean exit() throws Exception{
		ExitPack exit = new ExitPack();
		out.writeObject(exit);
		out.reset();
		return isCorrectOnServer();
	}


	public static void closeSocket(){
		socketIsEmpty = true;
	}


}