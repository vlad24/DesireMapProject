package com.example.testgraphicdrawerapp;


import java.util.ArrayList;

import slidingmenu.MenuCustomDrawerListAdapter;
import slidingmenu.MenuDrawerItem;
import blur.BlurActionBarDrawerToggle;
import fragments.ChatFragment;
import fragments.ExploreFragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ChatFragment chatFragment;
	private ExploreFragment exploreFragment;

	private ListView menuList;
	private DrawerLayout drawerLayout;
	private BlurActionBarDrawerToggle drawerToggle;
	private ActionBarDrawerToggle mDrawerToggle;
	private ImageView blurImageView;
	
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
		.commit();
				
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, //nav menu toggle icon
                R.string.open, // nav drawer open - description for accessibility
                R.string.close // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            switchFragment(1);
        }
	}

	private void initializeMenuList(){
		if(menuList == null){
			Log.d(TAG, "menu list null");
		}
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}
