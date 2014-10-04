package com.wazapps.familybox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PhotoAlbumsActivity extends ActivityWithDrawer {

	static final String TAG_PHOTO_ALBUM = "photoAlbum";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new PhotoAlbumFragment(), TAG_PHOTO_ALBUM);
		ft.commit();
	}

	public void initDrawer() {
		super.initDrawer();
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon

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
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.content_frame, new PhotoAlbumFragment(),
					TAG_PHOTO_ALBUM);
			ft.commit();
			break;
		case NOTES_POS:

			break;
		case NEWS_POS:
			Intent intent = new Intent(this, NewsfeedActivity.class);
			startActivity(intent);
			break;
		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);

	}

}
