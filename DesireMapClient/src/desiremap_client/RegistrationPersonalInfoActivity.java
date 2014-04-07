package desiremap_client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import desireMapApplicationPackage.Client;



public class RegistrationPersonalInfoActivity extends Activity implements OnClickListener{

	private Button bRegister;
	private EditText personName;
	private EditText birthdate;
	private ProgressBar prBar;
	private TextView waiting;
	private RelativeLayout progressLayout;

	private boolean isCorrect;
	private String login;
	private String password;
	private char male;


	private String TAG = "RegistrationTask";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_personal_info);
		bRegister = (Button) findViewById(R.id.bRegister);
		bRegister.setOnClickListener(this);
		personName = (EditText) findViewById(R.id.person_name);
		birthdate = (EditText) findViewById(R.id.birthdate);
		progressLayout = (RelativeLayout) findViewById(R.id.registrationRelativeLayout);
		prBar = new ProgressBar(RegistrationPersonalInfoActivity.this);
		waiting = new TextView(RegistrationPersonalInfoActivity.this);

		male = 'M';

		Intent intent = getIntent();
		login = intent.getStringExtra("login");
		password = intent.getStringExtra("password");
	}

	public class RegistrationTask extends AsyncTask<String, Exception, Boolean> {

		protected void onProgressUpdate(Exception... error){
			super.onProgressUpdate(error);
			Toast.makeText(RegistrationPersonalInfoActivity.this, "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
		}

		protected void onPreExecute(){
			Log.d(TAG, "before Client.sendRegistration() in onPreExecute");
			bRegister.setEnabled(false);
			ProgressBarClient.showProgressBar(RegistrationPersonalInfoActivity.this, progressLayout, prBar, waiting);
		}


		protected Boolean doInBackground(String... params){
			Log.d(TAG, "before Client.sendRegistration() in doInBackground");
			try{
				return Client.sendRegistration(params[0], params[1], params[2], male, params[3]);
			} catch(Exception error){
				publishProgress(error);
				Client.closeSocket();
				return false;
			}
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.sendRegistration() onPostExecute");
			bRegister.setEnabled(true);
			ProgressBarClient.hideProgressBar(prBar, waiting);
		}


	}


	public void onRadioButtonClick(View v) {
		RadioButton button = (RadioButton) v;
		male = button.getText().toString().charAt(0);
	}

	@Override
	public void onClick(View v) {
		KeyboardClient.hideKeyboard(this);
		Log.d(TAG, "try to start");
		RegistrationTask task = new RegistrationTask();
		Log.d("RegistrationTask", "try to execute");
		task.execute(login, password, personName.getText().toString(), birthdate.getText().toString());

		try {
			isCorrect = task.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(isCorrect){
			Client.setName(personName.getText().toString());
			Intent intent = new Intent(this, DesireMenuActivity.class);
			intent.putExtra("login", Client.getName());
			startActivity(intent);
		}else{
			Toast.makeText(this, "Wrong Information", Toast.LENGTH_SHORT).show();
		}
	}

}
