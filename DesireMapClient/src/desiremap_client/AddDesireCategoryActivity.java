package desiremap_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;

public class AddDesireCategoryActivity extends Activity implements OnClickListener {

	private Button bSport;
	private Button bDating;
	private Button bMainMenu;
	private int category;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_desire_category);
		bSport = (Button) findViewById(R.id.bSport);
		bDating = (Button) findViewById(R.id.bDating);
		bMainMenu = (Button) findViewById(R.id.bMainMenu);
		bSport.setOnClickListener(this);
		bDating.setOnClickListener(this);
		bMainMenu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case(R.id.bSport):{
			category = CodesMaster.Categories.SportCode;
			intent = new Intent(this, AddDesireActivity.class);
			intent.putExtra("category", category);
			startActivity(intent);
			break;
		}
		case(R.id.bDating):{
			category = CodesMaster.Categories.DatingCode;
			intent = new Intent(this, AddDesireActivity.class);
			intent.putExtra("category", category);
			startActivity(intent);
			break;
		}
		case(R.id.bMainMenu):{
			intent = new Intent(this, DesireMenuActivity.class);
			startActivity(intent);
			break;
		}
		}
		
	}
}