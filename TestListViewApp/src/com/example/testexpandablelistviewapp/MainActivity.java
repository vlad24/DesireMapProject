package com.example.testexpandablelistviewapp;


import java.util.ArrayList;
import java.util.Arrays;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

public class MainActivity extends Activity implements OnClickListener {

	final String LOG_TAG = "ListActivity";

	Handler handler;
	ListView lvMain;
	ListView lvDesires;
	ListView currentList;
	ListView previousList;

	RelativeLayout listNavPanel;
	Button backButton;
	Button addButton;
	Button removeButton;
	Button removeOkButton;
	Button addSportButton;
	Button addDatingButton;

	RelativeLayout addingSportPanel;
	RelativeLayout addingDatingPanel;
	LinearLayout infoSportPanel;
	LinearLayout infoDatingPanel;

	ArrayAdapter<String> desiresShowAdapter;
	ArrayAdapter<String> desiresDeleteAdapter;

	AnimationAdapter animDesiresAdapter;
	OnItemClickListener desiresListener;

	String[] lst= {"Сгонять на турнички","Поиграть в волейбольчик", "Сходить на самбо",
			"Сгонять на турнички","Поиграть в волейбольчик", "Сходить на самбо",
			"Сгонять на турнички","Поиграть в волейбольчик", "Сходить на самбо",
			"Сгонять на турнички","Поиграть в волейбольчик", "Сходить на самбо",
			"Сгонять на турнички","Поиграть в волейбольчик", "Сходить на самбо"};
	String[] descr = {"0 info","1 info","2 info"};
	ArrayList<String> desiresContent;
	ArrayList<String> desiresDescription;

	int category = 0;
	boolean addingPanelIsVisible = false;
	boolean infoPanelIsVisible = false;

	AbstractWheel fromAgeWheel;
	AbstractWheel toAgeWheel;
	// Wheel scrolled flag
	private boolean fromAgeWheelScrolled = false;
	private boolean toAgeWheelScrolled = false;
	private boolean fromAgeWheelChanged = false;
	private boolean toAgeWheelChanged = false;
	private char male = 'M';

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydesires_layout);        

		handler = new Handler();
		lvMain = (ListView) findViewById(R.id.lvMain);
		lvDesires = (ListView) findViewById(R.id.lvDesires);

		listNavPanel = (RelativeLayout) findViewById(R.id.listNavPanel);
		backButton = (Button) findViewById(R.id.backBtn);
		addButton = (Button) findViewById(R.id.addBtn);
		removeButton = (Button) findViewById(R.id.removeBtn);
		removeOkButton = (Button) findViewById(R.id.removeOkBtn);
		addSportButton = (Button) findViewById(R.id.sportAddBtn);
		addDatingButton = (Button) findViewById(R.id.datingAddBtn);

		backButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		removeButton.setOnClickListener(this);
		removeOkButton.setOnClickListener(this);

		addSportButton.setOnClickListener(this);
		addDatingButton.setOnClickListener(this);

		addingSportPanel = (RelativeLayout) findViewById(R.id.addingSportEditPanel);
		addingDatingPanel = (RelativeLayout) findViewById(R.id.addingDatingEditPanel);
		infoSportPanel = (LinearLayout) findViewById(R.id.infoSportPanel);
		infoDatingPanel = (LinearLayout) findViewById(R.id.infoDatingPanel);

		initLists();
		initWheels();


	}


	private void initLists(){
		currentList = lvMain;
		desiresContent = new ArrayList<String>();
		desiresContent.addAll(Arrays.asList(lst));
		desiresDescription = new ArrayList<String>();
		desiresDescription.addAll(Arrays.asList(descr));


		ArrayAdapter<CharSequence> mainAdapter = ArrayAdapter.createFromResource(
				this, R.array.categories, android.R.layout.simple_list_item_1);
		lvMain.setAdapter(mainAdapter);

		desiresShowAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, desiresContent);

		desiresDeleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, desiresContent);

		desiresListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showInfoPanel(desiresDescription);
				//	Toast.makeText(MainActivity.this, desiresDescription.get(position), Toast.LENGTH_SHORT).show();
			}
		};


		lvDesires.setAdapter(desiresShowAdapter);
		lvDesires.setOnItemClickListener(desiresListener);

		//		lvDesires.setAdapter(desiresDeleteAdapter);
		//		lvDesires.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		lvMain.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
						+ id);
				category = position;
				Animation topDown = AnimationUtils.loadAnimation(MainActivity.this,
						R.animator.list_menu_top_down);
				listNavPanel.setAnimation(topDown);
				listNavPanel.setVisibility(View.VISIBLE);
				changeListView(lvDesires);
			}
		});
	}

	private void initWheels(){
		fromAgeWheel = (AbstractWheel) findViewById(R.id.fromAgeWheel);
		fromAgeWheel.setViewAdapter(new NumericWheelAdapter(this, 18, 98));
		fromAgeWheel.setCurrentItem(1);
		fromAgeWheel.setCyclic(false);
		fromAgeWheel.setInterpolator(new AnticipateOvershootInterpolator());

		toAgeWheel = (AbstractWheel) findViewById(R.id.toAgeWheel);
		toAgeWheel.setViewAdapter(new NumericWheelAdapter(this, 18, 98));
		toAgeWheel.setCurrentItem(1);
		toAgeWheel.setCyclic(false);
		toAgeWheel.setInterpolator(new AnticipateOvershootInterpolator());

		fromAgeWheel.addChangingListener(new OnWheelChangedListener(){
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				if(!toAgeWheelScrolled)
					if(fromAgeWheel.getCurrentItem() > toAgeWheel.getCurrentItem()){
						toAgeWheelChanged = true;
						toAgeWheel.setCurrentItem(fromAgeWheel.getCurrentItem());
						toAgeWheelChanged = false;
					}
			}
		});
		fromAgeWheel.addScrollingListener(new OnWheelScrollListener(){
			@Override
			public void onScrollingStarted(AbstractWheel wheel) {
				fromAgeWheelScrolled = true;
			}

			@Override
			public void onScrollingFinished(AbstractWheel wheel) {
				fromAgeWheelScrolled = false;
				if(!toAgeWheelScrolled)
					if(fromAgeWheel.getCurrentItem() > toAgeWheel.getCurrentItem()){
						toAgeWheelChanged = true;
						toAgeWheel.setCurrentItem(fromAgeWheel.getCurrentItem());
						toAgeWheelChanged = false;
					}

			}});

		toAgeWheel.addChangingListener(new OnWheelChangedListener(){
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {				
				if(!fromAgeWheelScrolled)
					if(fromAgeWheel.getCurrentItem() > toAgeWheel.getCurrentItem()){
						fromAgeWheelChanged = true;
						fromAgeWheel.setCurrentItem(toAgeWheel.getCurrentItem());
						fromAgeWheelChanged = false;
					}
			}
		});
		toAgeWheel.addScrollingListener(new OnWheelScrollListener(){
			@Override
			public void onScrollingStarted(AbstractWheel wheel) {
				toAgeWheelScrolled = true;
			}

			@Override
			public void onScrollingFinished(AbstractWheel wheel) {
				toAgeWheelScrolled = false;
				if(!fromAgeWheelScrolled)
					if(fromAgeWheel.getCurrentItem() > toAgeWheel.getCurrentItem()){
						fromAgeWheelChanged = true;
						fromAgeWheel.setCurrentItem(toAgeWheel.getCurrentItem());
						fromAgeWheelChanged = false;
					}
			}});

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backBtn:
			if(addingPanelIsVisible){
				hideAddingPanel();
			}else
				if(infoPanelIsVisible){
					hideInfoPanel();
				}else{
					Animation topUp = AnimationUtils.loadAnimation(this,
							R.animator.list_menu_top_up);

					listNavPanel.setAnimation(topUp);
					listNavPanel.setVisibility(View.GONE);
					changeListView(previousList);
				}
			break;
		case R.id.removeBtn:
			setDeletingMode();
			break;
		case R.id.removeOkBtn:
			deleteSelectedItems();
			break;

		case R.id.addBtn:
			showAddingPanel();
			break;
		case R.id.sportAddBtn:
			sendSportDesireInfo();
			break;
		case R.id.datingAddBtn:
			sendDatingDesireInfo();
			break;
		}

	}

	private void changeListView(ListView newList){
		Animation panelIn = AnimationUtils.loadAnimation(this,
				R.animator.list_panel_in);

		currentList.setVisibility(View.GONE);
		previousList = currentList;

		newList.setAnimation(panelIn);
		newList.setVisibility(View.VISIBLE);
		currentList = newList;
	}

	private void setDeletingMode(){
		addButton.setVisibility(View.GONE);
		removeButton.setVisibility(View.GONE);
		removeOkButton.setVisibility(View.VISIBLE);
		lvDesires.setAdapter(desiresDeleteAdapter);
		lvDesires.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvDesires.setOnItemClickListener(null);
	}

	private void deleteSelectedItems(){
		SparseBooleanArray checked = lvDesires.getCheckedItemPositions();
		int itemCount = lvDesires.getCount();
		int lastVisible = lvDesires.getLastVisiblePosition();
		int firstVisible = lvDesires.getFirstVisiblePosition();
		//count of checked positions;
		
		for (int i = itemCount-1; i > lastVisible; i--) {
			if (checked.get(i)){
				Log.d(LOG_TAG, i+" Invisible\n");
				desiresContent.remove(i);
			}
		}
		
		int countAnimated = 0;
		final long postDelayTime = 200;
		final long animDuration = 400;
		for (int i = lastVisible; i >= firstVisible; i--) {
			if (checked.get(i)){
				Log.d(LOG_TAG, i+"Visible\n");
				animateDeletingListElement(i, firstVisible, animDuration, postDelayTime*countAnimated);
				countAnimated++;
			}
		}
		
		removeOkButton.setVisibility(View.GONE);
		if(countAnimated > 0)
		 notifyAndChangeAdapter(firstVisible, checked, postDelayTime*(countAnimated-1) + animDuration + 50);
		else
		 notifyAndChangeAdapter(firstVisible, checked, 50);
	}

	private void animateDeletingListElement(final int position, final int firstVisible, final long duration, final long postDelayTime){
		handler.post(new Runnable(){
			@Override
			public void run() {
				Animation fade_out = AnimationUtils.loadAnimation(MainActivity.this,
						R.animator.fade_out);
				fade_out.setDuration(duration);
				fade_out.setStartOffset(postDelayTime);
				fade_out.setAnimationListener(new AnimationListener(){
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
        				desiresContent.remove(position);
//						desiresDescription.remove(position);
						Log.d(LOG_TAG, "Animation finished\n");
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}	
				});
				lvDesires.getChildAt(position-firstVisible).setAnimation(fade_out);
			}

		});
	}
	
	private void notifyAndChangeAdapter(final int firstVisible, final SparseBooleanArray checked, final long timeDelay){
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				Log.d(LOG_TAG, "In postDelayed\n");
				for(int i = firstVisible-1; i >= 0; i--)
					if(checked.get(i)){
						Log.d(LOG_TAG, i+"Invisible\n");
						desiresContent.remove(i);
					}
				
				checked.clear();		
				addButton.setVisibility(View.VISIBLE);
				removeButton.setVisibility(View.VISIBLE);
				lvDesires.setAdapter(desiresShowAdapter);
				lvDesires.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				lvDesires.setOnItemClickListener(desiresListener);
			}
		}, timeDelay);
	}

	private void showAddingPanel(){
		Animation fadeOut = AnimationUtils.loadAnimation(this,
				R.animator.fade_out);
		currentList.setAnimation(fadeOut);
		currentList.setVisibility(View.GONE);
		addingPanelIsVisible = true;

		addButton.setAnimation(fadeOut);
		removeButton.setAnimation(fadeOut);
		addButton.setVisibility(View.GONE);
		removeButton.setVisibility(View.GONE);
		Animation desirePanelUp = AnimationUtils.loadAnimation(this,
				R.animator.desire_panel_up);
		switch(category){
		case 0:
			addingSportPanel.setAnimation(desirePanelUp);
			addingSportPanel.setVisibility(View.VISIBLE);
			break;
		case 1:
			addingDatingPanel.setAnimation(desirePanelUp);
			addingDatingPanel.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void hideAddingPanel(){
		Animation panelIn = AnimationUtils.loadAnimation(this,
				R.animator.list_panel_in);
		currentList.setAnimation(panelIn);
		currentList.setVisibility(View.VISIBLE);

		addingPanelIsVisible = false;
		Animation fadeIn = AnimationUtils.loadAnimation(this,
				R.animator.fade_in);

		addButton.setAnimation(fadeIn);
		removeButton.setAnimation(fadeIn);
		addButton.setVisibility(View.VISIBLE);
		removeButton.setVisibility(View.VISIBLE);

		Animation desirePanelDown = AnimationUtils.loadAnimation(this,
				R.animator.desire_panel_down);
		switch(category){
		case 0:
			addingSportPanel.setAnimation(desirePanelDown);
			addingSportPanel.setVisibility(View.GONE);
			break;
		case 1:
			addingDatingPanel.setAnimation(desirePanelDown);
			addingDatingPanel.setVisibility(View.GONE);
			break;
		}
	}

	private void showInfoPanel(ArrayList<String> info){
		Animation fadeOut = AnimationUtils.loadAnimation(this,
				R.animator.fade_out);
		currentList.setAnimation(fadeOut);
		currentList.setVisibility(View.GONE);
		infoPanelIsVisible = true;

		addButton.setAnimation(fadeOut);
		removeButton.setAnimation(fadeOut);
		addButton.setVisibility(View.GONE);
		removeButton.setVisibility(View.GONE);
		Animation infoPanelIn = AnimationUtils.loadAnimation(this,
				R.animator.info_panel_in);
		switch(category){
		case 0:
			TextView sportName = (TextView) findViewById(R.id.sportNameInfo);
			TextView sportDescription = (TextView) findViewById(R.id.sportDescriptionInfo);
			TextView sportAdvantage = (TextView) findViewById(R.id.sportAdvantageInfo);
			sportName.setText("Спорт: "+info.get(0));
			sportDescription.setText("Желание: "+info.get(1));
			sportAdvantage.setText("Уровень: "+info.get(2));
			infoSportPanel.setAnimation(infoPanelIn);
			infoSportPanel.setVisibility(View.VISIBLE);
			break;
		case 1:
			TextView datingDescription = (TextView) findViewById(R.id.datingDescriptionInfo);
			TextView datingMale = (TextView) findViewById(R.id.datingMaleInfo);
			TextView datingAge = (TextView) findViewById(R.id.datingAgeInfo);
			datingDescription.setText("Желание: "+info.get(0));
			datingMale.setText("С кем: "+info.get(1));
			datingAge.setText("От "+info.get(2)+" до "+info.get(3));
			infoDatingPanel.setAnimation(infoPanelIn);
			infoDatingPanel.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void hideInfoPanel(){
		Animation panelIn = AnimationUtils.loadAnimation(this,
				R.animator.list_panel_in);
		currentList.setAnimation(panelIn);
		currentList.setVisibility(View.VISIBLE);

		infoPanelIsVisible = false;
		Animation fadeIn = AnimationUtils.loadAnimation(this,
				R.animator.fade_in);

		addButton.setAnimation(fadeIn);
		removeButton.setAnimation(fadeIn);
		addButton.setVisibility(View.VISIBLE);
		removeButton.setVisibility(View.VISIBLE);

		Animation infoPanelOut = AnimationUtils.loadAnimation(this,
				R.animator.info_panel_out);
		switch(category){
		case 0:
			infoSportPanel.setAnimation(infoPanelOut);
			infoSportPanel.setVisibility(View.GONE);
			break;
		case 1:
			infoDatingPanel.setAnimation(infoPanelOut);
			infoDatingPanel.setVisibility(View.GONE);
			break;
		}
	}


	private void sendSportDesireInfo(){
		EditText sportName = (EditText) findViewById(R.id.sportNameEditText);
		EditText sportDescription = (EditText) findViewById(R.id.sportDescriptionEditText);
		EditText sportAdvantage = (EditText) findViewById(R.id.sportAdvantageEditText);
		String sportNameString = sportName.getText().toString();
		String sportDescriptionString = sportDescription.getText().toString();
		String sportAdvantageString = sportAdvantage.getText().toString();
		if(sportNameString.isEmpty() ||
				sportDescriptionString.isEmpty() ||
				sportAdvantageString.isEmpty()){
			if(sportNameString.isEmpty())
				Toast.makeText(this, "Укажи спорт", Toast.LENGTH_SHORT).show();
			else {
				if(sportDescriptionString.isEmpty())
					Toast.makeText(this, "Опиши желание", Toast.LENGTH_SHORT).show();
				else Toast.makeText(this, "Опиши свой уровень в двух словах", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "Спорт: "+sportName.getText()+
					"\nЖелание: "+sportDescription.getText()+
					"\nУровень: "+sportAdvantage.getText(), Toast.LENGTH_SHORT).show();
		}
	}

	private void sendDatingDesireInfo(){
		EditText datingDescription = (EditText) findViewById(R.id.datingDescriptionEditText);
		String datingDescriptionString = datingDescription.getText().toString();
		RadioGroup radiogroup = (RadioGroup) findViewById(R.id.maleRadioGroup);

		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioMan:
					male = 'M';
				case R.id.radioWoman:
					male = 'W';
				}
			}
		});

		if(!datingDescriptionString.isEmpty()){
			Toast.makeText(this, "Desire: "+ datingDescriptionString
					+"\nMale: "+male
					+"\nFromAge:"+(fromAgeWheel.getCurrentItem()+18)
					+"\nToAge:"+(toAgeWheel.getCurrentItem()+18), Toast.LENGTH_SHORT).show();
		} else Toast.makeText(this, "Введите желание", Toast.LENGTH_SHORT).show();
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

}
