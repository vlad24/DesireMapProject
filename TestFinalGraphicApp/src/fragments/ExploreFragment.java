package fragments;

import com.example.testfinalgraphicapp.MainActivity;
import com.example.testfinalgraphicapp.R;

import graphics.uk.co.chrisjenx.paralloid.views.ParallaxHorizontalScrollView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class ExploreFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.explore_layout, null);
		
        ParallaxHorizontalScrollView hscrollView1 = (ParallaxHorizontalScrollView) view.findViewById(R.id.hscroll1);
        hscrollView1.parallaxViewBackgroundBy(hscrollView1, getResources().getDrawable(R.drawable.green67e46f), .2f);
        
        ImageView firstItemImageView = (ImageView) hscrollView1.findViewById(R.id.imageView1);
        firstItemImageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				switchFragment(0);
			}});
        
        ParallaxHorizontalScrollView hscrollView2 = (ParallaxHorizontalScrollView) view.findViewById(R.id.hscroll2);
        hscrollView2.parallaxViewBackgroundBy(hscrollView2, getResources().getDrawable(R.drawable.blue34c6cd), .2f);
        
        ParallaxHorizontalScrollView hscrollView3 = (ParallaxHorizontalScrollView) view.findViewById(R.id.hscroll3);
        hscrollView3.parallaxViewBackgroundBy(hscrollView3, getResources().getDrawable(R.drawable.orangeff9840), .2f);  
        
        ParallaxHorizontalScrollView hscrollView4 = (ParallaxHorizontalScrollView) view.findViewById(R.id.hscroll4);
        hscrollView4.parallaxViewBackgroundBy(hscrollView4, getResources().getDrawable(R.drawable.redff4540), .2f);
        
        return view;
	}
	
	private void switchFragment(int position){
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity ma = (MainActivity) getActivity();
			ma.switchFragment(position);
		}
	}
	
}