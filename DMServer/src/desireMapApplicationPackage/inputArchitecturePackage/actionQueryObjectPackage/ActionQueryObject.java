package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import java.io.Serializable;

public class ActionQueryObject implements Serializable{
	public final char actionCode;
	////
	protected ActionQueryObject(char newActionCode){
		actionCode = newActionCode;
	}

}
