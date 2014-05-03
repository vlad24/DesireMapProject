package com.example.mapshower;


import java.util.ArrayList;
import java.util.Random;

import pack.clusterization.ClusterPoint;
import pack.clusterization.ClusterizationAlgorithm;
import pack.quadtree.QuadTreeNode;
import pack.quadtree.QuadTreeNodeBox;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capricorn.RayMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;


public class GoogleMapActivity extends FragmentActivity implements OnMapClickListener, OnCameraChangeListener, OnMarkerClickListener {

	private double latitude;
	private double longitude;
	private GoogleMap map;
	private MapView mapView;
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


	SlidingUpPanelLayout slidingLayout;
	private LinearLayout panelLayout;
	private LinearLayout panelInfoLayout;
	private RelativeLayout panelDragableLayout;
	private LinearLayout likeLayout;
	private int numberOfLikes = 100;
	
	private ListView lvMapDesires;


	private static final int[] ITEM_DRAWABLES = {R.drawable.info, R.drawable.mail};

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		Intent intent = getIntent();
		latitude = intent.getDoubleExtra("latitude", 0);
		longitude = intent.getDoubleExtra("longitude",0);

		//		Resources r = getResources();
		//		panelHeightPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 68, r.getDisplayMetrics());


		panelInfoLayout = (LinearLayout) findViewById(R.id.PanelInfoView);
		panelInfoLayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(GoogleMapActivity.this, "Here info appears", Toast.LENGTH_SHORT).show();
			}
		});
		panelDragableLayout = (RelativeLayout) findViewById(R.id.PanelDragableView);

		likeLayout = (LinearLayout) findViewById(R.id.likeLayout);
		likeLayout.setOnClickListener(new OnClickListener(){
			boolean liked = false;
			@Override
			public void onClick(View v) {
				ImageView likeImage = (ImageView) panelLayout.findViewById(R.id.likeImageView);
				TextView likeInfo = (TextView) panelLayout.findViewById(R.id.likeInfoView);
				if(!liked){
					liked = true;
					numberOfLikes++;
					likeImage.setImageResource(R.drawable.red_heart);
				} else{
					liked = false;
					numberOfLikes--;
					likeImage.setImageResource(R.drawable.gray_heart);
				}
				likeInfo.setText(Integer.toString(numberOfLikes));
			}});

		slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		slidingLayout.setCoveredFadeColor(Color.argb(0, 0, 0, 0));
		slidingLayout.setOverlayed(true);
		slidingLayout.setDragView(panelDragableLayout);

		panelLayout = (LinearLayout) findViewById(R.id.PanelView);
		panelLayout.setVisibility(View.GONE);

		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
		rayMenu.setInfoView(panelInfoLayout);
		initRayMenu(rayMenu, ITEM_DRAWABLES);
		
		//init desires list
		lvMapDesires = (ListView) findViewById(R.id.lvMapDesires);
		MapCustomAdapter mapAdapter = new MapCustomAdapter(this);
		lvMapDesires.setAdapter(mapAdapter);

		tapTextView = (TextView) findViewById(R.id.tap_text);
		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);

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

	private void initRayMenu(RayMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(GoogleMapActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
				}
			});
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

		marker.setVisible(true);
		handler.post(new Runnable() {
			@Override
			public void run() {
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
			if(mapView != null){
				map = mapView.getMap();
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
		Animation bottomDown = AnimationUtils.loadAnimation(this,
				R.animator.bottom_down);
		if(slidingLayout.isExpanded()){
			slidingLayout.smoothSlideTo(1, 0);
		}else{
			panelLayout.startAnimation(bottomDown);
			panelLayout.setVisibility(View.GONE);
		}
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
		Animation bottomUp = AnimationUtils.loadAnimation(this,
				R.animator.bottom_up);
		panelLayout.startAnimation(bottomUp);
		panelLayout.setVisibility(View.VISIBLE);


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
	protected void onResume() {
		mapView.onResume();
		super.onResume();

	}

	@Override
	protected void onPause() {
		mapView.onResume();
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
}
