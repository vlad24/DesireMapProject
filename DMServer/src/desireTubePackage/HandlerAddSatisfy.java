package desireTubePackage;

import java.util.ArrayList;

import queryObjectPackage.Desire;

import desireThreadPackage.CommandBinary;
import desireThreadPackage.CommandForDesireThread;
import desireThreadPackage.CommandToAddDesire;
import desireThreadPackage.CommandToGetSatisfiers;
import desireThreadPackage.DesireThread;


public class HandlerAddSatisfy extends Handler{

	public HandlerAddSatisfy(DesireThread surroundingThread) {
		super(surroundingThread);
	}
	
	@Override
	public CommandForDesireThread handleString(String input) {
		//Example of input string :   _DATING_WITHWOMEN/beautiful girl/girl/2.4/1.6/72
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String tableSuffix = stringSet.get(0);
		String desireString = stringSet.get(1);
		String tag = stringSet.get(2);  
		String latitude = stringSet.get(3);  
		String longitude = stringSet.get(4); 
		String radius = stringSet.get(5);
		
		Desire gotDesire = new Desire(ownerThread.getCurrentUser(), tableSuffix, desireString, tag, latitude, longitude);
		CommandBinary complex = new CommandBinary(new CommandToAddDesire(ownerThread, gotDesire), new CommandToGetSatisfiers(ownerThread, gotDesire, radius));
		return complex;
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'G';
	}

}
