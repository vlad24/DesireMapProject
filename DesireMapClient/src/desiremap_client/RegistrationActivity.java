package desiremap_client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import desireMapApplicationPackage.Client;

public class RegistrationActivity extends Activity implements OnClickListener{
	
	private Button bRegisterNext;
	private EditText personName;
	private EditText password;
	private EditText password2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		bRegisterNext = (Button) findViewById(R.id.bRegisterNext);
		bRegisterNext.setOnClickListener(this);
		personName = (EditText) findViewById(R.id.reg_login);
		password = (EditText) findViewById(R.id.reg_password);
		password2 = (EditText) findViewById(R.id.reg_password2);
	}


	@Override
	public void onClick(View v) {
		KeyboardClient.hideKeyboard(this);
		String personNameText = personName.getText().toString();
		String passwordText = password.getText().toString();
		String password2Text = password2.getText().toString();
		
		if(passwordText.equals(password2Text)&&Client.isCorrect(personNameText)&&Client.isCorrect(passwordText)){
			Intent intent = new Intent(this, RegistrationPersonalInfoActivity.class);
			intent.putExtra("login", personNameText);
			intent.putExtra("password", passwordText);
			startActivity(intent);
		}else{
			Toast.makeText(this, "Wrong login or password", Toast.LENGTH_SHORT).show();
		}
		
	}
}
