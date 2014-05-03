package com.example.mapshower;

import java.util.ArrayList;

import com.capricorn.RayMenu;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapCustomAdapter extends BaseAdapter {
	private Context context;
	private static final int[] ITEM_DRAWABLES = {R.drawable.info, R.drawable.mail};
	private static final int itemCount = ITEM_DRAWABLES.length;
	private static ArrayList<DesireContent> desires;
	
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
	
	private static class DesireContent{
		String nameInfo = "Roma";
		String desireInfo = "to walk";
		int likes = 10;
		boolean liked = false;
	}

	
	public MapCustomAdapter(Context context){
		super();
		this.context = context;
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
        DesireContent desire = (DesireContent) this.getItem(position);
        
        ViewHolder holder;
        if(convertView == null){
        	holder = new ViewHolder();
        	convertView = LayoutInflater.from(context).inflate(R.layout.map_panel_list_row, parent, false);
        	holder.panelInfoView = (LinearLayout) convertView.findViewById(R.id.maplist_PanelInfoView);
        	holder.nameInfoView = (TextView) convertView.findViewById(R.id.maplist_nameInfoView);
        	holder.desireInfoView = (TextView) convertView.findViewById(R.id.maplist_desireInfoView);
        	holder.likeInfoView = (TextView) convertView.findViewById(R.id.maplist_likeInfoView);
        	holder.likeImageView = (ImageView) convertView.findViewById(R.id.maplist_likeImageView);
        	holder.listRayMenu = (RayMenu) convertView.findViewById(R.id.maplist_ray_menu);
        	
        	//sets fading background for ray menu
            holder.listRayMenu.setInfoView(holder.panelInfoView);
            
        	for (int i = 0; i < itemCount; i++) {
    			ImageView item = new ImageView(context);
    			item.setImageResource(ITEM_DRAWABLES[i]);

    			final int menu_position = i;
    			final int positionInList = position;
    			holder.listRayMenu.addItem(item, new OnClickListener() {

    				@Override
    				public void onClick(View v) {
    					Toast.makeText(context,"position in list: " + positionInList + " menu position:" + menu_position, Toast.LENGTH_SHORT).show();
    				}
    			});
    		}
        	
        	convertView.setTag(holder);
        } 
        else holder = (ViewHolder) convertView.getTag();
        
        holder.listRayMenu.setTag(position);
        holder.nameInfoView.setText(desire.nameInfo);
        holder.desireInfoView.setText(desire.desireInfo);
        holder.likeInfoView.setText(Integer.toString(desire.likes));
        
        if(desire.liked){
        	holder.likeImageView.setImageResource(R.drawable.red_heart);
        } else{
        	holder.likeImageView.setImageResource(R.drawable.gray_heart);
        }
       
        
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.animator.text_fade_in);
	    convertView.startAnimation(fadeIn);
        
		return convertView;
	}
	
	private static class ViewHolder
	{
		LinearLayout panelInfoView;
		TextView nameInfoView;
		TextView desireInfoView;
		TextView likeInfoView;
		ImageView likeImageView;
		RayMenu listRayMenu;
	}

}
