package desireMapApplicationPackage.codeConstantsPackage;

public final class CodesMaster {
	
	public static class ActionCodes{
		public static final char RegistrationCode = 'R';
		public static final char ExitCode = 'E';
		public static final char AddCode = 'A';
		public static final char SatisfyCode = 'Z';
		public static final char LoginCode = 'L';
		public static final char ShowInfoCode = 'I';
		public static final char ShowPersonalDesiresCode = 'S';
		public static final char DeleteCode = 'D';
		public static final char AddSatisfyCode = 'G';
		public static final char MessageDeliverCode = 'M';
		public static final char MessageSendCode = 'C';
		public static final char TilesCode = 'T';
	}
	public static class Categories{
		public static final int DatingCode = 1;
		public static final int SportCode = 0;
	}
	
	public static boolean someAddCode(char codeSymbol){
		return ((codeSymbol == ActionCodes.AddCode) || (codeSymbol == ActionCodes.AddSatisfyCode));
	}
	
	public static boolean someSatisfyCode(char codeSymbol){
		return ((codeSymbol == ActionCodes.SatisfyCode) || (codeSymbol == ActionCodes.AddSatisfyCode));
	}
	
	
}
