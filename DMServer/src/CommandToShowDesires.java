import java.sql.ResultSet;
import java.util.ArrayList;

public class CommandToShowDesires extends CommandForDesireThread{
	
	private DesireThread receiver;
	private String category;
	
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
				ArrayList<ArrayList<String>> table = receiver.resultSetConverter.getArrayListFromResultSet(desires);
				receiver.socketOut.writeObject(table);
				System.out.println("*** THREAD " + receiver.currentUser + " SENT ArrayList of desires");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
