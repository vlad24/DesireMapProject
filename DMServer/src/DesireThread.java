import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DesireThread extends Thread{

	private Socket interactiveSocket;
	private DesireTube tube;
	protected String currentUser;
	protected DesireInstrument instrument;
	protected ObjectOutputStream socketOut;
	protected BufferedReader socketIn;
	//--
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

	protected void confirmSuccess(){
		try{
			socketOut.writeBoolean(true);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED SUCCESS");
		}
		catch(Exception error){
			System.out.println("Problem with socket(confirm true)");
		}
	}

	protected void confirmFail(){
		try{
			socketOut.writeBoolean(false);
			socketOut.flush();
			System.out.println("*** THREAD CONFIRMED FAIL");
		}
		catch(IOException error){
			System.out.println("Problem with socket(confirm fail)");
		}
	}

	protected String scanString() throws IOException{
		String receivedString = socketIn.readLine();
		if (receivedString != null){
			return receivedString;
		}
		else{
			throw new IOException("NULL STRING GOT");
		}
	}
	
	protected ArrayList<ArrayList<String>> getArrayListFromResultSet(ResultSet resultRows, int columnsAmount) throws SQLException{
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		int row = 0;
		while(resultRows.next())
		{
			row++;
			table.set(row,  new ArrayList<String>());
			for (int column = 0; column < columnsAmount ; column++){ 
				table.get(row).set(column, resultRows.getString(column));
			}
		}
		return table;
	}

	public void run(){
		while(!isInterrupted()){
			try {
				System.out.println("*** THREAD " + currentUser + " : Waiting for a string");
				String receivedString = scanString();
				CommandForDesireThread threadCommand = tube.processString(receivedString);
				//Here we can store our commands somewhere and if needed undo them 
				try{
					threadCommand.execute();
				}
				catch(Exception error){
					System.out.println("***THREAD Command failed during execution");
					System.out.println(error.getMessage());
				}
			}
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
