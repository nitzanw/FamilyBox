package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class PhotoAlbumScreenActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_album_screen);
		// get the activity arguments
		Bundle args = getIntent().getBundleExtra(
				PhotoAlbumScreenFragment.ALBUM_ITEM);
		
		// create a new PhotoAlbumScreenFragment and give it the arguments
		PhotoAlbumScreenFragment photoAlbumFrag = new PhotoAlbumScreenFragment();
		photoAlbumFrag.setArguments(args);
		// set the fragment to the container
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, photoAlbumFrag,
						PhotoAlbumScreenFragment.PHOTO_ALBUM_SCREEN_FRAG)
				.commit();
		initActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		getActionBar().setIcon(
				getResources().getDrawable(
						R.drawable.action_bar_fc_icon_with_arrow));

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}
}
