package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class PhotoPagerFragment extends DialogFragment{
	protected static final String PHOTO_FIRST_POS = "first photo pos";
	protected static final String PHOTO_ITEM_LIST = "photo list";
	protected static final String PHOTO_PAGER_FRAG = "photo pager dialog";
	static CustomViewPager mPager;
	private View root;
	private PhotoPagerAdapter mAdapter;
	private PhotoItem[] photoList;
	private int firstPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			photoList = (PhotoItem[]) args.getParcelableArray(PHOTO_ITEM_LIST);
			firstPosition = args.getInt(PHOTO_FIRST_POS);
		}
		mAdapter = new PhotoPagerAdapter(getChildFragmentManager(), photoList);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		root = (View) inflater.inflate(R.layout.photo_pager_layout, container,
				false);
		mPager = (CustomViewPager) root.findViewById(R.id.photo_pager);
		mPager.setAdapter(mAdapter);
		return root;
	}
}
