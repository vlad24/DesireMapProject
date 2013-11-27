import java.util.ArrayList;


public class HandlerAddingDesire extends Handler{
	public HandlerAddingDesire(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String desireString = stringSet.get(0);
		String tag = stringSet.get(1);
		String latitude = stringSet.get(2);
		String longitude = stringSet.get(3);
		//Here in forth future we can analyze what category of desires we have and generate special commands based on learned
		return (new CommandToAddDesire(ownerThread, desireString, tag, latitude, longitude));
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'A';
	}
}
