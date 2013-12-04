package desireThreadPackage;


import java.sql.ResultSet;
import java.util.ArrayList;

public class CommandToShowInfo extends CommandForDesireThread{
			
	public  CommandToShowInfo(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception{
		System.out.println("***Showing info");
		try{
			synchronized(receiver){
				ResultSet infoSet = receiver.instrument.getInfo(receiver.getCurrentUser());
				receiver.confirmSuccess();
				ArrayList<ArrayList<String>> table = receiver.resultSetConverter.getArrayListFromResultSet(infoSet);
				receiver.socketOut.writeObject(table);
				System.out.println("*** THREAD " + receiver.getCurrentUser() + " SENT ArrayList<ArrayList<String>> of Info");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}

}
