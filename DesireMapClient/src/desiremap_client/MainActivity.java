package desiremap_client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import desireMapApplicationPackage.Client;



public class MainActivity extends Activity implements OnClickListener {

	private Button bEnter;
	private EditText personName;
	private EditText password;
	private EditText ipConfig;
	private TextView registration;
	private TextView waiting;
	private ProgressBar prBar;
	private RelativeLayout progressLayout;
	private boolean isCorrect;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		progressLayout = (RelativeLayout) findViewById(R.id.enterRelativeLayout);
		prBar = new ProgressBar(MainActivity.this);
		waiting = new TextView(MainActivity.this);

		bEnter = (Button) findViewById(R.id.bMyDesires);
		bEnter.setOnClickListener(this);

		personName = (EditText)findViewById(R.id.personName);
		password = (EditText)findViewById(R.id.password);
		ipConfig = (EditText)findViewById(R.id.ipConfig);
		ipConfig.setText(Client.getIP());

		registration = (TextView) findViewById(R.id.registration);
		registration.setOnClickListener(this);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public class LoginTask extends AsyncTask<String, Exception, Boolean> {
		private String TAG = "AsyncTask";

		protected void onProgressUpdate(Exception... error){
			personName.setEnabled(true);
			password.setEnabled(true);
			bEnter.setEnabled(true);
			ProgressBarClient.hideProgressBar(prBar, waiting);

			Toast.makeText(MainActivity.this,"Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
		}

		protected void onPreExecute(){
			Log.d(TAG, "before Client.sendLogin() in onPreExecute");
			personName.setEnabled(false);
			password.setEnabled(false);
			bEnter.setEnabled(false);
			ProgressBarClient.showProgressBar(MainActivity.this, progressLayout, prBar, waiting);
		}


		protected Boolean doInBackground(String... params) {
			Log.d(TAG, "before Client.sendLogin() in doInBackground");
			try {
				return Client.sendLogin(params[0], params[1]);
			} catch (Exception error) {
				publishProgress(error);
				Client.closeSocket();
				return false;
			}
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.sendLogin()");
			isCorrect = result;

			if(isCorrect){
				Client.setName(personName.getText().toString());
				Intent intent = new Intent(MainActivity.this, DesireMenuActivity.class);
				intent.putExtra("login", Client.getName());
				startActivity(intent);
			}else{
				personName.setEnabled(true);
				password.setEnabled(true);
				bEnter.setEnabled(true);
				ProgressBarClient.hideProgressBar(prBar, waiting);
				Toast.makeText(MainActivity.this, "Wrong login or password", Toast.LENGTH_SHORT).show();
			}
		}


	}


	@Override
	public void onClick(View v) {
		Client.changeIP(ipConfig.getText().toString());
		switch(v.getId()){
		case(R.id.bMyDesires):{
			KeyboardClient.hideKeyboard(this);
			LoginTask task = new LoginTask();
			task.execute(personName.getText().toString(), password.getText().toString());
			break;
		}
		case(R.id.registration):{
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			break;
		}
		}
	}

}
