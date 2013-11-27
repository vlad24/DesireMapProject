
public class CommandToAddDesire extends CommandForDesireThread{
	
	private String desireString;
	//--
	public CommandToAddDesire(DesireThread newReceiver, String newDesireString){
		receiver = newReceiver;
		desireString = newDesireString;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("*** Adding a desire");
		try{
			synchronized(receiver){
				receiver.instrument.addDesire(receiver.currentUser, desireString);
			}
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
