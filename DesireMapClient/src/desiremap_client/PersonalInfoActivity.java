package desiremap_client;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import desireMapApplicationPackage.Client;
import desireMapApplicationPackage.userDataPackage.MainData;

public class PersonalInfoActivity extends Activity implements OnClickListener {
	
	Button bMainMenu;
	TextView Info;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		bMainMenu = (Button) findViewById(R.id.bMainMenu);
		bMainMenu.setOnClickListener(this);
		Info = (TextView) findViewById(R.id.Info);
		ShowInfoTask task = new ShowInfoTask();
		task.execute();
		try {
			MainData personInfoSet = task.get();
			Info.append("\nИмя: "+personInfoSet.name);
			Info.append("\nПол: "+personInfoSet.sex);
			Info.append("\nДата Рождения: "+personInfoSet.birth);
			Info.append("\nРейтинг: "+personInfoSet.rating);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class ShowInfoTask extends AsyncTask<Void, Void, MainData> {

		private String TAG = "showInfoTask";

		protected void onPreExecute(){
			Log.d(TAG, "before Client.getPersonalInfo() in onPreExecute");
		}

		protected MainData doInBackground(Void... params) {
			Log.d(TAG, "before Client.getPersonalInfo() in doInBackground");
			try {
				return Client.getPersonalInfo();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.getPersonalInfo()");
		}
	}

	@Override
	public void onClick(View v) {
	   Intent intent = new Intent(this, DesireMenuActivity.class);
	   startActivity(intent);
	}
}
