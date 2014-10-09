package com.wazapps.familybox.profiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamilyTreeActivity;
import com.wazapps.familybox.newsfeed.NewsfeedActivity;
import com.wazapps.familybox.photos.PhotoAlbumsActivity;

public class ProfileActivity extends ActivityWithDrawer {
	static final String TAG_PROFILE = "Profile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.profile_title);
		overridePendingTransition(R.anim.enter, R.anim.exit); //TODO: handle transition animations in a better way
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new ProfileFragment(), TAG_PROFILE);
		ft.commit();
	}
	
	@Override
	public void selectItem(int position) {
		mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:			
			break;

		case FAMILY_TREE_POS:
			Intent familyTreeIntent = new Intent(this, FamilyTreeActivity.class);
			startActivity(familyTreeIntent);
			break;
			
		case PHOTOS_POS:
			Intent photoIntent = new Intent(this, PhotoAlbumsActivity.class);
			startActivity(photoIntent);
			break;
			
		case NOTES_POS:

			break;
			
		case NEWS_POS:
			Intent newsIntent = new Intent(this, NewsfeedActivity.class);
			startActivity(newsIntent);
			break;
			
		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}
		
		this.mDrawerLayout.closeDrawer(this.mDrawerList);
	}

}
