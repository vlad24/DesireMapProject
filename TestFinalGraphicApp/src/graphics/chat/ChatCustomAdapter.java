package graphics.chat;

import java.util.ArrayList;

import com.example.testfinalgraphicapp.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 *
 */
@SuppressLint("ResourceAsColor")
public class ChatCustomAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<ChatMessage> mMessages;



	public ChatCustomAdapter(Context context, ArrayList<ChatMessage> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
	}
	@Override
	public int getCount() {
		return mMessages.size();
	}
	@Override
	public Object getItem(int position) {		
		return mMessages.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessage message = (ChatMessage) this.getItem(position);

		ViewHolder holder; 
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_list_item, parent, false);
			holder.message = (TextView) convertView.findViewById(R.id.message_text);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.message.setText(message.getMessage());
		
		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		//check if it is a status message then remove background, and change text color.
		if(message.isStatusMessage())
		{
			holder.message.setBackgroundDrawable(null);
			lp.gravity = Gravity.LEFT;
			holder.message.setTextColor(R.color.textFieldColor);
		}
		else
		{		
			//Check whether message is mine to show green background and align to right
			if(message.isMine())
			{
				holder.message.setBackgroundResource(R.drawable.speech_bubble_green);
				lp.gravity = Gravity.RIGHT;
			}
			//If not mine then it is from sender to show orange background and align to left
			else
			{
				holder.message.setBackgroundResource(R.drawable.speech_bubble_orange);
				lp.gravity = Gravity.LEFT;
			}
			holder.message.setLayoutParams(lp);
			holder.message.setTextColor(R.color.textColor);	
		}
		return convertView;
	}
	private static class ViewHolder
	{
		TextView message;
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
