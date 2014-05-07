package logic.clusterization;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class ClusterPoint {
	public double xCenter;
	public double yCenter;
	public ArrayList<DesireContent> points;
	public int count;

	public ClusterPoint(DesireContent newCenter){
		xCenter = newCenter.coordinates.latitude*1e5;
		yCenter = newCenter.coordinates.longitude*1e5;
		points = new ArrayList<DesireContent>();
		points.add(newCenter);
		count = 1;
	}

	public void addToCluster(DesireContent newPoint){
		xCenter = (xCenter*count + newPoint.coordinates.latitude*1e5)/(count+1);
		yCenter = (yCenter*count + newPoint.coordinates.longitude*1e5)/(count+1);

		binaryInsert(newPoint);
		
		count++;
	}

	public void addToCluster(ClusterPoint newCluster){
		xCenter = (xCenter*count + newCluster.xCenter*newCluster.count)/(count+newCluster.count);
		yCenter = (yCenter*count + newCluster.yCenter*newCluster.count)/(count+newCluster.count);

		merge(newCluster.points);

		count += newCluster.count;
	}


	private void binaryInsert(DesireContent newPoint){

		//try to find index
		int low = 0;
		int high = count-1;
		//index to insert
		int index;
		int mid = 0;
		while(low <= high){
			mid = (low + high) >>> 1;
			int midVal = points.get(mid).likes;
			if(newPoint.likes < midVal)
				high = midVal - 1;
			else if(newPoint.likes > midVal)
				low = midVal + 1;
			else break;
		}

		//if mid element less than newPoint than put newPoint to next
		if(points.get(mid).likes <= newPoint.likes)
			index = mid + 1;
		else index = mid;
		
		points.add(index, newPoint);
	}


	//merge arrayList newPoints in points 
	private void merge(ArrayList<DesireContent> newPoints){
		int m = count;
		int n = newPoints.size();

		while(m > 0 && n > 0){
			if(points.get(m-1).likes < newPoints.get(n-1).likes){
				points.set(m+n-1, points.get(m-1));
				m--;
			}else{
				points.set(m+n-1, newPoints.get(n-1));
				n--;
			}
		}
		//if n == 0 we do nothing
		//else add rest of newPoints to points
		while(n > 0){
			points.set(n-1, newPoints.get(n-1));
			n--;
		}
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public LatLng getCenter(){
		return new LatLng(xCenter*1e-5, yCenter*1e-5);
	}


}
