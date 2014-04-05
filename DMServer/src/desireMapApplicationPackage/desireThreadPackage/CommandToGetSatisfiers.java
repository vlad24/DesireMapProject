package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;

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
			synchronized(this){
				SatisfySet satisfiers = receiver.getSatisfiers(satisfyPack);
				receiver.confirmSuccess();
				receiver.socketOut.writeObject(satisfiers);
				System.out.println("*** THREAD " + receiver.getUserName() + " SENT SATISFIERS");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
