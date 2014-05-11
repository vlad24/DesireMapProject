package desireMapApplicationPackage.desireThreadPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.handlerTubePackage.HandlerTube;
import desireMapApplicationPackage.messageSystemPackage.ChatKing;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.MainData;

public class DesireThread implements Runnable{
	private String currentUser;
	private String deviceID;
	private final HandlerTube tube;
	private final Socket interactiveSocket;
	protected ObjectOutputStream socketOut;
	protected ObjectInputStream socketIn;
	protected ChatKing chater;
	protected DesireInstrument instrument;
	protected ThreadState stateObject;
	public boolean isRunning;
	StatePool states;
	
	public DesireThread(Socket givenSocket) throws IOException{
		System.out.println("*** Thread is initializing\n");
		setCurrentUser("?");
		setDeviceID("?");
		states = new StatePool();
		isRunning = false;
		stateObject = states.getFreshStateStart(this);
		tube = new HandlerTube(this);
		instrument = DesireInstrument.getInstance();
		chater = ChatKing.getInstance();
		interactiveSocket = givenSocket;
		socketOut = new ObjectOutputStream(interactiveSocket.getOutputStream());
		socketOut.flush();
		socketIn = new ObjectInputStream(interactiveSocket.getInputStream());
	}
	
	/////////////////////////////////////
	public String getDeviceID() {
		return deviceID;
	}
	
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
	public String getUserName() {
		return currentUser;
	}

	protected void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	
	protected void sendTrue(){
		try{
			socketOut.writeBoolean(true);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED SUCCESS");
		}
		catch(Exception error){
			System.out.println("*** Problem with socket in confirm true");
		}
	}

	protected void sendFalse(){
		try{
			socketOut.writeBoolean(false);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED FAIL");
		}
		catch(IOException error){
			System.out.println("*** Problem with socket in confirm fail");
		}
	}
	
	protected void sendID(String generatedID){
		try{
			socketOut.writeUTF(generatedID);
			socketOut.flush();
			System.out.println("*** THREAD SENT THE ID : " + generatedID);
		}
		catch(IOException error){
			System.out.println("*** Problem with socket in sending id");
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
	/////////////////////////////////////////////////////////////
	
	////API:
	public void register(RegistrationPack regPack) throws Exception{
		stateObject.register(regPack);
	}
	
	public void authorize(LoginPack logPack) throws Exception{
		stateObject.authorize(logPack);
	}
	
	public String addDesire(AddPack addPack) throws Exception{
		return stateObject.addDesire(addPack);
	}
	
	public void delete(DeletePack pack) throws Exception{
		stateObject.delete(pack);
	}
		
	public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception{
			return stateObject.getSatisfiers(sPack);
	}
	
	public SatisfySet updateSatisfiers(TilesPack tilesPack) throws Exception {
		try {
			SatisfySet result = stateObject.updateSatisfiers(tilesPack);
			return result;
		} catch (Exception error) {
			throw error;
		}
	}
	
	public MainData getInfo() throws Exception{
		return stateObject.getInfo();
	}
	
	public DesireSet getPersonalDesires(int category) throws Exception{
		return stateObject.getPersonalDesires(category);
	}
	
	public void postMessage(ClientMessage clientMessage) throws Exception {
		stateObject.postMessage(clientMessage);
	}
	
	public void loadNewMessages() throws Exception {
		stateObject.loadNewMessages();
	}
	
	public MessageSet getOldMessagesByCryteria(MessageDeliverPack pack) throws Exception {
		return stateObject.getOldMessagesByCryteria(pack);
	}

	public UserSet getUsersTalkedTo() throws Exception {
		return stateObject.getUsersTalkedTo();
	}
	
	public void likeDesire(LikePack pack) throws Exception{
		stateObject.likeDesire(pack);
	}
	
	public void exit(){
		stateObject.exit();
	}
	///API_END
	
	//////////////////////////////
	//////////////////////////////
	//////////////////////////////
	
	public void run(){
		isRunning = true;
		while(!Thread.currentThread().isInterrupted()){
			try {
				System.out.println("*** THREAD " + getUserName() + " : Waiting for a client command...");
				ActionQueryObject query = scanQuery();
				CommandsList threadCommands = tube.processClientQuery(query);
				//Here we can store our commands somewhere and if needed undo them 
				try{
					threadCommands.executeAllSafely();
				}
				catch(Exception error){
					System.out.println("****** THREAD Commands failed during execution");
					System.out.println(error.getMessage());
				}
			}
			catch(IOException | ClassNotFoundException error){
				System.out.println("****** (Socket || Proceeding input) error" + error.getMessage());
				try {
					interactiveSocket.close();
					System.out.println("****** Socket has been closed");
				} 
				catch(IOException socketError){
					System.out.println("****** Socket has not been closed. DANGER! : " + socketError.getMessage());
				}
				finally{
					isRunning = false;
					Thread.currentThread().interrupt();
					System.out.println("*** THREAD HAS BEEN INTERRUPTED ");
					exit();
					System.out.println("*** THREAD HAS EXITED ");
				}
			}
		}
	}

	
}
