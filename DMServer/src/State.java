import java.sql.*;

public abstract class State{
	protected DesireInstrument owner;
	public abstract void register(String login, String password, String name, String sex, String birthdate) throws Exception;
	public abstract void logIn(String login, String password) throws Exception;
	public abstract void addDesire(Desire desire) throws Exception;
	public abstract ResultSet getDesires(String login, String category) throws Exception;
	public abstract ResultSet getInfo(String login) throws Exception;
	public abstract ResultSet getSatisfiersToday(Desire desire, String radius) throws Exception;
	public abstract void exit();	
	protected void changeState(State newState){
		owner.stateObject = newState; 
	}
}