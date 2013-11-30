import java.sql.ResultSet;

import com.sun.rowset.CachedRowSetImpl;

public class CommandToShowInfo extends CommandForDesireThread{
	public  CommandToShowInfo(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception{
		System.out.println("***Showing info");
		try{
			synchronized(receiver){
				ResultSet desires = receiver.instrument.getInfo(receiver.currentUser);
				receiver.confirmSuccess();
				CachedRowSetImpl cachedDesires = new CachedRowSetImpl();
				cachedDesires.populate(desires);
				receiver.socketOut.writeObject(cachedDesires);
				System.out.println("*** THREAD " + receiver.currentUser + " SENT ResultSet of Info");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}

}
