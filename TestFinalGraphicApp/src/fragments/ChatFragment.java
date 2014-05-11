package fragments;


import graphics.chat.ChatCustomAdapter;
import graphics.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import logic.Client;


import com.example.testfinalgraphicapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.MessageSet;
import desireMapApplicationPackage.outputSetPackage.UserSet;


import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.ViewGroup;
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


	EditText chatEditText;
	Button sendButton;
	Button backButton;

	static String sender;
	//	String[] lst= {"1","q","vlad","her"};
	ArrayList<String> chatFellowContent;
	ArrayList<Boolean> loadedChat;
	//identify user's position in list
	HashMap<String, Integer> userPositionMap;
	final int historyHoursRadius = 10;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		progressHandler = new Handler();
		usersHashMap = new HashMap<String, ChatCustomAdapter>();
		userPositionMap = new HashMap<String, Integer>();

		menuView = inflater.inflate(R.layout.chat_slidingmenu_layout, null, false);
		Log.d(LOG_TAG, "all inflated");

		lvFellows = (ListView) menuView.findViewById(R.id.lvChatFellows);
		lvChat = (ListView) menuView.findViewById(R.id.lvChat);
		fellowLogin = (TextView) menuView.findViewById(R.id.chatFellowLoginText);
		chatProgressBar = (ProgressBar) menuView.findViewById(R.id.chatProgressBar);
		chatProgressBar.setMax(progressMax);


		chatEditText = (EditText) menuView.findViewById(R.id.chatEditText);
		backButton = (Button) menuView.findViewById(R.id.chatBackBtn);
		sendButton = (Button) menuView.findViewById(R.id.sendButton);
		backButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		sender = "Romchic";

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
				if(!loadedChat.get(position)){
					Log.d("GCM", "try to load from server for" + chatFellowContent.get(position));
					loadHistory(chatFellowContent.get(position), historyHoursRadius);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		menu.toggle();
		getChatUsers();

		getActivity().setTitle(sender);
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
					//		chatFellowContent.addAll(Arrays.asList(lst));
					//		return new UserSet(chatFellowContent);
					return Client.getChatUsers();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(UserSet resultSet) {
				if(resultSet != null){
					chatFellowContent.clear();
					chatFellowContent.addAll(resultSet.uSet);
					loadedChat = new ArrayList<Boolean>(Collections.nCopies(chatFellowContent.size(), false));  //chatFellowContent.size());
					Log.d("GCM", "size of loadedChat = "+loadedChat.size());

					int position = 0;
					for(String user : chatFellowContent){
						userPositionMap.put(user, position++);
						usersHashMap.put(user, new ChatCustomAdapter(getActivity(), 
								new ArrayList<ChatMessage>()));
					}
					chatFellowAdapter.notifyDataSetChanged();
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

	public void addNewChatMessage(String receiver, ChatMessage message){

		if(usersHashMap.get(receiver) == null){
			usersHashMap.put(receiver, new ChatCustomAdapter(getActivity(), 
					new ArrayList<ChatMessage>()));

			loadedChat.add(0, true);
			chatFellowContent.add(0, receiver);
			chatFellowAdapter.notifyDataSetChanged();
			//update user index
			for(String user : userPositionMap.keySet()){
				userPositionMap.put(user, userPositionMap.get(user)+1);
			}
			userPositionMap.put(receiver, 0);
		}

		usersHashMap.get(receiver).mMessages.add(message);
		usersHashMap.get(receiver).notifyDataSetChanged();

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
