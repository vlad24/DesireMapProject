package fragments;

import java.util.ArrayList;
import java.util.Arrays;

import logic.Client;
import logic.GPSTracker;

import com.example.testfinalgraphicapp.LoginActivity;
import com.example.testfinalgraphicapp.MainActivity;
import com.example.testfinalgraphicapp.R;

import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.outputSetPackage.DesireSet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;


public class MyDesiresFragment extends Fragment implements OnClickListener {

	final String LOG_TAG = "ListActivity";

	Handler handler;
	View view;
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
	OnItemClickListener desiresListener;

	StringBuffer removeBuffer;
	String[] lst= {"Сгонять на турнички","Поиграть в волейбольчик", "Сходить на самбо"};

	ArrayList<String> desiresContent;
	DesireSet desireSet;

	int category = 0;
	boolean addingPanelIsVisible = false;
	boolean infoPanelIsVisible = false;
	boolean backPressed = false;

	AbstractWheel fromAgeWheel;
	AbstractWheel toAgeWheel;
	// Wheel scrolled flag
	private boolean fromAgeWheelScrolled = false;
	private boolean toAgeWheelScrolled = false;
	private boolean fromAgeWheelChanged = false;
	private boolean toAgeWheelChanged = false;
	private char male = 'M';
	private GPSTracker gps;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		handler = new Handler();
		view =  inflater.inflate(R.layout.mydesires_layout, null);

		lvMain = (ListView) view.findViewById(R.id.lvMain);
		lvDesires = (ListView) view.findViewById(R.id.lvDesires);

		listNavPanel = (RelativeLayout) view.findViewById(R.id.listNavPanel);
		backButton = (Button) view.findViewById(R.id.backBtn);
		addButton = (Button) view.findViewById(R.id.addBtn);
		removeButton = (Button) view.findViewById(R.id.removeBtn);
		removeOkButton = (Button) view.findViewById(R.id.removeOkBtn);
		addSportButton = (Button) view.findViewById(R.id.sportAddBtn);
		addDatingButton = (Button) view.findViewById(R.id.datingAddBtn);

		backButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		removeButton.setOnClickListener(this);
		removeOkButton.setOnClickListener(this);
		removeBuffer = new StringBuffer();

		addSportButton.setOnClickListener(this);
		addDatingButton.setOnClickListener(this);

		addingSportPanel = (RelativeLayout) view.findViewById(R.id.addingSportEditPanel);
		addingDatingPanel = (RelativeLayout) view.findViewById(R.id.addingDatingEditPanel);
		infoSportPanel = (LinearLayout) view.findViewById(R.id.infoSportPanel);
		infoDatingPanel = (LinearLayout) view.findViewById(R.id.infoDatingPanel);

		initMainList();
		initWheels();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gps = new GPSTracker(getActivity());
	}

	private void initMainList(){
		currentList = lvMain;

		ArrayAdapter<CharSequence> mainAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.categories, android.R.layout.simple_list_item_1);
		lvMain.setAdapter(mainAdapter);

		lvMain.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
						+ id);
				category = position;
				getDesiresList();
			}
		});

		desiresContent = new ArrayList<String>();
		desiresShowAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, 
				desiresContent);

		desiresDeleteAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_multiple_choice,
				desiresContent);

		lvDesires.setAdapter(desiresShowAdapter);

		LayoutAnimationController desiresController = AnimationUtils
				.loadLayoutAnimation(getActivity(), R.animator.list_animator);
		lvDesires.setLayoutAnimation(desiresController);

	}

	private void initDesireList(DesireSet resultSet){

		desireSet = resultSet;
		desiresContent.clear();
		for(DesireContent content : desireSet.dArray){
			desiresContent.add(content.description);
		}

		LayoutAnimationController desiresController = AnimationUtils
				.loadLayoutAnimation(getActivity(), R.animator.list_animator);
		lvDesires.setLayoutAnimation(desiresController);


		desiresListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showInfoPanel(desireSet.dArray.get(position));
			}
		};

		lvDesires.setOnItemClickListener(desiresListener);
	}

	private void initWheels(){
		fromAgeWheel = (AbstractWheel) view.findViewById(R.id.fromAgeWheel);
		fromAgeWheel.setViewAdapter(new NumericWheelAdapter(getActivity(), 18, 98));
		fromAgeWheel.setCurrentItem(1);
		fromAgeWheel.setCyclic(false);
		fromAgeWheel.setInterpolator(new AnticipateOvershootInterpolator());

		toAgeWheel = (AbstractWheel) view.findViewById(R.id.toAgeWheel);
		toAgeWheel.setViewAdapter(new NumericWheelAdapter(getActivity(), 18, 98));
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
		backPressed = false;
		switch(v.getId()){
		case R.id.backBtn:
			backPressed = true;
			if(addingPanelIsVisible){
				hideAddingPanel();
			}else
				if(infoPanelIsVisible){
					hideInfoPanel();
				}else{
					Animation topUp = AnimationUtils.loadAnimation(getActivity(),
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

	private void getDesiresList(){
		new AsyncTask<Void, Void, DesireSet>() {
			@Override
			protected DesireSet doInBackground(Void... params) {
				try {
					return Client.getPersonalDesires(category);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(DesireSet resultSet) {
				if(resultSet != null){
					initDesireList(resultSet);
					Animation topDown = AnimationUtils.loadAnimation(getActivity(),
							R.animator.list_menu_top_down);
					listNavPanel.setAnimation(topDown);
					listNavPanel.setVisibility(View.VISIBLE);
					changeListView(lvDesires);
				} else Toast.makeText(getActivity(), "Some error", Toast.LENGTH_SHORT).show();
			}
		}.execute();
	}

	private void sendDesire(final DesireContent newContent){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					return Client.sendDesire(newContent);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(String resultID) {
				Toast.makeText(getActivity(), resultID, Toast.LENGTH_SHORT).show();
				if(resultID != null){
					hideAddingPanel();
					long timeDelay = 800;
					long duration = 1000;
					newContent.desireID = resultID;
					desireSet.dArray.add(0, newContent);
					addListItem(newContent.description, duration, timeDelay);
				}
			}
		}.execute();
	}


	private void addListItem(final String item,final long duration, final long postDelayTime){
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				desiresContent.add(0, item);
				desiresShowAdapter.notifyDataSetChanged();
				if(lvDesires.getFirstVisiblePosition() == 0){
					Animation fade_in = AnimationUtils.loadAnimation(getActivity(),
							R.animator.fade_in);
					fade_in.setDuration(duration);
					lvDesires.getChildAt(0).setAnimation(fade_in);
				}
			}}, postDelayTime);
	}


	private void deleteDesiresFromServer(final String contents){
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return Client.deleteDesires(contents);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			@Override
			protected void onPostExecute(Boolean result) {
				Toast.makeText(getActivity(), Boolean.toString(result), Toast.LENGTH_SHORT).show();
			}
		}.execute();
	}


	private void changeListView(ListView newList){
		Animation panelIn = AnimationUtils.loadAnimation(getActivity(),
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
		removeBuffer.setLength(0);
		int itemCount = lvDesires.getCount();
		int lastVisible = lvDesires.getLastVisiblePosition();
		int firstVisible = lvDesires.getFirstVisiblePosition();


		for (int i = itemCount-1; i > lastVisible; i--) {
			if (checked.get(i)){
				Log.d(LOG_TAG, i+" Invisible\n");
				removeBuffer.append(",\'"+desireSet.dArray.get(i).desireID+"\'");
				desiresContent.remove(i);
				desireSet.dArray.remove(i);
			}
		}

		//count of visible checked positions;
		int countAnimated = 0;
		final long postDelayTime = 200;
		final long animDuration = 600;
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
				Animation fade_out = AnimationUtils.loadAnimation(getActivity(),
						R.animator.fade_out);
				fade_out.setDuration(duration);
				fade_out.setStartOffset(postDelayTime);
				fade_out.setAnimationListener(new AnimationListener(){
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						removeBuffer.append(",\'"+desireSet.dArray.get(position).desireID+"\'");
						desiresContent.remove(position);
						desireSet.dArray.remove(position);
						Log.d(LOG_TAG, "Animation finished\n");
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}	
				});
				lvDesires.getChildAt(position-firstVisible).setAnimation(fade_out);
				lvDesires.getChildAt(position-firstVisible).setVisibility(View.GONE);
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
						removeBuffer.append(",\'"+desireSet.dArray.get(i).desireID+"\'");
						desiresContent.remove(i);
						desireSet.dArray.remove(i);
					}

				checked.clear();
				removeBuffer.deleteCharAt(0);
				String resultContent = removeBuffer.toString();
				deleteDesiresFromServer(resultContent);

				addButton.setVisibility(View.VISIBLE);
				removeButton.setVisibility(View.VISIBLE);
				lvDesires.setAdapter(desiresShowAdapter);
				lvDesires.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				lvDesires.setOnItemClickListener(desiresListener);
			}
		}, timeDelay);
	}

	private void showAddingPanel(){
		Animation fadeOut = AnimationUtils.loadAnimation(getActivity(),
				R.animator.fade_out);
		currentList.setAnimation(fadeOut);
		currentList.setVisibility(View.GONE);
		addingPanelIsVisible = true;

		addButton.setAnimation(fadeOut);
		removeButton.setAnimation(fadeOut);
		addButton.setVisibility(View.GONE);
		removeButton.setVisibility(View.GONE);
		Animation desirePanelUp = AnimationUtils.loadAnimation(getActivity(),
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
		Animation panelIn = AnimationUtils.loadAnimation(getActivity(),
				R.animator.list_panel_in);
		currentList.setAnimation(panelIn);
		currentList.setVisibility(View.VISIBLE);

		addingPanelIsVisible = false;
		Animation fadeIn = AnimationUtils.loadAnimation(getActivity(),
				R.animator.fade_in);

		addButton.setAnimation(fadeIn);
		removeButton.setAnimation(fadeIn);
		addButton.setVisibility(View.VISIBLE);
		removeButton.setVisibility(View.VISIBLE);

		Animation desirePanelDown = AnimationUtils.loadAnimation(getActivity(),
				R.animator.desire_panel_down);
		switch(category){
		case 0:
			addingSportPanel.setAnimation(desirePanelDown);
			addingSportPanel.setVisibility(View.GONE);
			if(!backPressed){
				EditText sportName = (EditText) view.findViewById(R.id.sportNameEditText);
				EditText sportDescription = (EditText) view.findViewById(R.id.sportDescriptionEditText);
				EditText sportAdvantage = (EditText) view.findViewById(R.id.sportAdvantageEditText);
				sportName.setText("");
				sportDescription.setText("");
				sportAdvantage.setText("");
			}
			break;
		case 1:
			addingDatingPanel.setAnimation(desirePanelDown);
			addingDatingPanel.setVisibility(View.GONE);
			if(!backPressed){
				EditText datingDescription = (EditText) view.findViewById(R.id.datingDescriptionEditText);
				datingDescription.setText("");
				fromAgeWheel.setCurrentItem(0);
				toAgeWheel.setCurrentItem(0);
			}
			break;
		}
	}

	private void showInfoPanel(DesireContent desireContent){
		Animation fadeOut = AnimationUtils.loadAnimation(getActivity(),
				R.animator.fade_out);
		currentList.setAnimation(fadeOut);
		currentList.setVisibility(View.GONE);
		infoPanelIsVisible = true;

		addButton.setAnimation(fadeOut);
		removeButton.setAnimation(fadeOut);
		addButton.setVisibility(View.GONE);
		removeButton.setVisibility(View.GONE);
		Animation infoPanelIn = AnimationUtils.loadAnimation(getActivity(),
				R.animator.info_panel_in);
		switch(category){
		case 0:
			DesireContentSport sportContent = (DesireContentSport) desireContent;
			TextView sportName = (TextView) view.findViewById(R.id.sportNameInfo);
			TextView sportDescription = (TextView) view.findViewById(R.id.sportDescriptionInfo);
			TextView sportAdvantage = (TextView) view.findViewById(R.id.sportAdvantageInfo);
			sportName.setText("Спорт: "+sportContent.sportName);
			sportDescription.setText("Желание: "+sportContent.description);
			sportAdvantage.setText("Уровень: "+sportContent.advantages);
			infoSportPanel.setAnimation(infoPanelIn);
			infoSportPanel.setVisibility(View.VISIBLE);
			break;
		case 1:
			DesireContentDating datingContent = (DesireContentDating) desireContent;
			TextView datingDescription = (TextView) view.findViewById(R.id.datingDescriptionInfo);
			TextView datingMale = (TextView) view.findViewById(R.id.datingMaleInfo);
			TextView datingAge = (TextView) view.findViewById(R.id.datingAgeInfo);
			datingDescription.setText("Желание: "+datingContent.description);
			datingMale.setText("С кем: "+datingContent.partnerSex);
			datingAge.setText("От " + datingContent.partnerAgeFrom + " до "+ datingContent.partnerAgeTo);
			infoDatingPanel.setAnimation(infoPanelIn);
			infoDatingPanel.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void hideInfoPanel(){
		Animation panelIn = AnimationUtils.loadAnimation(getActivity(),
				R.animator.list_panel_in);
		currentList.setAnimation(panelIn);
		currentList.setVisibility(View.VISIBLE);

		infoPanelIsVisible = false;
		Animation fadeIn = AnimationUtils.loadAnimation(getActivity(),
				R.animator.fade_in);

		addButton.setAnimation(fadeIn);
		removeButton.setAnimation(fadeIn);
		addButton.setVisibility(View.VISIBLE);
		removeButton.setVisibility(View.VISIBLE);

		Animation infoPanelOut = AnimationUtils.loadAnimation(getActivity(),
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
		EditText sportName = (EditText) view.findViewById(R.id.sportNameEditText);
		EditText sportDescription = (EditText) view.findViewById(R.id.sportDescriptionEditText);
		EditText sportAdvantage = (EditText) view.findViewById(R.id.sportAdvantageEditText);
		String sportNameString = sportName.getText().toString();
		String sportDescriptionString = sportDescription.getText().toString();
		String sportAdvantageString = sportAdvantage.getText().toString();
		if(sportNameString.isEmpty() ||
				sportDescriptionString.isEmpty() ||
				sportAdvantageString.isEmpty()){
			if(sportNameString.isEmpty())
				Toast.makeText(getActivity(), "Укажи спорт", Toast.LENGTH_SHORT).show();
			else {
				if(sportDescriptionString.isEmpty())
					Toast.makeText(getActivity(), "Опиши желание", Toast.LENGTH_SHORT).show();
				else Toast.makeText(getActivity(), "Опиши свой уровень в двух словах", Toast.LENGTH_SHORT).show();
			}
		}else{
			Coordinates coord = new Coordinates(gps.getLatitude(), gps.getLongitude());
			DesireContentSport newContent = new DesireContentSport(Client.getName(), null, sportDescriptionString,
					coord, null,
					sportNameString, sportAdvantageString);
			sendDesire(newContent);
			Toast.makeText(getActivity(), "Спорт: "+sportName.getText()+
					"\nЖелание: "+sportDescription.getText()+
					"\nУровень: "+sportAdvantage.getText(), Toast.LENGTH_SHORT).show();
		}
	}

	private void sendDatingDesireInfo(){
		EditText datingDescription = (EditText) view.findViewById(R.id.datingDescriptionEditText);
		String datingDescriptionString = datingDescription.getText().toString();
		RadioGroup radiogroup = (RadioGroup) view.findViewById(R.id.maleRadioGroup);

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
			int partnerAgeFrom = fromAgeWheel.getCurrentItem()+18;
			int partnerAgeTo = toAgeWheel.getCurrentItem()+18;

			Coordinates coord = new Coordinates(gps.getLatitude(), gps.getLongitude());
			DesireContentDating newContent = new DesireContentDating(Client.getName(), null, datingDescriptionString,
					coord, null,
					male, partnerAgeFrom, partnerAgeTo);
			sendDesire(newContent);

			Toast.makeText(getActivity(), "Desire: "+ datingDescriptionString
					+"\nMale: "+male
					+"\nFromAge:"+(partnerAgeFrom)
					+"\nToAge:"+(partnerAgeTo), Toast.LENGTH_SHORT).show();
		} else Toast.makeText(getActivity(), "Введите желание", Toast.LENGTH_SHORT).show();
	}

}
