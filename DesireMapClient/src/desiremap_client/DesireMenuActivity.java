package desiremap_client;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import desireMapApplicationPackage.Client;

public class DesireMenuActivity extends Activity implements OnClickListener {

	private Button bAddDesire;
	private Button bMyDesires;
	private Button bInfo;
	private Button bExit;
	private TextView login;
	private Boolean isCorrect;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.desire_menu);
		bAddDesire = (Button) findViewById(R.id.bAddDesire);
		bMyDesires = (Button) findViewById(R.id.bMyDesires);
		bInfo = (Button) findViewById(R.id.bInfo);
		bExit = (Button) findViewById(R.id.bExit);
		login = (TextView) findViewById(R.id.login);
		bAddDesire.setOnClickListener(this);
		bMyDesires.setOnClickListener(this);
		bInfo.setOnClickListener(this);
		bExit.setOnClickListener(this);

		Intent intent = getIntent();
		login.setText(intent.getStringExtra("login"));
	}
	
	
	public class ExitTask extends AsyncTask<String, Void, Boolean> {
		
		private String TAG = "ExitTask";

		protected void onPreExecute(){
		//	bExit.setVisibility(View.INVISIBLE);
			Log.d(TAG, "before Cleint.exit() in onPreExecute");
		}


		protected Boolean doInBackground(String... params) {
			Log.d(TAG, "before Client.exit() in doInBackground");
			try {
				return Client.exit();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.exit()");
			bExit.setVisibility(View.VISIBLE);
		}


	}
	
	private void exiting(){
		ExitTask task = new ExitTask();
		task.execute();
		
		try {
			isCorrect = task.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(isCorrect){
			Client.closeSocket();
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else Toast.makeText(this, "Can't exit", Toast.LENGTH_SHORT).show();
	}


	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.bAddDesire:
			intent = new Intent(this, AddDesireCategoryActivity.class);
			startActivity(intent);
			break;
		case R.id.bMyDesires:
			intent = new Intent(this, MyDesiresCategoryActivity.class);
			startActivity(intent);
			break;
		case R.id.bInfo:
			intent = new Intent(this, PersonalInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.bExit:
			exiting();
		}
	}
	
	
	public void onBackPressed(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Exit");
		dialog.setMessage("Are you sure want to exit?");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				exiting();
				Intent intent = new Intent(DesireMenuActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		dialog.show();
	}
}
