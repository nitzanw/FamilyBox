package com.wazapps.familybox.newsfeed;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.PhotoAlbumsActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;

public class NewsfeedActivity extends ActivityWithDrawer {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.news_feed_title);
		overridePendingTransition(R.anim.enter, R.anim.exit); //TODO: handle transition animation in a better way
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new NewsFeedFragment(), TAG_NEWS_FEED);
		ft.commit();
	}

	public void initDrawer() {
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		super.initDrawer();
	}

	@Override
	public void selectItem(int position) {
		mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:

			break;
		case FAMILY_TREE_POS:

			break;
		case PHOTOS_POS:
			Intent intent = new Intent(this, PhotoAlbumsActivity.class);
			startActivity(intent);
			break;
		case NOTES_POS:

			break;
		case NEWS_POS:

			break;
		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}
		
		mDrawerLayout.closeDrawer(mDrawerList);
	}

}
