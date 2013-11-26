import java.io.*;
import java.net.*;

public class DesireThread extends Thread{
	private Socket interactiveSocket;
	private DesireTube tube;
	protected String currentUser;
	protected DesireInstrument instrument;
	protected ObjectOutputStream socketOut;
	protected BufferedReader socketIn;

	public DesireThread(Socket givenSocket) throws IOException{//
		System.out.println("*** Thread is being constructed\n");
		this.setDaemon(true);
		currentUser = "?";
		interactiveSocket = givenSocket;
		instrument = new DesireInstrument();
		tube = new DesireTube(this);
		socketIn = new BufferedReader(new InputStreamReader(interactiveSocket.getInputStream()));
		socketOut = new ObjectOutputStream(interactiveSocket.getOutputStream());
	}
		
	public void run(){
		while(!isInterrupted()){
			try {
				System.out.println("*** THREAD " + currentUser + " : Waiting for a string");
				String receivedString = socketIn.readLine();
				System.out.println("Received : " + receivedString);
				try{
					tube.processString(receivedString);
				}
				catch(Exception error){
					System.out.println(error.getMessage());
				}
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
