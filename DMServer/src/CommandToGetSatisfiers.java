import java.sql.ResultSet;

import com.sun.rowset.CachedRowSetImpl;


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
				CachedRowSetImpl cachedSatisfiers = new CachedRowSetImpl();
				cachedSatisfiers.populate(satisfiers);
				receiver.socketOut.writeObject(cachedSatisfiers);
				System.out.println("*** THREAD " + receiver.currentUser + " SENT RESULT SET OF SATISFIERS");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
