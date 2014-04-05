package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;

public class CategoryManagerSport extends CategoryManager{
	//string sportName, int advantages

	protected void tryToAddDesire(int desId, DesireContent content) throws SQLException {
		if(myContent(content)){
			DesireContentSport sportContent = (DesireContentSport) content;
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
