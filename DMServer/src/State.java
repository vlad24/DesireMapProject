import java.sql.*;

public abstract class State{
	protected DesireInstrument owner;
	public abstract void register(String login, String password, String name, String sex, String birthdate) throws Exception;
	public abstract void logIn(String login, String password) throws Exception;
	public abstract void addDesire(String login, String desireString, String tag, String latitude, String longitude) throws Exception;
	public abstract ResultSet getDesires(String login) throws Exception;
	public abstract ResultSet getInfo(String login) throws Exception;
	public abstract void exit();	
	protected void changeState(State newState){
		owner.stateObject = newState; 
	}
}