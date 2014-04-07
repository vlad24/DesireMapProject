package actionCodeConstantsPackage;

public final class ActionCodesMaster {
	
	public static class ActionCodes{
		public static final char RegistrationCode = 'R';
		public static final char ExitCode = 'E';
		public static final char AddCode = 'A';
		public static final char SatisfyCode = 'Z';
		public static final char LoginCode = 'L';
		public static final char ShowInfoCode = 'I';
		public static final char ShowPersonalDesiresCode = 'S';
		public static final char DeleteByContentCode = 'd';
		public static final char DeleteByCryteriaCode = 'D';
		public static final char AddSatisfyCode = 'G';
	}
	public static class Categories{
		public static final String Dating = "_DATING";
		public static final String Sport = "_SPORT";
		public static final int MainDataCode = 100;
		public static final int LoginDataCode = 101;
		public static final int RegistrationDataCode = 102;
		public static final int CryteriaCode = 10;
		public static final int SportContentCode = 1110;
		public static final int DatingContentCode = 1111;
		public static final int SimpleQueryObjectCode = 12;
		public static final int CryterizedDesireCode = 12134;

	}
	
	public static boolean someDeleteCode(char codeSymbol){
		return ((codeSymbol == ActionCodes.DeleteByContentCode) || (codeSymbol == ActionCodes.DeleteByCryteriaCode));
	}
	
	public static boolean someAddCode(char codeSymbol){
		return ((codeSymbol == ActionCodes.AddCode) || (codeSymbol == ActionCodes.AddSatisfyCode));
	}
	
	public static boolean someSatisfyCode(char codeSymbol){
		return ((codeSymbol == ActionCodes.SatisfyCode) || (codeSymbol == ActionCodes.AddSatisfyCode));
	}
	
	
}
