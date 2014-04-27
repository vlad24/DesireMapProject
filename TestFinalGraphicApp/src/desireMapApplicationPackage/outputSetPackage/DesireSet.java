package desireMapApplicationPackage.outputSetPackage;

import java.io.Serializable;
import java.util.ArrayList;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class DesireSet implements Serializable{
	public DesireSet(){
		dArray = new ArrayList<DesireContent>();
	}
	public ArrayList<DesireContent> dArray;
}