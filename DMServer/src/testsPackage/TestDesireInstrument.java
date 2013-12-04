package testsPackage;

import java.sql.ResultSet;

import desireInstrumentPackage.*;
import desiresPackage.Desire;

public final class TestDesireInstrument {
	private static final DesireInstrument instrument;
	private static final Desire desireRun1;
	private static final Desire desireRun2;
	
	static{
		instrument = new DesireInstrument();
		desireRun1 = new Desire("test", "_TEST", "run with smb", "run", "3.21", "4.32");
		desireRun2 = new Desire("tset", "_TEST", "lets run people", "run", "4.32", "3.21");
		try {
			instrument.register("test", "test", "test", "test", "test");
		} catch (Exception e) {}
	}
	//--
	
	private static void clearDesires_Test() throws Exception{
		instrument.clearUsersCategory("test", "_TEST");
		instrument.clearUsersCategory("tset", "_TEST");
	}
	
	protected static void executeTests() throws Exception {
		System.out.println("--- Testing DesireInstrument ---");
		testInstrumentExiting();
		testInstrumnetLoggingIn();
		clearDesires_Test();
		testInstrumentAdding();
		testInstrumentSatisfying();
		System.out.println("--- Testing DesireInstrument is successfully finished ---");
	}
	
	private static void testInstrumentExiting() {
		instrument.exit();
	}

	private static void testInstrumnetLoggingIn() throws Exception {
		instrument.logIn("test", "test");
	}
	
	private static void testInstrumentAdding() throws Exception {
		instrument.addDesire(desireRun1);
	}
	
	
	private static void testInstrumentSatisfying() throws Exception {
		ResultSet set = instrument.getSatisfiersToday(desireRun2, "5");
		int count = 0;
		while(set.next()){
			if (!set.getString("login").equals("test")){
				throw new Exception("Wrong result set of satisfiers");
			}
			count++;
		}
		if (count != 1){
			throw new Exception("Wrong result set of satisfiers");
		}
	}
}
