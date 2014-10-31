package com.wazapps.familybox.profiles;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

public class EditProfileFragment extends Fragment 
implements OnClickListener, OnFocusChangeListener {

	public interface EditProfileCallback {
		public void openBirthdayDialog();
		public void openPhonePhotoBrowsing();
	}

	public static final String EDIT_PROFILE_FRAG = "edit profile fragment";
	public static final String EDIT_PROFILE_DATA = "edit profile data";

	private View root;
	private RoundedImageView mEditImage;
	private EditText mName;
	private EditText mCurrentFamilyName;
	private EditText mNickname;
	private EditText mPreviousFamilyName;
	private EditText mMiddleName;
	private EditText mPhoneNumber;
	private EditText mBirthday;
	private EditText mAddress;
	private LinearLayout mFamilyListHolder;
	private UserHandler userHandler;

	private ArrayList<UserData> mFamilyMembersData = null;
	private ArrayList<ParseUser> mFamilyMembers = null;
	private byte[] profilePictureData = null;
	private UserData mCurrentUserData;
	private ParseUser mCurrentUser;
	String profilePictureName;
	private boolean pictureUpdated = false;

	private EditProfileFamilyListAdapter mFamilyListAdapter;
	private EditProfileCallback editCallback;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.editCallback = (EditProfileCallback) getActivity();
		}

		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement "
					+ "EditProfileCallback interface");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		userHandler = new UserHandler();
		mCurrentUser = ParseUser.getCurrentUser();
		if (mCurrentUser == null) {
			//TODO: handle error
		}

		mCurrentUserData = new UserData(mCurrentUser, UserData.ROLE_UNDEFINED);
		mCurrentUserData.downloadProfilePicSync(mCurrentUser);
		mFamilyMembers = new ArrayList<ParseUser>();
		mFamilyMembersData = new ArrayList<UserData>();	
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile_edit, 
				container,false);
		mEditImage = (RoundedImageView) 
				root.findViewById(R.id.riv_edit_profile_change_pic);
		mName = (EditText) 
				root.findViewById(R.id.et_edit_name);
		mCurrentFamilyName = (EditText) 
				root.findViewById(R.id.et_edit_last);
		mNickname = (EditText) 
				root.findViewById(R.id.et_edit_profile_nickname);
		mPreviousFamilyName = (EditText) 
				root.findViewById(R.id.et_edit_previous_last_name);
		mMiddleName = (EditText) 
				root.findViewById(R.id.et_edit_middle_name);
		mPhoneNumber = (EditText) 
				root.findViewById(R.id.et_edit_phone);
		mBirthday = (EditText) 
				root.findViewById(R.id.et_edit_birthday);
		mAddress = (EditText) 
				root.findViewById(R.id.et_edit_profile_address);
		mFamilyListHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_members_list_holder);

		initViews();
		initFamilyListView();
		return root;
	}

	private void initFamilyListView() {
		mFamilyListAdapter = new EditProfileFamilyListAdapter(
				this.getActivity(), mFamilyMembersData.toArray(
						new UserData[mFamilyMembersData.size()]), 
						mCurrentUserData);

		for (int i = 0; i < mFamilyListAdapter.getCount(); i++) {
			View v = mFamilyListAdapter.getView(i, null, (ViewGroup) getView());
			mFamilyListHolder.addView(v);
		}
	}

	private void initViews() {
		if (mCurrentUserData != null) {
			String firstName = InputHandler
					.capitalizeName(mCurrentUserData.getName());
			String lastName = InputHandler
					.capitalizeName(mCurrentUserData.getLastName());
			String nickName = InputHandler
					.capitalizeName(mCurrentUserData.getNickname());
			String prevLastName = InputHandler
					.capitalizeName(mCurrentUserData.getPreviousLastName());
			String middleName = InputHandler
					.capitalizeName(mCurrentUserData.getMiddleName());
			String phoneNumber = mCurrentUserData.getPhoneNumber();
			String birthday = mCurrentUserData.getBirthday();
			String address = mCurrentUserData.getAddress();
			Bitmap photo = mCurrentUserData.getprofilePhoto();

			mName.setText(firstName);
			mCurrentFamilyName.setText(lastName);
			mNickname.setText(nickName);
			mPreviousFamilyName.setText(prevLastName);
			mMiddleName.setText(middleName);
			mPhoneNumber.setText(phoneNumber);
			mBirthday.setText(birthday);
			mBirthday.setOnClickListener(this);
			mBirthday.setOnFocusChangeListener(this);
			mAddress.setText(address);

			if (photo != null) {
				mEditImage.setImageBitmap(photo);
				mEditImage.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));	
			}

			mEditImage.setOnClickListener(this);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu.findItem(R.id.action_accept) == null) {
			inflater.inflate(R.menu.menu_accept, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			boolean prevFamilyUpdated = handleUserEdit();
			
			if (!prevFamilyUpdated) {
				getActivity().finish();				
			} else {
				
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean handleUserEdit() {
		boolean detailsUpdated = false;
		boolean prevFamilyUpdated = false;
		
		String nickname = mNickname.getText().toString().toLowerCase().trim();
		String prevLastName = mPreviousFamilyName.getText().toString().toLowerCase().trim();
		String middleName = mMiddleName.getText().toString().toLowerCase().trim();
		String phoneNumber = mPhoneNumber.getText().toString().trim();
		String birthday = mBirthday.getText().toString();
		String address = mAddress.getText().toString().toLowerCase().trim();
		
		if (!mCurrentUserData.getNickname().equals(nickname)) {
			detailsUpdated = true;
			mCurrentUser.put(UserHandler.NICKNAME_KEY, nickname);
		}
		
		if (!mCurrentUserData.getPreviousLastName().equals(prevLastName)) {
			detailsUpdated = true;
			prevFamilyUpdated = true;
			mCurrentUser.put(UserHandler.PREV_LAST_NAME_KEY, prevLastName);
		}
		
		if (!mCurrentUserData.getMiddleName().equals(middleName)) {
			detailsUpdated = true;
			mCurrentUser.put(UserHandler.MIDDLE_NAME_KEY, middleName);
		}
		
		if (!mCurrentUserData.getPhoneNumber().equals(phoneNumber)) {
			detailsUpdated = true;
			mCurrentUser.put(UserHandler.PHONE_NUMBER_KEY, phoneNumber);
		}
		
		if (!mCurrentUserData.getBirthday().equals(birthday)) {
			detailsUpdated = true;
			mCurrentUser.put(UserHandler.BIRTHDATE_KEY, birthday);
		}
		
		if (!mCurrentUserData.getAddress().equals(address)) {
			detailsUpdated = true;
			mCurrentUser.put(UserHandler.ADDRESS_KEY, address);
		}
		
		if (detailsUpdated) {
			mCurrentUser.saveEventually();
		}
		
		return prevFamilyUpdated;
	}

	public void setDate(String date) {
		mBirthday.setText(date);
	}
	
	public void setProfileImage(Bitmap bitmap, byte[] fileData, 
			String filename) {
		mEditImage.setImageBitmap(bitmap);
		mEditImage.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));	
		profilePictureData = Arrays.copyOf(fileData, fileData.length);
		profilePictureName = filename;
		pictureUpdated = true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_edit_birthday:
			editCallback.openBirthdayDialog();
			InputMethodManager bimm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			bimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;

		case R.id.riv_edit_profile_change_pic:
			editCallback.openPhonePhotoBrowsing();
			break;

		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.et_edit_birthday:
			if (hasFocus) {
				editCallback.openBirthdayDialog();
				InputMethodManager bimm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				bimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}

			break;

		default:
			break;
		}
	}
}
