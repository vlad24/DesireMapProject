package desireMapApplicationPackage.outputArchitecturePackage;

import java.io.Serializable;
import java.util.HashSet;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class DesireSet implements Serializable{
	public DesireSet(){
		dSet = new HashSet<DesireContent>();
	}
	public HashSet<DesireContent> dSet;
}