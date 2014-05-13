package graphics.chat;

import graphics.view_badger.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.testfinalgraphicapp.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 *
 */
@SuppressLint("ResourceAsColor")
public class ChatListCustomAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<String> chatFellowContent;
	private HashMap<String, Integer> unreadMessages;


	public ChatListCustomAdapter(Context context, ArrayList<String> chatFellowContent, HashMap<String, Integer> unreadMessages) {
		super();
		this.mContext = context;
		this.chatFellowContent = chatFellowContent;
		this.unreadMessages = unreadMessages;
	}
	
	@Override
	public int getCount() {
		return chatFellowContent.size();
	}
	
	@Override
	public Object getItem(int position) {		
		return chatFellowContent.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String fellowNameString = chatFellowContent.get(position);
		int unreadMessagesCount = unreadMessages.get(fellowNameString);
        Log.d("ClientTag", "fellowName= "+fellowNameString+" unreadMessagesCount= "+unreadMessagesCount);
		ViewHolder holder; 
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2, parent, false);
			holder.fellowName = (TextView) convertView.findViewById(android.R.id.text1);
			holder.badge = new BadgeView(mContext, holder.fellowName);
			holder.badge.setBackgroundResource(R.drawable.counter_bg);
			holder.badge.setBadgePosition(BadgeView.POSITION_CENTER_RIGHT);
			holder.badge.setBadgeMargin(15, 0);
			holder.badge.setTextSize(18);
			holder.badge.setText("0");
			holder.badge.hide();
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.fellowName.setText(fellowNameString);
		if(unreadMessagesCount > 0){
			holder.badge.setText(Integer.toString(unreadMessagesCount));
			holder.badge.show(true);
		} else {
			holder.badge.hide();
		}

		return convertView;
	}
	
	private static class ViewHolder
	{
		BadgeView badge;
		TextView fellowName;
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
