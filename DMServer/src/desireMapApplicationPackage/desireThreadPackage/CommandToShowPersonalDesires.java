package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;



public class CommandToShowPersonalDesires extends CommandForDesireThread{
	
	private DesireThread receiver;
	private Cryteria cryteria;
	
	public CommandToShowPersonalDesires(DesireThread newReceiver, Cryteria newCryteria){
		receiver = newReceiver;
		cryteria = newCryteria;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("***Showing personal desires");
		try{
			synchronized(receiver){
				DesireSet dSet = receiver.getPersonalDesires(cryteria);
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
