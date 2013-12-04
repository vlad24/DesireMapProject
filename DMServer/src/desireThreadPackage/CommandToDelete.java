package desireThreadPackage;

public class CommandToDelete extends CommandForDesireThread{
	private String category;
	public CommandToDelete(DesireThread nReceiver, String nCategory){
		receiver = nReceiver;
		category = nCategory;
	}
	public void execute() throws Exception{
		try{
			System.out.println("*** Deleting desires of " + receiver.getCurrentUser() +  " from " + category);
			receiver.instrument.clearUsersCategory(receiver.getCurrentUser(), category);
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
		
	}

}
