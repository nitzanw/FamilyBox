package com.wazapps.familybox.newsfeed;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;
import com.wazapps.familybox.familyTree.FamilyTreeActivity;
import com.wazapps.familybox.photos.PhotoAlbumsActivity;
import com.wazapps.familybox.profiles.ProfileActivity;

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
		ft.add(R.id.content_frame, new NewsFeedTabsFragment(), TAG_NEWS_FEED);
		ft.commit();
	}

	@Override
	public void selectItem(int position) {
		mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:
			Intent profileIntent = new Intent(this, ProfileActivity.class);
			startActivity(profileIntent);
			break;

		case FAMILY_TREE_POS:
			Intent familyTreeIntent = new Intent(this, FamilyTreeActivity.class);
			startActivity(familyTreeIntent);
			break;
			
		case PHOTOS_POS:
			Intent photosIntent = new Intent(this, PhotoAlbumsActivity.class);
			startActivity(photosIntent);
			break;
			
		case NOTES_POS:

			break;
			
		case NEWS_POS:
			TabsFragment frag = (TabsFragment) getSupportFragmentManager()
			.findFragmentByTag(TAG_NEWS_FEED);
			frag.switchTab(0);
			break;
			
		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}
		
		this.mDrawerLayout.closeDrawer(this.mDrawerList);
	}

}
