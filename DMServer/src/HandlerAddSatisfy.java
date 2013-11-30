import java.util.ArrayList;


public class HandlerAddSatisfy extends Handler{

	public HandlerAddSatisfy(DesireThread surroundingThread) {
		super(surroundingThread);
	}
	
	@Override
	public CommandForDesireThread handleString(String input) {
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String tableSuffix = stringSet.get(0); System.out.println(tableSuffix);
		String desireString = stringSet.get(1);  System.out.println(desireString);
		String tag = stringSet.get(2); System.out.println(tag);
		String latitude = stringSet.get(3); System.out.println(latitude);
		String longitude = stringSet.get(4); System.out.println(longitude);
		String radius = stringSet.get(5); System.out.println(radius);
		
		Desire gotDesire = new Desire(ownerThread.currentUser, tableSuffix, desireString, tag, latitude, longitude);
		CommandBinary complex = new CommandBinary(new CommandToAddDesire(ownerThread, gotDesire), new CommandToGetSatisfiers(ownerThread, gotDesire, radius));
		return complex;
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'G';
	}

}
