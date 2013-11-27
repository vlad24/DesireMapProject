import java.util.ArrayList;

public class HandlerLogIn extends Handler{

	public HandlerLogIn(DesireThread inThread) {
		super(inThread);
	}
	
	@Override
	public CommandForDesireThread handleString(String input){
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String login = stringSet.get(0);
		String password = stringSet.get(1);
		return (new CommandToLogIn(ownerThread, login, password));
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'L';
	}
}
