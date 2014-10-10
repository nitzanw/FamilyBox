package com.wazapps.familybox.profiles;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.util.HorizontialListView;
import com.wazapps.familybox.util.RoundedImageView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FamilyProfileFragment extends Fragment {

	private View root;

	private HorizontialListView mChildernList;
	private HorizontialListView mPhotoAlbumList;
	private FamilyProfileParentAdapter mParentAdapter;
	private LinearLayout mParentLayoutRight;
	private LinearLayout mParentLayoutLeft;

	private FamilyProfileChildAdapter mChildrenAdapter;

	private FamilyProfileAlbumAdapter mAlbumsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_family_profile, container,
				false);
		mParentLayoutRight = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_right);
		mParentLayoutLeft = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_left);
		mChildernList = (HorizontialListView) root
				.findViewById(R.id.hlv_family_profile_children);
		mPhotoAlbumList = (HorizontialListView) root
				.findViewById(R.id.hlv_family_profile_albums);
		initViews();

		return root;
	}

	private void initViews() {
		FamilyMemberDetails dad = new FamilyMemberDetails("0", "",
				getString(R.string.father_name), getString(R.string.parent));
		FamilyMemberDetails mom = new FamilyMemberDetails("1", "",
				getString(R.string.mother_name), getString(R.string.parent));
		FamilyMemberDetails child1 = new FamilyMemberDetails("2", "",
				getString(R.string.user_name) + " 1", getString(R.string.child));
		FamilyMemberDetails child2 = new FamilyMemberDetails("3", "",
				getString(R.string.user_name) + " 2", getString(R.string.child));
		FamilyMemberDetails child3 = new FamilyMemberDetails("4", "",
				getString(R.string.user_name) + " 3", getString(R.string.child));
		FamilyMemberDetails child4 = new FamilyMemberDetails("5", "",
				getString(R.string.user_name) + " 4", getString(R.string.child));
		FamilyMemberDetails child5 = new FamilyMemberDetails("6", "",
				getString(R.string.user_name) + " 5", getString(R.string.child));

		final FamilyMemberDetails[] parentsList = { dad, mom };
		final FamilyMemberDetails[] childrenList = { child1, child2, child3,
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

		mChildrenAdapter = new FamilyProfileChildAdapter(getActivity(),
				childrenList);
		mChildernList.setAdapter(mChildrenAdapter);

		mParentAdapter = new FamilyProfileParentAdapter(getActivity(),
				parentsList);
		mParentLayoutRight.addView(mParentAdapter.getView(0, null,
				(ViewGroup) getView()));
		mParentLayoutLeft.addView(mParentAdapter.getView(1, null,
				(ViewGroup) getView()));
		
		mAlbumsAdapter = new FamilyProfileAlbumAdapter(getActivity(), albumList);
		mPhotoAlbumList.setAdapter(mAlbumsAdapter);

	}
}
