package fragments;


import graphics.chat.ChatCustomAdapter;
import graphics.chat.ChatMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;

import com.example.testfinalgraphicapp.R;

import desireMapApplicationPackage.messageSystemPackage.ClientMessage;

import logic.Client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



public class ChatFragment extends ListFragment implements OnClickListener{

	private ArrayList<ChatMessage> chatMessages;
	private ChatCustomAdapter adapter;

	private EditText text;
	private Button sendButton;
	private ListView chatList;
	private Handler chatUIHandler;
	private Thread messageThread;
	private ChatRunnable chatRunnable;

	static String sender;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		View view =  inflater.inflate(R.layout.chat_layout, container, false);
		text = (EditText) view.findViewById(R.id.text);
		sendButton = (Button) view.findViewById(R.id.sendButton);
		chatList = (ListView) view.findViewById(android.R.id.list);
		sendButton.setOnClickListener(this);
		sender = "Romchic";

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle(sender);
		chatMessages = new ArrayList<ChatMessage>();
		chatMessages.add(new ChatMessage("Hello", false));
		chatMessages.add(new ChatMessage("Hi!", true));
		chatMessages.add(new ChatMessage("Wassup??", false));
		chatMessages.add(new ChatMessage("nothing much, working on speech bubbles.", true));
		chatMessages.add(new ChatMessage("you say!", true));
		chatMessages.add(new ChatMessage("oh thats great. how are you showing them", false));


		adapter = new ChatCustomAdapter(getActivity(), chatMessages);
		setListAdapter(adapter);
		
		chatList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			    mgr.hideSoftInputFromWindow(text.getWindowToken(), 0);
			}});
		
		addNewChatMessage(new ChatMessage("mmm, well, using 9 patches png to show them.", true));

		//		chatUIHandler = new Handler();
		//		chatRunnable = new ChatRunnable();
		//		messageThread = new Thread(chatRunnable);
		//		messageThread.start();
	}

	public void sendChatMessage(){
		String newChatMessageText = text.getText().toString().trim(); 
		if(newChatMessageText.length() > 0)
		{
			text.setText("");
			addNewChatMessage(new ChatMessage(newChatMessageText, true));
			//			chatRunnable.sendChatMessage(newChatMessageText, "q");
			//			chatRunnable.receiveChatMessages();
		}
	}

	void addNewChatMessage(ChatMessage m){

		chatMessages.add(m);
		adapter.notifyDataSetChanged();
		getListView().setSelection(chatMessages.size()-1);
	}

	@Override
	public void onClick(View v) {
		sendChatMessage();
	}


	private class ChatRunnable implements Runnable{

		private Deque<ClientMessage> messageSet;

		@Override
		public void run() {
		}

		public void sendChatMessage(String newChatMessageText, String receiver){
			try {
				Client.sendMessage(newChatMessageText, receiver);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void receiveChatMessages(){
			try {
			//	messageSet = Client.getMessages();
				chatUIHandler.post(new Runnable(){

					@Override
					public void run() {
						for(ClientMessage message : messageSet){
							addNewChatMessage(new ChatMessage(message.text, false));
						}

					}});
			} catch (Exception e) {
				e.printStackTrace();
			}
			messageSet = null;

		}
	}

}
