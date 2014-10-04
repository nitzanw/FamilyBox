package com.wazapps.familybox;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;

public class NewsfeedActivity extends Activity {
	ActionBar.Tab newsTab, eventsTab;
	Fragment newsFragment = new NewsFragmentTab();
	Fragment eventsFragment = new EventsFragmentTab();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		
		ActionBar actionBar = getActionBar();
		// Hide Actionbar Icon - remove this?
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Hide Actionbar Title - remove this?
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set Tab Titles
        newsTab = actionBar.newTab().setText("News");
        eventsTab = actionBar.newTab().setText("Events");
        
        // Set Tab Listeners
        newsTab.setTabListener(new TabListener(newsFragment));
        eventsTab.setTabListener(new TabListener(eventsFragment));
        
        // Add tabs to actionbar
        actionBar.addTab(newsTab);
        actionBar.addTab(eventsTab);
	}
}
