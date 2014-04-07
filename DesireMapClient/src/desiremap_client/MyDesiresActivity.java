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
import android.widget.Toast;
import desireMapApplicationPackage.Client;
import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeleteByCryteriaPack;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;

public class MyDesiresActivity extends Activity implements OnClickListener {

	private Button bMainMenu;
	private Button bDelete;
	private TextView textDesires;
//	private String category;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_desires);
		bMainMenu = (Button) findViewById(R.id.bMainMenu);
		bMainMenu.setOnClickListener(this);
		bDelete = (Button) findViewById(R.id.bDelete);
		bDelete.setOnClickListener(this);
		textDesires = (TextView) findViewById(R.id.textDesires);

//		Intent intent = getIntent();
//		category = intent.getStringExtra("category");

		ShowDesiresTask task = new ShowDesiresTask();
		Cryteria showCryteria = new Cryteria(500, 200, CodesMaster.Categories.DatingCode); //hard code!!

		task.execute(showCryteria);
		try {
			DesireSet desireSet = task.get();
			Log.d("set", "received set from task, size of set = "+desireSet.dSet.size());
			if((desireSet != null)&&(desireSet.dSet.size()!=0)){
				textDesires.setText("");
				for(int i = 0; i < desireSet.dSet.size();i++)
					textDesires.append((i+1)+". "+desireSet.dSet.get(i).description+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public class ShowDesiresTask extends AsyncTask<Cryteria, Void, DesireSet> {

		private String TAG = "showDesireTask";


		protected void onPreExecute(){
			Log.d(TAG, "before Client.getDesires() in onPreExecute");
		}

		protected DesireSet doInBackground(Cryteria... params) {
			Log.d(TAG, "before Client.getDesires() in doInBackground");
			try {
				return Client.getPersonalDesires(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.getDesires()");
		}
	}



	public class DeleteDesiresTask extends AsyncTask<DeletePack, Void, Boolean> {

		private String TAG = "DeleteDesireTask";


		protected void onPreExecute(){
			Log.d(TAG, "before Client.deleteDesires() in onPreExecute");
		}

		protected Boolean doInBackground(DeletePack... params) {
			Log.d(TAG, "before Client.deleteDesires() in doInBackground");
			Log.d(TAG, "action code "+params[0].actionCode);
			return Client.deleteDesires(params[0]);
		}

		protected void onPostExecute(Boolean result){
			Log.d(TAG, "after Client.deleteDesires()");
		}
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case(R.id.bMainMenu):{
			Intent intent = new Intent(this, DesireMenuActivity.class);
			intent.putExtra("login", Client.getName());
			startActivity(intent);
			break;
		}
		case(R.id.bDelete):{
			DeleteDesiresTask task = new DeleteDesiresTask();
			Cryteria deleteCryteria = new Cryteria(400, 300, CodesMaster.Categories.DatingCode);  //hard code!!
			DeleteByCryteriaPack deletePack = new DeleteByCryteriaPack(deleteCryteria); //hard code!!
			task.execute(deletePack);
			try {
				Boolean isCorrect = task.get();
				if(isCorrect){
					Toast.makeText(this, "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
					textDesires.setText("Нет");
				}else
					Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
				
			}
			
		}
		}
	}