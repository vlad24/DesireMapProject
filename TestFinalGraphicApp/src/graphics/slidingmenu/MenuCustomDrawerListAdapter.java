package graphics.slidingmenu;

import graphics.view_badger.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;

import com.example.testfinalgraphicapp.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuCustomDrawerListAdapter extends BaseAdapter {
	private final Context context;

	private final ArrayList<MenuDrawerItem> menuDrawerItems;
	private BadgeView chatBadge;

	public MenuCustomDrawerListAdapter(Context context, ArrayList<MenuDrawerItem> menuDrawerItems){
		this.context = context;
		this.menuDrawerItems = menuDrawerItems;
	}
	
	public BadgeView getChatBadge(){
		return chatBadge;
	}

	@Override
	public int getCount() {
		return menuDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {       
		return menuDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.menu_list_item, null);
		}
		
		TextView textTitle = (TextView) convertView.findViewById(R.id.itemTitleTextView);
		ImageView imageIcon = (ImageView)convertView.findViewById(R.id.listImage);
		
		imageIcon.setImageResource(menuDrawerItems.get(position).getIcon());        
		textTitle.setText(menuDrawerItems.get(position).getTitle());

		
		if((position == 2) && (chatBadge == null)){
			chatBadge = new BadgeView(context, textTitle);
			chatBadge.setBackgroundResource(R.drawable.counter_bg);
			chatBadge.setBadgePosition(BadgeView.POSITION_CENTER_RIGHT);
			chatBadge.setBadgeMargin(0, 0);
			chatBadge.setTextSize(18);
			chatBadge.hide();
		}

		return convertView;
		
	}
}