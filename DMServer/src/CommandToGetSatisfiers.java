
import java.sql.ResultSet;
import java.util.ArrayList;

public class CommandToGetSatisfiers extends CommandForDesireThread{
	
	private Desire desire;
	private String radius;
	//--
	public CommandToGetSatisfiers(DesireThread ownerThread, Desire nDesire, String nRadius){
		receiver = ownerThread;
		desire = nDesire;
		radius = nRadius;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("***Getting satisfiers");
		try{
			synchronized(receiver){
				ResultSet satisfiers = receiver.instrument.getSatisfiersToday(desire, radius);
				receiver.confirmSuccess();
				ArrayList<ArrayList<String>> table = receiver.resultSetConverter.getArrayListFromResultSet(satisfiers);
				receiver.socketOut.writeObject(table);
				System.out.println("*** THREAD " + receiver.currentUser + " SENT TABLE OF SATISFIERS");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
