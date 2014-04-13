package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;

public class CategoryManagerDating extends CategoryManager {

	protected void tryToAddDesire(int desId, AddPack pack) throws SQLException {
		if(myContent(pack.desireContent)){
			DesireContentDating datingContent = (DesireContentDating) pack.desireContent;
			Statement adder =  DesireInstrument.getAccessToDesireBase().createStatement();
			System.out.println("__ADDING INTO DESIRES_DATING");
			adder.execute("INSERT INTO DESIRES_DATING VALUES("+ desId + ", '" + datingContent.partnerSex +"', '" + datingContent.partnerAge + "');");
			adder.close();
		}
	}

	protected boolean myContent(DesireContent content) {
		return content.category == CodesMaster.Categories.DatingCode;
	}

}
