package com.wazapps.familybox.photos;

import java.util.Timer;
import java.util.TimerTask;

import com.wazapps.familybox.R;
import com.wazapps.familybox.profiles.EditProfileFragment;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.util.LogUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoPagerFragment extends Fragment implements OnClickListener {
	protected static final String PHOTO_FIRST_POS = "first photo pos";
	protected static final String PHOTO_ITEM_LIST = "photo list";
	protected static final String PHOTO_PAGER_FRAG = "photo pager dialog";
	static ViewPager mPager;
	private View root;
	private PhotoPagerAdapter mAdapter;
	private PhotoItem[] photoList;
	private int currentPosition;
	private RelativeLayout mImageFrame;
	private TextView mImageCaption;
	private boolean captionFrameOn = true;
	private FrameLayout mImage;
	private ImageView mEditButton;
	private ImageView mAcceptEdit;
	private EditText mImageEditCaption;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {

			Parcelable[] ps = (Parcelable[]) args
					.getParcelableArray(PHOTO_ITEM_LIST);
			photoList = new PhotoItem[ps.length];
			System.arraycopy(ps, 0, photoList, 0, ps.length);
			currentPosition = args.getInt(PHOTO_FIRST_POS);
		}
		mAdapter = new PhotoPagerAdapter(getChildFragmentManager(), photoList);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = (View) inflater.inflate(R.layout.photo_pager_layout, container,
				false);
		mPager = (ViewPager) root.findViewById(R.id.photo_pager);
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
		root.findViewById(R.id.iv_back_icon).setOnClickListener(this);

		mEditButton = (ImageView) root.findViewById(R.id.iv_image_edit_caption);
		mEditButton.setOnClickListener(this);
		mAcceptEdit = (ImageView) root
				.findViewById(R.id.iv_accept_caption_edit);
		mAcceptEdit.setOnClickListener(this);

		mImageFrame = (RelativeLayout) root.findViewById(R.id.rl_image_frame);

		makeFrameDisappear(3000);

		mImageEditCaption = (EditText) root
				.findViewById(R.id.et_image_caption_edit);
		mImageEditCaption.setText(photoList[currentPosition].getCaption());
		mImageCaption = (TextView) root.findViewById(R.id.tv_image_caption);
		mImageCaption.setText(photoList[currentPosition].getCaption());
		return root;
	}

	private void makeFrameDisappear(int timeInMilli) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				captionFrameOn = false;
				mImageFrame.setVisibility(View.INVISIBLE);

			}
		}, timeInMilli);
	}

	private void itemClicked() {

		// toggle the visibility of the orange frame
		if (captionFrameOn) {
			mImageFrame.setVisibility(View.INVISIBLE);
		} else {
			mImageFrame.setVisibility(View.VISIBLE);
			makeFrameDisappear(6000);
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

		} else if (R.id.iv_image_edit_caption == v.getId()) {
			mEditButton.setVisibility(View.INVISIBLE);
			mAcceptEdit.setVisibility(View.VISIBLE);
			mImageEditCaption.setVisibility(View.VISIBLE);
			mImageCaption.setVisibility(View.INVISIBLE);

		} else if (R.id.iv_accept_caption_edit == v.getId()) {
			String text = mImageEditCaption.getText().toString();
			if (!TextUtils.isEmpty(text)) {

				photoList[currentPosition].setCaption(text);
				mImageCaption.setText(text);
			}
			mEditButton.setVisibility(View.VISIBLE);
			mAcceptEdit.setVisibility(View.INVISIBLE);
			mImageEditCaption.setVisibility(View.INVISIBLE);
			mImageCaption.setVisibility(View.VISIBLE);
		} else if (R.id.iv_back_icon == v.getId()) {
			getActivity().finish();
		}
	}
}
