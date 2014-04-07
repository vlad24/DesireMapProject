package inputArchitecturePackage;

public class Cryteria {
	private final double mapRadius;
	private final int timeRadius;
	private final String searchCategory;
	
	public Cryteria(String newLogin, double newMapRadius,int newTimeRadius, String newSearchCategory){
		mapRadius = newMapRadius;
		timeRadius = newTimeRadius;
		searchCategory = newSearchCategory;
	}

	public double getMapRadius() {
		return mapRadius;
	}

	public int getTimeRadius() {
		return timeRadius;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

}
