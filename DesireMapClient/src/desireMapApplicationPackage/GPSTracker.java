package desireMapApplicationPackage;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	private final Context appContext;
	private LocationManager manager;
	private WifiManager wifi;
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
	private int count;
	private final String TAG = "GPSTracker";

	public GPSTracker(Context context){
		appContext = context;
		isNetworkEnabled = false;
		isGPSEnabled = false;
		canGetLocation = false;
		accuracy = 200;
		count = 0;
	}


	public Location getLocation(){
		try{
			manager = (LocationManager) appContext.getSystemService(LOCATION_SERVICE);
			wifi = (WifiManager) appContext.getSystemService(WIFI_SERVICE);

			if(manager != null){
				isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

				if(isNetworkEnabled && wifi.isWifiEnabled()){
					canGetLocation = true;
					manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
					location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					Log.d(TAG,"accuracy = "+location.getAccuracy() + " latitude ="+location.getLatitude()+" longitude = "+location.getLongitude());
					if(location != null){
						if(location.hasAccuracy()&&(location.getAccuracy()<accuracy)){
							bestLocation = location;
							accuracy = location.getAccuracy();
						}
					}
					if(bestLocation != null){
						latitude = bestLocation.getLatitude();
						longitude = bestLocation.getLongitude();
					}
				}
				else if(isGPSEnabled){
					canGetLocation = true;
					manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
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

		return bestLocation;
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
		AlertDialog.Builder LocationAlertDialog = new AlertDialog.Builder(appContext);

		// Setting Dialog Title
		LocationAlertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		LocationAlertDialog.setMessage("GPS permission is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		//LocationAlertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		LocationAlertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				appContext.startActivity(intent);
			}
		});


		AlertDialog.Builder WiFiAlertDialog = new AlertDialog.Builder(appContext);

		// Setting Dialog Title
		WiFiAlertDialog.setTitle("Wi-Fi settings");

		// Setting Dialog Message
		WiFiAlertDialog.setMessage("Wi-Fi is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		//LocationAlertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		WiFiAlertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				appContext.startActivity(intent);
			}
		});


		// on pressing cancel button
		WiFiAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		if(!wifi.isWifiEnabled())
			WiFiAlertDialog.show();
		else
			LocationAlertDialog.show();
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {

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
