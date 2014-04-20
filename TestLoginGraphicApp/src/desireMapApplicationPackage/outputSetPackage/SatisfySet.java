package desireMapApplicationPackage.outputSetPackage;

import java.util.ArrayList;
import java.util.HashMap;

import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.userDataPackage.MainData;


public class SatisfySet extends DesireSet{
	public SatisfySet(){
		desireAuthors = new HashMap<String, MainData>();
	}
	public HashMap<String, MainData> desireAuthors;
}
