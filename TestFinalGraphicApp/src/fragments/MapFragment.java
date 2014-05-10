package fragments;

import graphics.map.MapCustomAdapter;
import graphics.map.expand_graphic.ExpandingListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



import logic.Client;
import logic.GPSTracker;
import logic.clusterization.ClusterPoint;
import logic.clusterization.ClusterizationAlgorithm;

import com.capricorn.RayMenu;
import com.example.testfinalgraphicapp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	private LatLng myPosition;
	private MarkerOptions myMarkerOptions;
	private Marker myMarker;
	private double newRadius;
	private GPSTracker gps;

	private float currentZoom;
	private float zoomChanging;
	private LatLng currentTarget;
	private double deltaX;
	private double deltaY;
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
	private MapCustomAdapter mapAdapter;
	private MapCustomAdapter subMapAdapter;
	private ArrayList<DesireContent> listMapData;
	private ArrayList<DesireContent> subListMapData;
	//current selected login
	private String currentLogin;

	private static final int[] ITEM_DRAWABLES = {R.drawable.info, R.drawable.mail};

	//declare map sets
	private SatisfySet newSatisfySet;
	//indicates if firstSending category
	private boolean isFirstSendingCategory = true;
	//for onMarkerClick() method to identify cluster
	private ConcurrentHashMap<Marker, ClusterPoint> globalClusterHashMap;
	private HashMap<String, MainData> globalMainDataHashMap;
	public static HashSet<String> globalLikedByUser = new HashSet<String>();
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


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.map_layout, container, false);

		slidingLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
		initSlidingPanel();

		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);

		initMapSets();

		tapTextView = (TextView) v.findViewById(R.id.tap_text);

		handler = new Handler();

		iconGenerator = new IconGenerator(getActivity());
		iconGenerator.setBackground(getResources().getDrawable(R.drawable.map_marker_bubble_pink_icon));
		iconGenerator.setTextAppearance(R.style.iconGenText);

		clusteringThread = new Thread(new Runnable(){

			private String TAG = "clusteringThread"; 
			private Bitmap icon;
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
						globalLikedByUser.clear();
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
					globalLikedByUser.addAll(newSatisfySet.likedByUser);
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
					Log.d("ClientTag", "newSet size = "+newSet.size());
					Log.d("ClientTag", "oldSet size = "+oldSet.size());
					addingOnMapSet.removeAll(oldSet);
					Log.d("ClientTag", "AddingOnMapSet size = "+addingOnMapSet.size());
					for(DesireContent desire : addingOnMapSet){
						Coordinates coord =desire.coordinates;
						Log.d("ClientTag", "AddingOnMapSet desire coord = "+coord.latitude+" "+coord.longitude);
					}
					//start clustering algorithm based on deltaSet
					outputList = ClusterizationAlgorithm.cluster(addingOnMapSet, newRadius);
					break;
				case ZOOMIN:
					fadingThread.run();
					Log.d("ClientTag", "AddingOnMapSet size = "+addingOnMapSet.size());
					for(DesireContent desire : addingOnMapSet){
						Coordinates coord =desire.coordinates;
						Log.d("ClientTag", "AddingOnMapSet desire coord = "+coord.latitude+" "+coord.longitude);
					}
					//just start clustering algorithm on newSet; no need to subtract
					outputList = ClusterizationAlgorithm.cluster(addingOnMapSet, newRadius);
					break;
				case ZOOMOUT:
					addingOnMapSet.removeAll(oldSet);
					Log.d("ClientTag", "AddingOnMapSet size = "+addingOnMapSet.size());
					//start clustering algorithm based on already calculated globalClusterHashMap
					outputList = ClusterizationAlgorithm.cluster(globalClusterHashMap, addingOnMapSet, newRadius);

					//delete old markers from map
					fadingThread.run();

					break;
				}


				handler.post(new Runnable(){

					@Override
					public void run() {
						Log.d("ClientTag", "outputlist size = "+outputList.size());
						for (ClusterPoint cluster : outputList){
							Log.d("ClientTag", "Here in creating");
							if(cluster.getCount()<100)
								icon = iconGenerator.makeIcon("  "+cluster.getCount());
							else
								icon = iconGenerator.makeIcon(Integer.toString(cluster.getCount()));
							Log.d("ClientTag", "cluster: x: "+cluster.xCenter*1e-5+" y: "+cluster.yCenter*1e-5);
							Marker clusterMarker = map.addMarker(new MarkerOptions().position(cluster.getCenter())
									.icon(BitmapDescriptorFactory.fromBitmap(icon))
									.title("Я").snippet("count: "+cluster.getCount()).alpha(0.6f).visible(false));

							Log.d("ClientTag", "globalClusterHashMap before size ="+globalClusterHashMap.size());

							globalClusterHashMap.put(clusterMarker, cluster);

							Log.d("ClientTag", "globalClusterHashMap size ="+globalClusterHashMap.size());
							animateCreatingMarker(clusterMarker);
						}
					}

				});

			}

		});

		removingThread = new Thread(new Runnable(){

			@Override
			public void run() {
				final VisibleRegion currentRegion = map.getProjection().getVisibleRegion();
				final Set<Marker> markerSet = globalClusterHashMap.keySet();
				for(Marker marker : markerSet){
					if(!currentRegion.latLngBounds.contains(marker.getPosition())){
						globalClusterHashMap.remove(marker);
						marker.remove();
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
						Log.d("ClientTag", "fadingThread started");
						if(globalClusterHashMap.isEmpty())
							Log.d("ClientTag", "globalClusterHashMap is empty");

						final Set<Marker> removingSet = globalClusterHashMap.keySet(); 
						Log.d("ClientTag", "fadingThread removingSet size ="+removingSet.size());
						for(Marker marker : removingSet){
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
		mapAdapter = new MapCustomAdapter(getActivity(), menu);
		lvMapDesires.setAdapter(mapAdapter);
		lvMapDesires.setDivider(null);

		lvMapSubDesires = (ExpandingListView) subMenu.findViewById(R.id.lvMapSubDesires);
		subMapAdapter = new MapCustomAdapter(getActivity(), subMenu, false);
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

		subListMapData = new ArrayList<DesireContent>();

		final TextView loginInfo = (TextView) subMenu.findViewById(R.id.map_loginInfo);
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
			Toast.makeText(getActivity(), "Cant't access map", Toast.LENGTH_SHORT).show();
		}

		//			// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		try {
			MapsInitializer.initialize(this.getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}

		map.clear();
		zoomChanging = 0;
		deltaX = 0;
		deltaY = 0;
		currentZoom = map.getCameraPosition().zoom;
		//remove old my marker
		if(myMarker != null){
			myMarker.remove();
		}

		gps = GPSTracker.getInstance(getActivity());
		gps.getLocation();
		myPosition = new LatLng(gps.getLatitude(), gps.getLongitude());
		myMarkerOptions = new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_ball_azure_icon)).title("Я").snippet("красавчик ваще :)").alpha(0.8f);
		myMarker = map.addMarker(myMarkerOptions);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));

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


	public void changeLoginContent(String login){
		if(currentLogin != null && !currentLogin.equals(login)){
			final TextView loginInfo = (TextView) subMenu.findViewById(R.id.map_loginInfo);
			final TextView nameInfo = (TextView) subMenu.findViewById(R.id.map_nameInfo);
			final TextView ageInfo = (TextView) subMenu.findViewById(R.id.map_ageInfo);
			final ImageView maleInfo = (ImageView) subMenu.findViewById(R.id.map_maleInfo);
			final TextView allDesiresInfo = (TextView) subMenu.findViewById(R.id.map_allDesiresInfo);
			//get main data linked to login name
			MainData loginData = globalMainDataHashMap.get(login);

			loginInfo.setText(login);
			nameInfo.setText(loginData.name);
			ageInfo.setText(loginData.birth);
			if(loginData.sex == 'M')
				maleInfo.setImageResource(R.drawable.user_male32);
			else
				maleInfo.setImageResource(R.drawable.user_female32);

			//generate new sublist data
			subListMapData.clear();
			//get data from main list of desires
			listMapData = mapAdapter.getData();

			String loginInfoString = loginInfo.getText().toString();
			for(DesireContent desire : listMapData){
				if(loginInfoString.equals(desire.login))
					subListMapData.add(desire);
			}

			//set number of current login's desires in cluster
			allDesiresInfo.setText(Integer.toString(subListMapData.size()));

			//refresh data in list
			subMapAdapter.changeData(subListMapData);
			currentLogin = login;
		}

	}

	@Override
	public void onMapClick(LatLng coord) {
		tapTextView.setText("coordinates:"+coord.latitude+" "+coord.longitude+"\nquad: "+worldRoot.geoPointToQuad(coord.latitude, coord.longitude, quadDepth)+"\nnewRadius: "+newRadius);
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		//define what cluster was clicked
		ClusterPoint clusterPoint = globalClusterHashMap.get(marker);
		//refresh data in list
		if(clusterPoint == null){
			Log.d("ClientTag", "clusterPoint null");
		}
		if(clusterPoint != null){
			mapAdapter.changeData(clusterPoint.points);
			Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
					R.animator.bottom_up);
			panelLayout.startAnimation(bottomUp);
			panelLayout.setVisibility(View.VISIBLE);
		}
		return true;
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

				return;
			}
		}

		if(Math.abs(deltaX) + Math.abs(deltaY) > verticalScreenSize/3){
			currentMapAction = MapAction.NOZOOM;

			LatLngBounds currentRegionBounds = currentRegion.latLngBounds;
			//set current screenBox
			screenBox.setBounds(currentRegionBounds.southwest.latitude, currentRegionBounds.southwest.longitude,
					currentRegionBounds.northeast.latitude, currentRegionBounds.northeast.longitude);
			sendToServerTilesPack(currentRegion.latLngBounds, verticalScreenSize);

			deltaX = 0;
			deltaY = 0;
		}

	}

	private void sendToServerTilesPack(LatLngBounds screenBounds, double verticalScreenSize){
		quadDepth = QuadTreeNode.getQuadDepth(verticalScreenSize);
		HashSet<String> tiles = worldRoot.getMapTiles(screenBounds.southwest, screenBounds.northeast, quadDepth);
		getSatisfyDesires(CodesMaster.Categories.DatingCode, tiles, quadDepth); //add here category
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



	//	private void getSatisfyDesires(final String desireID, final HashSet<String> tiles, final int tileDepth){
	//		new AsyncTask<Void, Void, SatisfySet>(){
	//			@Override
	//			protected SatisfySet doInBackground(Void... params) {
	//				try {
	//					return Client.getSatisfyDesires(desireID, tiles, tileDepth);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//					return null;
	//				}
	//			}
	//
	//			@Override
	//			protected void onPostExecute(SatisfySet resultSet) {
	//				newSatisfySet = resultSet;
	//				if(currentMapAction == MapAction.NOZOOM){
	//					removingThread.run();
	//				} else fadingThread.run();
	//				clusteringThread.run();
	//
	//			}
	//
	//		}.execute();
	//	}

	private void getSatisfyDesires(final int category, final HashSet<String> tiles, final int tileDepth){
		new AsyncTask<Void, Void, SatisfySet>(){
			@Override
			protected SatisfySet doInBackground(Void... params) {
				try {
					if(isFirstSendingCategory){
						isFirstSendingCategory = false;
						return Client.getSatisfyDesires(category, tiles, tileDepth);
					}else
						return Client.getSatisfyDesires(tiles, tileDepth);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(SatisfySet resultSet) {
				newSatisfySet = resultSet;
				if(resultSet != null){
					Log.d("ClientTag", "size of SatisfySet authors ="+resultSet.desireAuthors.size());
				}

				if(currentMapAction == MapAction.NOZOOM){
					removingThread.run();
				}

				clusteringThread.run();

			}

		}.execute();
	}

	@Override
	public void onStart() {
		initializeMap();
		super.onStart();
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
