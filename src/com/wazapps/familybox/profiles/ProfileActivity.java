package com.wazapps.familybox.profiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyProfiles.FamilyProfileActivity;
import com.wazapps.familybox.familyTree.FamilyTreeActivity;
import com.wazapps.familybox.newsfeed.NewsfeedActivity;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoAlbumsActivity;
import com.wazapps.familybox.photos.PhotoItem;


public class ProfileActivity extends ActivityWithDrawer {
	static final String TAG_PROFILE = "Profile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.profile_title);
		overridePendingTransition(R.anim.enter, R.anim.exit); // TODO: handle
																// transition
																// animations in
																// a better way
		ProfileFragment profileFrag = new ProfileFragment();
		setProfileArgsTemp(profileFrag);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, profileFrag, TAG_PROFILE);
		ft.commit();
	}

	// TODO remove when real data comes
	private void setProfileArgsTemp(ProfileFragment frag) {
		ProfileDetails[] profileDetailsData = { null, null, null, null };
		profileDetailsData[0] = (new ProfileDetails("Address",
				"K. yovel, mozkin st."));
		profileDetailsData[1] = (new ProfileDetails("Birthday", "19.10.1987"));
		profileDetailsData[2] = (new ProfileDetails("Previous Family Names",
				"No previous family names"));
		profileDetailsData[3] = (new ProfileDetails("Quotes",
				"For every every there exists exists"));

		FamilyMemberDetails dad = new FamilyMemberDetails("0", "",
				getString(R.string.father_name), "Zohar",
				getString(R.string.parent), profileDetailsData);
		FamilyMemberDetails mom = new FamilyMemberDetails("1", "",
				getString(R.string.mother_name), "Zohar",
				getString(R.string.parent), profileDetailsData);
		FamilyMemberDetails child1 = new FamilyMemberDetails("2", "",
				getString(R.string.name) + " 1", "Zohar",
				getString(R.string.child), profileDetailsData);
		FamilyMemberDetails child2 = new FamilyMemberDetails("3", "",
				getString(R.string.name) + " 2", "Zohar",
				getString(R.string.child), profileDetailsData);
		FamilyMemberDetails child3 = new FamilyMemberDetails("4", "",
				getString(R.string.name) + " 3", "Zohar",
				getString(R.string.child), profileDetailsData);
		FamilyMemberDetails child4 = new FamilyMemberDetails("5", "",
				getString(R.string.name) + " 4", "Zohar",
				getString(R.string.child), profileDetailsData);
		FamilyMemberDetails child5 = new FamilyMemberDetails("6", "",
				getString(R.string.name) + " 5", "Zohar",
				getString(R.string.child), profileDetailsData);

		final FamilyMemberDetails[] parentsList = { dad, mom };
		final FamilyMemberDetails[] childrenList = { child1, child2, child3,
				child4, child5 };
		final FamilyMemberDetails[] child1Family = { dad, mom, child2, child3,
				child4, child5 };

		AlbumItem[] albumList = { null, null, null, null, null, null };
		String albumName = "Temp Album Name ";
		PhotoItem[] tempData = { null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null };

		for (int i = 0; i < 18; i++) {
			tempData[i] = new PhotoItem("11.2.201" + i, "www.bla.com",
					"This is me and my friend Dan " + i);
		}

		for (int i = 0; i < 6; i++) {

			albumList[i] = new AlbumItem(String.valueOf(i), tempData, albumName
					+ i, "December 201" + i);
		}
		Bundle args = new Bundle();
		args.putParcelable(ProfileFragment.MEMBER_ITEM, child1);
		args.putParcelableArray(ProfileFragment.FAMILY_MEMBER_LIST,
				child1Family);
		frag.setArguments(args);
	}

	@Override
	public void selectItem(int position) {
		mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:
			break;
		case MY_FAMILY_PROFILE_POS:
			Intent familyProfileIntent = new Intent(this,
					FamilyProfileActivity.class);
			startActivity(familyProfileIntent);
			break;
		case FAMILY_TREE_POS:
			Intent familyTreeIntent = new Intent(this, FamilyTreeActivity.class);
			startActivity(familyTreeIntent);
			break;

		case PHOTOS_POS:
			Intent photoIntent = new Intent(this, PhotoAlbumsActivity.class);
			startActivity(photoIntent);
			break;

		case NOTES_POS:

			break;

		case NEWS_POS:
			Intent newsIntent = new Intent(this, NewsfeedActivity.class);
			startActivity(newsIntent);
			break;

		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}

		this.mDrawerLayout.closeDrawer(this.mDrawerList);
	}

}
