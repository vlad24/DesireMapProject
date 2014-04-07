package com.example.testgraphicapp;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragments.ExploreFragment;
import fragments.MenuFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu menu;
    private Fragment menuFragment;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        setContentView(R.layout.content_frame);
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new ExploreFragment())
		.commit();
          
        
        setBehindContentView(R.layout.menu_frame);
        menuFragment = new MenuFragment();
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, menuFragment)
		.commit();
 
        
        menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowDrawable(R.drawable.shadow_gradient);
		menu.setShadowWidth(0);
		menu.setFadeDegree(0.0f);
		menu.setBehindWidth(200);
		
		
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			menu.toggle(true);
			break;
		}
		return true;
	}
    
}
