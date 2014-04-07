package desireMapApplicationPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ExitPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ShowInfoPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ShowPersonalDesiresPack;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
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
			Log.d(TAG, "after sending regData");
			return isCorrectOnServer();
		}
		return false;
	}

	public static SatisfySet getSatisfyDesires(Coordinates coord, Cryteria cryteria) throws Exception{
		SatisfyPack satisfyPack = new SatisfyPack(coord, cryteria);
		SatisfySet set;
		out.writeObject(satisfyPack);
		Log.d(TAG, "Try to receive DesireSet");
		set = (SatisfySet) in.readObject();
		return set;		
	}

	public static DesireSet getPersonalDesires(Cryteria cryteria) throws Exception{
		ShowPersonalDesiresPack desiresPack = new ShowPersonalDesiresPack(cryteria);
		DesireSet set;
		out.writeObject(desiresPack);
		Log.d(TAG, "Try to receive DesireSet");
		set = (DesireSet) in.readObject();
		return set;		
	}

	public static MainData getPersonalInfo() throws Exception{
		ShowInfoPack infoPack = new ShowInfoPack();
		MainData infoData = null;
		out.writeObject(infoPack);
		Log.d(TAG, "Try to receive MainData");
		if(isCorrectOnServer())
			infoData = (MainData) in.readObject();
		return infoData;
	}


	public static boolean sendDesire(DesireContent newContent) throws Exception{
		//		if(isCorrect(desireString)&&(latitude!=0)&&(longitude!=0)){
		Log.d(TAG, "try to sendDesire()");
		AddPack addPack = new AddPack(newContent);
		out.writeObject(addPack);
		return isCorrectOnServer();
	}


	public static boolean deleteDesires(DeletePack delPack){
		try {
			out.writeObject(delPack);
			return isCorrectOnServer();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean exit() throws Exception{
		ExitPack exit = new ExitPack();
		out.writeObject(exit);
		return isCorrectOnServer();
	}


	public static void closeSocket(){
		socketIsEmpty = true;
	}


}
