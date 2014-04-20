package desireMapApplicationPackage.desireContentPackage;
import java.io.Serializable;

public class Coordinates implements Serializable{
	public final double latitude;
	public final double longitude;
	
	public Coordinates(double newLatitude, double newLongitude){
		latitude = newLatitude;
		longitude = newLongitude;
	}

}
