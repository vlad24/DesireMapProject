import java.util.Iterator;
import java.util.LinkedList;

public class DesireTube {
	private LinkedList<Handler> tube;
	public DesireTube(DesireThread inThread){
		tube = new LinkedList<Handler>();
		tube.add(new HandlerExiting(inThread));
		tube.add(new HandlerLogIn(inThread));
		tube.add(new HandlerRegister(inThread));
		tube.add(new HandlerAddingDesire(inThread));
		tube.add(new HandlerShowerDesires(inThread));
		tube.add(new HandlerShowerInfo(inThread));
	}
	public void processString(String input){
		Iterator<Handler> iterator = tube.iterator();
		Handler handler = iterator.next();
		while(handler != null){
			if (handler.myJob(input)){
				System.out.println(handler.getClass().toString() + " is activated");
				handler.handleString(input);
				return;
			}
			else{
				handler = iterator.next();
			}
		}
	}
}