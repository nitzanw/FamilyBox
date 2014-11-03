package com.wazapps.familybox.familyProfiles;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.wazapps.familybox.R;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment.AddFamilyProfileFragmentListener;
import com.wazapps.familybox.profiles.ProfileFragment;
import com.wazapps.familybox.profiles.EditProfileFragment.EditProfileCallback;
import com.wazapps.familybox.profiles.ProfileFragment.AddProfileFragmentListener;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class FamilyProfileScreenActivity extends AbstractScreenActivity 
implements AddFamilyProfileFragmentListener, 
AddProfileFragmentListener {
	public static String FAMILY_PROFILE_ARGS = "family profile args";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Family Profile");
		overridePendingTransition(R.anim.enter, R.anim.exit);
		
		Bundle args = getIntent().getBundleExtra(FAMILY_PROFILE_ARGS);
		FamilyProfileFragment familyProfileFrag = new FamilyProfileFragment();
		familyProfileFrag.setArguments(args);
		
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fragment_container, familyProfileFrag,
				FamilyProfileFragment.FAMILY_PROFILE_FRAGMENT).commit();
	}
	@Override
	public void addProfileFragment(Bundle args) {
		ProfileFragment profileFrag = new ProfileFragment();
		profileFrag.setArguments(args);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_reverse,
				R.anim.fade_out_fast);
		ft.add(R.id.fragment_container, profileFrag,
				ProfileFragment.PROFILE_FRAG).addToBackStack(null);
		ft.commit();
	}
	
	@Override
	public void addFamilyProfileFragment(Bundle args) {
		FamilyProfileFragment familyProfileFrag = new FamilyProfileFragment();
		familyProfileFrag.setArguments(args);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_reverse,
				R.anim.fade_out_fast);
		ft.add(R.id.fragment_container, familyProfileFrag,
				ProfileFragment.PROFILE_FRAG).addToBackStack(null);
		ft.commit();
	}
}
