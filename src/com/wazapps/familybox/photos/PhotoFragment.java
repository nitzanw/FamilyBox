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

	protected static final String PHOTO_DIALOG_FRAG = "photo dialog fragment";
	protected static final String PHOTO_ITEM = "photo item";
	private View root;
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
}
