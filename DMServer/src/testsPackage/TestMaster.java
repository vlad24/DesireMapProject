package testsPackage;

public final class TestMaster {
	public static void jExec() {
		System.out.println("------- Some testing --------\n");
		try{
			TestDataBase.executeTests();
			TestDesireInstrument.executeTests();
			System.out.println("------- Testing is successfully finished --------\n");
			System.out.println("\n\n\n\n");
		}
		catch(Exception fail){
			System.out.print("FAIL!!! - ");
			System.out.print(fail.getMessage());
			System.out.println("------- Testing failed --------\n");
		}
	}
}
