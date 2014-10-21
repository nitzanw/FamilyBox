package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PhotoPagerActivity extends FragmentActivity{
	protected static final String PHOTO_BUNDLE = "photo data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
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
