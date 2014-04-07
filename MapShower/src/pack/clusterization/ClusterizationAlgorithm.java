package pack.clusterization;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


public class ClusterizationAlgorithm {

	public static ArrayList<ClusterPoint> cluster(ArrayList<LatLng> inputPoints, double radius){
		LatLng firstPoint = inputPoints.get(0);
		ClusterPoint firstCluster = new ClusterPoint(firstPoint);
		firstCluster.setCount(0);
		
		ArrayList<ClusterPoint> outputPoints = new ArrayList<ClusterPoint>();
		outputPoints.add(firstCluster);

		outer : for(LatLng point : inputPoints){
			ClusterPoint bestCluster = outputPoints.get(0);
			double minDistance = SphericalUtil.computeDistanceBetween(bestCluster.getCenter(), point);
			double distance;
			for(ClusterPoint clusterPoint : outputPoints){
				distance = SphericalUtil.computeDistanceBetween(clusterPoint.getCenter(), point);
				if(minDistance > distance){
					minDistance = distance;
					bestCluster = clusterPoint;
				}
			}

			if(minDistance < radius){
				bestCluster.addToCluster(point);
				continue outer;
			}

			ClusterPoint newClusterPoint = new ClusterPoint(point);
			outputPoints.add(newClusterPoint);
		}
		return outputPoints;		
	}
}
