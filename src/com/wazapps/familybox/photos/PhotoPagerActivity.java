package com.wazapps.familybox.photos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.splunk.mint.Mint;
import com.wazapps.familybox.R;

public class PhotoPagerActivity extends FragmentActivity {
	protected static final String PHOTO_BUNDLE = "photo data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(PhotoPagerActivity.this, "ad50ec84");
		setContentView(R.layout.activity_container);

		Bundle args = getIntent().getBundleExtra(PHOTO_BUNDLE);
		PhotoPagerFragment photoDialog = new PhotoPagerFragment();

		photoDialog.setArguments(args);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, photoDialog,
						PhotoPagerFragment.PHOTO_PAGER_FRAG).commit();

	}
}
