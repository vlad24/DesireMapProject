package desireMapApplicationPackage.desireThreadPackage;

import java.sql.SQLException;
import java.util.HashSet;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;
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
			categoryCode = owner.instrument.getCategoryTableByID(desireID);
		} catch (SQLException error) {
			categoryCode = null;
			error.printStackTrace();
		}
	}
	
	@Override
	public void register(RegistrationPack regPack) throws Exception {
		throw new Exception("-Unable now");
	}

	@Override
	public void authorize(LoginPack logPack) throws Exception {
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
				SatisfySet satisfySet = owner.instrument.getSatisfiersAtDB(desireID, categoryCode, tilesPack.tiles, null);
				//Adding new tiles at this level at already sent tiles
				loadedTiles.addAll(tilesPack.tiles);
				return satisfySet;
			}			
		}
		else if(currentDepth > lastDepth){
			System.out.println("!!! Depth has increased : " + currentDepth +  ". Refreshing");
			SatisfySet satisfySet = owner.instrument.getSatisfiersAtDB(desireID, categoryCode, tilesPack.tiles,  null);
			loadedTiles = tilesPack.tiles;
			System.out.println("!!! Loaded tiles has been updated");
			lastDepth = currentDepth;
			System.out.println("!!! Last length has been updated");
			return satisfySet;
		}
		else{
			System.out.println("!!! Depth has decreased. DB will account loaded tiles");
			SatisfySet satisfySet = owner.instrument.getSatisfiersAtDB(desireID, categoryCode, tilesPack.tiles, loadedTiles);
			loadedTiles = tilesPack.tiles;
			System.out.println("!!! Loaded tiles has been updated");
			lastDepth = currentDepth;
			System.out.println("!!! Last length has been updated");
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
	public void postMessage(ClientMessage clientMessage) throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.postMessage(clientMessage);
		
	}

	@Override
	public void loadNewMessages() throws Exception {
		changeState(new ThreadStateBasic(owner));
		owner.loadNewMessages();
	}

	@Override
	public MessageSet getOldMessagesByCryteria(MessageDeliverPack pack) throws Exception {
		changeState(new ThreadStateBasic(owner));
		return owner.getOldMessagesByCryteria(pack);
	}

	@Override
	public UserSet getUsersTalkedTo() throws Exception {
		changeState(new ThreadStateBasic(owner));
		return owner.getUsersTalkedTo();
	}

	@Override
	public void likeDesire(LikePack pack){
		owner.instrument.likeDesireAtDB(pack.desireID, pack.isLiked);
	}
	
}
