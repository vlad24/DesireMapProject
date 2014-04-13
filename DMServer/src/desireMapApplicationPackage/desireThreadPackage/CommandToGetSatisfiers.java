package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;

public class CommandToGetSatisfiers extends CommandForDesireThread{
	private final DesireThread receiver;
	private final SatisfyPack satisfyPack;
	//--
	public CommandToGetSatisfiers(DesireThread newReceiver, SatisfyPack newSatisfyPack){
		receiver = newReceiver;
		satisfyPack = newSatisfyPack;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("***Getting satisfiers");
		try{
				SatisfySet satisfiers = receiver.getSatisfiers(satisfyPack);
				if (satisfiers != null){
					receiver.confirmSuccess();
					receiver.socketOut.writeObject(satisfiers);
					System.out.println("*** THREAD " + receiver.getUserName() + " SENT SATISFIERS");
				}
				else{
					throw new Exception("...no satisfiers");
				}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
