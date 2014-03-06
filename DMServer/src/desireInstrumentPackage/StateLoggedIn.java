package desireInstrumentPackage;
import java.sql.*;

import queryObjectPackage.Desire;


public class StateLoggedIn extends State{
        
        public StateLoggedIn(DesireInstrument instrument) {
                owner = instrument;
        }
        
        public void register(String login, String password, String name, String sex, String birthdate) throws Exception {
                throw new Exception("!!! Unable now\n Hint : exit first, please");
        }
        
        public void logIn(String login, String password) throws Exception {
                throw new Exception("!!! Unable now\n Hint : you have been already logged in");
        }

        public void addDesire(Desire desire) throws Exception {
        		String login = desire.getMaster();
        		String category = desire.getCategory();
        		String desireString = desire.getDesireString();
        		String tag = desire.getTag();
        		String latitude = desire.getLatitude();
        		String longitude = desire.getLongitude();
                Statement inserter = DesireInstrument.getAccessToDesireBase().createStatement();
                inserter.execute("INSERT INTO DESIRES" + category + " VALUES('" + login + "','" + desireString + "','" + tag + "','" + latitude + "','" + longitude + "', datetime('now'))");
                inserter.close();
        }
        
		@Override
		public void clearUsersCategory(String login, String category) throws Exception {
			 Statement deleter = DesireInstrument.getAccessToDesireBase().createStatement();
             deleter.executeUpdate("DELETE FROM DESIRES" + category + " WHERE LOGIN = '" + login + "'");
             deleter.close(); 
		}
        
        public ResultSet getDesires(String login, String category) throws Exception {
                Statement selector = DesireInstrument.getAccessToDesireBase().createStatement();
                System.out.println("SELECT DESIRE FROM DESIRES" + category + " WHERE LOGIN = '" + login + "'");
                ResultSet desires = selector.executeQuery("SELECT DESIRE FROM DESIRES" + category + " WHERE LOGIN = '" + login + "'");
                //selector.close();? 
                return desires;
        }
        
        public ResultSet getInfo(String login) throws Exception{
                Statement selector = DesireInstrument.getAccessToDesireBase().createStatement();
                ResultSet info = selector.executeQuery("SELECT NAME, SEX, BIRTH FROM INFO WHERE LOGIN = '" + login + "'");
                //selector.close(); ?
                return info;
        }
        
        public ResultSet getSatisfiersToday(Desire desire, String radius) throws Exception{ // draft
        	String neededFields = " login, desire, latitude, longitude ";
        	String deltaLatitude = " (LATITUDE - " + desire.getLatitude() + ") ";
        	String deltaLongitude = " (LONGITUDE - " + desire.getLongitude() + ") ";
        	String deltaLatitudeSquared = deltaLatitude + "*" + deltaLatitude;
        	String deltaLongitudeSquared = deltaLongitude + "*" + deltaLongitude;
        	String actualRadiusSquared = deltaLatitudeSquared + "+" + deltaLongitudeSquared;
        	String tagMask = "'%" + desire.getTag() + "%'";
        	String givenRadiusSquared = " (" + radius + " * " + radius + ") ";
        	String desireWasPostedToday = " date(TIME) = date('now')";
        	String tableSuffix = desire.getCategory();
        	String client = desire.getMaster();
        	String satisfierIsNotClient = "login != '" + client + "' ";
        	
        	String satisfyQuery = "SELECT" + neededFields + "FROM DESIRES" + tableSuffix +
        			" WHERE TAG LIKE " + tagMask + " AND " + 
        			actualRadiusSquared + " < " + givenRadiusSquared + " AND " + satisfierIsNotClient +
        			"AND" + desireWasPostedToday;
        	
        	System.out.println(satisfyQuery);
        	
        	Statement selector = DesireInstrument.getAccessToDesireBase().createStatement();
        	ResultSet satisfiersInfo = selector.executeQuery(satisfyQuery);
			return satisfiersInfo;
        }
        
        public void exit() {
                changeState(new StateStart(owner));
        }

}