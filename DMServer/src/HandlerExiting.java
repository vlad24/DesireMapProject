
public class HandlerExiting extends Handler{
	
	public HandlerExiting(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		return (new CommandToExit(ownerThread));		
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'E';
	}

}
