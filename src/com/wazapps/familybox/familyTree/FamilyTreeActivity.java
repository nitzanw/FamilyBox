package com.wazapps.familybox.familyTree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.newsfeed.NewsfeedActivity;
import com.wazapps.familybox.photos.PhotoAlbumsActivity;
import com.wazapps.familybox.profiles.FamilyProfileActivity;
import com.wazapps.familybox.profiles.ProfileActivity;
import com.wazapps.familybox.profiles.ProfileFragment;

public class FamilyTreeActivity extends ActivityWithDrawer {
	static final String TAG_FAMILY_TREE = "Family Tree";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.family_tree_title);
		overridePendingTransition(R.anim.enter, R.anim.exit); // TODO: handle
																// transition
																// animations in
																// a better way
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new FamiliesListFragment(), TAG_FAMILY_TREE);
		ft.commit();
	}

	@Override
	public void selectItem(int position) {
		mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:
			Intent ProfileIntent = new Intent(this, ProfileActivity.class);
			startActivity(ProfileIntent);
			break;
		case MY_FAMILY_PROFILE_POS:
			Intent familyProfileIntent = new Intent(this,
					FamilyProfileActivity.class);
			startActivity(familyProfileIntent);
			break;
		case FAMILY_TREE_POS:
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
