import java.util.ArrayList;


public class HandlerLogIn extends Handler{

	public HandlerLogIn(DesireThread inThread) {
		super(inThread);
	}
	
	@Override
	public void handleString(String input){
		try{
		System.out.println("*** Logging IN");
		ArrayList<String> stringSet = divider.parseSlashedString(input);
		String login = stringSet.get(0);
		String password = stringSet.get(1);
		synchronized(this){
				ownerThread.instrument.logIn(login, password);
		}
		ownerThread.currentUser = login;
		System.out.println("***" + login + " online");
		confirmSuccess();
		}
		catch(Exception error){
			confirmFail();
			System.out.println(error.getMessage());
		}
	}

	@Override
	protected boolean myJob(char code) {
		return code == 'L';
	}
}
