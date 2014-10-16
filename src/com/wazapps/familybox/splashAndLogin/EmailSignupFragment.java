package com.wazapps.familybox.splashAndLogin;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.RoundedImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

public class EmailSignupFragment extends Fragment 
implements OnClickListener, OnFocusChangeListener{
	private View root;
	private SignupScreenCallback signupCallback = null;
	private EditText birthday, firstName, lastName, password, passwordConfirm;
	
	private RoundedImageView uploadImage;
	private EditText email;

	public interface SignupScreenCallback {
		public void openBirthdayInputDialog();
		public void openPhonePhotoBrowsing();
		public void signUp(String firstName, String lastName, String email,
				String birthday, String password, String passwordConfirm);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.signupCallback = (SignupScreenCallback) getActivity();
		} 

		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement " +
					"SignupScreenCallback interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_signup_email, container, false);
		initViews();
		return root;
	}

	private void initViews() {
		this.uploadImage = (RoundedImageView) root.findViewById(R.id.riv_signup_upload_image);
		this.uploadImage.setOnClickListener(this);
		this.firstName = (EditText) root.findViewById(R.id.et_signup_name);
		this.lastName = (EditText) root.findViewById(R.id.et_signup_last);
		this.email = (EditText) root.findViewById(R.id.et_signup_email);
		this.birthday = (EditText) root.findViewById(R.id.et_signup_birthday);
		this.birthday.setOnFocusChangeListener(this);
		this.birthday.setOnClickListener(this);
		this.password = (EditText) root.findViewById(R.id.et_signup_password);
		this.passwordConfirm = (EditText) root.findViewById(R.id.et_signup_confirm_password);

		Button signupButton = (Button) root.findViewById(R.id.button_signup);
		signupButton.setOnClickListener(this);
	}

	/**
	 * Sets a date in the birthday field. used by the wrapping activity 
	 * to set a date via the BirthdaySignupDialogFragment
	 */
	public void setBirthday(String date) {
		birthday.setText(date);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_signup_birthday:
			signupCallback.openBirthdayInputDialog();
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;

		case R.id.riv_signup_upload_image:
			signupCallback.openPhonePhotoBrowsing();
			break;

		case R.id.button_signup:
			String firstNameContent, lastNameContent, emailContent, 
			birthdayContent, pwContent, pwConfirmContent;
			
			firstNameContent = firstName.getText().toString().trim();
			lastNameContent = lastName.getText().toString().trim();
			emailContent = email.getText().toString().trim();
			birthdayContent = birthday.getText().toString().trim();
			pwContent = password.getText().toString().trim();
			pwConfirmContent = passwordConfirm.getText().toString().trim();
			
			signupCallback.signUp(firstNameContent, lastNameContent, 
					emailContent, birthdayContent, pwContent, pwConfirmContent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.et_signup_birthday:
			if (hasFocus) {
				signupCallback.openBirthdayInputDialog();
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			break;

		default:
			break;
		}
	}

	public void setDrawable(Drawable d) {
		uploadImage.setBackground(d);
		uploadImage.setImageDrawable(d);
	}

	public void setBitmap(Bitmap myBitmap) {
		uploadImage.setImageBitmap(myBitmap);
		uploadImage.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
	}
}
