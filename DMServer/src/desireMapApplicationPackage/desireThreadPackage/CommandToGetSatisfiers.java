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
		System.out.println("***Getting satisfiers executed");
		SatisfySet satisfiers = receiver.getSatisfiers(satisfyPack);
		receiver.socketOut.writeObject(satisfiers);
		receiver.socketOut.reset();
		System.out.println("*** THREAD " + receiver.getUserName() + " SENT SATISFIERS");
	}

	@Override
	public void unexecute() {
		System.out.println("Unexecuting getting satisfiers. No unexecution available.");
	}
	
}
