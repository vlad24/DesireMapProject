package com.example.mapshower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button bShow;
	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		bShow = (Button) findViewById(R.id.bShow);
		bShow.setOnClickListener(this);
		gps = new GPSTracker(this);
	}

	@Override
	public void onClick(View v) {
		gps.getLocation();
		if(gps.canGetLocation()){
            Toast.makeText(this, "Latitude: "+gps.getLatitude()+"\nLongitude: "+gps.getLongitude(), Toast.LENGTH_LONG).show();
//			Uri uri = Uri.parse(String.format("geo:"+gps.getLatitude()+","+gps.getLongitude()));
//			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//			startActivity(intent);
			Intent intent = new Intent(this, GoogleMapActivity.class);
			intent.putExtra("latitude", gps.getLatitude());
			intent.putExtra("longitude", gps.getLongitude());
			startActivity(intent);
		}else
			Toast.makeText(this, "Can't get location", Toast.LENGTH_SHORT).show();
	}

}
