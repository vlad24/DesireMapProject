package pack.clusterization;

import com.google.android.gms.maps.model.LatLng;

public class ClusterPoint {
	private double xCenter;
	private double yCenter;
	private int count;
	
	public ClusterPoint(LatLng newCenter){
		xCenter = newCenter.latitude*1e5;
		yCenter = newCenter.longitude*1e5;
		count = 1;
	}
	
	public void addToCluster(LatLng newPoint){
		xCenter = (xCenter*count + newPoint.latitude*1e5)/(count+1);
		yCenter = (yCenter*count + newPoint.longitude*1e5)/(count+1);
		count++;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
	
	public LatLng getCenter(){
		return new LatLng(xCenter/1e5, yCenter/1e5);
	}
	
	public double getXCenter() {
		return xCenter;
	}
	
	public double getYCenter() {
		return yCenter;
	}
	
}
