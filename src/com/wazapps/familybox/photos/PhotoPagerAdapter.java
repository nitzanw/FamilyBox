package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<String> photoIdList;

	public PhotoPagerAdapter(FragmentManager fm, ArrayList<String> photoIdList) {
		super(fm);
		this.photoIdList = photoIdList;
	}

	@Override
	public Fragment getItem(int position) {
		PhotoFragment photo = new PhotoFragment();
		Bundle args = new Bundle();
		args.putString(PhotoFragment.PHOTO_ITEM_ID, photoIdList.get(position));
		photo.setArguments(args);
		return photo;
	}

	@Override
	public int getCount() {

		return photoIdList.size();
	}
}
