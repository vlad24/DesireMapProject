package logic;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	private final Context appContext;
	private LocationManager manager;
	private boolean isNetworkEnabled;
	private boolean isGPSEnabled;
	private boolean canGetLocation;
	private Location location;
	private Location bestLocation;
	private double latitude;
	private double longitude;
	private final int MIN_DISTANCE = 10;
	private final int MIN_TIME = 2000;
	private float accuracy;
	private final String TAG = "GPSTracker";
	private static GPSTracker gps;

	private GPSTracker(Context context){
		appContext = context;
		isNetworkEnabled = false;
		isGPSEnabled = false;
		canGetLocation = false;
		accuracy = 200;
	}
	
	public static GPSTracker getInstance(Context context){
		if(gps == null)
			gps = new GPSTracker(context);
		
		return gps;
	}

	public void getLocation(){
		try{
			bestLocation = null;
			manager = (LocationManager) appContext.getSystemService(LOCATION_SERVICE);

			if(manager != null){
				isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

				if(isNetworkEnabled){
					canGetLocation = true;
					for(int i = 0; i<20;i++){
						manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, MIN_DISTANCE, this);
						location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location != null){
							if(location.hasAccuracy()&&(location.getAccuracy()<accuracy)){
								bestLocation = location;
								accuracy = location.getAccuracy();
							}
						}
					}
					if(bestLocation != null){
						latitude = bestLocation.getLatitude();
						longitude = bestLocation.getLongitude();
					}
				}
				else if(isGPSEnabled){
					canGetLocation = true;
					manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
					location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if(location != null){
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				} 
				else canGetLocation = false;

			} else canGetLocation = false;
		} catch (Exception e){
			Log.d(TAG, "Exception in GPSTracker");
			e.printStackTrace();
		}
		if(bestLocation == null)
			canGetLocation = false;
		
		}


	public double getLatitude(){
		return latitude;
	}


	public double getLongitude(){
		return longitude;
	}


	public void stopUsingGPS(){
		if(manager != null)
			manager.removeUpdates(this);
	}


	public boolean canGetLocation(){
		return canGetLocation;
	}


	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(appContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		//alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				appContext.startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
