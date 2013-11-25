import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.sun.rowset.CachedRowSetImpl;

public class DesireThread extends Thread{
	private StringDivider stringDivider;
	private String currentUser;
	private Socket interactiveSocket;
	private DesireInstrument instrument;
	private ObjectOutputStream socketOut;
	private BufferedReader socketIn;

	public DesireThread(Socket givenSocket) throws IOException{//
		System.out.println("*** Thread is being constructed\n");
		this.setDaemon(true);
		currentUser = "unknown";
		interactiveSocket = givenSocket;
		instrument = new DesireInstrument();
		stringDivider = new StringDivider();
		socketIn = new BufferedReader(new InputStreamReader(interactiveSocket.getInputStream()));
		//socketOut = new DataOutputStream(interactiveSocket.getOutputStream());	
		socketOut = new ObjectOutputStream(interactiveSocket.getOutputStream());
	}
	
	private void confirmSuccess() throws IOException{
		socketOut.writeBoolean(true);
		socketOut.flush();
		System.out.println("*** THREAD SENDED OBJECT TRUE");
	}
	
	private void confirmFail() throws IOException{
		socketOut.writeBoolean(false);
		socketOut.flush();
		System.out.println("*** THREAD SENDED OBJECT FALSE");
	}
	
	private ArrayList<String> getListFromDesires(ResultSet set) throws SQLException{ // to be changed
		ArrayList<String> list = new ArrayList<String>();
			while(set.next()){
				list.add(set.getString("desire"));
			}
			return list;
	}
	
//	private String scanString() throws Exception{
//		String receivedString = socketIn.readLine();
//		if (receivedString != null){
//			return receivedString;
//		}
//		else{
//			throw new Exception("Null string got");
//		}
//	}

	public void run(){
		while(!isInterrupted()){
			try {
				System.out.println("*** THREAD " + currentUser + " : Waiting for a string");
				String receivedString = socketIn.readLine();
				System.out.println("*** THREAD " + currentUser + " string is recieved : " + receivedString);
				char commandChar = receivedString.charAt(0);
				switch (commandChar){
				case('R') : { //Registering//
					System.out.println("*** THREAD " + currentUser + " Registering ");
					ArrayList<String> stringSet = stringDivider.parseSlashedString(receivedString);
					String login = stringSet.get(0);
					String password = stringSet.get(1);
					String name = stringSet.get(2);
					String sex = stringSet.get(3);
					String  birth = stringSet.get(4);
					try{
						synchronized(this){
							instrument.register(login, password, name, sex, birth);
						}
						confirmSuccess();
						currentUser = login;
						System.out.println("*** THREAD " + currentUser + " online " );
					}
					catch(Exception error){
						confirmFail();
						System.out.println("*** THREAD " + currentUser + " Error while registering");
						System.out.println(error.getMessage());
					}
					break;
				}
				case('L') : { // Logging In//
					System.out.println("*** THREAD " + currentUser + " Logging IN");
					ArrayList<String> stringSet = stringDivider.parseSlashedString(receivedString);
					String login = stringSet.get(0);
					String password = stringSet.get(1);
					try{
						synchronized(this){
							instrument.logIn(login, password);
						}
						confirmSuccess();
						currentUser = login;
						System.out.println("*** THREAD " + currentUser + " online");
					}
					catch(Exception error){
						confirmFail();
						System.out.println("*** THREAD " + currentUser + " ERROR WHILE LOGGING IN");
						System.out.println(error.getMessage());
					}
					break;
				}
				case('A') : { //Adding a desire//
					String newDesireString = receivedString.substring(1);
					try{
						synchronized(this){
							instrument.addDesire(currentUser, newDesireString);
						}
						System.out.println("*** THREAD " + currentUser + " DESIRE ADDED");
						confirmSuccess();
					}
					catch(Exception error){
						confirmFail();
						System.out.println("*** THREAD " + currentUser + " ERROR WHILE ADDING");
						System.out.println(error.getMessage());
					}
					break;
				}
				case('S') : { // Showing desires//
					try{
						synchronized(this){
							ResultSet desires = instrument.getDesires(currentUser);
//							CachedRowSetImpl cachedDesires = new CachedRowSetImpl();
//							cachedDesires.populate(desires);
							ArrayList<String> cachedDesires = getListFromDesires(desires);
							confirmSuccess();
							socketOut.writeObject(cachedDesires);
							System.out.println("*** THREAD " + currentUser + " SENT ResultSet of desires");
						}
					}
					catch(Exception error){
						confirmFail();
						System.out.println("*** THREAD " + currentUser + " Error WHILE TAKING DESIRES");
						System.out.println(error.getMessage());
					}
					break;
				}
				case('I') : {
					try{
						synchronized(this){
							ResultSet desires = instrument.getInfo(currentUser);
							CachedRowSetImpl cachedInfo = new CachedRowSetImpl();
							cachedInfo.populate(desires);
							confirmSuccess();
							socketOut.writeObject(cachedInfo);
							System.out.println("*** THREAD " + currentUser + "SENT ResultSet of info");
						}
					}
					catch(Exception error){
						confirmFail();
						System.out.println("*** THREAD " + currentUser + " Error WHILE TAKING INFO");
						System.out.println(error.getMessage());
					}
					break;
				}
				case('E') : {
					System.out.println("*** THREAD " + currentUser + " exiting");
					interrupt();
					confirmSuccess();
					System.out.print("*** THREAD INTERRUPTED ");
					System.out.println(isInterrupted());
					//instrument.exit();
					//currentUser = "unknown";
					//interactiveSocket.close();
					break;
				}
				default:{
					confirmFail();
					interrupt();
					System.out.println("*** THREAD INTERRUPTED");
					System.out.println(isInterrupted());
					//instrument.exit();
					//currentUser = "unknown";
					//interactiveSocket.close();
					break;
				}//default                        
				}//switch
			}//main try
			catch(IOException ioError){
				System.out.println("Connection error(socket, proceeding input)");
				System.out.println(ioError.getMessage());
				ioError.printStackTrace();
				try {
					interactiveSocket.close();
				} 
				catch(IOException error){
					System.out.println("Socket has not been closed.\n");
				}
				finally{
					interrupt();
					System.out.println("*** THREAD INTERRUPTED");
					System.out.println(isInterrupted());
				}
			}
		}//while
	}//run
}
