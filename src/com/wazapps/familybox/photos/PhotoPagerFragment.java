package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoPagerFragment extends DialogFragment implements
		OnClickListener {
	protected static final String PHOTO_FIRST_POS = "first photo pos";
	protected static final String PHOTO_ITEM_LIST = "photo list";
	protected static final String PHOTO_PAGER_FRAG = "photo pager dialog";
	static CustomViewPager mPager;
	private View root;
	private PhotoPagerAdapter mAdapter;
	private PhotoItem[] photoList;
	private int currentPosition;
	private RelativeLayout mImageFrame;
	private TextView mImageCaption;
	private boolean captionFrameOn = true;
	private FrameLayout mImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			photoList = (PhotoItem[]) args.getParcelableArray(PHOTO_ITEM_LIST);
			currentPosition = args.getInt(PHOTO_FIRST_POS);
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
		mPager.setCurrentItem(currentPosition);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				currentPosition = position;
				mImageCaption.setText(photoList[currentPosition].getCaption());

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		// to make the orange frame appear and disappear:
		mPager.setOnTouchListener(new OnTouchListener() {
			float oldX = 0, newX = 0, sens = 5;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					oldX = event.getX();
					break;

				case MotionEvent.ACTION_UP:
					newX = event.getX();
					if (Math.abs(oldX - newX) < sens) {
						itemClicked();
						return true;
					}
					oldX = 0;
					newX = 0;
					break;
				}

				return false;
			}

		});
		mImage = (FrameLayout) root.findViewById(R.id.fl_image_layout);
		mImage.setOnClickListener(this);

		root.findViewById(R.id.ib_right_arrow).setOnClickListener(this);
		root.findViewById(R.id.ib_left_arrow).setOnClickListener(this);
		root.findViewById(R.id.iv_favorite_icon).setOnClickListener(this);
		root.findViewById(R.id.iv_share_icon).setOnClickListener(this);
		mImageFrame = (RelativeLayout) root.findViewById(R.id.rl_image_frame);

		mImageCaption = (TextView) root.findViewById(R.id.tv_image_caption);
		mImageCaption.setText(photoList[currentPosition].getCaption());
		return root;
	}

	private void itemClicked() {
		
		// toggle the visibility of the orange frame
		if (captionFrameOn) {
			mImageFrame.setVisibility(View.INVISIBLE);
		} else {
			mImageFrame.setVisibility(View.VISIBLE);
		}
		captionFrameOn = !captionFrameOn;

	}

	@Override
	public void onClick(View v) {
		if (R.id.ib_right_arrow == v.getId()) {
			if (currentPosition == photoList.length - 1) {
				return;
			} else {
				currentPosition++;
			}
			mPager.setCurrentItem(currentPosition);
			mImageCaption.setText(photoList[currentPosition].getCaption());
		} else if (R.id.ib_left_arrow == v.getId()) {
			if (currentPosition == 0) {
				return;
			} else {
				currentPosition--;
			}
			mPager.setCurrentItem(currentPosition);
			mImageCaption.setText(photoList[currentPosition].getCaption());
		} else if (R.id.iv_favorite_icon == v.getId()) {
			// TODO add to favorite album

		} else if (R.id.iv_share_icon == v.getId()) {

		}
	}
}
