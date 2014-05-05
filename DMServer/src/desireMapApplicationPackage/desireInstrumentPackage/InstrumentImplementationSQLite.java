package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.dataBasePackage.DataBaseSQLite;
import desireMapApplicationPackage.dataBasePackage.GeneralDBConstantsMaster;
import desireMapApplicationPackage.outputSetPackage.MessageSet;

public class InstrumentImplementationSQLite extends InstrumentImplementation{

	public InstrumentImplementationSQLite(DesireInstrument newOwner) {
		super(newOwner);
	}

	public void initInstrument() {
		System.out.println("...LOADING DESIRE INSTRUMENT");
		owner.desireDataBase = new DataBaseSQLite("C:/Databases/desireMapMain.db");
		System.out.println("+MAIN DB IS LOADED !!!");
		try {
			owner.desireDataBase.connectToBase();
			owner.desireDataBase.init();
			owner.tube = new CategoryTube(owner);
			System.out.println("+CATEGORY ADDERS - DONE");

			owner.dataBaseSuffixes = new HashMap<Integer, String>();
			owner.dataBaseSuffixes.put(CodesMaster.Categories.SportCode, GeneralDBConstantsMaster.sportTableSuffix);
			owner.dataBaseSuffixes.put(CodesMaster.Categories.DatingCode, GeneralDBConstantsMaster.datingTableSuffix);
			System.out.println("+HashMapSuffixes - DONE !!!");

			owner.rsMaster = new ResultSetMaster();
			System.out.println("+Initialization - DONE !!!\n\n");

		} catch (ClassNotFoundException | SQLException error) {
			System.out.println("-Instrument hasn't managed to initialize the base !!!");
			error.printStackTrace();
		}
	}

	public void makeCurrentMessageFresh(MessageSet set, int index){
		try (Statement fresher = getAccessToDesireBase().createStatement()){
			String currentMessageID = set.mSet.get(index).messageID;
			String markNotSentQuery = "update MESSAGES set STATUS = -1 where (messageID ='" + currentMessageID + "') AND (STATUS = 0);";
			System.out.println(markNotSentQuery);
			fresher.executeUpdate(markNotSentQuery);
		}
		catch(Exception error){
			System.out.println("-Couldn't make the current message fresh _ at instrumentImplementation");
		}

	}


}
