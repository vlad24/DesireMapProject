
public class HandlerShowerDesires extends Handler{

	public HandlerShowerDesires(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		return new CommandToShowDesires(ownerThread);		
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'S';
	}
}
