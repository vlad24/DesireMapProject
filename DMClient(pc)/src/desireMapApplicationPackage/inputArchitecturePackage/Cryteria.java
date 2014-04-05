package desireMapApplicationPackage.inputArchitecturePackage;

import java.io.Serializable;

public class Cryteria implements Serializable{
	public final double mapRadius;
	public final int timeRadius;
	public final int searchCategory;
	public Cryteria(double newMapRadius,int newTimeRadius, int newSearchCategory){
		mapRadius = newMapRadius;
		timeRadius = newTimeRadius;
		searchCategory = newSearchCategory;
	}
}
