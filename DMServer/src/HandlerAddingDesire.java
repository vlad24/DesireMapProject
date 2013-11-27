
public class HandlerAddingDesire extends Handler{
	public HandlerAddingDesire(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		String desireString = input.substring(1);
		//Here in forth future we can analyze what category of desires we have and generate special commands based on learned
		return (new CommandToAddDesire(ownerThread, desireString));
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'A';
	}
}
