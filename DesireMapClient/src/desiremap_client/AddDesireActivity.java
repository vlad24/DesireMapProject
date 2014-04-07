package desiremap_client;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import desireMapApplicationPackage.Client;
import desireMapApplicationPackage.GPSTracker;
import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;

public class AddDesireActivity extends Activity implements OnClickListener {

	private Button bMainMenu;
	private Button bSendDesire;
	private EditText newDesire;
	private TextView waiting;
	private ProgressBar prBar;
	private RelativeLayout progressLayout;

	private boolean isCorrect;
	private GPSTracker gps;
	private int category;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_desire);
		gps = new GPSTracker(this);

		prBar = new ProgressBar(this);
		waiting = new TextView(this);
		bMainMenu = (Button) findViewById(R.id.bMainMenu);
		bSendDesire = (Button) findViewById(R.id.bSendDesire);
		newDesire = (EditText) findViewById(R.id.newDesire);
		bMainMenu.setOnClickListener(this);
		bSendDesire.setOnClickListener(this);
		progressLayout = (RelativeLayout) findViewById(R.id.addDesireRelativeLayout);


		Intent intent = getIntent();
		category = intent.getIntExtra("category", -1);
	}


	public class GetGPSTask extends AsyncTask<Void, Void, Void> {

		private String TAG = "GettaskGPS";
		private GPSTracker taskGPS;
		GetGPSTask(GPSTracker gps){
			taskGPS = gps;
		}

		protected void onProgressUpdate(Exception... error){
			bSendDesire.setEnabled(true);
			newDesire.setEnabled(true);
			ProgressBarClient.hideProgressBar(prBar, waiting);
			Toast.makeText(AddDesireActivity.this,"Возникли проблемы", Toast.LENGTH_SHORT).show();
		}

		protected void onPreExecute(){
			Log.d(TAG, "before taskGPS.getLocation() in onPreExecute");
			bSendDesire.setEnabled(false);
			newDesire.setEnabled(false);
			ProgressBarClient.showProgressBar(AddDesireActivity.this, progressLayout, prBar, waiting);
			Log.d(TAG, "before taskGPS.getLocation() in onPreExecute 2");
		}


		protected Void doInBackground(Void... params) {
			Log.d(TAG, "before taskGPS.getLocation() in doInBackground");
			try{
				if(Looper.myLooper() == null)
					Looper.prepare();
				for(int i = 0; i < 10; i++){
					taskGPS.getLocation();
					TimeUnit.MILLISECONDS.sleep(200);
				}

			}catch(Exception error){
				error.printStackTrace();
				onProgressUpdate(error);
			}
			return null;
		}


		protected void onPostExecute(Void result){
			Log.d(TAG, "after taskGPS.getLocation() in onPostExecute");
			taskGPS.stopUsingGPS();
		}
	}


	public class AddDesireTask extends AsyncTask<Void, Void, Boolean> {	

		private String TAG = "AddDesireTask";
		DesireContent desireContent;

		public AddDesireTask(DesireContent newContent){
			desireContent = newContent;
		}

		protected void onProgressUpdate(Exception... error){
			bSendDesire.setEnabled(true);
			newDesire.setEnabled(true);
			ProgressBarClient.hideProgressBar(prBar, waiting);			
			Toast.makeText(AddDesireActivity.this,"Возникли проблемы", Toast.LENGTH_SHORT).show();
		}

		protected void onPreExecute(){
			Log.d(TAG, "before Client.sendDesire() in onPreExecute");
		}


		protected Boolean doInBackground(Void... params) {

			try{
				Log.d(TAG, "before Client.sendDesire() in doInBackground");
				return Client.sendDesire(desireContent);
			}catch(Exception error){
				onProgressUpdate(error);
				return false;
			}
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.sendDesire()");
			isCorrect = result;
			if(isCorrect){
				Toast.makeText(AddDesireActivity.this, "Желание добавлено", Toast.LENGTH_SHORT).show();
				ProgressBarClient.hideProgressBar(prBar, waiting);
			}
			else{
				bSendDesire.setEnabled(true);
				newDesire.setEnabled(true);
				ProgressBarClient.hideProgressBar(prBar, waiting);
				Toast.makeText(AddDesireActivity.this, "Возникли проблемы", Toast.LENGTH_SHORT).show();
			}
		}
	}


	public class GetSatisfiersTask extends AsyncTask<Void, Void, SatisfySet> {

		private String TAG = "GetSatisfiersTask";
		private Coordinates coord;
		private Cryteria cryteria;
		
		public GetSatisfiersTask(Coordinates newCoord, Cryteria newCryteria){
			coord = newCoord;
			cryteria = newCryteria;
		}

		protected void onProgressUpdate(Exception... error){
			bSendDesire.setEnabled(true);
			newDesire.setEnabled(true);
			ProgressBarClient.hideProgressBar(prBar, waiting);			
			Toast.makeText(AddDesireActivity.this,"Возникли проблемы", Toast.LENGTH_SHORT).show();
		}

		protected void onPreExecute(){
			Log.d(TAG, "before Client.getInformation() in onPreExecute");
		}


		protected SatisfySet doInBackground(Void... params) {
			Log.d(TAG, "before Client.getInformation() in doInBackground");
			try{
				return Client.getSatisfyDesires(coord, cryteria);
			}catch(Exception error){
				onProgressUpdate(error);
				return null;
			}
		}

		protected void onPostExecute(SatisfySet result){
			Log.d(TAG, "after Client.getInformation() in onPostExecute");

			if((result != null)&&(result.dSet.size()>0)){
				Intent mapIntent = new Intent(AddDesireActivity.this, MapActivity.class);
				Log.d(TAG, "after creating mapIntent");
				mapIntent.putExtra("set", (Serializable)result);
				mapIntent.putExtra("myLatitude", gps.getLatitude());
				mapIntent.putExtra("myLongitude", gps.getLongitude());
				startActivity(mapIntent);
			}else{
				bSendDesire.setEnabled(true);
				newDesire.setEnabled(true);
				ProgressBarClient.hideProgressBar(prBar, waiting);
				Toast.makeText(AddDesireActivity.this, "Не найдены соответствия", Toast.LENGTH_SHORT).show();
			}
			
			bSendDesire.setEnabled(true);
			newDesire.setEnabled(true);
			ProgressBarClient.hideProgressBar(prBar, waiting);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bMainMenu:
			Intent menuIntent = new Intent(this, DesireMenuActivity.class);
			menuIntent.putExtra("login", Client.getName());
			startActivity(menuIntent);
			break;
		case R.id.bSendDesire:
			KeyboardClient.hideKeyboard(this);
			GetGPSTask gpsTask = new GetGPSTask(gps);
			gpsTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
			
			DesireContent newContent = null;
			Coordinates coord = new Coordinates(gps.getLatitude(), gps.getLongitude());
			switch(category){
			case CodesMaster.Categories.SportCode:
				newContent = new DesireContentSport(null, 0, newDesire.getText().toString(), coord, "football", 5);
				break;
			case CodesMaster.Categories.DatingCode:
				newContent = new DesireContentDating(null, 0, newDesire.getText().toString(), coord, 'W', 21);
				break;
			}
			
			AddDesireTask addTask = new AddDesireTask(newContent);
			addTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

		}
	}
}
