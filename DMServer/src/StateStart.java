import java.sql.*;

public class StateStart extends State{

        public StateStart(DesireInstrument instrument){
                owner = instrument;
        }
        
        public void logIn(String login, String password) throws Exception {
                Statement selector = DesireInstrument.dataBase.getConnection().createStatement();
                ResultSet setAfterSelection = selector.executeQuery("SELECT * FROM USERS WHERE LOGIN = '" + login + "' AND PASSWORD = '" + password + "'");
                if (setAfterSelection.next()){
                        changeState(new StateLoggedIn(owner));
                        selector.close();
                }
                else{
                		selector.close();
                        throw new Exception("Nothing found in database\n");
                }
        }
        
        public void register(String login, String password, String name, String sex, String birthdate) throws Exception{
        	Statement inserter = DesireInstrument.dataBase.getConnection().createStatement();
        	try{
        		inserter.execute("INSERT INTO USERS(LOGIN,PASSWORD) VALUES('" + login + "', '" + password + "')");                
        		inserter.execute("INSERT INTO INFO (LOGIN,NAME,SEX,BIRTH) VALUES('" + login + "', '" + name + "', '" + sex + "', '" + birthdate + "')");
        		changeState(new StateLoggedIn(owner));
        	}
        	catch (Exception error){
        		throw error;
        	}  
        	finally{
        		inserter.close();
        	}
        }

        public void addDesire(Desire desire) throws Exception {
                throw new Exception("Unable now\n Hint : log in or register");
        }

        public ResultSet getDesires(String login, String category) throws Exception{
                throw new Exception("Unable now\n Hint : log in or register");
        }

        public ResultSet getInfo(String login) throws Exception {
                throw new Exception("Unable now\n Hint : log in or register");
        }
        
        public void exit() {
                changeState(new StateStart(owner));
        }

		@Override
		public ResultSet getSatisfiersToday(Desire desire, String radius)
				throws Exception {
			throw new Exception("Unable now\n Hint : log in or register");
		}

        
}