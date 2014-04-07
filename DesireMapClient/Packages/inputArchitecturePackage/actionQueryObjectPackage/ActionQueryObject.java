package inputArchitecturePackage.actionQueryObjectPackage;

import java.io.Serializable;

public abstract class ActionQueryObject implements Serializable{
	protected final char actionCode;
	////
	protected ActionQueryObject(char newActionCode){
		actionCode = newActionCode;
	}
	
	public char getActionCode(){
		return actionCode;
	}

}
