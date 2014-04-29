package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.desireContentPackage.DesireContent;
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
		System.out.println("***Showing personal desires executed");
		try{
				DesireSet dSet = receiver.getPersonalDesires(category);
				System.out.println("+dSet is formed : ");
				for (DesireContent content : dSet.dArray){
					System.out.println(content.desireID + " " + content.login + " " + content.description);
				}
				//receiver.sendTrue();
				receiver.socketOut.writeObject(dSet);
				receiver.socketOut.flush();
				receiver.socketOut.reset();
				System.out.println("*** THREAD " + receiver.getUserName() + " SENT desireSet");
		}
		catch(Exception error){
			//receiver.sendFalse();
			receiver.socketOut.writeObject(null);
			throw error;
		}
	}
	
}
