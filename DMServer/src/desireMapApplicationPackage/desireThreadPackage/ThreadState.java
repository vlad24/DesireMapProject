package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.MainData;


public abstract class ThreadState{
	protected DesireThread owner;
	////
	protected void changeState(ThreadState newState){
		owner.stateObject = newState; 
		System.out.println("+State has changed");
	}
	public abstract void register(RegistrationPack regData) throws Exception;
	public abstract void authorize(LoginPack logData) throws Exception;
	public abstract String addDesire(AddPack addPack) throws Exception;
	public abstract void delete(DeletePack delPack) throws Exception;
	public abstract SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception;
	public abstract MainData getInfo() throws Exception;
	public abstract DesireSet getPersonalDesires(int category) throws Exception;
	public abstract void exit();
	public abstract void postMessage(ClientMessage clientMessage) throws Exception;
	public abstract SatisfySet updateSatisfiers(TilesPack tilesPack) throws Exception;
	public abstract void loadNewMessages() throws Exception;
	public abstract MessageSet getOldMessagesByCryteria(MessageDeliverPack deliverPack) throws Exception;
	public abstract UserSet getUsersTalkedTo() throws Exception;
		
}