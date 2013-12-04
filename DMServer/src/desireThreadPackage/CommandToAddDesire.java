package desireThreadPackage;

import desiresPackage.Desire;






public class CommandToAddDesire extends CommandForDesireThread{
	private Desire desire;
	//--
	public CommandToAddDesire(DesireThread newReceiver, Desire nDesire){
		receiver = newReceiver;
		desire = nDesire;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("*** Adding a desire");
		try{
			synchronized(receiver){
				receiver.instrument.addDesire(desire);
			}
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
