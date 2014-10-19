package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class EditProfileScreenActivity extends AbstractScreenActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// get the activity arguments
		Bundle args = getIntent().getBundleExtra(
				EditProfileFragment.EDIT_PROFILE_DATA);
		if (args != null) {
			EditProfileFragment editProfileFrag = new EditProfileFragment();
			editProfileFrag.setArguments(args);
			// set the fragment to the container
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_container, editProfileFrag,
							EditProfileFragment.EDIT_PROFILE_FRAG).commit();
			getActionBar().setTitle(getString(R.string.edit_profile));
		}

	}
}
