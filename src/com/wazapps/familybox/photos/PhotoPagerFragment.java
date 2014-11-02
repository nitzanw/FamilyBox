package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

public class PhotoPagerFragment extends Fragment implements OnClickListener {
	protected static final String PHOTO_FIRST_POS = "first photo pos";
	protected static final String PHOTO_ITEM_LIST = "photo list";
	protected static final String PHOTO_PAGER_FRAG = "photo pager dialog";
	protected static final String PHOTO_ALBUM_ID = "photo album id";
	protected static final String PHOTO_ID = "photo id";
	static ViewPager mPager;
	private View root;
	private PhotoPagerAdapter mAdapter;

	private int currentPosition;
	private RelativeLayout mImageFrame;
	private TextView mImageCaption;
	private boolean captionFrameOn = true;
	private FrameLayout mImage;
	private ImageView mEditButton;
	private ImageView mAcceptEdit;
	private EditText mImageEditCaption;
	private boolean onPageScrollStateChanged = false;
	private String albumId;
	private String firstPhotoId;
	private ArrayList<String> photoIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			photoIdList = args.getStringArrayList(PHOTO_ITEM_LIST);
			albumId = args.getString(PHOTO_ALBUM_ID);
			firstPhotoId = args.getString(PHOTO_ID);
			currentPosition = args.getInt(PHOTO_FIRST_POS);

		}
		if (!photoIdList.isEmpty()) {
			mAdapter = new PhotoPagerAdapter(getChildFragmentManager(),
					photoIdList);
		} else {
			LogUtils.logError(getTag(), "the photo id list is empty!!!");
		}
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
//				currentPosition = position;
//				ParseQuery<PhotoItem_ex> query = ParseQuery
//						.getQuery(PhotoItem_ex.class);
//
//				query.getInBackground(photoIdList.get(position),
//						new GetCallback<PhotoItem_ex>() {
//
//							@Override
//							public void done(PhotoItem_ex object,
//									ParseException e) {
//								if (null == e) {
//									object.fetchIfNeededInBackground(new GetCallback<PhotoItem_ex>() {
//
//										@Override
//										public void done(PhotoItem_ex object,
//												ParseException e) {
//											if (e == null) {
//												if (object.getObjectId() == photoIdList
//														.get(currentPosition)) {
//													mImageCaption.setText(object
//															.getCaption());
//												}
//											} else {
//												LogUtils.logError(getTag(),
//														e.getMessage());
//											}
//
//										}
//									});
//								} else {
//									LogUtils.logError(getTag(), e.getMessage());
//								}
//
//							}
//						});
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				onPageScrollStateChanged = true;
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
		mImageCaption = (TextView) root.findViewById(R.id.tv_image_caption);

		// set the image caption
		ParseQuery<PhotoItem_ex> query = ParseQuery
				.getQuery(PhotoItem_ex.class);
		query.getInBackground(photoIdList.get(currentPosition),
				new GetCallback<PhotoItem_ex>() {

					@Override
					public void done(PhotoItem_ex object, ParseException e) {
						if (e == null) {
							mImageCaption.setText(object.getCaption());
							mImageEditCaption.setText(object.getCaption());
						} else {
							LogUtils.logError(getTag(), e.getMessage());
						}
					}
				});

		return root;
	}

	private void makeFrameDisappear(final int timeInMilli) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!onPageScrollStateChanged) {
					// if the user hasen't moved the pager:
					captionFrameOn = false;
					mImageFrame.setVisibility(View.INVISIBLE);
				} else {
					// if the user has moved the pager, wait until he stops
					// moving it, and when he does, you can make the frame
					// disappear
					onPageScrollStateChanged = false;
					makeFrameDisappear(timeInMilli);
				}
			}
		}, timeInMilli);
	}

	private void itemClicked() {

		// toggle the visibility of the orange frame
		if (captionFrameOn) {
			mImageFrame.setVisibility(View.INVISIBLE);
		} else {
			mImageFrame.setVisibility(View.VISIBLE);
			makeFrameDisappear(5000);
		}
		captionFrameOn = !captionFrameOn;

	}

	@Override
	public void onClick(View v) {
		if (R.id.ib_right_arrow == v.getId()) {
			if (currentPosition == photoIdList.size() - 1) {
				return;
			} else {
				currentPosition++;
			}
			mPager.setCurrentItem(currentPosition);

			ParseQuery<PhotoItem_ex> query = ParseQuery
					.getQuery(PhotoItem_ex.class);
			query.getInBackground(photoIdList.get(currentPosition),
					new GetCallback<PhotoItem_ex>() {

						@Override
						public void done(PhotoItem_ex object, ParseException e) {
							// TODO Auto-generated method stub
							mImageCaption.setText(object.getCaption());

						}
					});

		} else if (R.id.ib_left_arrow == v.getId()) {
			if (currentPosition == 0) {
				return;
			} else {
				currentPosition--;
			}
			mPager.setCurrentItem(currentPosition);
			ParseQuery<PhotoItem_ex> query = ParseQuery
					.getQuery(PhotoItem_ex.class);
			query.getInBackground(photoIdList.get(currentPosition),
					new GetCallback<PhotoItem_ex>() {

						@Override
						public void done(PhotoItem_ex object, ParseException e) {
							// TODO Auto-generated method stub
							mImageCaption.setText(object.getCaption());

						}
					});
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

				ParseQuery<PhotoItem_ex> query = ParseQuery
						.getQuery(PhotoItem_ex.class);
				query.getInBackground(photoIdList.get(currentPosition),
						new GetCallback<PhotoItem_ex>() {

							private String text;

							@Override
							public void done(PhotoItem_ex object,
									ParseException e) {
								// TODO Auto-generated method stub
								mImageCaption.setText(object.getCaption());
								object.setObjectId(text);
							}

							GetCallback<PhotoItem_ex> init(String text) {
								this.text = text;
								return this;
							}
						}.init(text));

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
