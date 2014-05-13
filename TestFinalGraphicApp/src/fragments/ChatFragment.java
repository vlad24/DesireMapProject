package fragments;


import graphics.KeyboardClient;
import graphics.chat.ChatCustomAdapter;
import graphics.chat.ChatListCustomAdapter;
import graphics.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import logic.Client;


import com.example.testfinalgraphicapp.MainActivity;
import com.example.testfinalgraphicapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.UserSet;


import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChatFragment extends Fragment implements OnClickListener {

	Handler progressHandler;
	String LOG_TAG = "ChatFragment";
	View menuView;

	ListView lvFellows;

	ListView lvChat;
	TextView fellowLogin;
	ProgressBar chatProgressBar;
	int progressMax = 1000;
	boolean isFinishedSending = false;


	private SlidingMenu menu;

	HashMap<String, ChatCustomAdapter> usersHashMap;
	ChatListCustomAdapter chatFellowAdapter;
	HashMap<String, Integer> newGCMmessages;
	boolean isUserSetReceived = false;
	String currentFellowName;

	EditText chatEditText;
	Button sendButton;
	Button backButton;


	ArrayList<String> chatFellowContent;
	ArrayList<Boolean> loadedChat;
	HashMap<String, Integer> unreadMessages;
	//identify user's position in list
	HashMap<String, Integer> userPositionMap;
	final int historyHoursRadius = 10;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		progressHandler = new Handler();
		usersHashMap = new HashMap<String, ChatCustomAdapter>();
		userPositionMap = new HashMap<String, Integer>();
		newGCMmessages = new HashMap<String, Integer>();
		loadedChat = new ArrayList<Boolean>();

		menuView = inflater.inflate(R.layout.chat_slidingmenu_layout, null, false);
		Log.d(LOG_TAG, "all inflated");

		lvFellows = (ListView) menuView.findViewById(R.id.lvChatFellows);

		lvChat = (ListView) menuView.findViewById(R.id.lvChat);
		lvChat.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			KeyboardClient.hideKeyboard(getActivity());	
			}
		});

		fellowLogin = (TextView) menuView.findViewById(R.id.chatFellowLoginText);
		chatProgressBar = (ProgressBar) menuView.findViewById(R.id.chatProgressBar);
		chatProgressBar.setMax(progressMax);


		chatEditText = (EditText) menuView.findViewById(R.id.chatEditText);
		backButton = (Button) menuView.findViewById(R.id.chatBackBtn);
		sendButton = (Button) menuView.findViewById(R.id.sendButton);
		backButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);

		initFellowList();
		initSlidingMenu();

		Log.d(LOG_TAG, "all initialized");
		return menuView;
	}

	private void initSlidingMenu(){
		menu = (SlidingMenu) menuView.findViewById(R.id.chat_slidingmenu);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(0);
		menu.setFadeDegree(0.0f);
		menu.setBehindScrollScale(0.0f);
		menu.setActionBarOverlay(true);

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
		unreadMessages = new HashMap<String, Integer>();

		chatFellowAdapter = new ChatListCustomAdapter(getActivity(), chatFellowContent, unreadMessages);

		OnItemClickListener fellowListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String userName = chatFellowContent.get(position);
				removeBadge(userName);

				if(!loadedChat.get(position)){
					Log.d("GCM", "try to load from server for" + chatFellowContent.get(position));
					loadHistory(userName, historyHoursRadius);
				}

				showChatPanel(position);
				menu.showContent();
			}
		};

		lvFellows.setAdapter(chatFellowAdapter);
		lvFellows.setOnItemClickListener(fellowListener);

	}

	public void startChat(String login){

		if(usersHashMap.get(login) == null){
			usersHashMap.put(login, new ChatCustomAdapter(getActivity(), 
					new ArrayList<ChatMessage>()));

			loadedChat.add(0, true);
			chatFellowContent.add(0, login);
			chatFellowAdapter.notifyDataSetChanged();
			//update user index
			for(String user : userPositionMap.keySet()){
				userPositionMap.put(user, userPositionMap.get(user)+1);
			}
			userPositionMap.put(login, 0);
		}

		int userPosition = userPositionMap.get(login);

		if(!loadedChat.get(userPosition)){
			Log.d("GCM", "try to load from server for" + chatFellowContent.get(userPosition));
			loadHistory(chatFellowContent.get(userPosition), historyHoursRadius);
		}

		showChatPanel(userPosition);
		if(menu.isMenuShowing()){
			menu.toggle();
		}
	}

	private void showChatPanel(int position){
		//set header to chat
		currentFellowName = chatFellowContent.get(position);
		fellowLogin.setText(currentFellowName);
		//change content of chat
		lvChat.setAdapter(usersHashMap.get(currentFellowName));
	}

	//check if we need to badge chat list item and update menu badger
	public boolean needToBadge(String login){
		return menu.isMenuShowing() || (!login.equals(currentFellowName));
	}

	//remove badge associated with userName
	private void removeBadge(String userName){
		//notify menu badge
		MainActivity ma = (MainActivity) getActivity();			
		ma.notifyMenuBadge(unreadMessages.get(userName));

		//update chat fellow list 
		unreadMessages.put(userName, 0);
		chatFellowAdapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		menu.toggle();
		getChatUsers();

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

					chatFellowContent.addAll(resultSet.uSet);
					loadedChat.addAll(Collections.nCopies(chatFellowContent.size(), false));  //chatFellowContent.size());
					Log.d("GCM", "size of loadedChat = "+loadedChat.size());

					int position = 0;
					for(String user : chatFellowContent){
						//check if gcm not already put this value
						if(unreadMessages.get(user) == null){
							unreadMessages.put(user, 0);
						}
						userPositionMap.put(user, position++);
						//check if gcm not already put this value
						if(usersHashMap.get(user) == null){
							usersHashMap.put(user, new ChatCustomAdapter(getActivity(), 
									new ArrayList<ChatMessage>()));
						}
					}

					chatFellowAdapter.notifyDataSetChanged();

					isUserSetReceived = true;

					//iterate through newGCMmessages and refresh unreadMessages
					for(Entry<String, Integer> pair : newGCMmessages.entrySet()){
						unreadMessages.put(pair.getKey(), pair.getValue());
					}
				}
			}
		}.execute();
	}

	public void loadHistory(final String partnerName, final int hoursRadius){
		new AsyncTask<Void, Void, MessageSet>(){
			@Override
			protected MessageSet doInBackground(Void... params) {
				try {
					return Client.getMessages(partnerName, hoursRadius);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(MessageSet resultSet) {
				loadedChat.set(userPositionMap.get(partnerName), true);
				if(resultSet != null){
					Log.d("GCM", "messageSet not null");
					ArrayList<ChatMessage> chatHistoryMessages = new ArrayList<ChatMessage>();
					for(ClientMessage message : resultSet.mSet){
						Log.d("GCM", "message sender = "+message.sender+" message receiver = "+message.receiver+" client name ="+Client.getName());
						if(message.sender.equals(Client.getName())){
							chatHistoryMessages.add(new ChatMessage(message.text, true));
						}else
							chatHistoryMessages.add(new ChatMessage(message.text, false));
					}
					//add two invisible elements
					usersHashMap.get(partnerName).mMessages.add(new ChatMessage(true, "invisible 1"));
					usersHashMap.get(partnerName).mMessages.add(new ChatMessage(true, "invisible 2"));

					usersHashMap.get(partnerName).mMessages.addAll(chatHistoryMessages);
					usersHashMap.get(partnerName).notifyDataSetChanged();
				}else Log.d("GCM", "messageSet null");
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

	//notify chat fragment that new gcm message received
	public void notifyNewChatMessage(String receiver, ChatMessage message){
		//if user set received we can add this message
		if(isUserSetReceived){
			addNewChatMessage(receiver, message);
		} else {
			if(newGCMmessages.get(receiver) == null){
				newGCMmessages.put(receiver, 1);
			} else
				//increase number of unread messsages	
				newGCMmessages.put(receiver, newGCMmessages.get(receiver)+1);
		}
	}

	public void addNewChatMessage(String receiver, ChatMessage message){

		Log.d("ClientTag", "receiver= "+receiver);
		if(usersHashMap.get(receiver) == null){
			usersHashMap.put(receiver, new ChatCustomAdapter(getActivity(), 
					new ArrayList<ChatMessage>()));

			loadedChat.add(0, true);
			chatFellowContent.add(0, receiver);
			unreadMessages.put(receiver, 0);
			//update user index
			for(String user : userPositionMap.keySet()){
				userPositionMap.put(user, userPositionMap.get(user)+1);
			}
			userPositionMap.put(receiver, 0);
		}

		usersHashMap.get(receiver).mMessages.add(message);
		usersHashMap.get(receiver).notifyDataSetChanged();

		//check if need to badge chat fellow content
		if(needToBadge(receiver)){
			unreadMessages.put(receiver, unreadMessages.get(receiver)+1);
		}

		chatFellowAdapter.notifyDataSetChanged(); //new

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

	public void clear(){
		chatFellowContent.clear();
		userPositionMap.clear();
		usersHashMap.clear();
		unreadMessages.clear();
		loadedChat.clear();
		newGCMmessages.clear();
		currentFellowName = "";
		isUserSetReceived = false;
	}

}
