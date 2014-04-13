package desireMapApplicationPackage.desireThreadPackage;

import java.sql.SQLException;
import java.util.Deque;
import java.util.HashSet;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class ThreadStateMapScanning extends ThreadState{

	private HashSet<String> loadedTiles;
	private int lastDepth;
	private String desireID;
	private Integer categoryCode;
	
	public ThreadStateMapScanning(DesireThread newOwner, SatisfyPack sPack){
		owner = newOwner;
		loadedTiles = sPack.tiles;
		lastDepth = sPack.tileDepth; 
		desireID = sPack.sDesireID;
		try {
			categoryCode = DesireInstrument.getCategoryTableByID(desireID);
		} catch (SQLException error) {
			categoryCode = null;
			error.printStackTrace();
		}
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
	public String addDesire(AddPack addPack) throws Exception {
		changeState(new ThreadStateBasic(owner));
		return owner.addDesire(addPack);
	}

	@Override
	public void delete(DeletePack delPack) throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.delete(delPack);
	}

	@Override
	public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception {
		changeState(new ThreadStateBasic(owner));
		return owner.getSatisfiers(sPack);
	}
	
	public SatisfySet updateSatisfiers(TilesPack tilesPack)	throws Exception {
		int currentDepth = tilesPack.depth;
		if (currentDepth == lastDepth){
			System.out.println("!!! Depth has stayed the same. Modifying needed tiles");
			tilesPack.tiles.removeAll(loadedTiles);
			if (tilesPack.tiles.isEmpty()){
				System.out.println("+No new tiles needed. No satisfiers will be sent");
				return null;
			}
			else{
				SatisfySet satisfySet = DesireInstrument.getSatisfiersAtDB(desireID, categoryCode, tilesPack.tiles, null);
				//Adding new tiles at this level at already sent tiles
				loadedTiles.addAll(tilesPack.tiles);
				return satisfySet;
			}			
		}
		else if(currentDepth > lastDepth){
			System.out.println("!!! Depth has increased : " + currentDepth +  ". Refreshing");
			SatisfySet satisfySet = DesireInstrument.getSatisfiersAtDB(desireID, categoryCode, tilesPack.tiles,  null);
			lastDepth = currentDepth;
			System.out.println("!!! Current length has been updated");
			return satisfySet;
		}
		else{
			System.out.println("!!! Depth has decreased. DB will account loaded tiles");
			SatisfySet satisfySet = DesireInstrument.getSatisfiersAtDB(desireID, categoryCode, tilesPack.tiles, loadedTiles);
			lastDepth = currentDepth;
			return satisfySet;
		}
	}

	@Override
	public MainData getInfo() throws Exception {
		changeState(new ThreadStateBasic(owner));
		return owner.getInfo();
	}

	@Override
	public DesireSet getPersonalDesires(int category) throws Exception {
		changeState(new ThreadStateBasic(owner));
		return owner.getPersonalDesires(category);
	}

	@Override
	public void exit() {
		changeState(new ThreadStateStart(owner));
	}

	@Override
	public void postMessage(Message message) throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.postMessage(message);
		
	}

	@Override
	public void sendDeliveredMessagesToClient() throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.sendDeliveredMessagesToClient();
	}

	@Override
	public void takeMessages(Deque<Message> deque) throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.takeMessages(deque);
	}

	@Override
	public void takeMessages(Message message) throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.takeMessages(message);
	}
	
}
