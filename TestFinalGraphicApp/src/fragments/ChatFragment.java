package fragments;


import graphics.chat.ChatCustomAdapter;
import graphics.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import logic.Client;


import com.example.testfinalgraphicapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import desireMapApplicationPackage.outputSetPackage.UserSet;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChatFragment extends Fragment implements OnClickListener {

	Handler progressHandler;
	String LOG_TAG = "ChatFragment";
	View contentView;
	View emptyMenuView;
	View menuView;

	ListView lvFellows;
	ListView lvChat;
	TextView fellowLogin;
	ProgressBar chatProgressBar;
	int progressMax = 1000;
	boolean isFinishedSending = false;


	private SlidingMenu menu;

	HashMap<String, ChatCustomAdapter> usersHashMap;
	ArrayAdapter<String> chatFellowAdapter;
	String currentFellowName;

	ArrayList<ChatMessage> messages;
	ChatCustomAdapter adapter;

	EditText chatEditText;
	Button sendButton;
	Button backButton;

	static String sender;
	String[] lst= {"Романыч","Владуха","Жен","Рус"};
	ArrayList<String> chatFellowContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		progressHandler = new Handler();
		usersHashMap = new HashMap<String, ChatCustomAdapter>();
		contentView =  inflater.inflate(R.layout.chat_panel_layout, container, false);
		emptyMenuView =  inflater.inflate(R.layout.chat_empty_menu_layout, null, false);
		menuView = inflater.inflate(R.layout.chat_menu_layout, container, false);
		Log.d(LOG_TAG, "all inflated");

		lvFellows = (ListView) menuView.findViewById(R.id.lvChatFellows);
		lvChat = (ListView) contentView.findViewById(R.id.lvChat);
		fellowLogin = (TextView) contentView.findViewById(R.id.chatFellowLoginText);
		chatProgressBar = (ProgressBar) contentView.findViewById(R.id.chatProgressBar);
		chatProgressBar.setMax(progressMax);


		chatEditText = (EditText) contentView.findViewById(R.id.chatEditText);
		backButton = (Button) contentView.findViewById(R.id.chatBackBtn);
		sendButton = (Button) contentView.findViewById(R.id.sendButton);
		backButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		sender = "Romchic";

		initFellowList();
		initSlidingMenu();

		Log.d(LOG_TAG, "all initialized");
		return contentView;
	}

	private void initSlidingMenu(){
		menu = new SlidingMenu(getActivity());
		menu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
	//	menu.setMenu(emptyMenuView);
		menu.setMenu(menuView);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(0);
		menu.setFadeDegree(0.0f);
		menu.setBehindScrollScale(0.0f);

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		menu.setBehindWidth(width);


		menu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
				fellowLogin.setAlpha((float) (1.0 - 1.7 * percentOpen));
			}
		});
	}

	private void initFellowList(){
		chatFellowContent = new ArrayList<String>();

		chatFellowAdapter = new ArrayAdapter<String>(
				getActivity(), 
				android.R.layout.simple_list_item_1, chatFellowContent);
		
		OnItemClickListener fellowListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showChatPanel(position);
				menu.showContent();
			}
		};

		lvFellows.setAdapter(chatFellowAdapter);
		lvFellows.setOnItemClickListener(fellowListener);
		
//		//set animation creating list elements
//		LayoutAnimationController fellowsController = AnimationUtils
//				.loadLayoutAnimation(getActivity(), R.animator.list_animator);
//		lvFellows.setLayoutAnimation(fellowsController);
	}

	private void showChatPanel(int position){
		//set header to chat
		currentFellowName = chatFellowContent.get(position);
		fellowLogin.setText(currentFellowName);
		lvChat.setAdapter(usersHashMap.get(currentFellowName));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		menu.toggle();
		getActivity().setTitle(sender);
//		sendMessage("q", "Hi");
//		messages = new ArrayList<ChatMessage>();
//		messages.add(new ChatMessage("Hello", false));
//		messages.add(new ChatMessage("Hi!", true));
//		messages.add(new ChatMessage("Wassup??", false));
//		messages.add(new ChatMessage("nothing much, working on speech bubbles.", true));
//		messages.add(new ChatMessage("you say!", true));
//		messages.add(new ChatMessage("oh thats great. how are you showing them", false));


//		adapter = new ChatCustomAdapter(getActivity(), messages);
//		lvChat.setAdapter(adapter);
	//	addNewChatMessage(new ChatMessage("mmm, well, using 9 patches png to show them.", true));
	}

	private void startAnimateProgressBar(){

		chatProgressBar.setProgress(0);
		chatProgressBar.setVisibility(View.VISIBLE);

		final long startTime = SystemClock.uptimeMillis();
		final long duration = 3000;

		final Interpolator interpolator = new AccelerateInterpolator();

		progressHandler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - startTime;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				chatProgressBar.setProgress((int)(t*progressMax));

				if (!isFinishedSending && (t < 1.0)) {
					progressHandler.postDelayed(this, 16);
				}
			}
		});
	}

	private void stopAnimateProgressBar(){
		final long startTime = SystemClock.uptimeMillis();
		final long duration = 500;

		final Interpolator interpolator = new AccelerateInterpolator();

		final int startProgress = chatProgressBar.getProgress();
		final int deltaProgress = progressMax - startProgress;

		progressHandler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - startTime;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				chatProgressBar.setProgress((int)(startProgress + t*deltaProgress));

				if (t < 1.0) {
					progressHandler.postDelayed(this, 2);
				} else chatProgressBar.setVisibility(View.GONE);
			}
		});
	}

	public void getChatUsers(){
		new AsyncTask<Void, Void, UserSet>(){
			@Override
			protected UserSet doInBackground(Void... params) {
				try {
					return Client.getChatUsers();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(UserSet resultSet) {
				if(resultSet != null){
					chatFellowContent = resultSet.uSet;
					for(String user : chatFellowContent){
						usersHashMap.put(user, new ChatCustomAdapter(getActivity(), 
								new ArrayList<ChatMessage>()));
					}
					chatFellowAdapter.notifyDataSetChanged();
					menu.setMenu(menuView);
				}
			}
		}.execute();
	}

	public void sendMessage(final String receiver, final String newMessageText){
		new AsyncTask<Void, Integer, Void>() {

			@Override
			protected void onPreExecute() {
				isFinishedSending = false;
				startAnimateProgressBar();			
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Client.sendMessage(newMessageText, receiver);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(Void result) {
				isFinishedSending = true;
				stopAnimateProgressBar();

				chatEditText.setText("");
				addNewChatMessage(receiver, new ChatMessage(newMessageText, true));
			}
		}.execute();

	}

	public void addNewChatMessage(String receiver, ChatMessage message){
        
		//эта проверка в картах будет
		if(usersHashMap.get(receiver) == null){
			usersHashMap.put(receiver, new ChatCustomAdapter(getActivity(), 
					new ArrayList<ChatMessage>()));
			
			chatFellowContent.add(0, receiver);
			chatFellowAdapter.notifyDataSetChanged();
		}
		
		usersHashMap.get(receiver).mMessages.add(message);
		usersHashMap.get(receiver).notifyDataSetChanged();
		
	//	messages.add(m);
	//	adapter.notifyDataSetChanged();
		lvChat.setSelection(usersHashMap.get(receiver).mMessages.size()-1);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sendButton:
			String newMessage = chatEditText.getText().toString().trim(); 
			if(newMessage.length() > 0)
				sendMessage(currentFellowName, newMessage); //send message to current fellow
			break;
		case R.id.chatBackBtn:
			menu.toggle(true);
			break;
		}
	}

}
