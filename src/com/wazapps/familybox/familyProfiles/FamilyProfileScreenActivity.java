package com.wazapps.familybox.familyProfiles;

import android.os.Bundle;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class FamilyProfileScreenActivity extends AbstractScreenActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FamilyProfileFragment familyProfileFrag = new FamilyProfileFragment();
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.fragment_container, familyProfileFrag,
				FamilyProfileFragment.FAMILY_PROFILE_FRAGMENT).commit();
	}
	
}
