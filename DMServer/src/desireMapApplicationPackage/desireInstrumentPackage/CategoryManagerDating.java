package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;

public class CategoryManagerDating extends CategoryManager {

	protected void tryToAddDesire(int desId, DesireContent content) throws SQLException {
		if(myContent(content)){
			DesireContentDating datingContent = (DesireContentDating) content;
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
