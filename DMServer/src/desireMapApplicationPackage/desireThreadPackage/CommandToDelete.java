package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;

public class CommandToDelete extends CommandForDesireThread{
	private DeletePack deletePack;
	public CommandToDelete(DesireThread nReceiver, DeletePack newDeletePack){
		receiver = nReceiver;
		deletePack = newDeletePack;
	}

	public void execute() throws Exception{
		try{
				DeletePack delPack = deletePack;
				System.out.println("*** Deleting desires of the user");
				receiver.delete(delPack);
				receiver.sendTrue();
		}
		catch(Exception error){
			receiver.sendFalse();
			throw error;
		}
		
	}

}
