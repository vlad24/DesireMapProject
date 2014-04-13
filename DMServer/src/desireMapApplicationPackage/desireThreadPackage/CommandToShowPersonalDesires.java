package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.outputSetPackage.DesireSet;



public class CommandToShowPersonalDesires extends CommandForDesireThread{
	
	private DesireThread receiver;
	private int category;
	
	public CommandToShowPersonalDesires(DesireThread newReceiver, int newCategory){
		receiver = newReceiver;
		category = newCategory;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("***Showing personal desires");
		try{
			synchronized(this){
				DesireSet dSet = receiver.getPersonalDesires(category);
				System.out.println("+dSet is formed");
				receiver.confirmSuccess();
				receiver.socketOut.writeObject(dSet);
				System.out.println("*** THREAD " + receiver.getUserName() + " SENT desireSet");
			}
		}
		catch(Exception error){
			System.out.println("Exception thrown at showing");
			receiver.confirmFail();
			throw error;
		}
	}
	
}
