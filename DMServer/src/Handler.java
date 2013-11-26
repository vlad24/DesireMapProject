import java.io.IOException;


public abstract class Handler {
	public Handler(DesireThread surroundingThread){
		userThread = surroundingThread;
		divider = new StringDivider();
	}
	protected DesireThread userThread;
	protected StringDivider divider;
	protected abstract boolean myJob(String input);
	public abstract void handleString(String input);
	
	protected void confirmSuccess(){
		try{
			userThread.socketOut.writeBoolean(true);
			userThread.socketOut.flush();
			System.out.println("*** THREAD SENDED OBJECT TRUE");
		}
		catch(Exception error){
			System.out.println("Problem with socket(confirm true)");
		}
	}
	protected void confirmFail(){
		try{
			userThread.socketOut.writeBoolean(false);
			userThread.socketOut.flush();
			System.out.println("*** THREAD SENDED OBJECT FALSE");
		}
		catch(IOException error){
			System.out.println("Problem with socket(confirm fail)");
		}
	}
}
