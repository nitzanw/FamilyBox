package com.wazapps.familybox.familyProfiles;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.AbstractScreenActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

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
