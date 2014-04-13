package desireMapApplicationPackage.desireThreadPackage;

import java.util.Deque;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;


public abstract class ThreadState{
	protected DesireThread owner;
	////
	protected void changeState(ThreadState newState){
		owner.stateObject = newState; 
		System.out.println("+State has changed");
	}
	public abstract void register(RegistrationData regData) throws Exception;
	public abstract void logIn(LoginData logData) throws Exception;
	public abstract String addDesire(AddPack addPack) throws Exception;
	public abstract void delete(DeletePack delPack) throws Exception;
	public abstract SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception;
	public abstract MainData getInfo() throws Exception;
	public abstract DesireSet getPersonalDesires(int category) throws Exception;
	public abstract void exit();
	public abstract void postMessage(Message message) throws Exception;
	public abstract void sendDeliveredMessagesToClient() throws Exception;
	public abstract void takeMessages(Deque<Message> deque) throws Exception;
	public abstract void takeMessages(Message message) throws Exception;
	public abstract SatisfySet updateSatisfiers(TilesPack tilesPack) throws Exception;

}