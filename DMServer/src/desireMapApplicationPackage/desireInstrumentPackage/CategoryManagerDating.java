package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;

public class CategoryManagerDating extends CategoryManager {
	
	public CategoryManagerDating(DesireInstrument newOwner){
		owner = newOwner;
	}

	protected void addDesire(String desId, AddPack pack) throws SQLException {
			DesireContentDating datingContent = (DesireContentDating) pack.desireContent;
			Statement adder =  owner.getAccessToDesireBase().createStatement();
			System.out.println("__ADDING INTO DESIRES_DATING");
			adder.execute("INSERT INTO DESIRES_DATING VALUES('"+ desId + "', '" + datingContent.partnerSex +"', " + datingContent.partnerAgeFrom + "," + datingContent.partnerAgeTo +");");
			adder.close();
	}
	
}
