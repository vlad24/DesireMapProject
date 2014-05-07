package logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
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
import desireMapApplicationPackage.actionQueryObjectPackage.UserChatHistoryPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.quadtree.QuadTreeNode;
import desireMapApplicationPackage.quadtree.QuadTreeNodeBox;
import desireMapApplicationPackage.userDataPackage.AndroidData;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;


public class Client {

	private static String TAG = "ClientTag";

	private final static String server = "5.19.236.176";
	//	private static String server = "192.168.1.227";
	private final static int port = 9252;
	private final static String PROJECT_NUMBER = "525115947093";
	private static Socket socket;
	private static ObjectInputStream in;
	private static ObjectOutputStream out;
	private static boolean socketIsEmpty;
	private static String clientName;
	private static AndroidData gcmIDdata;
	private static GoogleCloudMessaging gcm;
	private static QuadTreeNode worldRoot;

	static{
		socketIsEmpty = true;
		QuadTreeNodeBox world = new QuadTreeNodeBox(-90, -180, 90, 180);
		worldRoot = new QuadTreeNode(world, "0", 0);
	}

	public static String getIP(){
		return server;
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


	public static void closeSocket(){
		socketIsEmpty = true;
	}

	private static void initializeClient(final Context context) throws Exception {
		Log.d(TAG, "trying to get google cloud messaging ID");
		gcm = GoogleCloudMessaging.getInstance(context);
		if(gcm != null)
			Log.d(TAG, "gcm not null");
		String regid = gcm.register(PROJECT_NUMBER);
		gcmIDdata = new AndroidData(regid);
		Log.d(TAG, "trying to make socket");
		socket = new Socket(InetAddress.getByName(server), port);
		Log.d(TAG, "created socket");
		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());
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

	public static boolean sendLogin(Context context, String log, String password) throws Exception{
		Log.d(TAG, "entered in sendLogin()");
		if(isCorrect(log)&&isCorrect(password)){		
			if(socketIsEmpty){
				initializeClient(context);
				Log.d(TAG, "finished initializeClient()");
				socketIsEmpty = false;
			}
			LoginData logData = new LoginData(log, password);
			LoginPack logPack = new LoginPack(logData, gcmIDdata);
			out.writeObject(logPack);
			out.reset();
			return isCorrectOnServer();
		}
		return false;
	}

	public static boolean sendRegistration(Context context, String log, String password, String name, char male, String birthdate) throws Exception{
		Log.d(TAG, "entered in sendRegistration()");
		if(socketIsEmpty){
			initializeClient(context);
			Log.d(TAG, "finished initializeClient()");
			socketIsEmpty = false;
		}

		if(isCorrect(name)&&isCorrect(birthdate)){
			RegistrationData regData = new RegistrationData(log, password, name, male, birthdate);
			RegistrationPack regPack = new RegistrationPack(regData, gcmIDdata);
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
		ClientMessage newMessage = new ClientMessage(null, clientName, receiver, newMessageText);
		MessageSendPack newMessagePack = new MessageSendPack(newMessage);
		out.writeObject(newMessagePack);
		out.reset();
	}


	public static String sendDesire(DesireContent newContent) throws Exception{
		//		if(isCorrect(desireString)&&(latitude!=0)&&(longitude!=0)){
		Log.d(TAG, "try to sendDesire()");
		String quadCoordinate = worldRoot.geoPointToQuad(newContent.coordinates.latitude,
				newContent.coordinates.longitude, 
				QuadTreeNode.maxDepth);

		AddPack addPack = new AddPack(newContent, quadCoordinate);
		out.writeObject(addPack);
		out.reset();
		String IDdesire = in.readUTF();
		return IDdesire;
	}


	public static MessageSet getMessages(String partnerName, int hoursRadius) throws Exception{
		MessageDeliverPack deliverPack = new MessageDeliverPack(partnerName, clientName, hoursRadius);
		MessageSet messageSet;
		out.writeObject(deliverPack);
		out.reset();
		Log.d(TAG, "Try to receive MessageSet");
		messageSet = (MessageSet) in.readObject();
		Log.d(TAG, "received MessageSet");
		return messageSet;
	}

	public static UserSet getChatUsers() throws Exception{
		UserChatHistoryPack userChatPack = new UserChatHistoryPack();
		UserSet userSet;
		out.writeObject(userChatPack);
		out.reset();
		Log.d(TAG, "Try to receive UserSet");
		userSet = (UserSet) in.readObject();
		return userSet;
	}

	public static SatisfySet getSatisfyDesires(String desireID, HashSet<String> tiles, int tileDepth) throws Exception{
		SatisfyPack satisfyPack = new SatisfyPack(desireID, tiles, tileDepth);
		SatisfySet set;
		out.writeObject(satisfyPack);
		out.reset();
		Log.d(TAG, "Try to receive SatisfySet");
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


	public static boolean deleteDesires(String contents){
		try {
			DeletePack delPack = new DeletePack(contents);
			out.writeObject(delPack);
			out.reset();
			return isCorrectOnServer();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean exit() throws Exception{
		socketIsEmpty = true;
		ExitPack exit = new ExitPack();
		out.writeObject(exit);
		out.reset();

		return isCorrectOnServer();
	}



}
