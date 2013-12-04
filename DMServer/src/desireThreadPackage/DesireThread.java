package desireThreadPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import desireInstrumentPackage.DesireInstrument;
import desireTubePackage.DesireTube;

public class DesireThread extends Thread{

	private Socket interactiveSocket;
	private DesireTube tube;
	private String currentUser;
	protected DesireInstrument instrument;
	protected ObjectOutputStream socketOut;
	protected BufferedReader socketIn;
	protected ResultSetMaster resultSetConverter;
	//--
	
	public DesireThread(Socket givenSocket) throws IOException{//
		System.out.println("*** Thread is initializing\n");
		this.setDaemon(true);
		interactiveSocket = givenSocket;
		instrument = new DesireInstrument();
		tube = new DesireTube(this);
		socketIn = new BufferedReader(new InputStreamReader(interactiveSocket.getInputStream()));
		socketOut = new ObjectOutputStream(interactiveSocket.getOutputStream());
		resultSetConverter = new ResultSetMaster();
	}

	public String getCurrentUser() {
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
			System.out.println("*** Problem with socket(confirm true)");
		}
	}

	protected void confirmFail(){
		try{
			socketOut.writeBoolean(false);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED FAIL");
		}
		catch(IOException error){
			System.out.println("*** Problem with socket(confirm fail)");
		}
	}

	protected String scanString() throws IOException{
		String receivedString = socketIn.readLine();
		if (receivedString != null){
			return receivedString;
		}
		else{
			throw new IOException("****** NULL STRING GOT");
		}
	}

	public void run(){
		while(!isInterrupted()){
			try {
				System.out.println("*** THREAD " + getCurrentUser() + " : Waiting for a client command...");
				String receivedString = scanString();
				CommandForDesireThread threadCommand = tube.processString(receivedString);
				//Here we can store our commands somewhere and if needed undo them 
				try{
					threadCommand.execute();
				}
				catch(Exception error){
					System.out.println("****** THREAD Command failed during execution");
					System.out.println(error.getMessage());
				}
			}
			catch(IOException ioError){
				System.out.println("****** Connection error(socket, proceeding input)");
				System.out.println(ioError.getMessage());
				ioError.printStackTrace();
				try {
					interactiveSocket.close();
				} 
				catch(IOException error){
					System.out.println("****** Socket has not been closed.\n");
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
