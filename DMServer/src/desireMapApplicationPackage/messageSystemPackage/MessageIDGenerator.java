package desireMapApplicationPackage.messageSystemPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;

public class MessageIDGenerator {
	private static int id = 0;
	private static MessageIDGenerator generator;
	////
	public static synchronized MessageIDGenerator getInstance() throws Exception{
		if (generator == null){
			generator = new MessageIDGenerator();
		}
		return generator;
	}
	
	
	public synchronized int getNextID(){
		System.out.println("+ id : " + (id + 1));
		return ++id;
	}
	
	private MessageIDGenerator() throws Exception{
		Connection connector = DesireInstrument.getAccessToDesireBase();
		if (connector != null){
			System.out.println("+ Intitializing mesID Generator ");
			Statement getSessionMax = connector.createStatement();
			ResultSet max = getSessionMax.executeQuery("SELECT messageID FROM Messages ORDER BY messageID DESC LIMIT 1");
			if (max.next()){
				int currentMax = max.getInt(1);
				id = currentMax;
				System.out.println("+ Strating id is set : " + id);
			}
			else{
				System.out.println("+ Fitst initialization");
				id = 0;
			}
			getSessionMax.close();
		}
		else{
			throw new Exception("-Can't connect to base");
		}
	}
}

