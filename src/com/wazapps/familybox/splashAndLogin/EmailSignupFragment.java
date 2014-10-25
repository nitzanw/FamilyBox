package com.wazapps.familybox.splashAndLogin;

import java.util.Arrays;

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
import android.widget.LinearLayout;

public class EmailSignupFragment extends Fragment implements OnClickListener,
		OnFocusChangeListener {
	private View root;
	private SignupScreenCallback signupCallback = null;
	private EditText birthday, firstName, lastName, password, passwordConfirm,
			gender;

	private RoundedImageView uploadImage;
	private EditText email;
	private byte[] profilePictureData;
	private String profilePictureName;
	private LinearLayout signupProgress;

	public interface SignupScreenCallback {
		public void openBirthdayInputDialog();

		public void openGenderInputDialog();

		public void openPhonePhotoBrowsing();

		public void signUp(String firstName, String lastName, String email,
				String birthday, String gender, String password,
				String passwordConfirm, byte[] profilePictureData,
				String profilePictureName);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.signupCallback = (SignupScreenCallback) getActivity();
		}

		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement "
					+ "SignupScreenCallback interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_signup_email, container,
				false);
		initViews();
		profilePictureData = null;
		profilePictureName = "";
		return root;
	}

	private void initViews() {
		this.uploadImage = (RoundedImageView) root
				.findViewById(R.id.riv_signup_upload_image);
		this.uploadImage.setOnClickListener(this);
		this.firstName = (EditText) root.findViewById(R.id.et_signup_name);
		this.lastName = (EditText) root.findViewById(R.id.et_signup_last);
		this.email = (EditText) root.findViewById(R.id.et_signup_email);
		this.birthday = (EditText) root.findViewById(R.id.et_signup_birthday);
		this.birthday.setOnFocusChangeListener(this);
		this.birthday.setOnClickListener(this);
		this.gender = (EditText) root.findViewById(R.id.et_signup_gender);
		this.gender.setOnFocusChangeListener(this);
		this.gender.setOnClickListener(this);
		this.password = (EditText) root.findViewById(R.id.et_signup_password);
		this.passwordConfirm = (EditText) root
				.findViewById(R.id.et_signup_confirm_password);
		this.signupProgress = (LinearLayout) root
				.findViewById(R.id.ll_progress_spinner);

		Button signupButton = (Button) root.findViewById(R.id.button_signup);
		signupButton.setOnClickListener(this);
	}

	/**
	 * Sets a date in the birthday field. used by the wrapping activity to set a
	 * date via the BirthdaySignupDialogFragment
	 */
	public void setBirthday(String date) {
		this.birthday.setText(date);
	}

	/**
	 * Sets a gender in the gender field. used by the wrapping activity to set a
	 * date via the GenderSignupDialogFragment
	 */
	public void setGender(String gender) {
		this.gender.setText(gender);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_signup_birthday:
			signupCallback.openBirthdayInputDialog();
			InputMethodManager bimm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			bimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;

		case R.id.et_signup_gender:
			signupCallback.openGenderInputDialog();
			InputMethodManager gimm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			gimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;

		case R.id.riv_signup_upload_image:
			signupCallback.openPhonePhotoBrowsing();
			break;

		case R.id.button_signup:
			String firstNameContent,
			lastNameContent,
			emailContent,
			birthdayContent,
			genderContent,
			pwContent,
			pwConfirmContent;

			firstNameContent = firstName.getText().toString().trim()
					.toLowerCase();
			lastNameContent = lastName.getText().toString().trim()
					.toLowerCase();
			emailContent = email.getText().toString().trim().toLowerCase();
			birthdayContent = birthday.getText().toString().trim();
			genderContent = gender.getText().toString().trim().toLowerCase();
			pwContent = password.getText().toString().trim();
			pwConfirmContent = passwordConfirm.getText().toString().trim();

			// TODO: set a method for clearing problematic text fields (probably
			// using
			// exceptions or function return value

			signupCallback.signUp(firstNameContent, lastNameContent,
					emailContent, birthdayContent, genderContent, pwContent,
					pwConfirmContent, profilePictureData, profilePictureName);
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
				InputMethodManager bimm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				bimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			break;

		case R.id.et_signup_gender:
			if (hasFocus) {
				signupCallback.openGenderInputDialog();
				InputMethodManager gimm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				gimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}

		default:
			break;
		}
	}

	public void setDrawable(Drawable d) {
		uploadImage.setBackground(d);
		uploadImage.setImageDrawable(d);
	}

	public void setProfileImage(Bitmap fileBitmap, byte[] fileData,
			String filename) {
		uploadImage.setImageBitmap(fileBitmap);
		uploadImage.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		profilePictureData = Arrays.copyOf(fileData, fileData.length);
		profilePictureName = filename;
	}

	public void turnOnProgress() {
		signupProgress.setVisibility(View.VISIBLE);

	}

	public void turnOffProgress() {
		signupProgress.setVisibility(View.INVISIBLE);
	}
}
