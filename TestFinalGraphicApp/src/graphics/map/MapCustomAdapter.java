package graphics.map;

import fragments.ChatFragment;
import fragments.MapFragment;
import graphics.map.expand_graphic.ExpandableListItem;
import graphics.map.expand_graphic.ExpandingLayout;
import graphics.map.expand_graphic.ViewHolder;

import java.util.ArrayList;
import java.util.HashSet;

import logic.Client;

import com.capricorn.RayMenu;
import com.example.testfinalgraphicapp.MainActivity;
import com.example.testfinalgraphicapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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


//class DesireContent{
//	String nameInfo = "Roma";
//	String desireInfo = "to walk through my arms i Would like to think about my life and expand its treasure ok i've found it";
//	int likes = 10;
//	boolean liked = false;
//}

public class MapCustomAdapter extends BaseAdapter {
	private Context context;
	private static final int[] ITEM_DRAWABLES = {R.drawable.info, R.drawable.mail};
	private static final int itemCount = ITEM_DRAWABLES.length;

	private final static int mCollapsedHeight = 68;
	private static ArrayList<DesireContent> desires;

	private SlidingMenu menu;
	private boolean isMainMenu = true;
	private HashSet<String> likedByUser = MapFragment.globalLikedByUser;

	static{	
		desires = new ArrayList<DesireContent>();
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

	public void changeData(ArrayList<DesireContent> newDesireList){
		desires = newDesireList;
		this.notifyDataSetChanged();
	}

	public ArrayList<DesireContent> getData(){
		return desires;
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
			holder.likeLayout = (LinearLayout) convertView.findViewById(R.id.maplist_likeLayout);
			holder.likeInfoView = (TextView) convertView.findViewById(R.id.maplist_likeInfoView);
			holder.likeImageView = (ImageView) convertView.findViewById(R.id.maplist_likeImageView);
			holder.listRayMenu = (RayMenu) convertView.findViewById(R.id.maplist_ray_menu);

			holder.viewObject = new ExpandableListItem(mCollapsedHeight);
			holder.expandingView = (ExpandingLayout) convertView.findViewById(R.id.expanding_layout);
			holder.convertViewLayoutParams = new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
					AbsListView.LayoutParams.WRAP_CONTENT);
			holder.fullDesireInfoView = (TextView) convertView.findViewById(R.id.maplist_fullDesireInfoView);

			holder.likeLayout.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					final int positionInList = (Integer) holder.listRayMenu.getTag();
					DesireContent desire = desires.get(positionInList);
					boolean liked = likedByUser.contains(desire.desireID);
					if(!liked){
						likedByUser.add(desire.desireID);
						desire.likes++;
						sendLike(desire.desireID, true);
						holder.likeImageView.setImageResource(R.drawable.red_heart);
					} else{
						likedByUser.remove(desire.desireID);
						desire.likes--;
						sendLike(desire.desireID, false);
						holder.likeImageView.setImageResource(R.drawable.gray_heart);
					}
					holder.likeInfoView.setText(Integer.toString(desire.likes));
				}});

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
								//change slidingmenu content
								changeLoginContent(desires.get(positionInList).login);
								menu.showContent();
							} else menu.toggle(true);
							break;
						case 1:
							startChat(desires.get(positionInList).login);
							break;
						}
						//	Toast.makeText(context,"position in list: " + positionInList + " menu position:" + menu_position, Toast.LENGTH_SHORT).show();
					}
				});
			}

			convertView.setTag(holder);
		} 
		else holder = (ViewHolder) convertView.getTag();

		holder.listRayMenu.disappear();

		holder.listRayMenu.setTag(position);

		holder.nameInfoView.setText(desire.login);
		holder.desireInfoView.setText(desire.description);
		holder.fullDesireInfoView.setText(desire.description);
		holder.likeInfoView.setText(Integer.toString(desire.likes));

		if(likedByUser.contains(desire.desireID)){
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

	private void startChat(String nameInfo){
		if(context instanceof MainActivity){
			MainActivity mainActivity = (MainActivity) context;
			mainActivity.startChat(nameInfo);
		}
	}

	private void changeLoginContent(String nameInfo){
		if(context instanceof MainActivity){
			MainActivity mainActivity = (MainActivity) context;
			mainActivity.changeLoginContent(nameInfo);
		}
	}

	private void sendLike(final String desireID, final boolean isLiked){
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Client.sendLike(desireID, isLiked);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

}
