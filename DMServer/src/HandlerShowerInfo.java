
public class HandlerShowerInfo extends Handler{

	public HandlerShowerInfo(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public CommandForDesireThread handleString(String input){
		return (new CommandToShowInfo(ownerThread));
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'I';
	}
}