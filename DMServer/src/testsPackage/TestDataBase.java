package testsPackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataBasePackage.DataBaseSQLite;

public final class TestDataBase {

	protected static void executeTests() throws Exception {
			System.out.println("--- Testing Data Bases ---");
			testDataBaseOpening();
			testBaseTableCreation();
			testBaseAdding();
			testBaseSelectingOnAmount();
			testBaseSelectingOnQuality();
			System.out.println("--- Testing Data Bases is successfully finished ---");
		}
	////++++
	private static final DataBaseSQLite initTestCase() throws ClassNotFoundException, SQLException{
		DataBaseSQLite base = new DataBaseSQLite("C://Databases/testCARS.db");
		base.connectToBase();
		return base;
	}
	
	private static final void testDataBaseOpening() throws Exception{
		DataBaseSQLite base = new DataBaseSQLite("C://Databases/testCARS.db");
		try {
			base.connectToBase();
			if (!base.turnedOn){
				throw new Exception("DB is not turned on!\n");
			}
		}
		catch(Exception error){
			throw new Exception("Problems with db connection\n");
		}
		finally{
			base.disconnect();
		}
	}

	private static final void testBaseTableCreation() throws Exception{
		DataBaseSQLite testBase = initTestCase();
		Statement creator = testBase.getConnection().createStatement();
		creator.execute("DROP TABLE IF EXISTS PRICES");
		creator.execute("CREATE TABLE PRICES(COMPANY TEXT, MODEL TEXT PRIMARY KEY, COST TEXT)");
		creator.close();
		testBase.disconnect();
	}

	public static final void testBaseAdding() throws Exception{
		DataBaseSQLite base = initTestCase();
		String company = "opel";
		String model = "astra";
		String price = "500000";
		String company2 = "renault";
		String model2 = "logan";
		String price2 = "300000";
		Statement adder = base.getConnection().createStatement();
		adder.execute("INSERT INTO PRICES VALUES('" + company + "', '" + model + "', '" + price + "')");
		adder.execute("INSERT INTO PRICES VALUES('" + company2 + "', '" + model2 + "', '" + price2 + "')");
		base.disconnect();
	}

	public static final void testBaseSelectingOnAmount() throws Exception{
		DataBaseSQLite base = initTestCase();
		Statement selector  = base.getConnection().createStatement();
		ResultSet result = selector.executeQuery("SELECT * FROM PRICES");
		int count = 0;
		while(result.next()){
			count++;
		}
		base.disconnect();
		if (count != 2){
			throw new Exception("testBaseSelectingOnAmount : Not right amount of elements have been selected");
		}
	}

	public static final void testBaseSelectingOnQuality() throws Exception{
		DataBaseSQLite base = initTestCase();
		Statement selector  = base.getConnection().createStatement();
		ResultSet result = selector.executeQuery("SELECT MODEL FROM PRICES WHERE COST > 400000");
		while(result.next()){
			if (!result.getString("model").equals("astra")){
				base.disconnect();
				throw new Exception("testBaseSelectingOnQuality : not the right select");
			}
		}
	}

}