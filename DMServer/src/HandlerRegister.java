import java.util.ArrayList;


public class HandlerRegister extends Handler{

	public HandlerRegister(DesireThread inThread) {
		super(inThread);
	}
	
	@Override
	public CommandForDesireThread handleString(String input){
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String login = stringSet.get(0);
		String password = stringSet.get(1);
		String name = stringSet.get(2);
		String sex = stringSet.get(3);
		String birth = stringSet.get(4);
		return (new CommandToRegister(ownerThread, login, password, name, sex, birth)); 
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'R';
	}

}
