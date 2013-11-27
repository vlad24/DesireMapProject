import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class HandlerShowerDesires extends Handler{

	public HandlerShowerDesires(DesireThread inThread) {
		super(inThread);
	}
	
	private ArrayList<String> getListFromDesires(ResultSet set) throws SQLException{ // to be changed
		ArrayList<String> list = new ArrayList<String>();
			while(set.next()){
				list.add(set.getString("desire"));
			}
			return list;
	}
	@Override
	public void handleString(String input){
		System.out.println("***Showing desires");
		try{
			synchronized(this){
				ResultSet desires = ownerThread.instrument.getDesires(ownerThread.currentUser);
				confirmSuccess();
				//		CachedRowSetImpl cachedDesires = new CachedRowSetImpl();
				//		cachedDesires.populate(desires);
				ArrayList<String> cachedDesires = getListFromDesires(desires); //x
				ownerThread.socketOut.writeObject(cachedDesires);
				System.out.println("*** THREAD " + ownerThread.currentUser + " SENT ArrayList of desires");
			}
		}
		catch(Exception error){
			confirmFail();
			System.out.println(error.getMessage());
		}
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'S';
	}
}
