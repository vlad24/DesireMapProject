package desireMapApplicationPackage.desireThreadPackage;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class ThreadStateMapScanning extends ThreadState{

	private HashSet<String> loadedTiles;
	private int lastLength;
	
	public ThreadStateMapScanning(DesireThread newOwner, HashSet<String> justLoadedTiles){
		owner = newOwner;
		loadedTiles = justLoadedTiles;
		Iterator<String> iterator = justLoadedTiles.iterator();
		lastLength = iterator.next().length();
	}
	
	@Override
	public void register(RegistrationData regData) throws Exception {
		throw new Exception("-Unable now");
	}

	@Override
	public void logIn(LoginData logData) throws Exception {
		throw new Exception("-Unable now");		
	}

	@Override
	public int addDesire(AddPack addPack) throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		return owner.addDesire(addPack);
	}

	@Override
	public void delete(DeletePack delPack) throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		owner.delete(delPack);
	}

	@Override
	public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception {
		Iterator<String> iterator = sPack.tiles.iterator();
		int currentLength = iterator.next().length();
		if (currentLength == lastLength){
			System.out.println("!!! Depth has stayed the same. Modifying needed tiles");
			sPack.tiles.removeAll(loadedTiles);
			if (sPack.tiles.isEmpty()){
				System.out.println("+No new tiles needed. No satisfiers will be sent");
				return null;
			}
			else{
				SatisfySet satisfySet = DesireInstrument.getSatisfiersAtDB(sPack, null);
				//Adding new tiles at this level at already sent tiles
				loadedTiles.addAll(sPack.tiles);
				return satisfySet;
			}			
		}
		else if(currentLength > lastLength){
			System.out.println("!!! Depth has increased : " + currentLength +  ". Refreshing");
			SatisfySet satisfySet = DesireInstrument.getSatisfiersAtDB(sPack, null);
			lastLength = currentLength;
			System.out.println("!!! Current length has been updated");
			return satisfySet;
		}
		else{
			System.out.println("!!! Depth has decreased. DB will account loaded tiles");
			SatisfySet satisfySet = DesireInstrument.getSatisfiersAtDB(sPack, loadedTiles);
			lastLength = currentLength;
			return satisfySet;
		}
	}

	@Override
	public MainData getInfo() throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		return owner.getInfo();
	}

	@Override
	public DesireSet getPersonalDesires(int category) throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		return owner.getPersonalDesires(category);
	}

	@Override
	public void exit() {
		changeState(new ThreadStateStart(owner));
	}

	@Override
	public void postMessage(Message message) throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		owner.postMessage(message);
		
	}

	@Override
	public void sendDeliveredMessagesToClient() throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		owner.sendDeliveredMessagesToClient();
	}

	@Override
	public void takeMessages(Deque<Message> deque) throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		owner.takeMessages(deque);
	}

	@Override
	public void takeMessages(Message message) throws Exception {
		changeState(new ThreadStateLoggedIn(owner));
		owner.takeMessages(message);
	}
	
}
