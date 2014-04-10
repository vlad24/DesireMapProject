package fragments;


import java.util.ArrayList;
import java.util.Random;

import com.example.testgraphicapp.R;

import chat.CustomAdapter;
import chat.Message;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChatFragment extends ListFragment implements OnClickListener {

	ArrayList<Message> messages;
	CustomAdapter adapter;
	
	EditText text;
	Button sendButton;
	static String sender;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.chat_layout, container, false);
		text = (EditText) view.findViewById(R.id.text);
		sendButton = (Button) view.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(this);
		sender = "Romchic";
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getActivity().setTitle(sender);
		messages = new ArrayList<Message>();
		messages.add(new Message("Hello", false));
		messages.add(new Message("Hi!", true));
		messages.add(new Message("Wassup??", false));
		messages.add(new Message("nothing much, working on speech bubbles.", true));
		messages.add(new Message("you say!", true));
		messages.add(new Message("oh thats great. how are you showing them", false));
		

		adapter = new CustomAdapter(getActivity(), messages);
		setListAdapter(adapter);
		addNewMessage(new Message("mmm, well, using 9 patches png to show them.", true));
	}
	
	public void sendMessage(){
		String newMessage = text.getText().toString().trim(); 
		if(newMessage.length() > 0)
		{
			text.setText("");
			addNewMessage(new Message(newMessage, true));
		}
	}
	
	void addNewMessage(Message m){
		
		messages.add(m);
		adapter.notifyDataSetChanged();
		getListView().setSelection(messages.size()-1);
	}

	@Override
	public void onClick(View v) {
		sendMessage();
	}

}
