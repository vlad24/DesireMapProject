
public class CommandToRegister extends CommandForDesireThread{
	
	private String login;
	private String password;
	private String name;
	private String sex;
	private String birth;
	//--
	public CommandToRegister(DesireThread newReceiver, String newLogin, String newPassword, String newName, String newSex, String newBirth) {
		receiver = newReceiver;
		login = newLogin;
		password = newPassword;
		sex = newSex;
		name = newName;
		birth = newBirth;
	}

	@Override
	public void execute() throws Exception {
		try{
			System.out.println("***Registering ");
			synchronized(receiver){
				receiver.instrument.register(login, password, name, sex, birth);
			}
			receiver.currentUser = login;
			System.out.println("***" + login + " online");
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}

