package fragments;

import graphics.map.MapCustomAdapter;
import graphics.map.expand_graphic.ExpandingListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;



import logic.Client;
import logic.clusterization.ClusterPoint;
import logic.clusterization.ClusterizationAlgorithm;
import logic.clusterization.MySphericalUtil;

import com.capricorn.RayMenu;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.quadtree.DataQuadTreeNode;
import desireMapApplicationPackage.quadtree.QuadTreeNode;
import desireMapApplicationPackage.quadtree.QuadTreeNodeBox;
import desireMapApplicationPackage.userDataPackage.MainData;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends Fragment implements OnMarkerClickListener, OnCameraChangeListener, OnMapClickListener {


	private MapView mapView;
	private GoogleMap map;
	private TextView tapTextView;
	private QuadTreeNode worldRoot; //helps us to build quadCoordinates
	private int quadDepth;
	private CircleOptions circleOptions;
	private LatLng myPosition;
	private MarkerOptions myMarkerOptions;
	private Marker myMarker;
	private double newRadius;
	private Circle myCircle;

	private float currentZoom;
	private float zoomChanging;
	private LatLng currentTarget;
	private double deltaX;
	private double deltaY;
	private float deltaChanging;
	private Handler handler;
	private ArrayList<ClusterPoint> outputList;
	private Thread clusteringThread;
	//fades all points on map
	private Thread fadingThread;
	//removes unnecessary points from map
	private Thread removingThread;
	private IconGenerator iconGenerator;
	//	private Bitmap backgroundBitmap;
	//	private Bitmap bmOverlay;
	private String TAG = "MapFragment";


	SlidingUpPanelLayout slidingLayout;
	private LinearLayout panelLayout;
	private LinearLayout panelInfoLayout;
	private RelativeLayout panelDragableLayout;
	private LinearLayout likeLayout;
	private int numberOfLikes = 100;

	private SlidingMenu menu;
	private SlidingMenu subMenu;
	private ExpandingListView lvMapDesires;
	private ExpandingListView lvMapSubDesires;

	//declare map sets
	private SatisfySet newSatisfySet;
	//for onMarkerClick() method to identify cluster
	private ConcurrentHashMap<Marker, ClusterPoint> globalClusterHashMap;
	private HashMap<String, MainData> globalMainDataHashMap;
	private DataQuadTreeNode globalDataQuadTree;
	private QuadTreeNodeBox screenBox;
	private HashSet<DesireContent> oldSet;
	private HashSet<DesireContent> newSet;
	//to hold oldSet for recycling sets and as result not creating new objects(sets)
	private HashSet<DesireContent> freeSet;
	private HashSet<DesireContent> addingOnMapSet;

	//to identify current map action
	enum MapAction { ZOOMIN, ZOOMOUT, NOZOOM }
	private MapAction currentMapAction;

	private static final int[] ITEM_DRAWABLES = {R.drawable.info, R.drawable.mail};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.map_layout, container, false);

		slidingLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
		initSlidingPanel();

		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);

		initializeMap();

		initMapSets();

		tapTextView = (TextView) v.findViewById(R.id.tap_text);

		handler = new Handler();

		iconGenerator = new IconGenerator(getActivity());
		iconGenerator.setBackground(getResources().getDrawable(R.drawable.map_marker_bubble_pink_icon));
		iconGenerator.setTextAppearance(R.style.iconGenText);

		clusteringThread = new Thread(new Runnable(){

			private String TAG = "clusteringThread"; 
			private Bitmap icon;
			private Marker clusterMarker;
			@Override
			public void run() {

				//updating by received information from server
				if(newSatisfySet != null){
					switch(currentMapAction){
					case NOZOOM:
						//here merging data quadtrees
						//condition for small performance; adding to larger tree
						if(globalDataQuadTree.count > newSatisfySet.dTree.count){
							globalDataQuadTree.merge(newSatisfySet.dTree);
						} else{
							newSatisfySet.dTree.merge(globalDataQuadTree);
							globalDataQuadTree = newSatisfySet.dTree;
						}
						break;
					case ZOOMIN:
						globalMainDataHashMap.clear();
						globalClusterHashMap.clear();
						//here must be deleting dataquadtree
						globalDataQuadTree = newSatisfySet.dTree;
						break;
					case ZOOMOUT:
						//here merging data quadtrees
						//condition for small performance; adding to larger tree
						if(globalDataQuadTree.count > newSatisfySet.dTree.count){
							globalDataQuadTree.merge(newSatisfySet.dTree);
						} else{
							newSatisfySet.dTree.merge(globalDataQuadTree);
							globalDataQuadTree = newSatisfySet.dTree;
						}
						break;
					}
					globalMainDataHashMap.putAll(newSatisfySet.desireAuthors);
				}

				//understand what points now on the screen
				globalDataQuadTree.getData(globalDataQuadTree, screenBox, freeSet);
				HashSet<DesireContent> tempSet;
				//swap sets newSet => oldSet and received freeSet => newSet
				tempSet = oldSet;
				oldSet = newSet;
				newSet = freeSet;
				freeSet = tempSet;
				freeSet.clear();
				//now we understand what points should be clustered
				addingOnMapSet.clear();
				addingOnMapSet.addAll(newSet);
				switch(currentMapAction){
				case NOZOOM:
					addingOnMapSet.removeAll(oldSet);
					//start clustering algorithm based on deltaSet
					outputList = ClusterizationAlgorithm.cluster(addingOnMapSet, newRadius);
					break;
				case ZOOMIN:
					//just start clustering algorithm on newSet; no need to subtract
					outputList = ClusterizationAlgorithm.cluster(addingOnMapSet, newRadius);
					break;
				case ZOOMOUT:
					addingOnMapSet.removeAll(oldSet);
					//start clustering algorithm based on already calculated globalClusterHashMap
					outputList = ClusterizationAlgorithm.cluster(globalClusterHashMap, addingOnMapSet, newRadius);
					//clear from old cluster points
					globalClusterHashMap.clear();
					break;
				}


				handler.post(new Runnable(){

					@Override
					public void run() {
						for (ClusterPoint cluster : outputList){
							Log.d(TAG, "Here in creating");
							if(cluster.getCount()<100)
								icon = iconGenerator.makeIcon("  "+cluster.getCount());
							else
								icon = iconGenerator.makeIcon(Integer.toString(cluster.getCount()));
							clusterMarker = map.addMarker(new MarkerOptions().position(cluster.getCenter())
									.icon(BitmapDescriptorFactory.fromBitmap(icon))
									.title("Я").snippet("count: "+cluster.getCount()).alpha(0.6f).visible(false));

							globalClusterHashMap.put(clusterMarker, cluster);
							animateCreatingMarker(clusterMarker);
						}
					}

				});

			}

		});

		removingThread = new Thread(new Runnable(){

			@Override
			public void run() {
				for(Marker marker : globalClusterHashMap.keySet()){
					if(!marker.isVisible()){
						globalClusterHashMap.remove(marker);
					}
				}
			}
		});

		fadingThread = new Thread(new Runnable(){

			@Override
			public void run() {
				handler.post(new Runnable(){

					@Override
					public void run() {
						for(Marker marker : globalClusterHashMap.keySet()){
							animateFadingMarker(marker);
						}
					}
				});
			}
		});

		return v;
	}


	private void initSlidingPanel(){
		panelLayout = (LinearLayout) slidingLayout.findViewById(R.id.PanelView);
		panelLayout.setDrawingCacheEnabled(true);


		menu = (SlidingMenu) panelLayout.findViewById(R.id.map_slidingmenu);
		subMenu = (SlidingMenu) menu.findViewById(R.id.map_sub_slidingmenu);
		initSlidingMenus();

		panelInfoLayout = (LinearLayout) panelLayout.findViewById(R.id.PanelInfoView);
		panelInfoLayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Here info appears", Toast.LENGTH_SHORT).show();
			}
		});
		panelDragableLayout = (RelativeLayout) panelLayout.findViewById(R.id.PanelDragableView);

		likeLayout = (LinearLayout) panelLayout.findViewById(R.id.likeLayout);
		likeLayout.setOnClickListener(new OnClickListener(){
			boolean liked = false;
			@Override
			public void onClick(View v) {
				ImageView likeImage = (ImageView) likeLayout.findViewById(R.id.likeImageView);
				TextView likeInfo = (TextView) likeLayout.findViewById(R.id.likeInfoView);
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


		slidingLayout.setCoveredFadeColor(Color.argb(0, 0, 0, 0));
		slidingLayout.setOverlayed(true);
		slidingLayout.setDragView(panelDragableLayout);


		RayMenu rayMenu = (RayMenu) panelLayout.findViewById(R.id.ray_menu);
		rayMenu.setInfoView(panelInfoLayout);
		initRayMenu(rayMenu, ITEM_DRAWABLES);

		//init desires list
		lvMapDesires = (ExpandingListView) menu.findViewById(R.id.lvMapDesires);
		MapCustomAdapter mapAdapter = new MapCustomAdapter(getActivity(), menu);
		lvMapDesires.setAdapter(mapAdapter);
		lvMapDesires.setDivider(null);

		lvMapSubDesires = (ExpandingListView) subMenu.findViewById(R.id.lvMapSubDesires);
		MapCustomAdapter subMapAdapter = new MapCustomAdapter(getActivity(), subMenu, false);
		lvMapSubDesires.setAdapter(subMapAdapter);
		lvMapSubDesires.setDivider(null);

	}


	private void initSlidingMenus(){
		menu.setMode(SlidingMenu.LEFT);
		menu.setShadowWidth(0);
		menu.setFadeDegree(0.0f);
		menu.toggle();

		subMenu.setMode(SlidingMenu.LEFT);
		subMenu.setShadowWidth(0);
		subMenu.setFadeDegree(0.0f);
		subMenu.toggle();

		subMenu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				if(percentOpen == 1.0){
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}
			}
		});

		Button nextBtn = (Button) subMenu.findViewById(R.id.map_nextBtn);
		nextBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				subMenu.toggle(true);
			}});
	}

	private void initRayMenu(RayMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(getActivity());
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
				}
			});
		}
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

	private void initMapSets(){
		QuadTreeNodeBox world = new QuadTreeNodeBox(-90, -180, 90, 180);
		worldRoot = new QuadTreeNode(world, "0", 0);
		globalClusterHashMap = new ConcurrentHashMap<Marker, ClusterPoint>();
		globalMainDataHashMap = new HashMap<String, MainData>();
		globalDataQuadTree = new DataQuadTreeNode();
		screenBox = new QuadTreeNodeBox(0,0,0,0);
		oldSet = new HashSet<DesireContent>();
		newSet = new HashSet<DesireContent>();
		freeSet = new HashSet<DesireContent>();
		addingOnMapSet = new HashSet<DesireContent>();
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
		quadDepth = QuadTreeNode.getQuadDepth(verticalScreenSize);

		zoomChanging = 0;
		currentZoom = map.getCameraPosition().zoom;
		currentTarget = map.getCameraPosition().target;
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
		tapTextView.setText("coordinates:"+coord.latitude+" "+coord.longitude+"\nquad: "+worldRoot.geoPointToQuad(coord.latitude, coord.longitude, quadDepth)+"\nnewRadius: "+newRadius);
	}

	@Override
	public void onCameraChange(CameraPosition updatedPosition) {
		VisibleRegion currentRegion = map.getProjection().getVisibleRegion();
		double verticalScreenSize = currentRegion.latLngBounds.northeast.latitude - currentRegion.latLngBounds.southwest.latitude;

		if(currentTarget != null){
			deltaX += currentTarget.latitude - updatedPosition.target.latitude;
			deltaY += currentTarget.longitude - updatedPosition.target.longitude;
		}
		currentTarget = updatedPosition.target;


		if((currentZoom != updatedPosition.zoom)){
			zoomChanging += updatedPosition.zoom - currentZoom;
			currentZoom = map.getCameraPosition().zoom;
			if(Math.abs(zoomChanging) > 0.7){
				if(zoomChanging > 0){
					currentMapAction = MapAction.ZOOMIN;
				} else currentMapAction = MapAction.ZOOMOUT;

				LatLngBounds currentRegionBounds = currentRegion.latLngBounds;
				//set current screenBox
				screenBox.setBounds(currentRegionBounds.southwest.latitude, currentRegionBounds.southwest.longitude,
						            currentRegionBounds.northeast.latitude, currentRegionBounds.northeast.longitude);
				
				sendToServerTilesPack(currentRegion.latLngBounds, verticalScreenSize);

				zoomChanging = 0;
				deltaX = 0;
				deltaY = 0;
				newRadius = SphericalUtil.computeDistanceBetween(currentRegion.latLngBounds.northeast, currentRegion.latLngBounds.southwest)/10;

				circleOptions = new CircleOptions().center(myPosition).fillColor(0x550099FF).radius(newRadius).strokeWidth(0);
				if(myCircle != null)
					myCircle.remove();
				myCircle = map.addCircle(circleOptions);

				return;
			}
		}

		if(Math.abs(deltaX) + Math.abs(deltaY) > verticalScreenSize/3){
			currentMapAction = MapAction.NOZOOM;
			sendToServerTilesPack(currentRegion.latLngBounds, verticalScreenSize);
			deltaX = 0;
			deltaY = 0;
		}

	}

	private void sendToServerTilesPack(LatLngBounds screenBounds, double verticalScreenSize){
		quadDepth = QuadTreeNode.getQuadDepth(verticalScreenSize);
		HashSet<String> tiles = worldRoot.getMapTiles(screenBounds.southwest, screenBounds.northeast, quadDepth);
		getSatisfyDesires(null, tiles, quadDepth); //add here category
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


	private void getSatisfyDesires(final String desireID, final HashSet<String> tiles, final int tileDepth){
		new AsyncTask<Void, Void, SatisfySet>(){
			@Override
			protected SatisfySet doInBackground(Void... params) {
				try {
					return Client.getSatisfyDesires(desireID, tiles, tileDepth);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(SatisfySet resultSet) {
				newSatisfySet = resultSet;
				if(currentMapAction == MapAction.NOZOOM){
					removingThread.run();
				} else fadingThread.run();
				clusteringThread.run();

			}

		}.execute();
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
