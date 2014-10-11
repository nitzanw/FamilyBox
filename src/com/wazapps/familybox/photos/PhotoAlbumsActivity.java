package com.wazapps.familybox.photos;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;
import com.wazapps.familybox.familyTree.FamilyTreeActivity;
import com.wazapps.familybox.newsfeed.NewsfeedActivity;
import com.wazapps.familybox.profiles.FamilyProfileActivity;
import com.wazapps.familybox.profiles.ProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class PhotoAlbumsActivity extends ActivityWithDrawer {
	static final String TAG_PHOTO_ALBUM = "photoAlbum";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.photo_albums);
		overridePendingTransition(R.anim.enter, R.anim.exit); 
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new PhotoAlbumsTabsFragment(),
				TAG_PHOTO_ALBUM);
		ft.commit();
	}

	@Override
	public void selectItem(int position) {
		this.mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:
			Intent profileIntent = new Intent(this, ProfileActivity.class);
			startActivity(profileIntent);
			break;
		case MY_FAMILY_PROFILE_POS:
			Intent familyProfileIntent = new Intent(this,
					FamilyProfileActivity.class);
			startActivity(familyProfileIntent);
			break;
		case FAMILY_TREE_POS:
			Intent familyTreeIntent = new Intent(this, FamilyTreeActivity.class);
			startActivity(familyTreeIntent);
			break;

		case PHOTOS_POS:
			TabsFragment frag = (TabsFragment) getSupportFragmentManager()
					.findFragmentByTag(TAG_PHOTO_ALBUM);
			frag.switchTab(0);
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
