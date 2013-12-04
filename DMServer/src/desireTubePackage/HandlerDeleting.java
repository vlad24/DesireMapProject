package desireTubePackage;

import desireThreadPackage.CommandForDesireThread;
import desireThreadPackage.CommandToDelete;
import desireThreadPackage.DesireThread;

public class HandlerDeleting extends Handler{

	public HandlerDeleting(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'D';
	}

	@Override
	public CommandForDesireThread handleString(String input) {
		String category = input.substring(1);
		return new CommandToDelete(ownerThread, category);
	}

}
