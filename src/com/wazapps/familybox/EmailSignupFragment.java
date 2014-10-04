package com.wazapps.familybox;

import com.wazapps.familybox.util.RoundedImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.DocumentsContract.Root;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class EmailSignupFragment extends Fragment implements OnClickListener {
	private View root;
	private signupCallbackListener signupCallback = null;
	private EditText birthdayView;
	private RoundedImageView uploadImage;

	public interface signupCallbackListener {
		public void openBirthdayInputDialog();

		public void openPhonePhotoBrowsing();

		// TODO remove this to correct location
		public void enterApp();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			signupCallback = (signupCallbackListener) getActivity();
		} catch (ClassCastException e) {
			Log.e(getTag(),
					"the activity did not implement signupCallbackListener interface");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_signup_email, container,
				false);

		initViews();

		return root;
	}

	private void initViews() {
		this.uploadImage = (RoundedImageView) root
				.findViewById(R.id.riv_signup_upload_image);
		this.uploadImage.setOnClickListener(this);
		EditText firstNameView = (EditText) root
				.findViewById(R.id.et_signup_name);
		EditText lastNameView = (EditText) root
				.findViewById(R.id.et_signup_last);
		EditText addressView = (EditText) root
				.findViewById(R.id.et_signup_address);
		this.birthdayView = (EditText) root
				.findViewById(R.id.et_signup_birthday);
		birthdayView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					signupCallback.openBirthdayInputDialog();
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				}
			}
		});
		this.birthdayView.setOnClickListener(this);

		EditText middleNameView = (EditText) root
				.findViewById(R.id.et_signup_middle_name);
		EditText previousView = (EditText) root
				.findViewById(R.id.et_signup_previous_last);
		EditText phoneNumView = (EditText) root
				.findViewById(R.id.et_signup_phone);
		Button signupButton = (Button) root.findViewById(R.id.button_signup);
		signupButton.setOnClickListener(this);
	}

	public void setBirthday(String date) {
		birthdayView.setText(date);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.et_signup_birthday) {
			signupCallback.openBirthdayInputDialog();
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} else if (v.getId() == R.id.riv_signup_upload_image) {
			signupCallback.openPhonePhotoBrowsing();
		} else if (v.getId() == R.id.button_signup) {
			signupCallback.enterApp();
		}

	}

	public void setDrawable(Drawable d) {
		this.uploadImage.setBackground(d);
		this.uploadImage.setImageDrawable(d);
	}

	public void setBitmap(Bitmap myBitmap) {
		this.uploadImage.setImageBitmap(myBitmap);
		this.uploadImage.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

	}
}
