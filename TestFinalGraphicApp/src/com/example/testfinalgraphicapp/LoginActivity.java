package com.example.testfinalgraphicapp;


import logic.Client;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class LoginActivity extends Activity implements OnClickListener {

	private ImageView logoImage;
	private LinearLayout mainPanelLayout;
	private Button loginButton;
	private Button registerButton;
	private Button signupButton;
	private Button signinButton;
	private RelativeLayout signinEditPanelLayout;
	private RelativeLayout signupEditPanelLayout;
	private char signupMale = 'M';

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);

		logoImage = (ImageView) findViewById(R.id.appLogo);
		mainPanelLayout = (LinearLayout) findViewById(R.id.loginPanel);
		loginButton = (Button) findViewById(R.id.login);
		registerButton = (Button) findViewById(R.id.signup);
		signinEditPanelLayout = (RelativeLayout) findViewById(R.id.signinEditPanel);
		signupEditPanelLayout = (RelativeLayout) findViewById(R.id.signupEditPanel);

		signinButton = (Button) findViewById(R.id.btnSignin);
		signupButton = (Button) findViewById(R.id.btnSignup);

		Animation bottomUp = AnimationUtils.loadAnimation(this,
				R.animator.bottom_up);

		Animation topDown = AnimationUtils.loadAnimation(this,
				R.animator.top_down);

		logoImage.setAnimation(topDown);
		logoImage.setVisibility(View.VISIBLE);

		mainPanelLayout.setAnimation(bottomUp);
		mainPanelLayout.setVisibility(View.VISIBLE);

		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		signinButton.setOnClickListener(this);
		signupButton.setOnClickListener(this);
		
		RadioGroup radiogroup = (RadioGroup) findViewById(R.id.maleRegistrationRadioGroup);

		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.signupRadioMan:
					signupMale = 'M';
					break;
				case R.id.signupRadioWoman:
					signupMale = 'F';
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		Animation bottomDown = AnimationUtils.loadAnimation(this,
				R.animator.bottom_down);
		Animation panelIn = AnimationUtils.loadAnimation(this,
				R.animator.panel_in);
		Animation logoUp = AnimationUtils.loadAnimation(this,
				R.animator.logo_up);

		logoUp.setFillAfter(true);

		switch(v.getId()){
		case R.id.login:
			mainPanelLayout.setAnimation(bottomDown);
			mainPanelLayout.setVisibility(View.GONE);

			signinEditPanelLayout.setAnimation(panelIn);
			signinEditPanelLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.signup: 
			mainPanelLayout.setAnimation(bottomDown);
			mainPanelLayout.setVisibility(View.GONE);

			logoImage.setAnimation(logoUp);

			signupEditPanelLayout.setAnimation(panelIn);
			signupEditPanelLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.btnSignin: 
			Toast.makeText(this, "signin button", Toast.LENGTH_SHORT).show();
			try {
				EditText signinLogin = (EditText) findViewById(R.id.signinLoginEditText);
				EditText signinPassword = (EditText) findViewById(R.id.signinPasswordEditText);
				sendLogin(this, signinLogin.getText().toString(), signinPassword.getText().toString());
			} catch (Exception e) {
				Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}	
			break;

		case R.id.btnSignup:
			Toast.makeText(this, "here sign up + gcm", Toast.LENGTH_SHORT).show();
			try {
				EditText signupName = (EditText) findViewById(R.id.signupNameEditText);
				EditText signupLogin = (EditText) findViewById(R.id.signupLoginEditText);
				EditText signupPassword = (EditText) findViewById(R.id.signupPasswordEditText);	
				EditText signupBirthdate = (EditText) findViewById(R.id.signupAgeEditText);
				
				sendRegistration(this, signupLogin.getText().toString(), signupPassword.getText().toString(),
	                      signupName.getText().toString(), signupMale, signupBirthdate.getText().toString());
			} catch (Exception e) {
				Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}	
			break;
		default: break;

		}
	}

	@Override
	public void onBackPressed() {
		if(mainPanelLayout.getVisibility()==View.VISIBLE){
			super.onBackPressed();
		}else{
			Animation bottomUp = AnimationUtils.loadAnimation(this,
					R.animator.bottom_up);

			Animation panelOut = AnimationUtils.loadAnimation(this,
					R.animator.panel_out);

			Animation logoDown = AnimationUtils.loadAnimation(this,
					R.animator.logo_down);

			logoDown.setFillAfter(true);

			if(signinEditPanelLayout.getVisibility() == View.VISIBLE){
				signinEditPanelLayout.setAnimation(panelOut);
				signinEditPanelLayout.setVisibility(View.GONE);
			}

			if(signupEditPanelLayout.getVisibility() == View.VISIBLE){
				signupEditPanelLayout.setAnimation(panelOut);
				signupEditPanelLayout.setVisibility(View.GONE);
				logoImage.setAnimation(logoDown);
			}


			mainPanelLayout.setAnimation(bottomUp);
			mainPanelLayout.setVisibility(View.VISIBLE);

		}
	}

	private void sendLogin(final Context context, final String login, final String password){
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return Client.sendLogin(context, login, password);
				} catch (Exception e) {
					Client.closeSocket();
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				Toast.makeText(LoginActivity.this, Boolean.toString(result), Toast.LENGTH_SHORT).show();
				if(result){
					Client.setName(login);
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		}.execute();
	}
	
	private void sendRegistration(final Context context, final String login, final String password,
			                      final String name, final char male, final String birthdate){
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return Client.sendRegistration(context, login, password, name, male, birthdate);
				} catch (Exception e) {
					Client.closeSocket();
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				Toast.makeText(LoginActivity.this, Boolean.toString(result), Toast.LENGTH_SHORT).show();
				if(result){
					Client.setName(login);
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		}.execute();
	}

}
