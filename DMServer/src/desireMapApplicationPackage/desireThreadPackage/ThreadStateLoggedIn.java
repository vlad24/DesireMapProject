package desireMapApplicationPackage.desireThreadPackage;


import java.util.Deque;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.messageSystemPackage.ChatKing;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.MessageSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class ThreadStateLoggedIn extends ThreadState{
	
        public ThreadStateLoggedIn(DesireThread inThread) {
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
		public int addDesire(AddPack pack) throws Exception {
			return DesireInstrument.addDesireAtDB(pack);
		}

		@Override
		public void delete(DeletePack delPack) throws Exception {
				DesireInstrument.deleteAtDB(delPack);
		}
		
		@Override
		public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception {
			SatisfySet set = DesireInstrument.getSatisfiersAtDB(sPack, null); 
			changeState(new ThreadStateMapScanning(owner, sPack.tiles));
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
			ChatKing.getInstance().postMessage(message);
		}


}