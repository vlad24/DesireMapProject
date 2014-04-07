package desiremap_client;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;

public class MapActivity extends Activity {

	private MapView map;
	private MapController mapController;
	private OverlayManager mOverlayManager;
	private SatisfySet setOfSatisfiers;
	private GeoPoint[] geoPoint;
	private OverlayItem[] overlayItem;
	private BalloonItem[] balloonItem;
	private double myLatitude;
	private double myLongitude;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_satisfiers);
		Intent intent = getIntent();
		setOfSatisfiers = (SatisfySet) intent.getSerializableExtra("set");
		myLatitude = intent.getDoubleExtra("myLatitude", 0);
		myLongitude = intent.getDoubleExtra("myLongitude", 0);

		geoPoint = new GeoPoint[setOfSatisfiers.dSet.size()];
		overlayItem = new OverlayItem[setOfSatisfiers.dSet.size()];
		balloonItem = new BalloonItem[setOfSatisfiers.dSet.size()];

		map = (MapView)findViewById(R.id.map);
		mapController = map.getMapController();
		mOverlayManager = mapController.getOverlayManager();

		Resources res = getResources();

		Overlay overlay = new Overlay(mapController);

		Coordinates coord;
		DesireContent desire;

		for(int i = 0; i < setOfSatisfiers.dSet.size(); i++){
				desire = setOfSatisfiers.dSet.get(i);
				coord =  desire.coordinates;
				
				geoPoint[i] = new GeoPoint(coord.latitude, coord.longitude);
				overlayItem[i] = new OverlayItem(geoPoint[i], res.getDrawable(R.drawable.red_small));
				balloonItem[i] = new BalloonItem(this, geoPoint[i]);
				balloonItem[i].setText("Ник: "+desire.login+"\nЖелание: "+desire.description);

				overlayItem[i].setBalloonItem(balloonItem[i]);
				overlay.addOverlayItem(overlayItem[i]);		
		}

		GeoPoint myPoint = new GeoPoint(myLatitude, myLongitude);
		OverlayItem myItem = new OverlayItem(myPoint, res.getDrawable(R.drawable.shop));
		BalloonItem myBalloon = new BalloonItem(this, myPoint);
		myBalloon.setText("Я");
		myItem.setBalloonItem(myBalloon);
		overlay.addOverlayItem(myItem);

		mapController.setPositionAnimationTo(myPoint);

		mOverlayManager.addOverlay(overlay);


	}
}
