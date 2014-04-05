package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class CategoryManagerMain extends CategoryManager{

	@Override
	protected void tryToAddDesire(int desId, DesireContent content)
			throws SQLException {
		System.out.println("_ADDING INTO DESIRES_MAIN");
		Statement adder =  DesireInstrument.getAccessToDesireBase().createStatement();
		adder.execute("INSERT INTO DESIRES_MAIN VALUES("+ desId + ", '" 
		+ content.login + "', '"
		+ content.description + "', '"
		+ "tag', '" + 
		content.coordinates.latitude + "', '" +
		content.coordinates.longitude + "', " +
		"datetime('now') );");
		
		adder.close();
	}
	
	protected boolean myContent(DesireContent content) {
		return true;
	}
}
