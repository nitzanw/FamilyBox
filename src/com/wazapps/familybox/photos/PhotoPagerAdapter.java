package com.wazapps.familybox.photos;

import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {
	


	private PhotoItem[] photoList;

	public PhotoPagerAdapter(FragmentManager fm, PhotoItem[] photoList) {
		super(fm);
		this.photoList = Arrays.copyOf(photoList, photoList.length,
				PhotoItem[].class);
	}

	@Override
	public Fragment getItem(int position) {
		PhotoFragment photo = new PhotoFragment();
		Bundle args = new Bundle();
		args.putParcelable(PhotoFragment.PHOTO_ITEM, photoList[position]);
		photo.setArguments(args);
		return photo;
	}

	@Override
	public int getCount() {

		return photoList.length;
	}
}
