package com.wazapps.familybox.profiles;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

public class EditProfileFragment extends Fragment {
	public static final String EDIT_PROFILE_FRAG = "edit profile fragment";

	public static final String EDIT_FAMILY_MEMBER_LIST = 
			ProfileFragment.FAMILY_MEMBER_LIST;
	public static final String EDIT_MEMBER_ITEM = 
			ProfileFragment.MEMBER_ITEM;
	public static final String EDIT_PROFILE_DATA = 
			"edit profile data";

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
	private UserData[] mFamilyMembersList;
	private UserData mCurrentUserDetails;

	private EditProfileFamilyListAdapter mFamilyListAdapter;
	private LinearLayout mFamilyListHolder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			Parcelable[] memberList = (Parcelable[]) args
					.getParcelableArray(EditProfileFragment
							.EDIT_FAMILY_MEMBER_LIST);

			mFamilyMembersList = new UserData[memberList.length];
			System.arraycopy(memberList, 0, mFamilyMembersList, 0, 
					memberList.length);
			
			mCurrentUserDetails = (UserData) args
					.getParcelable(EDIT_MEMBER_ITEM);
			
			setHasOptionsMenu(true);
		} 
		
		else {
			LogUtils.logWarning(getTag(), 
					"argument didn't pass correctly");
		}
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
				this.getActivity(), mFamilyMembersList, mCurrentUserDetails);
		for (int i = 0; i < mFamilyListAdapter.getCount(); i++) {
			View v = mFamilyListAdapter.getView(i, null, (ViewGroup) getView());
			mFamilyListHolder.addView(v);
		}
	}

	private void initViews() {
		if (mCurrentUserDetails != null) {
			String firstName = InputHandler
					.capitalizeName(mCurrentUserDetails.getName());
			String lastName = InputHandler
					.capitalizeName(mCurrentUserDetails.getLastName());
			String nickName = InputHandler
					.capitalizeName(mCurrentUserDetails.getNickname());
			String prevLastName = InputHandler
					.capitalizeName(mCurrentUserDetails.getPreviousLastName());
			String middleName = InputHandler
					.capitalizeName(mCurrentUserDetails.getMiddleName());
			String phoneNumber = mCurrentUserDetails.getPhoneNumber();
			String birthday = mCurrentUserDetails.getBirthday();
			String address = mCurrentUserDetails.getAddress();
			Bitmap photo = mCurrentUserDetails.getprofilePhoto();
			
			mName.setText(firstName);
			mCurrentFamilyName.setText(lastName);
			mNickname.setText(nickName);
			mPreviousFamilyName.setText(prevLastName);
			mMiddleName.setText(middleName);
			mPhoneNumber.setText(phoneNumber);
			mBirthday.setText(birthday);
			mAddress.setText(address);
			
			if (photo != null) {
				mEditImage.setImageBitmap(photo);
				mEditImage.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));	
			}
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
			getActivity().finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
