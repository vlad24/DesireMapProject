package graphics.slidingmenu;

import java.util.ArrayList;

import com.example.testgraphicdrawerapp.R;
import com.example.testgraphicdrawerapp.R.drawable;
import com.example.testgraphicdrawerapp.R.id;
import com.example.testgraphicdrawerapp.R.layout;

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

	public MenuCustomDrawerListAdapter(Context context, ArrayList<MenuDrawerItem> menuDrawerItems){
		this.context = context;
		this.menuDrawerItems = menuDrawerItems;
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
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
		
		imageIcon.setImageResource(menuDrawerItems.get(position).getIcon());        
		textTitle.setText(menuDrawerItems.get(position).getTitle());

		// displaying count
		// check whether it set visible or not
		if(menuDrawerItems.get(position).getCounterVisibility()){
			txtCount.setText(menuDrawerItems.get(position).getCount());
		}else{
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
		
	}
}