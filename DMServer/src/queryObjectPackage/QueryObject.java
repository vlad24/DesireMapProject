package queryObjectPackage;

import java.io.Serializable;

public abstract class QueryObject implements Serializable{	
	protected final char typeCode;
	////
	protected QueryObject(char nTypeCode){
		typeCode = nTypeCode;
	}
}
