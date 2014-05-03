package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.userDataPackage.MainData;


public class CommandToShowInfo extends CommandForDesireThread{
			
	public  CommandToShowInfo(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception{
		System.out.println("***Showing info executed");
		try{
				MainData infoData = receiver.getInfo();
				//receiver.sendTrue();
				receiver.socketOut.writeObject(infoData);
				receiver.socketOut.flush();
				System.out.println("*** THREAD " + receiver.getUserName() + " Info HAS BEEN SENT");
		}
		catch(Exception error){
			receiver.socketOut.writeObject(null);
			throw error;
		}
	}
	@Override
	public void unexecute() {
		System.out.println("Unexecuting showInfo. No unexecution available.");
	}

}
