package desireMapApplicationPackage.userDataPackage;

import java.io.Serializable;

public class AndroidData implements Serializable{
	
	public AndroidData(String newRegID) throws Exception{
		if (newRegID != null){
			regID = newRegID;
		}
		else{
			throw new Exception("-null id!");
		}
	}
	
	public final String regID;
	
}
