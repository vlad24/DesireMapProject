package desiremap_client;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgressBarClient {
	private static String TAG = "ProgressBarClient";
	protected static void showProgressBar(Context appContext, RelativeLayout rLayout, ProgressBar prBar, TextView waiting){
		
		Log.d(TAG, "in showProgressBar()");
		DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        rLayout.removeAllViews();
		
		RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		rParams.addRule(RelativeLayout.ALIGN_LEFT);
		rParams.height = Math.round(50 * displayMetrics.density);
		rParams.width = Math.round(50 * displayMetrics.density);;
		prBar.setId(1000);
		
		rLayout.addView(prBar, rParams);
		
		rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rParams.addRule(RelativeLayout.RIGHT_OF, prBar.getId());
		rParams.leftMargin = Math.round(20 * displayMetrics.density);;
		rParams.topMargin = Math.round(10 * displayMetrics.density);;
		
		waiting.setText("Подождите...");
		waiting.setTextSize(17);
		
		rLayout.addView(waiting, rParams);
	}
	
	protected static void hideProgressBar(ProgressBar prBar, TextView waiting){
		Log.d(TAG, "in hideProgressBar()");
		prBar.setVisibility(View.GONE);
		waiting.setVisibility(View.GONE);
	}

}
