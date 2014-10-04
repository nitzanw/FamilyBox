package com.wazapps.familybox;

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_photo_album, menu);
		final MenuItem searchMenuItem = menu.findItem(R.id.action_extra);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		menu.findItem(R.id.action_extra).setVisible(!drawerOpen);
		return true;
	}

}
