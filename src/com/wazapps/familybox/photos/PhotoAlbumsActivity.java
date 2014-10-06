package com.wazapps.familybox.photos;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;
import com.wazapps.familybox.newsfeed.NewsfeedActivity;
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
		overridePendingTransition(R.anim.enter, R.anim.exit); //TODO: handle transition animations in a better way
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new PhotoAlbumsTabsFragment(), TAG_PHOTO_ALBUM);
		ft.commit();
	}

	@Override
	public void selectItem(int position) {
		this.mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:
			Intent intent1 = new Intent(this, ProfileActivity.class);
			startActivity(intent1);
			break;
			
		case FAMILY_TREE_POS:

			break;
			
		case PHOTOS_POS:
			TabsFragment frag = (TabsFragment) getSupportFragmentManager()
			.findFragmentByTag(TAG_PHOTO_ALBUM);
			frag.switchTab(0);
			break;
			
		case NOTES_POS:

			break;
			
		case NEWS_POS:
			Intent intent2 = new Intent(this, NewsfeedActivity.class);
			startActivity(intent2);
			break;
			
		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}
		
		this.mDrawerLayout.closeDrawer(this.mDrawerList);
	}

}
