package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;

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
			int generatedID = 0;
			synchronized(receiver){
				generatedID = receiver.addDesire(addPack);
			}
			receiver.sendID(generatedID);
		}
		catch(Exception error){
			receiver.sendID(-1);
			throw error;
		}
	}
	
}
