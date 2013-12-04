package desireThreadPackage;








public class CommandToLogIn extends CommandForDesireThread{
	
	private String login;
	private String password;
	//--
	public CommandToLogIn(DesireThread newReceiver, String newLogin, String newPassword) {
		receiver = newReceiver;
		login = newLogin;
		password = newPassword;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** Logging IN");
			synchronized(receiver){
				receiver.instrument.logIn(login, password);
			}
			receiver.confirmSuccess();
			receiver.setCurrentUser(login);
			System.out.println("*** " + login + " ONLINE");
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
