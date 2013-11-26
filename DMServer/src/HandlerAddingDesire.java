
public class HandlerAddingDesire extends Handler{
	public HandlerAddingDesire(DesireThread inThread) {
		super(inThread);
	}
	
	@Override
	public void handleString(String input){
			System.out.println("*** Adding a desire");
			String newDesireString = input.substring(1);
			try{
				synchronized(this){
					ownerThread.instrument.addDesire(ownerThread.currentUser, newDesireString);
				}
				confirmSuccess();
			}
			catch(Exception error){
				confirmFail();
				System.out.println(error.getMessage());
			}
	}
	
	@Override
	protected boolean myJob(char code) {
		return code == 'A';
	}

}
