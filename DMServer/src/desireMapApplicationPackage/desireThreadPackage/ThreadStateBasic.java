package desireMapApplicationPackage.desireThreadPackage;

import java.sql.SQLException;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.messageSystemPackage.ChatKing;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.MainData;

public class ThreadStateBasic extends ThreadState{

	private boolean isBasicSatisfyPack(SatisfyPack sPack) {
		return (sPack.sDesireID == null);
	}

	public ThreadStateBasic(DesireThread inThread) {
		owner = inThread;
	}

	@Override
	public void register(RegistrationPack regPack) throws Exception {
		throw new Exception("!!! Unable now\n Hint : exit first, please");
	}

	@Override
	public void authorize(LoginPack logPack) throws Exception {
		throw new Exception("!!! Unable now\n Hint : exit first, please");
	}

	@Override
	public String addDesire(AddPack pack) throws Exception {
		return owner.instrument.addDesireAtDB(pack);
	}

	@Override
	public void delete(DeletePack delPack) throws Exception {
		owner.instrument.deleteAtDB(delPack);
	}

	@Override
	public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception {
		if (isBasicSatisfyPack(sPack)){
			Integer categoryCode = sPack.sCategoryCode;
			SatisfySet set = owner.instrument.getSatisfiersAtDBBasic(owner.getUserName() , categoryCode, sPack.tiles, null);
			changeState(new ThreadStateMapScanning(owner, sPack, categoryCode));
			return set;
		}
		else{
			Integer categoryCode = owner.instrument.getCategoryTableByID(sPack.sDesireID);
			if (categoryCode != null){
				SatisfySet set = owner.instrument.getSatisfiersAtDB(sPack.sDesireID, categoryCode, sPack.tiles, null);
				changeState(new ThreadStateMapScanning(owner, sPack, categoryCode));
				return set;
			}
			else{
				changeState(new ThreadStateMapScanning(owner, sPack, categoryCode));
				throw new Exception("Category is wrong");
			}
		}
	}


	@Override
	public DesireSet getPersonalDesires(int category) throws Exception {
		return owner.instrument.getPersonalDesiresAtDB(owner.getUserName(), category);
	}

	public MainData getInfo() throws Exception{
		return owner.instrument.getInfoAtDB(owner.getUserName());                
	}

	public void exit(){
		owner.instrument.cleanBaseOnExit(owner.getUserName(), owner.getDeviceID());
		changeState(new ThreadStateStart(this.owner));
	}


	public void postMessage(ClientMessage clientMessage) throws Exception {
		ChatKing.getInstance().postMessage(clientMessage);
	}

	@Override
	public SatisfySet updateSatisfiers(TilesPack tilesPack)	throws Exception {
		throw new Exception("!!! Unable now\n Hint : exit first, please");
	}

	@Override
	public void loadNewMessages() throws SQLException {
		owner.chater.getUndeliveredMessagesForThread(this.owner);
	}

	@Override
	public MessageSet getOldMessagesByCryteria(MessageDeliverPack pack) throws Exception {
		return owner.chater.getMessagesByCryteria(pack);
	}

	@Override
	public UserSet getUsersTalkedTo() throws Exception {
		return owner.chater.getUsersTalkedToAtChat(this.owner);
	}

	@Override
	public void likeDesire(LikePack pack) throws Exception {
		throw new Exception("!!! Unable now\n Scan the map!");
	}


}