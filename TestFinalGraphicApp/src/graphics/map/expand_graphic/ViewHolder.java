package graphics.map.expand_graphic;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.capricorn.RayMenu;

public class ViewHolder
{
	public LinearLayout panelInfoView;
	public TextView nameInfoView;
	public TextView desireInfoView;
	public LinearLayout likeLayout;
	public TextView likeInfoView;
	public ImageView likeImageView;
	public RayMenu listRayMenu;

	public LayoutParams convertViewLayoutParams;
	public ExpandingLayout expandingView;
	public ExpandableListItem viewObject;
	public TextView fullDesireInfoView;
}
