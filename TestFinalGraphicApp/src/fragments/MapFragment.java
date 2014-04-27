package fragments;

import java.util.ArrayList;
import java.util.Random;



import logic.clusterization.ClusterPoint;
import logic.clusterization.ClusterizationAlgorithm;

import com.example.testfinalgraphicapp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
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

import desireMapApplicationPackage.quadtree.QuadTreeNode;
import desireMapApplicationPackage.quadtree.QuadTreeNodeBox;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends Fragment implements OnMarkerClickListener, OnCameraChangeListener, OnMapClickListener {


	private MapView mapView;
	private GoogleMap map;
	private TextView tapTextView;
	private QuadTreeNode worldRoot;
	private int quadDepth;
	private CircleOptions circleOptions;
	private LatLng myPosition;
	private MarkerOptions myMarkerOptions;
	private Marker myMarker;
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
	private Bitmap backgroundBitmap;
	private Bitmap bmOverlay;
	private String TAG = "MapFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.map_layout, container, false);
		
		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		
		initializeMap();

		tapTextView = (TextView) v.findViewById(R.id.tap_text);
		QuadTreeNodeBox world = new QuadTreeNodeBox(-90, -180, 90, 180);
		worldRoot = new QuadTreeNode(world, "0", 0);

		inputList = new ArrayList<LatLng>();

		handler = new Handler();

		iconGenerator = new IconGenerator(getActivity());
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

		return v;
	}


	public void refresh(double latitude, double longitude){

		if(myMarker != null)
			myMarker.remove();
		myPosition = new LatLng(latitude, longitude);
		myMarkerOptions = new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_ball_azure_icon)).title("Я").snippet("красавчик ваще :)").alpha(0.8f);
		myMarker = map.addMarker(myMarkerOptions);

		Toast.makeText(getActivity(), "Latitude: "+latitude+"\nLongitude: "+longitude, Toast.LENGTH_SHORT).show();
		VisibleRegion currentRegion = map.getProjection().getVisibleRegion();
		double verticalScreenSize = currentRegion.latLngBounds.northeast.longitude - currentRegion.latLngBounds.southwest.longitude;
		quadDepth = worldRoot.getQuadDepth(verticalScreenSize);

		zoomChanging = 0;
		currentZoom = map.getCameraPosition().zoom;
		myPosition = new LatLng(latitude, longitude);
		myMarkerOptions = new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_ball_azure_icon)).title("Я").snippet("красавчик ваще :)").alpha(0.8f);
		myMarker = map.addMarker(myMarkerOptions);

		inputList.clear();
		Random r = new Random();
		LatLng randomPosition;
		for(int i = 0; i < 1000; i++){
			randomPosition = new LatLng(latitude+r.nextDouble()/10, longitude+r.nextDouble()/10);
			inputList.add(randomPosition);
		}


		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, (map.getMaxZoomLevel()+map.getMinZoomLevel())/2));
		Toast.makeText(getActivity(), String.format("MaxZoomLevel: %f\nMinZoomLevel: %f\nCurrentZoomLevel: %f",map.getMaxZoomLevel(),map.getMinZoomLevel(),map.getCameraPosition().zoom), Toast.LENGTH_SHORT).show();

		//delete old points
		//download new quadtree
	}


	private void initializeMap(){
		map = mapView.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		map.setBuildingsEnabled(true);
		map.setOnMapClickListener(this);
		map.setOnCameraChangeListener(this);
		map.setOnMarkerClickListener(this);
		
		// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		try {
			MapsInitializer.initialize(this.getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		
		
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
				if(myCircle != null)
					myCircle.remove();
				myCircle = map.addCircle(circleOptions);
			}
		}

	}
	
	public Bitmap getMapImage(int width, int height) {  

		Log.d(TAG, "in getImage");
        /* Capture drawing cache as bitmap */  
//        mapView.setDrawingCacheEnabled(true);  
//        Bitmap bmp = Bitmap.createBitmap(mapView.getDrawingCache());  
//        mapView.setDrawingCacheEnabled(false); 
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		mapView.draw(c);
        return bmp;  
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


	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

}
