package pack.quadtree;

import com.google.android.gms.maps.model.LatLng;

public class QuadTreeNodeBox {
	public final double x0;
	public final double y0;
	public final double x1;
	public final double y1;

	public QuadTreeNodeBox(double x0, double y0, double x1, double y1){
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public boolean contains(LatLng point){
		return (point.latitude >= x0) && (point.latitude <= x1) && (point.longitude >= y0) && (point.longitude <= y1);
	}
	
	public boolean notIntersect(QuadTreeNodeBox box){
		return (y1 < box.y0) || (box.y1 < y0) || (x1 < box.x0) || (box.x1 < x0);
	}
}
