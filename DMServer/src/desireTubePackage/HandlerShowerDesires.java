package desireTubePackage;

import desireThreadPackage.CommandForDesireThread;
import desireThreadPackage.CommandToShowDesires;
import desireThreadPackage.DesireThread;






public class HandlerShowerDesires extends Handler{

	public HandlerShowerDesires(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		String category = input.substring(1);
		return new CommandToShowDesires(ownerThread, category);		
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'S';
	}
}
