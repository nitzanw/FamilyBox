package com.wazapps.familybox.profiles;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.HorizontialListView;
import com.wazapps.familybox.util.LogUtils;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	public static final String PROFILE_FRAG = "profile fragment";
	public static final String MEMBER_ITEM = "member item";
	protected static final String FAMILY_MEMBER_LIST = "family member list";
	protected static final String PROFILE_DATA = "profile data";
	private View root;
	private HorizontialListView mFamilyList;
	private ListView mProfileDetailsList;
	private ProfileFamilyListAdapter familyListAdapter;
	private ProfileDetailsAdapter profileDetailsAdapter;
	private TextView mUserName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);

		mFamilyList = (HorizontialListView) root
				.findViewById(R.id.family_members_list);
		mProfileDetailsList = (ListView) root
				.findViewById(R.id.profile_details);

		mUserName = (TextView) root.findViewById(R.id.tv_profile_username);
		// Clear the listView's top highlight scrolling effect
		int glowDrawableId = root.getResources().getIdentifier(
				"overscroll_glow", "drawable", "android");
		Drawable androidGlow = root.getResources().getDrawable(glowDrawableId);
		androidGlow.setColorFilter(R.color.orange_fb, Mode.CLEAR);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();

		Bundle args = getArguments();
		if (args != null) {
			// get the data for the profile
			FamilyMemberDetails[] familyMemberList = (FamilyMemberDetails[]) args
					.getParcelableArray(FAMILY_MEMBER_LIST);
			FamilyMemberDetails selectedMember = (FamilyMemberDetails) args
					.getParcelable(MEMBER_ITEM);

			// init the detail adapter
			profileDetailsAdapter = new ProfileDetailsAdapter(getActivity(),
					selectedMember.getDetails());
			mProfileDetailsList.setAdapter(profileDetailsAdapter);
			familyListAdapter = new ProfileFamilyListAdapter (
					this.getActivity(), familyMemberList);
			mFamilyList.setAdapter(familyListAdapter);
			mUserName.setText(selectedMember.getName() + " "
					+ selectedMember.getLastName());
		} 
		
		else {
			LogUtils.logWarning(getTag(), "the args did not pass!!");
		}
	}

}
