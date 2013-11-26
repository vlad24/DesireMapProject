import java.sql.*;

public class StateLoggedIn extends State{
        
        public StateLoggedIn(DesireInstrument instrument) {
                owner = instrument;
        }
        
        public void register(String login, String password, String name, String sex, String birthdate) throws Exception {
                throw new Exception("Unable now\n Hint : exit first, please");
        }
        
        public void logIn(String login, String password) throws Exception {
                throw new Exception("Unable now\n  Hint : you have been already logged in");
        }

        public void addDesire(String login, String desireString) throws Exception {
                Statement inserter = DesireInstrument.dataBase.getConnection().createStatement();
                inserter.execute("INSERT INTO DESIRES VALUES('" + login + "','" + desireString + "')");
                inserter.close();
        }
        
        public ResultSet getDesires(String login) throws Exception {
                Statement selector = DesireInstrument.dataBase.getConnection().createStatement();
                ResultSet desires = selector.executeQuery("SELECT DESIRE FROM DESIRES WHERE LOGIN = '" + login + "'");
                //selector.close();? 
                return desires;
        }
        
        public ResultSet getInfo(String login) throws Exception{
                Statement selector = DesireInstrument.dataBase.getConnection().createStatement();
                ResultSet info = selector.executeQuery("SELECT NAME, SEX, BIRTH FROM INFO WHERE LOGIN = '" + login + "'");
                //selector.close(); ?
                return info;
        }
        
        public void exit() {
                changeState(new StateStart(owner));
        }

}