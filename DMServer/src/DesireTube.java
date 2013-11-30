import java.util.Iterator;
import java.util.LinkedList;

public class DesireTube {

	protected DesireThread userThread;
	private LinkedList<Handler> tube;
	//--
	public DesireTube(DesireThread inThread){
		userThread = inThread;
		tube = new LinkedList<Handler>();
		tube.add(new HandlerExiting(inThread));
		tube.add(new HandlerLogIn(inThread));
		tube.add(new HandlerRegister(inThread));
		tube.add(new HandlerAddingDesire(inThread));
		tube.add(new HandlerShowerDesires(inThread));
		tube.add(new HandlerShowerInfo(inThread));
		tube.add(new HandlerAddSatisfy(inThread));
	}
	
	private Handler chooseHandler(char firstSymbol){
		Iterator<Handler> iterator = tube.iterator();
		Handler handler = iterator.next();
		while(handler != null){
			if (handler.myJob(firstSymbol)){
				return handler;
			}
			else{
				System.out.println("I didnt match :" + handler.getClass().toString());
				handler = iterator.next();
			}
		}
		return null;
	}

	public CommandForDesireThread processString(String input){
		//Here we suppose that our string is not null (it has been checked beforehand)
		Handler forthHandler = chooseHandler(input.charAt(0));
		if (forthHandler != null){
			System.out.println(forthHandler.toString());
			return (forthHandler.handleString(input));
		}
		else{
			System.out.println("No handler found to process the query. Time to exit");
			return (new CommandToExit(userThread));
		}
	}
}