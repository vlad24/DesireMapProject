package overscroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

@SuppressLint("NewApi") public class MyOverScrollView extends ScrollView {

	private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;

	private Context mContext;
	private int mMaxYOverscrollDistance;
	private int currentDistance;
	private boolean isFreeScreenEvent;
	private String TAG = "OverScrollView";

	public MyOverScrollView(Context context) 
	{
		super(context);
		mContext = context;
		initBounceScrollView();
	}

	public MyOverScrollView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		initBounceScrollView();
	}

	public MyOverScrollView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		mContext = context;
		initBounceScrollView();
	}

	private void initBounceScrollView()
	{
		//get the density of the screen and do some maths with it on the max overscroll distance
		//variable so that you get similar behaviors no matter what the screen size

		final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		final float density = metrics.density;

		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
		currentDistance = 0;
		isFreeScreenEvent = false;
	}

	private int recalculate(int deltaY){
		return (int)((deltaY / 4) * Math.exp(-0.1 / Math.abs(mMaxYOverscrollDistance - currentDistance)));
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){ 
		//This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
		if(!isTouchEvent){
			Log.d(TAG, "not touch event");
			isFreeScreenEvent = true;
			return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);

		}else{
			if(isFreeScreenEvent){
				isFreeScreenEvent = false;
				currentDistance = 0;
			}
			if((scrollY > 0) && (scrollY < scrollRangeY))
				return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
			int newdY = recalculate(deltaY);
			currentDistance += newdY;
			return super.overScrollBy(deltaX, newdY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);

		}
	}
}

