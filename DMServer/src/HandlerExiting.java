
public class HandlerExiting extends Handler{
	public HandlerExiting(DesireThread inThread) {
		super(inThread);
	}
	

	@Override
	public void handleString(String input){
		try{
			System.out.println("***" + ownerThread.currentUser + " exiting");
				ownerThread.currentUser = "?";
				ownerThread.instrument.exit();
				ownerThread.interrupt();
				confirmSuccess();
		}
		catch(Exception error){
			confirmFail();
		}
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'E';
	}

}
