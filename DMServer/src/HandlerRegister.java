import java.util.ArrayList;


public class HandlerRegister extends Handler{

	public HandlerRegister(DesireThread inThread) {
		super(inThread);
	}
	
	@Override
	public void handleString(String input){
		try{
			System.out.println("***Registering ");
			System.out.println(input);
			ArrayList<String> stringSet = divider.parseSlashedString(input);
			String login = stringSet.get(0);
			String password = stringSet.get(1);
			String name = stringSet.get(2);
			String sex = stringSet.get(3);
			String  birth = stringSet.get(4);
			System.out.println("Received : " + login + password + name + sex + birth);
			synchronized(this){
				ownerThread.instrument.register(login, password, name, sex, birth);
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
		return code == 'R';
	}

}
