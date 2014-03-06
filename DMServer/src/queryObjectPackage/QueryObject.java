package queryObjectPackage;

import java.io.Serializable;

public abstract class QueryObject implements Serializable{
	protected final char actionCode;
	protected final char typeCode;
	////
	protected QueryObject(char newActionCode, char newTypeCode){
		actionCode = newActionCode;
		typeCode = newTypeCode;
	}
}
