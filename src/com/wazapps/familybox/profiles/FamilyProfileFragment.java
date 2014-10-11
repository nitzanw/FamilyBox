package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.util.HorizontialListView;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FamilyProfileFragment extends Fragment implements OnClickListener {

	private static final String CHILD_TYPE = "child";

	private static final int ITEM_TYPE = R.string.type;

	private static final int ITEM_POS = R.string.position;

	private static final String PARENT_TYPE = "parent";

	private View root;

	// private HorizontialListView mChildernList;
	private HorizontialListView mPhotoAlbumList;
	private FamilyProfileParentAdapter mParentAdapter;
	private LinearLayout mParentLayoutRight;
	private LinearLayout mParentLayoutLeft;

	private FamilyProfileChildAdapter mChildrenAdapter;

	private FamilyProfileAlbumAdapter mAlbumsAdapter;

	private TextView mFamilyTitle;

	private LinearLayout mChildrenHolder;

	private FamilyMemberDetails[] parentsList;

	private FamilyMemberDetails[] childrenList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_family_profile, container,
				false);
		mFamilyTitle = (TextView) root
				.findViewById(R.id.tv_family_profile_title);
		mParentLayoutRight = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_right);
		mParentLayoutLeft = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_left);
		mChildrenHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_children_holder);
		// mChildernList = (HorizontialListView) root
		// .findViewById(R.id.hlv_family_profile_children);
		mPhotoAlbumList = (HorizontialListView) root
				.findViewById(R.id.hlv_family_profile_albums);
		initViews();

		return root;
	}

	private void initViews() {
		initParentAndChildrenLists();

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

		// init the children level:
		mChildrenAdapter = new FamilyProfileChildAdapter(getActivity(),
				childrenList);
		for (int i = 0; i < childrenList.length; i++) {
			View v = mChildrenAdapter.getView(i, null, (ViewGroup) getView());
			v.setTag(ITEM_TYPE, CHILD_TYPE);
			v.setTag(ITEM_POS, i);
			v.setOnClickListener(this);
			mChildrenHolder.addView(v);
		}

		mParentAdapter = new FamilyProfileParentAdapter(getActivity(),
				parentsList);
		initParent(mParentLayoutRight, 0);
		initParent(mParentLayoutLeft, 1);

		mAlbumsAdapter = new FamilyProfileAlbumAdapter(getActivity(), albumList);
		mPhotoAlbumList.setAdapter(mAlbumsAdapter);

		// maybe the family parents does not have the same family name - hyphen
		// them!
		String familyName = parentsList[0].getLastName();
		if (!parentsList[0].getLastName().equals(parentsList[1].getLastName())) {
			familyName += " - " + parentsList[1].getLastName();
		}
		familyName += " " + getString(R.string.family);
		mFamilyTitle.setText(familyName);

	}

	private void initParent(LinearLayout base, int position) {
		View v = mParentAdapter.getView(position, null, (ViewGroup) getView());
		v.setTag(ITEM_TYPE, PARENT_TYPE);
		v.setTag(ITEM_POS, position);
		v.setOnClickListener(this);
		base.addView(v);

	}

	// TODO remove thus and add some real data
	private void initParentAndChildrenLists() {
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

		FamilyMemberDetails[] localParentsList = { dad, mom };
		FamilyMemberDetails[] localChildrenList = { child1, child2, child3,
				child4, child5 };
		parentsList = localParentsList;
		childrenList = localChildrenList;
	}

	@Override
	public void onClick(View v) {

		Intent profileIntent = new Intent(getActivity(),
				ProfileScreenActivity.class);
		Bundle args = new Bundle();
		int pos = (Integer) v.getTag(ITEM_POS);
		ArrayList<FamilyMemberDetails> familyMembers = createFamilyList(pos,
				(String) v.getTag(ITEM_TYPE));
		args.putParcelable(ProfileFragment.MEMBER_ITEM,
				getMemberItem((String) v.getTag(ITEM_TYPE), pos));
		args.putParcelableArrayList(
				ProfileScreenActivity.FAMILY_MEMBER_ARRAY_LIST, familyMembers);
		profileIntent.putExtra(ProfileFragment.PROFILE_DATA, args);
		getActivity().startActivity(profileIntent);

	}

	private FamilyMemberDetails getMemberItem(String type, int pos) {
		if (PARENT_TYPE.equals(type)) {
			return parentsList[pos];
		} else if (CHILD_TYPE.equals(type)) {
			return childrenList[pos];
		} else {
			LogUtils.logWarning(getTag(), "bad type entered");
			return null;
		}
	}

	private ArrayList<FamilyMemberDetails> createFamilyList(int pos, String type) {
		ArrayList<FamilyMemberDetails> familyMembers = new ArrayList<FamilyMemberDetails>();
		if (PARENT_TYPE.equals(type)) {
			for (int i = 0; i < childrenList.length; i++) {
				familyMembers.add(childrenList[i]);

			}
			for (int j = 0; j < parentsList.length; j++) {
				if (j != pos) {
					familyMembers.add(parentsList[j]);
				}
			}
		} else if (CHILD_TYPE.equals(type)) {
			for (int i = 0; i < childrenList.length; i++) {
				if (i != pos) {
					familyMembers.add(childrenList[i]);
				}
			}
			for (int j = 0; j < parentsList.length; j++) {
				familyMembers.add(parentsList[j]);
			}
		} else {
			LogUtils.logWarning(getTag(), "bad type entered");
		}
		return familyMembers;
	}
}
