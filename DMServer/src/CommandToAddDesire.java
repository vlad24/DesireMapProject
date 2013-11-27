
public class CommandToAddDesire extends CommandForDesireThread{
	
	private String desireString;
	private String tag;
	private String latitude;
	private String longitude;
	//--
	public CommandToAddDesire(DesireThread newReceiver, String newDesireString, String newTag, String newLatitude, String newLongitude){
		receiver = newReceiver;
		desireString = newDesireString;
		tag = newTag;
		latitude = newLatitude;
		longitude = newLongitude;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("*** Adding a desire");
		try{
			synchronized(receiver){
				System.out.println("here in Command");
				receiver.instrument.addDesire(receiver.currentUser, desireString, tag, latitude, longitude);
			}
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
