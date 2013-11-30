import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.rowset.CachedRowSetImpl;


public class CommandToShowDesires extends CommandForDesireThread{
	
	private DesireThread receiver;
	private String category;
	//--
	private ArrayList<String> getListFromDesires(ResultSet set) throws SQLException{
		ArrayList<String> list = new ArrayList<String>();
			while(set.next()){
				list.add(set.getString("desire"));
			}
			return list;
	} // TO BE DELETED
	
	public CommandToShowDesires(DesireThread newReceiver, String nCategory){
		receiver = newReceiver;
		category = nCategory;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("***Showing desires");
		try{
			synchronized(receiver){
				ResultSet desires = receiver.instrument.getDesires(receiver.currentUser, category);
				receiver.confirmSuccess();
				//CachedRowSetImpl cachedDesires = new CachedRowSetImpl();
				//cachedDesires.populate(desires);
				ArrayList<String> cachedDesires = getListFromDesires(desires); // TO BE DELETED
				receiver.socketOut.writeObject(cachedDesires);
				System.out.println("*** THREAD " + receiver.currentUser + " SENT ArrayList of desires");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
