package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment.SignupScreenCallback;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.OnSwipeTouchListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoFragment extends Fragment {

	interface clickForCaptionListener {
		void imageClicked();
	}
	protected static final String PHOTO_DIALOG_FRAG = "photo dialog fragment";
	protected static final String PHOTO_ITEM = "photo item";
	private View root;
	// private PhotoItem[] photoList;
	// private TextView mImageCaption;
	// private FrameLayout mImage;
	// private boolean captionFrameOn = true;
	// private RelativeLayout mImageFrame;
	private PhotoItem photoItem;
	private ImageView mImage;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_image_dialog, container,
				false);
		// mImage = (FrameLayout) root.findViewById(R.id.fl_image_layout);
		// mImage.setOnClickListener(this);
		mImage = (ImageView) root.findViewById(R.id.iv_actual_image);

		// mImage.setOnTouchListener(new OnSwipeTouchListener() {
		// public boolean onSwipeTop() {
		//
		// return false;
		// }
		//
		// public boolean onSwipeRight() {
		// navigateLeft();
		// return true;
		// }
		//
		// public boolean onSwipeLeft() {
		// navigateRight();
		// return true;
		// }
		//
		// public boolean onSwipeBottom() {
		//
		// return false;
		// }
		// });

		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			photoItem = (PhotoItem) args.getParcelable(PHOTO_ITEM);
			// TODO load image
			// mImage.setBackground(((PhotoItem)photoList.get(firstPhotoIndex)).getUrl());

		} else {
			LogUtils.logWarning(getTag(),
					"the argument did not pass properlly!");
		}
	}

	// private void navigateRight() {
	// // increase the index by one to go right, if there are no more
	// // items, go back to the beginning of the list
	// mainPhotoIndex++;
	// if (mainPhotoIndex > photoList.length - 1) {
	// mainPhotoIndex = 0;
	// }
	// //
	// mImage.setBackground(((PhotoItem)photoList.get(mainPhotoIndex)).getUrl());
	// mImageCaption.setText(((PhotoItem) photoList[mainPhotoIndex])
	// .getCaption());
	// }

	// private void navigateLeft() {
	// // decrease the index by one to go left, if there are no more items,
	// // go back to the end of the list
	// mainPhotoIndex--;
	// if (mainPhotoIndex < 0) {
	// mainPhotoIndex = photoList.length - 1;
	// }
	// //
	// mImage.setBackground(((PhotoItem)photoList.get(mainPhotoIndex)).getUrl());
	// mImageCaption.setText(((PhotoItem) photoList[mainPhotoIndex])
	// .getCaption());
	// }
}