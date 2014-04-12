package blur;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class BlurActionBarDrawerToggle extends ActionBarDrawerToggle {

	private ImageView blurImage;
	private String TAG = "BlurActionBarDrawerToggle";

	public BlurActionBarDrawerToggle(Activity activity,
			DrawerLayout drawerLayout, ImageView blurImage, int drawerImageRes,
			int openDrawerContentDescRes, int closeDrawerContentDescRes) {
		super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
				closeDrawerContentDescRes);
		Log.d(TAG, "in constructor");
		this.blurImage = blurImage;
	}
	
	@Override
	public void onDrawerClosed(View drawerView) {
		Log.d(TAG, "in onDrawerClosed");
		clearBlurImage();
	}
	
	@Override
	public void onDrawerOpened(View drawerView) {
		Log.d(TAG, "in onDrawerOpened");
		clearBlurImage();
	}

	@Override
	public void onDrawerSlide(final View drawerView, final float slideOffset) {
		super.onDrawerSlide(drawerView, slideOffset);
		Log.d(TAG, "in onDrawerSlide");
		if (slideOffset > 0.0f) {
			setBlurAlpha(drawerView, slideOffset);
		}
		else {
			clearBlurImage();
		}
	}


	private void setBlurAlpha(View drawerView, float slideOffset) {
		if(blurImage.getVisibility() != View.VISIBLE){
			setBlurImage(drawerView);
		}

		blurImage.setAlpha(slideOffset);
	}


	private void setBlurImage(View drawerView) {
		blurImage.setImageBitmap(null);
		blurImage.setVisibility(View.VISIBLE);
		
		//get screenshot of fragmentView
		Bitmap bmp = Bitmap.createBitmap(drawerView.getWidth(), drawerView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		drawerView.draw(c);
		
		blurImage.setImageBitmap(blur(bmp));	
		
	}

	private void clearBlurImage() {
		blurImage.setVisibility(View.GONE);
		blurImage.setImageBitmap(null);
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private Bitmap blur(Bitmap bkg) {
		
		float scaleFactor = 8;
		int radius = 2;
		
		Bitmap overlay = Bitmap.createBitmap((int)(bkg.getWidth()/scaleFactor),(int)(bkg.getHeight()/scaleFactor), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(overlay);

	//	canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);

		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = FastBlur.doBlur(overlay, radius, true);
		return getResizedBitmap(overlay, bkg.getHeight(), bkg.getWidth());
		
	}
	
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// CREATE A MATRIX FOR THE MANIPULATION

		Matrix matrix = new Matrix();

		// RESIZE THE BIT MAP

		matrix.postScale(scaleWidth, scaleHeight);

		// RECREATE THE NEW BITMAP

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

		return resizedBitmap;
	}


}
