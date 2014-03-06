package desireTubePackage;

import java.util.ArrayList;

import queryObjectPackage.Desire;
import desireThreadPackage.CommandForDesireThread;
import desireThreadPackage.CommandToAddDesire;
import desireThreadPackage.DesireThread;


public class HandlerAddingDesire extends Handler{
	public HandlerAddingDesire(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String tableSuffix = stringSet.get(0);
		String desireString = stringSet.get(1);
		String tag = stringSet.get(2);
		String latitude = stringSet.get(3);
		String longitude = stringSet.get(4);
		
		Desire gotDesire = new Desire(ownerThread.getCurrentUser(), tableSuffix, desireString, tag,  latitude, longitude);
		return (new CommandToAddDesire(ownerThread, gotDesire));
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'A';
	}
}
