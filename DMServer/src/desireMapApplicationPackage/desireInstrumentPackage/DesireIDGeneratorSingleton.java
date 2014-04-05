package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DesireIDGeneratorSingleton {
	
	private static int id = 0;
	////
	public static synchronized DesireIDGeneratorSingleton getInstance() throws Exception{
		if (generator == null){
			generator = new DesireIDGeneratorSingleton();
		}
		return generator;
	}
	
	private static DesireIDGeneratorSingleton generator = null;
	
	public synchronized int getNextID(){
		System.out.println("+ id : " + (id + 1));
		return ++id;
	}
	
	private DesireIDGeneratorSingleton() throws Exception{
		Connection connector = DesireInstrument.getAccessToDesireBase();
		if (connector != null){
			System.out.println("+ Intitializing ID Generator ");
			Statement getSessionMax = connector.createStatement();
			ResultSet max = getSessionMax.executeQuery("SELECT desireID FROM DESIRES_MAIN ORDER BY desireID DESC LIMIT 1");
			if (max.next()){
				int currentMax = max.getInt(1);
				id = currentMax;
				System.out.println("+ Strating id is set : " + id);
			}
			else{
				System.out.println("+ Fitst initialization");
				id = 1;
			}
			getSessionMax.close();
		}
		else{
			throw new Exception("-Can't connect to base");
		}
	}
}
