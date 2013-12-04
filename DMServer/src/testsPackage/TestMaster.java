package testsPackage;

public final class TestMaster {
	public static void jExec() {
		System.out.println("------- Some testing --------\n");
		try{
			TestDataBase.executeTests();
			TestDesireInstrument.executeTests();
		}
		catch(Exception fail){
			System.out.print("FAIL!!! - ");
			System.out.print(fail.getMessage());
		}
	}
}
