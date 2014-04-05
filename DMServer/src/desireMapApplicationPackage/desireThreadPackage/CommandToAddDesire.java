package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.AddPack;

public class CommandToAddDesire extends CommandForDesireThread{
	private AddPack addPack;
	//--
	public CommandToAddDesire(DesireThread newReceiver, AddPack newAddPack){
		receiver = newReceiver;
		addPack = newAddPack;
	}

	@Override
	public void execute() throws Exception {
		System.out.println("*** Adding ");
		try{
			synchronized(receiver){
				receiver.addDesire(addPack.desireContent);
			}
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
