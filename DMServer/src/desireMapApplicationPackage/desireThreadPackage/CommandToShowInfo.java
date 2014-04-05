package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.userDataPackage.MainData;





public class CommandToShowInfo extends CommandForDesireThread{
			
	public  CommandToShowInfo(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception{
		System.out.println("***Showing info");
		try{
			synchronized(receiver){
				MainData infoData = receiver.getInfo();
				receiver.confirmSuccess();
				receiver.socketOut.writeObject(infoData);
				System.out.println("*** THREAD " + receiver.getUserName() + " Info HAS BEEN SENT");
			}
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}

}
