package pack.clusterization;

import com.google.android.gms.maps.model.LatLng;

public class MapPoint {
	public float color;
	public LatLng coord;
	public MapPoint(LatLng coord, float color){
		this.color = color;
		this.coord = coord;
	}
}
