import java.io.IOException;


public abstract class Handler {
	public Handler(DesireThread surroundingThread){
		ownerThread = surroundingThread;
		divider = new StringDivider();
	}
	protected DesireThread ownerThread;
	protected StringDivider divider;
	protected abstract boolean myJob(char code);
	public abstract void handleString(String input);
	
	protected void confirmSuccess(){
		try{
			ownerThread.socketOut.writeBoolean(true);
			ownerThread.socketOut.flush();
			System.out.println("*** THREAD SENDED OBJECT TRUE");
		}
		catch(Exception error){
			System.out.println("Problem with socket(confirm true)");
		}
	}
	protected void confirmFail(){
		try{
			ownerThread.socketOut.writeBoolean(false);
			ownerThread.socketOut.flush();
			System.out.println("*** THREAD SENDED OBJECT FALSE");
		}
		catch(IOException error){
			System.out.println("Problem with socket(confirm fail)");
		}
	}
}
