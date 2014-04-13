package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class CategoryManagerMain extends CategoryManager{

	@Override
	protected void tryToAddDesire(int desId, AddPack pack) throws SQLException {
		System.out.println("_ADDING INTO DESIRES_MAIN");
		Statement adder =  DesireInstrument.getAccessToDesireBase().createStatement();
		String query = "INSERT INTO DESIRES_MAIN VALUES(" 
				+ desId + ", '" 
				+ pack.desireContent.login + "', '"
				+ pack.desireContent.description + "', '"
				+ pack.desireContent.category + "', '"
				+ pack.desireContent.coordinates.latitude + "', '"
				+ pack.desireContent.coordinates.longitude + "', '"
				+ pack.tile + "', "
				+ "datetime('now') );";
		System.out.println(query);
		adder.execute(query);
		adder.close();
	}
	
	protected boolean myContent(DesireContent content) {
		return true;
	}
}
