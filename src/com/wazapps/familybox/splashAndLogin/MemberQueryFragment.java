package com.wazapps.familybox.splashAndLogin;

import java.util.ArrayList;

import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

import com.wazapps.familybox.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MemberQueryFragment extends Fragment {
	private View root;
	private RoundedImageView mMemberProfilePic;
	private Spinner mRelationPicker;
	private TextView mFamilyMemberName;
	private FamilyMemberDetails mFamilyMember;
	private ArrayList<String> mSpinnerOptions;
	
	public static final String FAMILY_MEMBER_ITEM = "family member item";
	public static final String FAMILY_MEMBER_RELATION_OPTIONS = "feamily member relation options";
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_member_query, 
				container, false);
		mMemberProfilePic = (RoundedImageView) 
				root.findViewById(R.id.riv_member_query_profile_picture);
		mRelationPicker = (Spinner) 
				root.findViewById(R.id.sp_member_query_relation_options);
		mFamilyMemberName = (TextView) 
				root.findViewById(R.id.tv_member_query_name);
		return root;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mFamilyMember = args.getParcelable(FAMILY_MEMBER_ITEM);
			mSpinnerOptions = args.getStringArrayList(FAMILY_MEMBER_RELATION_OPTIONS);
		} else {
			LogUtils.logWarning(getTag(), "member query arguments did not pass");
		}		
	}
	
	public void onResume() {
		MemberQuerySpinnerAdapter querySpinnerAdapter = new MemberQuerySpinnerAdapter(getActivity(), mSpinnerOptions);
		mRelationPicker.setAdapter(querySpinnerAdapter);
		mFamilyMemberName.setText(mFamilyMember.getName() + " " + mFamilyMember.getLastName());
		super.onResume();
	}
}
