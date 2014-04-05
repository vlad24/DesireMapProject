package desireMapApplicationPackage.desireThreadPackage;

import java.util.Deque;

import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;


public class ThreadStateStart extends ThreadState{

        public ThreadStateStart(DesireThread inThread){
                owner = inThread;
        }
        
        public void logIn(LoginData logData) throws Exception {
        		//Unpacking
        		String login = logData.login;
        		String password = logData.password;
        		//
        		try{
        			DesireInstrument.logInAtDB(login, password);
        			changeState(new ThreadStateLoggedIn(owner));
        		}
        		catch(Exception error){
        			System.out.println("Instrument hasn't logged in");
        			throw error;
        		}
        }
        
        public void register(RegistrationData regData) throws Exception{
        	try{
        		DesireInstrument.registerAtDB(regData);
        		changeState(new ThreadStateLoggedIn(owner));
        	}
        	catch(Exception error){
        		System.out.println("Instrument hasn't logged in");
        		throw error;
        	}
        }

        public void addDesire(DesireContent desireContent) throws Exception {
                throw new Exception("- Unable now\n Hint : log in or register");
        }

        public DesireSet getDesires(String login, String category) throws Exception{
                throw new Exception("- Unable now\n Hint : log in or register");
        }

        public MainData getInfo(String login) throws Exception {
                throw new Exception("- Unable now\n Hint : log in or register");
        }
        
        public void exit() {
        	System.out.println("+Already offline");
        }

		@Override
		public void delete(DeletePack content) throws Exception{
			throw new Exception("- Unable now\n Hint : log in or register");
			
		}

		@Override
		public SatisfySet getSatisfiers(SatisfyPack sPack) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public DesireSet getPersonalDesires(Cryteria cryteria) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public MainData getInfo() throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public void sendDeliveredMessagesToClient() throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");			
		}

		@Override
		public void takeMessages(Deque<Message> deque) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");			
		}

		@Override
		public void takeMessages(Message message) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public void postMessage(Message message) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

}