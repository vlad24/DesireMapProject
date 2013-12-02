import java.sql.ResultSet;

public class CommandToShowInfo extends CommandForDesireThread{
			
	public  CommandToShowInfo(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception{
		System.out.println("***Showing info");
		try{
			synchronized(receiver){
				ResultSet infoSet = receiver.instrument.getInfo(receiver.currentUser);
				receiver.confirmSuccess();
				receiver.socketOut.writeObject(receiver.getArrayListFromResultSet(infoSet, 3));
				System.out.println("*** THREAD " + receiver.currentUser + " SENT ArrayList<ArrayList<String>> of Info");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}

}
