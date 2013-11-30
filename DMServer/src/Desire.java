
public class Desire {
	public Desire(String nMaster, String nCategory, String nDesireString, String nTag, String nLatitude, String nLongitude){
		master = nMaster;
		category = nCategory;
		desireString = nDesireString;
		tag = nTag;
		latitude = nLatitude;
		longitude = nLongitude;
		System.out.println("Desire has been created " + master + category + desireString + tag + latitude + longitude);
	}

	public String getCategory() {
		return category;
	}
	
	public String getTag() {
		return tag;
	}

	public String getDesireString() {
		return desireString;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getMaster() {
		return master;
	}

	private String master;
	private String category;
	private String desireString;
	private String tag;
	private String latitude;
	private String longitude;
	
}
