
public class HandlerExiting extends Handler{
	public HandlerExiting(DesireThread inThread) {
		super(inThread);
	}
	
	@Override
	public void handleString(String input){
		try{
			System.out.println("***" + userThread.currentUser + " exiting");
				userThread.currentUser = "?";
				userThread.instrument.exit();
				userThread.interrupt();
				confirmSuccess();
		}
		catch(Exception error){
			confirmFail();
		}
	}
	
	@Override
	protected boolean myJob(String input) {
		return input.charAt(0) == 'E';
	}

}
