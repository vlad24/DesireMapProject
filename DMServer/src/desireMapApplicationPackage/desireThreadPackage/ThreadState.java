package desireMapApplicationPackage.desireThreadPackage;

import java.util.Deque;

import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
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
	public abstract void addDesire(DesireContent desireContent) throws Exception;
	public abstract void delete(DeletePack delPack) throws Exception;
	public abstract SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception;
	public abstract MainData getInfo() throws Exception;
	public abstract DesireSet getPersonalDesires(Cryteria cryteria) throws Exception;
	public abstract void exit();
	public abstract void postMessage(Message message) throws Exception;
	public abstract void sendDeliveredMessagesToClient() throws Exception;
	public abstract void takeMessages(Deque<Message> deque) throws Exception;
	public abstract void takeMessages(Message message) throws Exception;

}