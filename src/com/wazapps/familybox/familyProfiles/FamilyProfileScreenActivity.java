package com.wazapps.familybox.familyProfiles;

import com.wazapps.familybox.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class FamilyProfileScreenActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		FamilyProfileFragment familyProfileFrag = new FamilyProfileFragment();
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.fragment_container, familyProfileFrag,
				FamilyProfileFragment.TAG_FAMILY_PROFILE_FRAGMENT).commit();
		initActionBar();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initActionBar() {
		getActionBar().setIcon(
				getResources().getDrawable(
						R.drawable.action_bar_fc_icon_with_arrow));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}
}
