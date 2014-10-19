package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.splashAndLogin.EmailLoginDialogueFragment.EmailLoginScreenCallback;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

import android.util.Log;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements OnClickListener {
	public static final String PROFILE_FRAG = "profile fragment";
	public static final String MEMBER_ITEM = "member item";
	public static final String FAMILY_MEMBER_LIST = "family member list";
	public static final String PROFILE_DATA = "profile data";
	private static final String MEMBER_ITEM_TYPE = "family member item";

	private static final int ITEM_TYPE = R.string.type;
	private static final int ITEM_POS = R.string.position;

	private View root;
	private LinearLayout mFamilyListHolder;
	private ListView mProfileDetailsList;
	private ProfileFamilyListAdapter mFamilyListAdapter;
	private ProfileDetailsAdapter mProfileDetailsAdapter;
	private FamilyMemberDetails[] mFamilyMembersList;
	private FamilyMemberDetails mCurrentUserDetails;
	private TextView mUserName;
	AddProfileFragmentListener addProfileCallback = null;
	private RoundedImageView mUserPhoto;

	public interface AddProfileFragmentListener {
		void addProfileFragment(Bundle args);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			addProfileCallback = (AddProfileFragmentListener) getActivity();
		}

		catch (ClassCastException e) {
			Log.e("loginCallbackErr",
					"Activity should implement AddProfileFragmentListener interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);

		mFamilyListHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_members_list_holder);

		mProfileDetailsList = (ListView) root
				.findViewById(R.id.profile_details);

		mUserName = (TextView) root.findViewById(R.id.tv_profile_username);
		mUserPhoto = (RoundedImageView) root
				.findViewById(R.id.riv_profile_image);
		// Clear the listView's top highlight scrolling effect
		int glowDrawableId = root.getResources().getIdentifier(
				"overscroll_glow", "drawable", "android");
		Drawable androidGlow = root.getResources().getDrawable(glowDrawableId);
		androidGlow.setColorFilter(R.color.orange_fb, Mode.CLEAR);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			// get the data for the profile
			mFamilyMembersList = (FamilyMemberDetails[]) args
					.getParcelableArray(FAMILY_MEMBER_LIST);
			mCurrentUserDetails = (FamilyMemberDetails) args
					.getParcelable(MEMBER_ITEM);
		}

		else {
			LogUtils.logWarning(getTag(), "profile arguments did not pass");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mFamilyMembersList != null) {
			initProfileDetailsViews();
			initFamilyListView();
		}
	}

	private void initProfileDetailsViews() {
		mUserName.setText(mCurrentUserDetails.getName() + " "
				+ mCurrentUserDetails.getLastName());
		mProfileDetailsAdapter = new ProfileDetailsAdapter(getActivity(),
				mCurrentUserDetails.getDetails());
		mProfileDetailsList.setAdapter(mProfileDetailsAdapter);
	}

	private void initFamilyListView() {
		mFamilyListAdapter = new ProfileFamilyListAdapter(this.getActivity(),
				mFamilyMembersList);
		for (int i = 0; i < mFamilyListAdapter.getCount(); i++) {
			View v = mFamilyListAdapter.getView(i, null, (ViewGroup) getView());
			v.setTag(ITEM_TYPE, MEMBER_ITEM_TYPE);
			v.setTag(ITEM_POS, i);
			v.setOnClickListener(this);
			mFamilyListHolder.addView(v);
		}
	}

	@Override
	public void onClick(View v) {

		Bundle args = new Bundle();
		int pos = (Integer) v.getTag(ITEM_POS); // get the current item position
												// in family list
		FamilyMemberDetails[] familyMembers = createFamilyList(pos);
		FamilyMemberDetails clickedUserDetails = mFamilyMembersList[pos];
		args.putParcelable(MEMBER_ITEM, clickedUserDetails);
		args.putParcelableArray(FAMILY_MEMBER_LIST, familyMembers);
		addProfileCallback.addProfileFragment(args);
	}

	/**
	 * This will be removed
	 */
	private FamilyMemberDetails[] createFamilyList(int pos) {
		// TODO: add real data
		ArrayList<FamilyMemberDetails> familyMembers = new ArrayList<FamilyMemberDetails>();
		for (int i = 0; i < mFamilyMembersList.length; i++) {
			if (i != pos) {
				familyMembers.add(mFamilyMembersList[i]);
			}
		}
		familyMembers.add(mCurrentUserDetails);
		FamilyMemberDetails[] arr = new FamilyMemberDetails[familyMembers
				.size()];
		return familyMembers.toArray(arr);
	}
}
