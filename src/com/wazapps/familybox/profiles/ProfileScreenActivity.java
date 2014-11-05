package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import android.os.Bundle;

import com.splunk.mint.Mint;
import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.PhotoAlbumScreenActivity;
import com.wazapps.familybox.profiles.ProfileFragment.AddProfileFragmentListener;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class ProfileScreenActivity extends AbstractScreenActivity implements
		AddProfileFragmentListener {

	public static final String FAMILY_MEMBER_ARRAY_LIST = "family member array list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(ProfileScreenActivity.this, "ad50ec84");

		// get the activity arguments
		Bundle args = getIntent().getBundleExtra(ProfileFragment.PROFILE_DATA);
		ArrayList<FamilyMemberDetails> arrList = args
				.getParcelableArrayList(FAMILY_MEMBER_ARRAY_LIST);
		FamilyMemberDetails[] list = arrList
				.toArray(new FamilyMemberDetails[arrList.size()]);
		args.putParcelableArray(ProfileFragment.FAMILY_MEMBER_LIST, list);
		ProfileFragment profileFrag = new ProfileFragment();
		profileFrag.setArguments(args);
		// set the fragment to the container
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, profileFrag,
						ProfileFragment.PROFILE_FRAG).commit();

	}

	@Override
	public void addProfileFragment(Bundle args) {
		// create a new PhotoAlbumScreenFragment and give it the arguments
		ProfileFragment profileFrag = new ProfileFragment();
		profileFrag.setArguments(args);
		// set the fragment to the container
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, profileFrag,
						ProfileFragment.PROFILE_FRAG).addToBackStack(null)
				.commit();
	}
}
