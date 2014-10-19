package com.wazapps.familybox.profiles;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditProfileFragment extends Fragment {
	public static final String EDIT_PROFILE_FRAG = "edit profile fragment";

	public static final String EDIT_FAMILY_MEMBER_LIST = ProfileFragment.FAMILY_MEMBER_LIST;
	public static final String EDIT_MEMBER_ITEM = ProfileFragment.MEMBER_ITEM;

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
	private FamilyMemberDetails[] mFamilyMembersList;
	private FamilyMemberDetails mCurrentUserDetails;

	private EditProfileFamilyListAdapter mFamilyListAdapter;

	private LinearLayout mFamilyListHolder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			Parcelable[] ps = (Parcelable[]) args
					.getParcelableArray(EditProfileFragment.EDIT_FAMILY_MEMBER_LIST);
			
			mFamilyMembersList = new FamilyMemberDetails[ps.length];
			System.arraycopy(ps, 0, mFamilyMembersList, 0, ps.length);
			mCurrentUserDetails = (FamilyMemberDetails) args
					.getParcelable(EDIT_MEMBER_ITEM);
		} else {
			LogUtils.logWarning(getTag(), "argument didn't pass correctly");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile_edit, container,
				false);
		mEditImage = (RoundedImageView) root
				.findViewById(R.id.riv_edit_profile_change_pic);
		mName = (EditText) root.findViewById(R.id.et_edit_name);
		mCurrentFamilyName = (EditText) root.findViewById(R.id.et_edit_last);
		mNickname = (EditText) root.findViewById(R.id.et_edit_profile_nickname);
		mPreviousFamilyName = (EditText) root
				.findViewById(R.id.et_edit_previous_last_name);
		mMiddleName = (EditText) root.findViewById(R.id.et_edit_middle_name);
		mPhoneNumber = (EditText) root.findViewById(R.id.et_edit_phone);
		mBirthday = (EditText) root.findViewById(R.id.et_edit_birthday);
		mAddress = (EditText) root.findViewById(R.id.et_edit_profile_address);
		mFamilyListHolder = (LinearLayout)root.findViewById(R.id.ll_family_members_list_holder);
		initViews();
		initFamilyListView();
		return root;
	}
	private void initFamilyListView() {
		mFamilyListAdapter = new EditProfileFamilyListAdapter(this.getActivity(),
				mFamilyMembersList);
		for (int i = 0; i < mFamilyListAdapter.getCount(); i++) {
			View v = mFamilyListAdapter.getView(i, null, (ViewGroup) getView());
			mFamilyListHolder.addView(v);
		}
	}

	private void initViews() {
		if (mCurrentUserDetails != null) {
			mName.setText(mCurrentUserDetails.getName());
			mCurrentFamilyName.setText(mCurrentUserDetails.getLastName());
			mNickname.setText(mCurrentUserDetails.getNickname());
			mPreviousFamilyName.setText(mCurrentUserDetails
					.getPreviousLastName());
			mMiddleName.setText(mCurrentUserDetails.getMiddleName());
			mPhoneNumber.setText(mCurrentUserDetails.getPhoneNumber());
			mBirthday.setText(mCurrentUserDetails.getBirthday());
			mAddress.setText(mCurrentUserDetails.getAddress());
		}
	}
}
