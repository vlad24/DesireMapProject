

public abstract class Handler {
	
	protected DesireThread ownerThread;
	protected StringDivider divider;
	//--
	public Handler(DesireThread surroundingThread){
		ownerThread = surroundingThread;
		divider = new StringDivider();
	}
	protected abstract boolean myJob(char code);
	public abstract CommandForDesireThread handleString(String input);
	
}
