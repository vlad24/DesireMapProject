package graphics.map;

import graphics.map.expand_graphic.ExpandableListItem;
import graphics.map.expand_graphic.ExpandingLayout;
import graphics.map.expand_graphic.ViewHolder;

import java.util.ArrayList;

import com.capricorn.RayMenu;
import com.example.testfinalgraphicapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


class DesireContent{
	String nameInfo = "Roma";
	String desireInfo = "to walk through my arms i Would like to think about my life and expand its treasure ok i've found it";
	int likes = 10;
	boolean liked = false;
}

public class MapCustomAdapter extends BaseAdapter {
	private Context context;
	private static final int[] ITEM_DRAWABLES = {R.drawable.info, R.drawable.mail};
	private static final int itemCount = ITEM_DRAWABLES.length;

	private final static int mCollapsedHeight = 68;
	private static ArrayList<DesireContent> desires;

	private SlidingMenu menu;
	private boolean isMainMenu = true;

	static{	
		desires = new ArrayList<DesireContent>();
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());
		desires.add(new DesireContent());

	}

	public MapCustomAdapter(Context context, SlidingMenu menu, boolean isMainMenu){
		super();
		this.context = context;
		this.menu = menu;
		this.isMainMenu = isMainMenu;
	}

	public MapCustomAdapter(Context context, SlidingMenu menu){
		super();
		this.context = context;
		this.menu = menu;
	}

	@Override
	public int getCount() {
		return desires.size();
	}

	@Override
	public Object getItem(int position) {
		return desires.get(position);
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DesireContent desire = desires.get(position);

		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.map_panel_list_row, parent, false);
			holder.panelInfoView = (LinearLayout) convertView.findViewById(R.id.maplist_PanelInfoView);
			holder.nameInfoView = (TextView) convertView.findViewById(R.id.maplist_nameInfoView);
			holder.desireInfoView = (TextView) convertView.findViewById(R.id.maplist_desireInfoView);
			holder.likeInfoView = (TextView) convertView.findViewById(R.id.maplist_likeInfoView);
			holder.likeImageView = (ImageView) convertView.findViewById(R.id.maplist_likeImageView);
			holder.listRayMenu = (RayMenu) convertView.findViewById(R.id.maplist_ray_menu);

			holder.viewObject = new ExpandableListItem(mCollapsedHeight);
			holder.expandingView = (ExpandingLayout) convertView.findViewById(R.id.expanding_layout);
			holder.convertViewLayoutParams = new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
					AbsListView.LayoutParams.WRAP_CONTENT);
			holder.fullDesireInfoView = (TextView) convertView.findViewById(R.id.maplist_fullDesireInfoView);

			holder.listRayMenu.setInfoView(holder.panelInfoView);


			for (int i = 0; i < itemCount; i++) {
				ImageView item = new ImageView(context);
				item.setImageResource(ITEM_DRAWABLES[i]);

				final int menu_position = i;
				holder.listRayMenu.addItem(item, new OnClickListener() {

					@Override
					public void onClick(View v) {
						final int positionInList = (Integer) holder.listRayMenu.getTag();
						switch(menu_position){
						case 0:
							if(isMainMenu){
								menu.showContent();
							} else menu.toggle(true);
							break;
						case 1:
							break;
						}
						Toast.makeText(context,"position in list: " + positionInList + " menu position:" + menu_position, Toast.LENGTH_SHORT).show();
					}
				});
			}

			convertView.setTag(holder);
		} 
		else holder = (ViewHolder) convertView.getTag();

		holder.listRayMenu.disappear();

		holder.listRayMenu.setTag(position);

		holder.nameInfoView.setText(desire.nameInfo);
		holder.desireInfoView.setText(desire.desireInfo);
		holder.fullDesireInfoView.setText(desire.desireInfo);
		holder.likeInfoView.setText(Integer.toString(desire.likes));

		if(desire.liked){
			holder.likeImageView.setImageResource(R.drawable.red_heart);
		} else{
			holder.likeImageView.setImageResource(R.drawable.gray_heart);
		}

		convertView.setLayoutParams(holder.convertViewLayoutParams);

		holder.expandingView.setExpandedHeight(holder.viewObject.mExpandedHeight);
		holder.expandingView.setSizeChangedListener(holder.viewObject);
		holder.expandingView.setVisibility(View.GONE);


		Animation fadeIn = AnimationUtils.loadAnimation(context, R.animator.text_fade_in);
		convertView.startAnimation(fadeIn);

		return convertView;
	}


}
