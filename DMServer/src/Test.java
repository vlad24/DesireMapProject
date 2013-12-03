import java.sql.*;
import java.util.ArrayList;
public class Test {

	public static void jExec() {
		try{
			System.out.println("---------Test program on");
			testDataBase();
			testBaseTableCreation();
			testBaseAdding();
			testBaseSelecting();
			//testArrayListCreatingFromResultSet();
			System.out.println("---------Tests are passed\n");
		}
		catch(Exception fail){
			System.out.println("FAIL!!!!!");
			System.out.println(fail.getMessage());
		}
	}

	public static final void testDataBase() throws Exception{
		DataBaseSQLite base = new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
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

	public static final void testBaseTableCreation() throws Exception{
		DataBaseSQLite base = new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
		base.connectToBase();
		Statement creator = base.getConnection().createStatement();
		creator.execute("DROP TABLE IF EXISTS PRICES");
		creator.execute("CREATE TABLE PRICES(COMPANY TEXT, MODEL TEXT PRIMARY KEY, COST TEXT)");
		creator.close();
		base.disconnect();
	}

	public static final void testBaseAdding() throws Exception{
		DataBaseSQLite base= new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
		base.connectToBase();
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

	public static final void testBaseSelecting() throws Exception{
		DataBaseSQLite base= new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
		base.connectToBase();
		Statement selector  = base.getConnection().createStatement();
		ResultSet result = selector.executeQuery("SELECT * FROM PRICES");
		int count = 0;
		while(result.next()){
			count++;
		}
		if (count != 2){
			throw new Exception("Not right amount of elements have been selected");
		}
		base.disconnect();
	}

	public static final void testCoordinatesWorking() throws Exception{
		DataBaseSQLite base= new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
		base.connectToBase();
		Statement selector  = base.getConnection().createStatement();
		ResultSet result = selector.executeQuery("SELECT * FROM PRICES");
		int count = 0;
		while(result.next()){
			count++;
			System.out.print(result.getString("company") + " ");
			System.out.print(result.getString("model") + " ");
			System.out.print(result.getString("cost") + "\n");
		}
		System.out.println(count);
		base.disconnect();
	}

//	public static final void testArrayListCreatingFromResultSet() throws Exception{
//		ResultSetMaster master = new ResultSetMaster();
//		StateLoggedIn state = new StateLoggedIn(null);
//		Desire wish = new Desire("vlad","_SPORT", "dance", "ourFirstTag", "2.3", "2.3");
//		state.addDesire(wish);
//		Desire desire = new Desire("sam","_SPORT", "beatbox", "ourFirstTag", "2.2", "2");
//		ResultSet set = state.getSatisfiersToday(desire, "5");
//		ArrayList<ArrayList<String>> list = master.getArrayListFromResultSet(set);
//		for (int i = 0 ; i < list.size(); i++){
//			for (int j = 0; j < list.get(i).size(); j++){
//				System.out.print(list.get(i).get(j) + " ");
//			}
//			System.out.println("");
//		}
//	}

}