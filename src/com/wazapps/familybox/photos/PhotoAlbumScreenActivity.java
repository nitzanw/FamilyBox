package com.wazapps.familybox.photos;

import android.os.Bundle;

import com.splunk.mint.Mint;
import com.wazapps.familybox.R;
import com.wazapps.familybox.splashAndLogin.LoginActivity;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class PhotoAlbumScreenActivity extends AbstractScreenActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(PhotoAlbumScreenActivity.this, "ad50ec84");
		// get the activity arguments
		Bundle args = getIntent().getBundleExtra
				(PhotoGridFragment.ALBUM_ITEM);
		// create a new PhotoAlbumScreenFragment and give it the arguments
		PhotoGridFragment photoAlbumFrag = new PhotoGridFragment();
		photoAlbumFrag.setArguments(args);
		// set the fragment to the container
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, photoAlbumFrag,
						PhotoGridFragment.PHOTO_ALBUM_SCREEN_FRAG)
				.commit();
	}
}
