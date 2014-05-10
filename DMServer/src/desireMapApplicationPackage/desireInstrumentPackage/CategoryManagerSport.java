package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;

public class CategoryManagerSport extends CategoryManager{
	//string sportName, string advantages
	
	public CategoryManagerSport(DesireInstrument newOwner){
		owner = newOwner;
	}

	protected void addDesire(String desId, AddPack pack) throws SQLException {
			DesireContentSport sportContent = (DesireContentSport) pack.desireContent;
			Statement adder =  owner.getAccessToDesireBase().createStatement();
			System.out.println("__ADDING IN DESIRES_SPORT");
			adder.execute("INSERT INTO DESIRES_SPORT VALUES('"+ desId + "', '" + sportContent.sportName +"', '" + sportContent.advantages + "');");
			adder.close();
	}
	
}
