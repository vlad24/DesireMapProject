import java.sql.ResultSet;
import com.sun.rowset.CachedRowSetImpl;


public class HandlerShowerInfo extends Handler{

	public HandlerShowerInfo(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void handleString(String input){
		System.out.println("***Showing info");
		try{
			ResultSet desires = null;
			synchronized(this){
				desires = ownerThread.instrument.getDesires(ownerThread.currentUser);
				confirmSuccess();
				CachedRowSetImpl cachedDesires = new CachedRowSetImpl();
				cachedDesires.populate(desires);
				ownerThread.socketOut.writeObject(cachedDesires);
				System.out.println("*** THREAD " + ownerThread.currentUser + " SENT ResultSet of Info");
			}
		}
		catch(Exception error){
			System.out.println(error.getMessage());
			confirmFail();
		}
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'I';
	}
}