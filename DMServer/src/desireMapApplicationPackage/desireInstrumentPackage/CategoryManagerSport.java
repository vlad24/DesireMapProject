package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;

public class CategoryManagerSport extends CategoryManager{
	//string sportName, int advantages

	protected void tryToAddDesire(int desId, AddPack pack) throws SQLException {
		if(myContent(pack.desireContent)){
			DesireContentSport sportContent = (DesireContentSport) pack.desireContent;
			Statement adder =  DesireInstrument.getAccessToDesireBase().createStatement();
			System.out.println("__ADDING IN DESIRES_SPORT");
			adder.execute("INSERT INTO DESIRES_SPORT VALUES("+ desId + ", '" + sportContent.sportName +"', " + sportContent.advantages + ");");
			adder.close();
		}
	}

	protected boolean myContent(DesireContent content) {
		return content.category == CodesMaster.Categories.SportCode;
	}

	
}
