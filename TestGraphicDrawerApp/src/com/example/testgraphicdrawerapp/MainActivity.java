package com.example.testgraphicdrawerapp;


import java.util.ArrayList;

import slidingmenu.MenuCustomDrawerListAdapter;
import slidingmenu.MenuDrawerItem;
import blur.FastBlur;
import fragments.ChatFragment;
import fragments.ExploreFragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {

	private ChatFragment chatFragment;
	private ExploreFragment exploreFragment;

	private ListView menuList;
	private DrawerLayout drawerLayout;
	private BlurActionBarDrawerToggle drawerToggle;
	private ActionBarDrawerToggle mDrawerToggle;

	private ImageView blurImageView;
	private View screenView;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<MenuDrawerItem> menuDrawerItems;
	private MenuCustomDrawerListAdapter adapter;

	private Handler handler;
	private String TAG = "DrawerAppMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);

		mTitle = mDrawerTitle = getTitle();

		handler = new Handler();
		chatFragment = new ChatFragment();
		exploreFragment = new ExploreFragment();

		blurImageView = (ImageView) findViewById(R.id.blur_image);
		screenView = getWindow().getDecorView().findViewById(android.R.id.content);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.menu_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.menu_drawer_icons);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		menuList = (ListView) findViewById(R.id.lvMain);

		menuDrawerItems = new ArrayList<MenuDrawerItem>();

		menuDrawerItems.add(new MenuDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		menuDrawerItems.add(new MenuDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		menuDrawerItems.add(new MenuDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, "25+"));
		menuDrawerItems.add(new MenuDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

		navMenuIcons.recycle();

		initializeMenuList();

		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.content_frame_layout, chatFragment)
		.add(R.id.content_frame_layout, exploreFragment)
		.hide(chatFragment)
		.commit();

		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);


		drawerToggle = new BlurActionBarDrawerToggle(this, drawerLayout, blurImageView,
				R.drawable.ic_navigation_drawer, //nav menu toggle icon
				R.string.open,
				R.string.close);
		//		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
		//                R.drawable.ic_navigation_drawer, //nav menu toggle icon
		//                R.string.open, // nav drawer open - description for accessibility
		//                R.string.close // nav drawer close - description for accessibility
		//        ){
		//            public void onDrawerClosed(View view) {
		//                getActionBar().setTitle(mTitle);
		//                // calling onPrepareOptionsMenu() to show action bar icons
		//                invalidateOptionsMenu();
		//            }
		// 
		//            public void onDrawerOpened(View drawerView) {
		//                getActionBar().setTitle(mDrawerTitle);
		//                // calling onPrepareOptionsMenu() to hide action bar icons
		//                invalidateOptionsMenu();
		//            }
		//        };
		drawerLayout.setDrawerListener(drawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			switchFragment(1);
		}
	}

	private void initializeMenuList(){

		menuList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				switchFragment(position);
			}
		});

		adapter = new MenuCustomDrawerListAdapter(this, menuDrawerItems);
		menuList.setAdapter(adapter);
	}

	public void switchFragment(final int position){
		handler.post(new Runnable(){

			@Override
			public void run() {
				FragmentManager manager = getSupportFragmentManager();
				FragmentTransaction ft = manager.beginTransaction()
						.hide(chatFragment)
						.hide(exploreFragment);
				switch(position){
				case 1:
					ft.show(exploreFragment);
					break;
				case 2:
					ft.show(chatFragment);
					break;
				}
				ft.commit();
				menuList.setItemChecked(position, true);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = drawerLayout.isDrawerOpen(menuList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		drawerToggle.onConfigurationChanged(newConfig);
	}


	private class BlurActionBarDrawerToggle extends ActionBarDrawerToggle {

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
		public void onDrawerSlide(final View drawerView, final float slideOffset) {
			super.onDrawerSlide(drawerView, slideOffset);
			Log.d(TAG, "in onDrawerSlide");
			if (slideOffset > 0.0f) {
				setBlurAlpha(slideOffset);
			}
			else {
				clearBlurImage();
			}
		}


		private void setBlurAlpha(float slideOffset) {
			if(blurImage.getVisibility() != View.VISIBLE){
				setBlurImage();
			}

			blurImage.setAlpha(slideOffset);
		}


		private void setBlurImage() {
			blurImage.setImageBitmap(null);
			blurImage.setVisibility(View.VISIBLE);
			blurImage.bringToFront();

			//get screenshot of screenView
			Bitmap bmp = Bitmap.createBitmap(screenView.getWidth(), screenView.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bmp);
			screenView.draw(c);

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


		public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

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

}
