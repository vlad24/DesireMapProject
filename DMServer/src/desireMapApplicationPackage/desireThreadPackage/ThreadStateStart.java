package desireMapApplicationPackage.desireThreadPackage;


import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.MainData;


public class ThreadStateStart extends ThreadState{

        public ThreadStateStart(DesireThread inThread){
                owner = inThread;
        }
        
        public void authorize(LoginPack logPack) throws Exception {
        		//Unpacking
        		String login = logPack.loginData.login;
        		String password = logPack.loginData.password;
        		//
        		try{
        			owner.instrument.logInAtDB(login, password);
        			owner.instrument.handleAndroidData(login, logPack.androidData);
        			changeState(new ThreadStateBasic(owner));
        		}
        		catch(Exception error){
        			System.out.println("-Instrument hasn't logged in");
        			throw error;
        		}
        }
        
        public void register(RegistrationPack regPack) throws Exception{
        	try{
        		owner.instrument.registerAtDB(regPack.registrationData);
        		System.out.println("Registered user");
        		owner.instrument.handleAndroidData(regPack.registrationData.login, regPack.androidData);
        		System.out.println("Registered android");
        		changeState(new ThreadStateBasic(owner));
        	}
        	catch(Exception error){
        		System.out.println("-Instrument hasn't registered");
        		throw error;
        	}
        }

        public String addDesire(AddPack pack) throws Exception {
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
		
		public SatisfySet updateSatisfiers(TilesPack tilesPack)	throws Exception {
			throw new Exception("!!! Unable now ");
		}

		@Override
		public DesireSet getPersonalDesires(int category) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public MainData getInfo() throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}


		@Override
		public void postMessage(ClientMessage clientMessage) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public void loadNewMessages() throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");
		}

		@Override
		public MessageSet getOldMessagesByCryteria(MessageDeliverPack pack) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");			
		}

		@Override
		public UserSet getUsersTalkedTo() throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");	
		}

		@Override
		public void likeDesire(LikePack pack) throws Exception {
			throw new Exception("- Unable now\n Hint : log in or register");			
		}

}