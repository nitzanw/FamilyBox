package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.AbstractScreenActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class PhotoAlbumScreenActivity extends AbstractScreenActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

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
