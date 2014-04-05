package desireMapApplicationPackage.desireThreadPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Deque;
import java.util.LinkedList;

import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.handlerTubePackage.HandlerTube;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.messageSystemPackage.ChatKing;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class DesireThread implements Runnable{
	
	private String currentUser;
	private final Socket interactiveSocket;
	protected ThreadState stateObject;
	protected ObjectOutputStream socketOut;
	protected ObjectInputStream socketIn;
	private final HandlerTube tube;
	protected Deque<Message> localMessages;
	
	public DesireThread(Socket givenSocket) throws IOException{
		System.out.println("*** Thread is initializing\n");
		setCurrentUser("?");
		stateObject = new ThreadStateStart(this);
		tube = new HandlerTube(this);
		localMessages = new LinkedList<Message>();
		interactiveSocket = givenSocket;
		socketOut = new ObjectOutputStream(interactiveSocket.getOutputStream());
		socketOut.flush();
		socketIn = new ObjectInputStream(interactiveSocket.getInputStream());
	}
	////API:
	public void register(RegistrationData regData) throws Exception{
		stateObject.register(regData);
	}
	
	public void logIn(LoginData logData) throws Exception{
		stateObject.logIn(logData);
	}
	
	public void addDesire(DesireContent desireContent) throws Exception{
		stateObject.addDesire(desireContent);
	}
	
	public void delete(DeletePack pack) throws Exception{
		stateObject.delete(pack);
	}
		
	public SatisfySet getSatisfiers(SatisfyPack sPack){
		try {
			return stateObject.getSatisfiers(sPack);
		} catch (Exception error) {
			error.printStackTrace();
			return null;
		}
	}
	
	public MainData getInfo() throws Exception{
		return stateObject.getInfo();
	}
	
	public DesireSet getPersonalDesires(Cryteria cryteria) throws Exception{
		return stateObject.getPersonalDesires(cryteria);
	}

	public void registerTalker(boolean deliverNeed){
		ChatKing.getInstance().registerThread(this, deliverNeed);
		System.out.println("+Thread is registered");
	}
	
	public void unregisterTalker(){
		ChatKing.getInstance().unregisterThread(this);
		System.out.println("+Thread is unregistered");
	}
	
	public void postMessage(Message message) throws Exception {
		stateObject.postMessage(message);
	}
	
	public void sendDeliveredMessagesToClient() throws Exception{
		stateObject.sendDeliveredMessagesToClient();
	}
	
	public void clearLocalMessageHistory(){
		localMessages.clear();
		System.out.println("+  Local messages history of " + currentUser +  " has been cleared");
}
	
	public void takeMessages(Deque<Message> deque) throws Exception{
		stateObject.takeMessages(deque);
	}
	
	public void takeMessages(Message message) throws Exception {
		stateObject.takeMessages(message);
	}
	
	public void exit(){
		stateObject.changeState(new ThreadStateStart(this));
	}
	
	/////////////////////////////////////////////////////////////////////////
	public String getUserName() {
		return currentUser;
	}

	protected void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	
	protected void confirmSuccess(){
		try{
			socketOut.writeBoolean(true);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED SUCCESS");
		}
		catch(Exception error){
			System.out.println("*** Problem with socket in confirm true");
		}
	}

	protected void confirmFail(){
		try{
			socketOut.writeBoolean(false);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED FAIL");
		}
		catch(IOException error){
			System.out.println("*** Problem with socket in confirm fail");
		}
	}

	protected ActionQueryObject scanQuery() throws IOException, ClassNotFoundException{
		ActionQueryObject query = (ActionQueryObject) socketIn.readObject();
		if (query != null){
			return query;
		}
		else{
			throw new IOException("****** NULL QUERY!!!");
		}
	}

//////////////////////////////
//////////////////////////////
//////////////////////////////
	public void run(){
		while(!Thread.currentThread().isInterrupted()){
			try {
				System.out.println("*** THREAD " + getUserName() + " : Waiting for a client command...");
				ActionQueryObject query = scanQuery();
				QueueOfCommands threadCommands = tube.processClientQuery(query);
				//Here we can store our commands somewhere and if needed undo them 
				try{
					threadCommands.executeAllPolling();
				}
				catch(Exception error){
					System.out.println("****** THREAD Commands failed during execution");
					System.out.println(error.getMessage());
				}
			}
			catch(IOException | ClassNotFoundException ioError){
				System.out.println("****** (Socket || Proceeding input) error");
				System.out.println(ioError.getMessage());
				ioError.printStackTrace();
				try {
					interactiveSocket.close();
				} 
				catch(IOException error){
					System.out.println("****** Socket has not been closed. DANGER!");
				}
				finally{
					Thread.currentThread().interrupt();
					System.out.println("*** THREAD INTERRUPTED ");
					System.out.println(Thread.currentThread().isInterrupted());
				}
			}
		}//while
	}//run
	
}
