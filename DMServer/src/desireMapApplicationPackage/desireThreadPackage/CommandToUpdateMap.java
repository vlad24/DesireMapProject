package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;

public class CommandToUpdateMap extends CommandForDesireThread {

	public final TilesPack tilesPack;
	
	public CommandToUpdateMap(DesireThread newReceiver, TilesPack newTiles){
		receiver = newReceiver;
		tilesPack = newTiles;
	}

	@Override
	public void execute() throws Exception {
		try{
			SatisfySet newSatisfiers = receiver.updateSatisfiers(tilesPack);
			System.out.println("...Sending updated satisfiers");
			receiver.socketOut.writeObject(newSatisfiers);
			receiver.socketOut.reset();
		}
		catch(Exception error){
			receiver.socketOut.writeObject(null);
			throw error;
		}
	}

	@Override
	public void unexecute() {
		System.out.println("Unexecuting updatingSatisfiers. No unexecution available.");		
	}

}
