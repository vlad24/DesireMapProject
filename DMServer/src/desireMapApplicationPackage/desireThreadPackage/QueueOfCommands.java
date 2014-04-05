package desireMapApplicationPackage.desireThreadPackage;
import java.util.LinkedList;
import java.util.Queue;

public class QueueOfCommands {
	private Queue<CommandForDesireThread> queue;
	////
	public QueueOfCommands(){
		queue = new LinkedList<CommandForDesireThread>();
	}
	
	public void addCommand(CommandForDesireThread command){
		queue.add(command);
	}
	
	public void executeAllPolling() throws Exception{
		for (CommandForDesireThread command : queue){
			command.execute();
		}
	}
	
	public void executeFirstPolling() throws Exception{
		queue.poll().execute();
	}
}
