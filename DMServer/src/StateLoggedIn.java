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

        public void addDesire(Desire desire) throws Exception {
        		String login = desire.getMaster();
        		String category = desire.getCategory();
        		String desireString = desire.getDesireString();
        		String tag = desire.getTag();
        		String latitude = desire.getLatitude();
        		String longitude = desire.getLongitude();
                Statement inserter = DesireInstrument.dataBase.getConnection().createStatement();
                inserter.execute("INSERT INTO DESIRES" + category + " VALUES('" + login + "','" + desireString + "','" + tag + "','" + latitude + "','" + longitude + "', datetime('now', 'localtime'))");
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
        
        public ResultSet getSatisfiersToday(Desire desire, String radius) throws Exception{ // draft
        	String neededFields = " login, latitude, longitude ";
        	String deltaLatitude = " (LATITUDE - " + desire.getLatitude() + ") ";
        	String deltaLongitude = " (LONGITUDE - " + desire.getLongitude() + ") ";
        	String deltaLatitudeSquared = deltaLatitude + "*" + deltaLatitude;
        	String deltaLongitudeSquared = deltaLongitude + "*" + deltaLongitude;
        	String actualRadiusSquared = deltaLatitudeSquared + "+" + deltaLongitudeSquared;
        	String tagMask = "'%" + desire.getTag() + "%'";
        	String givenRadiusSquared = " (" + radius + " * " + radius + ") ";
        	String desireWasPostedToday = " julianday(time) = julianday('now')";
        	String tableSuffix = desire.getCategory();
        	
        	String satisfyQuery = "SELECT" + neededFields + "FROM DESIRES" + tableSuffix +
        			" WHERE TAG LIKE " + tagMask + " AND " + 
        			actualRadiusSquared + " < " + givenRadiusSquared + " AND " +  actualRadiusSquared + " > 0 ";
        			//"AND" + desireWasPostedToday;
        	
        	System.out.println(satisfyQuery);
        	
        	Statement selector = DesireInstrument.dataBase.getConnection().createStatement();
        	ResultSet satisfiersInfo = selector.executeQuery(satisfyQuery);
			return satisfiersInfo;
        }
        
        public void exit() {
                changeState(new StateStart(owner));
        }

}