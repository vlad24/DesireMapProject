package com.example.mapshower;


import java.util.ArrayList;
import java.util.Random;

import pack.clusterization.ClusterPoint;
import pack.clusterization.ClusterizationAlgorithm;
import pack.quadtree.QuadTreeNode;
import pack.quadtree.QuadTreeNodeBox;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;


public class GoogleMapActivity extends FragmentActivity implements OnMapClickListener, OnCameraChangeListener, OnMarkerClickListener {

	private double latitude;
	private double longitude;
	private GoogleMap map;
	private SupportMapFragment fm;
	private TextView tapTextView;
	private QuadTreeNode worldRoot;
	private int quadDepth;
	private CircleOptions circleOptions;
	private LatLng myPosition;
	private MarkerOptions myMarkerOptions;
	private double newRadius;
	private Circle myCircle;
	private ArrayList<LatLng> inputList;
	private ArrayList<ClusterPoint> outputList;
	private ArrayList<Marker> oldClusterMarkerList;
	private ArrayList<Marker> newClusterMarkerList;
	private float currentZoom;
	private float zoomChanging;
	private Handler handler;
	private Thread clusteringThread;
	private Thread fadingThread;
	private IconGenerator iconGenerator;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		Intent intent = getIntent();
		latitude = intent.getDoubleExtra("latitude", 0);
		longitude = intent.getDoubleExtra("longitude",0);

		tapTextView = (TextView) findViewById(R.id.tap_text);
		QuadTreeNodeBox world = new QuadTreeNodeBox(-90, -180, 90, 180);
		worldRoot = new QuadTreeNode(world, "0", 0);

		inputList = new ArrayList<LatLng>();

		handler = new Handler();

		iconGenerator = new IconGenerator(this);
		iconGenerator.setBackground(getResources().getDrawable(R.drawable.map_marker_bubble_pink_icon));
		iconGenerator.setTextAppearance(R.style.iconGenText);

		clusteringThread = new Thread(new Runnable(){

			private String TAG = "clusteringThread"; 
			@Override
			public void run() {


				if(outputList != null)
					outputList.clear();
				outputList = ClusterizationAlgorithm.cluster(inputList, newRadius);

				handler.post(new Runnable(){

					@Override
					public void run() {
						Bitmap icon;
						Marker clusterMarker;
						newClusterMarkerList = new ArrayList<Marker>();
						for (ClusterPoint cluster : outputList){
							Log.d(TAG, "Here in creating");
							if(cluster.getCount()<100)
								icon = iconGenerator.makeIcon("  "+cluster.getCount());
							else
								icon = iconGenerator.makeIcon(Integer.toString(cluster.getCount()));
							clusterMarker = map.addMarker(new MarkerOptions().position(cluster.getCenter())
									.icon(BitmapDescriptorFactory.fromBitmap(icon))
									.title("Я").snippet("count: "+cluster.getCount()).alpha(0.6f).visible(false));
							newClusterMarkerList.add(clusterMarker);
							animateCreatingMarker(clusterMarker);
						}
						oldClusterMarkerList = newClusterMarkerList;
					}

				});

			}

		});
		
		fadingThread = new Thread(new Runnable(){

			@Override
			public void run() {
				handler.post(new Runnable(){

					@Override
					public void run() {
						if(oldClusterMarkerList != null){
							for(Marker marker : oldClusterMarkerList){
								animateFadingMarker(marker);
							}
							oldClusterMarkerList.clear();
						}
					}
					
				});
			}
			
		});

	}

	private void animateCreatingMarker(final Marker marker){
		final long startTime = SystemClock.uptimeMillis();
		final long duration = 1500;

		final Interpolator interpolator = new AccelerateInterpolator();
		//		final Interpolator interpolator = new BounceInterpolator();
		//		final int startIconHeight = icon.getHeight()/2;
		//		final int finalIconHeight = icon.getHeight();
		//		final int iconWidth = icon.getWidth();

		handler.post(new Runnable() {
			@Override
			public void run() {
				marker.setVisible(true);
				long elapsed = SystemClock.uptimeMillis() - startTime;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				marker.setAlpha(t);
				//				int iconHeight = (int)(t * finalIconHeight + (1 - t) * startIconHeight);
				//				Bitmap currentBitmap = Bitmap.createScaledBitmap(icon, iconWidth, iconHeight, true);
				//				marker.setIcon(BitmapDescriptorFactory.fromBitmap(currentBitmap));

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	private void animateFadingMarker(final Marker marker){
		final long startTime = SystemClock.uptimeMillis();
		final long duration = 700;

		final Interpolator interpolator = new AccelerateInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - startTime;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				marker.setAlpha(1-t);

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				}
			}
		});
	}



	private void initializeMap(){
		try{
			fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			if(fm != null){
				map = fm.getMap();
				map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				map.setBuildingsEnabled(true);
				map.setOnMapClickListener(this);
				map.setOnCameraChangeListener(this);
				map.setOnMarkerClickListener(this);
			}
		}catch(Exception e){
			Toast.makeText(this, "Cant't access map", Toast.LENGTH_SHORT).show();
		}


		VisibleRegion currentRegion = map.getProjection().getVisibleRegion();
		double verticalScreenSize = currentRegion.latLngBounds.northeast.longitude - currentRegion.latLngBounds.southwest.longitude;
		quadDepth = worldRoot.getQuadDepth(verticalScreenSize);

		zoomChanging = 0;
		currentZoom = map.getCameraPosition().zoom;
		myPosition = new LatLng(latitude, longitude);
		myMarkerOptions = new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_ball_azure_icon)).title("Я").snippet("красавчик ваще :)").alpha(0.8f);
		map.addMarker(myMarkerOptions);

		Random r = new Random();
		LatLng randomPosition;
		for(int i = 0; i < 1000; i++){
			randomPosition = new LatLng(latitude+r.nextDouble()/10, longitude+r.nextDouble()/10);
			inputList.add(randomPosition);
		}


		newRadius = SphericalUtil.computeDistanceBetween(currentRegion.latLngBounds.northeast, currentRegion.latLngBounds.southwest)/10;
		circleOptions = new CircleOptions().center(myPosition).fillColor(0x550099FF).radius(newRadius).strokeWidth(0);
		myCircle = map.addCircle(circleOptions);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, (map.getMaxZoomLevel()+map.getMinZoomLevel())/2));
		Toast.makeText(this, String.format("MaxZoomLevel: %f\nMinZoomLevel: %f\nCurrentZoomLevel: %f",map.getMaxZoomLevel(),map.getMinZoomLevel(),map.getCameraPosition().zoom), Toast.LENGTH_LONG).show();


	}

	@Override
	public void onStart() {
		initializeMap();
		super.onStart();
	}


	@Override
	public void onMapClick(LatLng coord) {
		tapTextView.setText("coordinates:"+coord.latitude+" "+coord.longitude+"\nquad: "+worldRoot.geoPointToQuad(coord, quadDepth)+"\nnewRadius: "+newRadius);
	}

	@Override
	public void onCameraChange(CameraPosition updatedPosition) {
		VisibleRegion currentRegion = map.getProjection().getVisibleRegion();
		double verticalScreenSize = currentRegion.latLngBounds.northeast.latitude - currentRegion.latLngBounds.southwest.latitude;
		quadDepth = worldRoot.getQuadDepth(verticalScreenSize);

		if(currentZoom != map.getCameraPosition().zoom){
			zoomChanging += map.getCameraPosition().zoom - currentZoom;
			currentZoom = map.getCameraPosition().zoom;
			if(Math.abs(zoomChanging) > 0.7){
				zoomChanging = 0;
				newRadius = SphericalUtil.computeDistanceBetween(currentRegion.latLngBounds.northeast, currentRegion.latLngBounds.southwest)/10;
				
				fadingThread.run();
				clusteringThread.run();

				circleOptions = new CircleOptions().center(myPosition).fillColor(0x550099FF).radius(newRadius).strokeWidth(0);
				myCircle.remove();
				myCircle = map.addCircle(circleOptions);
			}
		}

	}

	@Override
	public boolean onMarkerClick(final Marker marker) {


		//		final long startTime = SystemClock.uptimeMillis();
		//		final long duration = 2000;
		//		
		//		Projection proj = map.getProjection();
		//		final LatLng markerLatLng = marker.getPosition();
		//		Point startPoint = proj.toScreenLocation(markerLatLng);
		//		startPoint.offset(0, -100);
		//		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		//
		//		final Interpolator interpolator = new BounceInterpolator();
		//
		//		handler.post(new Runnable() {
		//			@Override
		//			public void run() {
		//				long elapsed = SystemClock.uptimeMillis() - startTime;
		//				float t = interpolator.getInterpolation((float) elapsed / duration);			
		//				double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
		//				double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
		//				marker.setPosition(new LatLng(lat, lng));
		//
		//				if (t < 1.0) {
		//					// Post again 16ms later.
		//					handler.postDelayed(this, 16);
		//				}
		//			}
		//		});
		return true;
	}
}
