
public class CommandBinary extends CommandForDesireThread{
	private CommandForDesireThread firstCommand;
	private CommandForDesireThread secondCommand;
	//--
	CommandBinary(CommandForDesireThread first, CommandForDesireThread second){
		firstCommand = first;
		secondCommand = second;
	}
	
	@Override
	public void execute() throws Exception {
		try{
		firstCommand.execute();
		secondCommand.execute();
		}
		catch(Exception error){
			System.out.println("Some error occured while executed composite command");
			throw error;
		}
	}
	
}
