package com.wazapps.familybox.profiles;

import java.util.ArrayList;
import java.util.Arrays;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.PhotoGridFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class ProfileScreenActivity extends FragmentActivity {

	public static final String FAMILY_MEMBER_ARRAY_LIST = "family member array list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		// get the activity arguments
		Bundle args = getIntent().getBundleExtra(ProfileFragment.PROFILE_DATA);
		ArrayList<FamilyMemberDetails> arrList = args
				.getParcelableArrayList(FAMILY_MEMBER_ARRAY_LIST);
		FamilyMemberDetails[] list = arrList
				.toArray(new FamilyMemberDetails[arrList.size()]);
		args.putParcelableArray(ProfileFragment.FAMILY_MEMBER_LIST, list);

		// create a new PhotoAlbumScreenFragment and give it the arguments
		ProfileFragment profileFrag = new ProfileFragment();
		profileFrag.setArguments(args);
		// set the fragment to the container
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, profileFrag,
						ProfileFragment.PROFILE_FRAG).commit();
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
