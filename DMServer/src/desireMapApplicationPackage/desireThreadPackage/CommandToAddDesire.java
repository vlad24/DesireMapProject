package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;

public class CommandToAddDesire extends CommandForDesireThread{
	private String localGeneratedID;
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
			String generatedID = "";
			generatedID = receiver.addDesire(addPack);
			localGeneratedID = generatedID;
			receiver.sendID(generatedID);
		}
		catch(Exception error){
			receiver.sendID("");
			throw error;
		}
	}

	@Override
	public void unexecute() {
		try {
			receiver.delete(new DeletePack(localGeneratedID));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Cannot rollback at adding");
		}
	}
	
}
