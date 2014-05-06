package com.example.testlogingraphicapp;


import android.app.Activity;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private ImageView logoImage;
	private LinearLayout mainPanelLayout;
	private Button loginButton;
	private Button registerButton;
	private Button signupButton;
	private Button signinButton;
	private RelativeLayout signinEditPanelLayout;
	private RelativeLayout signupEditPanelLayout;
	
	private EditText signinLogin;
	private EditText signinPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		logoImage = (ImageView) findViewById(R.id.appLogo);
		mainPanelLayout = (LinearLayout) findViewById(R.id.loginPanel);
		loginButton = (Button) findViewById(R.id.login);
		registerButton = (Button) findViewById(R.id.signup);
		signinEditPanelLayout = (RelativeLayout) findViewById(R.id.signinEditPanel);
		signupEditPanelLayout = (RelativeLayout) findViewById(R.id.signupEditPanel);

		signinButton = (Button) findViewById(R.id.btnSignin);
		signinLogin = (EditText) findViewById(R.id.signinLoginEditText);
		signinPassword = (EditText) findViewById(R.id.signinPasswordEditText);
		
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
		
//		logoUp.setAnimationListener(new AnimationListener(){
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				logoImage.setScaleX(0.5f);
//				logoImage.setScaleY(0.5f);
//				logoImage.setTranslationY(-35f);
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {				
//			}
//			
//		});

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
			try {
				sendLogin(this, signinLogin.getText().toString(), signinPassword.getText().toString());
			} catch (Exception e) {
				Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}	
			break;
		
		case R.id.btnSignup:
			Toast.makeText(this, "here sign up + gcm", Toast.LENGTH_SHORT).show();
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
					e.printStackTrace();
					return false;
				}
            }
            
            @Override
            protected void onPostExecute(Boolean result) {
            	Toast.makeText(MainActivity.this, Boolean.toString(result), Toast.LENGTH_SHORT).show();
            }
            }.execute();
	}

}