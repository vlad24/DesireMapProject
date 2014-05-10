package logic.clusterization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;


import desireMapApplicationPackage.desireContentPackage.DesireContent;


public class ClusterizationAlgorithm {

	private static ArrayList<ClusterPoint> outputPoints = new ArrayList<ClusterPoint>();

	public static ArrayList<ClusterPoint> cluster(HashSet<DesireContent> inputPoints,  double radius){
		outputPoints.clear();
		if(!inputPoints.isEmpty()){
			DesireContent firstPoint = inputPoints.iterator().next();
			ClusterPoint firstCluster = new ClusterPoint(firstPoint);
			Log.d("ClientTag", "in cluster first cluster: x:"+firstCluster.xCenter+" y:"+firstCluster.yCenter);
			outputPoints.add(firstCluster);
			inputPoints.remove(firstPoint);
			outer : for(DesireContent point : inputPoints){
				ClusterPoint bestCluster = outputPoints.get(0);
				double minDistance = MySphericalUtil.computeDistanceBetween(bestCluster.xCenter*1e-5, bestCluster.yCenter*1e-5,
						point.coordinates.latitude, point.coordinates.longitude);
				double distance;
				for(ClusterPoint clusterPoint : outputPoints){
					distance = MySphericalUtil.computeDistanceBetween(clusterPoint.xCenter*1e-5, clusterPoint.yCenter*1e-5,
							point.coordinates.latitude, point.coordinates.longitude);

					//redefine closest cluster
					if(minDistance > distance){
						minDistance = distance;
						bestCluster = clusterPoint;
					}
				}

				if(minDistance < radius){
					Log.d("ClientTag", "we adding to cluster");
					bestCluster.addToCluster(point);
					continue outer;
				}

				ClusterPoint newClusterPoint = new ClusterPoint(point);
				outputPoints.add(newClusterPoint);
			}		
		}
		for(ClusterPoint point : outputPoints){
			Log.d("ClientTag", "in outputlist cluster : x:"+point.xCenter+" y:"+point.yCenter);
		}
		return outputPoints;
	}

	public static ArrayList<ClusterPoint> cluster(ConcurrentHashMap<Marker, ClusterPoint> oldHashMap, HashSet<DesireContent> inputPoints,  double radius){
		outputPoints.clear();
		Collection<ClusterPoint> oldClusterPoints = oldHashMap.values();
		if(!oldClusterPoints.isEmpty()){
			ClusterPoint firstCluster = oldClusterPoints.iterator().next();

			outputPoints.add(firstCluster);
			oldClusterPoints.remove(firstCluster);
			outer : for(ClusterPoint oldHashmapCluster : oldClusterPoints){
				ClusterPoint bestCluster = outputPoints.get(0);
				double minDistance = MySphericalUtil.computeDistanceBetween(bestCluster.xCenter*1e-5, bestCluster.yCenter*1e-5,
						oldHashmapCluster.xCenter*1e-5, oldHashmapCluster.yCenter*1e-5);
				double distance;
				for(ClusterPoint clusterPoint : outputPoints){
					distance = MySphericalUtil.computeDistanceBetween(clusterPoint.xCenter*1e-5, clusterPoint.yCenter*1e-5,
							oldHashmapCluster.xCenter*1e-5, oldHashmapCluster.yCenter*1e-5);
					//redefine closest cluster
					if(minDistance > distance){
						minDistance = distance;
						bestCluster = clusterPoint;
					}
				}

				if(minDistance < radius){
					bestCluster.addToCluster(oldHashmapCluster);
					continue outer;
				}

				outputPoints.add(oldHashmapCluster);
			}
		}
		//now we iterate through delta set
		if(!inputPoints.isEmpty()){
			outer : for(DesireContent point : inputPoints){
				ClusterPoint bestCluster = outputPoints.get(0);
				double minDistance = MySphericalUtil.computeDistanceBetween(bestCluster.xCenter*1e-5, bestCluster.yCenter*1e-5,
						point.coordinates.latitude, point.coordinates.longitude);
				double distance;
				for(ClusterPoint clusterPoint : outputPoints){
					distance = MySphericalUtil.computeDistanceBetween(clusterPoint.xCenter*1e-5, clusterPoint.yCenter*1e-5,
							point.coordinates.latitude, point.coordinates.longitude);

					//redefine closest cluster
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
		}

		return outputPoints;
	}












}