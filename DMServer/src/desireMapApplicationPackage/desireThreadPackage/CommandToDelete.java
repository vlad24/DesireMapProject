package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;

public class CommandToDelete extends CommandForDesireThread{
	private DeletePack deletePack;
	public CommandToDelete(DesireThread nReceiver, DeletePack newDeletePack){
		receiver = nReceiver;
		deletePack = newDeletePack;
	}

	public void execute() throws Exception{
		try{
				DeletePack delByContentPack = deletePack;
				System.out.println("*** Deleting a desires of the user)");
				receiver.delete(delByContentPack);
				receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
		
	}

}
