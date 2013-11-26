import java.util.Iterator;
import java.util.LinkedList;

public class DesireTube {
	protected DesireThread userThread;
	private LinkedList<Handler> tube;
	
	private Handler chooseHandler(char firstSymbol){
		Iterator<Handler> iterator = tube.iterator();
		Handler handler = iterator.next();
		while(handler != null){
			if (handler.myJob(firstSymbol)){
				return handler;
			}
			else{
				handler = iterator.next();
			}
		}
		return null;
	}
	
	public DesireTube(DesireThread inThread){
		userThread = inThread;
		tube = new LinkedList<Handler>();
		tube.add(new HandlerExiting(inThread));
		tube.add(new HandlerLogIn(inThread));
		tube.add(new HandlerRegister(inThread));
		tube.add(new HandlerAddingDesire(inThread));
		tube.add(new HandlerShowerDesires(inThread));
		tube.add(new HandlerShowerInfo(inThread));
	}
	public void processString(String input){
		Handler forthHandler = chooseHandler(input.charAt(0));
		if (forthHandler != null){
			forthHandler.handleString(input);
		}
		else{
			tube.getFirst().confirmFail();
			System.out.println("No handler found to process the query. Query-sender has been informed about the fail");
		}
	}
}