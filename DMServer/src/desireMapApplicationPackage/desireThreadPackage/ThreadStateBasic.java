package desireMapApplicationPackage.desireThreadPackage;


import java.util.Deque;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.messageSystemPackage.ChatKing;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class ThreadStateBasic extends ThreadState{
	
        public ThreadStateBasic(DesireThread inThread) {
                owner = inThread;
        }

		@Override
		public void register(RegistrationData regData) throws Exception {
			 throw new Exception("!!! Unable now\n Hint : exit first, please");
		}

		@Override
		public void logIn(LoginData logData) throws Exception {
			 throw new Exception("!!! Unable now\n Hint : exit first, please");
		}
		
		@Override
		public String addDesire(AddPack pack) throws Exception {
			return DesireInstrument.addDesireAtDB(pack);
		}

		@Override
		public void delete(DeletePack delPack) throws Exception {
				DesireInstrument.deleteAtDB(delPack);
		}
		
		@Override
		public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception {
			int categoryCode = DesireInstrument.getCategoryTableByID(sPack.sDesireID);
			SatisfySet set = DesireInstrument.getSatisfiersAtDB(sPack.sDesireID, categoryCode, sPack.tiles, null); 
			changeState(new ThreadStateMapScanning(owner, sPack));
			return set;
		}

		@Override
		public DesireSet getPersonalDesires(int category) throws Exception {
			return DesireInstrument.getPersonalDesiresAtDB(owner.getUserName(), category);
		}
		
        public MainData getInfo() throws Exception{
        	return DesireInstrument.getInfoAtDB(owner.getUserName());                
        }
  
        public void exit() {
            changeState(new ThreadStateStart(this.owner));
    }

		@Override
		public void sendDeliveredMessagesToClient() throws Exception{
			for (Message m : owner.localMessages){
				System.out.println(m.sender + " " + m.text);
			}
			MessageSet setOfMessages = new MessageSet(owner.localMessages);
			System.out.println("+Sending set of messages to a client");
			owner.socketOut.writeObject(setOfMessages);
			owner.socketOut.flush();
			System.out.println("!Sent");
		}

		@Override
		public void takeMessages(Deque<Message> deque) throws Exception{
			if (!(deque == null)){
				owner.localMessages.addAll(deque);
			}
			else{
				System.out.println("+Nothing is inserted in local message history");
			}
		}

		@Override
		public void takeMessages(Message message) throws Exception{
			if (!(message == null)){
				owner.localMessages.add(message);
			}
			else{
				System.out.println("+Empty message to take in history");
			}
		}
		
		public void postMessage(Message message) throws Exception {
			ChatKing.getInstance().postMessage(message, true);
		}

		@Override
		public SatisfySet updateSatisfiers(TilesPack tilesPack)	throws Exception {
			throw new Exception("!!! Unable now\n Hint : exit first, please");
		}


}