package desireMapApplicationPackage.desireThreadPackage;
import java.util.ArrayList;

public class CommandsList {
	private ArrayList<CommandForDesireThread> queue;
	////
	public CommandsList(){
		queue = new ArrayList<CommandForDesireThread>();
	}
	
	public void addCommand(CommandForDesireThread command){
		queue.add(command);
	}
	
	public void executeAll() throws Exception{
		for (CommandForDesireThread command : queue){
			command.execute();
		}
	}
	
	public void executeAllSafely() throws Exception{
		int successesAmount = 0;
		try{
			for (CommandForDesireThread command : queue){
				command.execute();
				successesAmount++;
			}
		}
		catch (Exception error){
			System.out.println("Unexecuting all prevoius successfully executed commands...");
			for (int i = successesAmount - 1; i >= 0; i--){
				queue.get(i).unexecute();
			}
			throw error;			
		}
	}
	
}
