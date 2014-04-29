package desireMapApplicationPackage.outputSetPackage;

import java.io.Serializable;
import java.util.ArrayList;

public class UserSet implements Serializable {
	public ArrayList<String> uSet;
	
	public UserSet(ArrayList<String> newUserSet){
		uSet = newUserSet;
	}
}
