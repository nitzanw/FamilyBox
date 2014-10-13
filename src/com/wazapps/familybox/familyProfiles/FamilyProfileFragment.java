package com.wazapps.familybox.familyProfiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoAlbumScreenActivity;
import com.wazapps.familybox.photos.PhotoGridFragment;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.ProfileDetails;
import com.wazapps.familybox.profiles.ProfileFragment;
import com.wazapps.familybox.profiles.ProfileScreenActivity;
import com.wazapps.familybox.util.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FamilyProfileFragment extends Fragment implements OnClickListener {
	private static final int ITEM_TYPE = R.string.type;
	private static final int ITEM_POS = R.string.position;
	private static final int FAMILY_MEMBER_TYPE = R.string.family;

	public static final String TAG_FAMILY_PROFILE_FRAGMENT = "family profile fragment";
	private static final String FAMILY_MEMBER_CHILD_TYPE = "child";
	private static final String FAMILY_MEMBER_PARENT_TYPE = "parent";
	private static final String FAMILY_MEMBER_ITEM_TYPE = "family member item";
	private static final String ALBUM_ITEM_TYPE = "album item";

	private View root;
	private FamilyProfileParentAdapter mParentAdapter;
	private LinearLayout mParentLayoutRight, mParentLayoutLeft;
	private FamilyProfileChildAdapter mChildrenAdapter;
	private FamilyProfileAlbumAdapter mAlbumsAdapter;
	private TextView mFamilyTitle;
	private LinearLayout mChildrenHolder;
	private FamilyMemberDetails[] parentsList, childrenList;
	private LinearLayout mPhotoAlbumsHolder;
	private AlbumItem[] albumList;

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
		
		mPhotoAlbumsHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_album_holder);
		
		// TODO add some real data!
		initParentAndChildrenListData();
		initAlbumListData();
		initViews();
		return root;
	}

	private void initViews() {
		// init the children list view
		mChildrenAdapter = new FamilyProfileChildAdapter(getActivity(), childrenList);
		initChildrenLevelView();

		//init the parents list view
		mParentAdapter = new FamilyProfileParentAdapter(getActivity(), parentsList);
		initParentView(mParentLayoutRight, 0);
		initParentView(mParentLayoutLeft, 1);

		//init the albums list view
		mAlbumsAdapter = new FamilyProfileAlbumAdapter(getActivity(), albumList);
		initAlbumsListView();

		//init the family name title view
		// maybe the family parents does not have the same family name 
		//- hyphen them!
		String familyName = parentsList[0].getLastName();
		if (!parentsList[0].getLastName().equals(parentsList[1].getLastName())) {
			familyName += " - " + parentsList[1].getLastName();
		}
		familyName += " " + getString(R.string.family);
		mFamilyTitle.setText(familyName);
	}

	/**
	 * Initialize the children list view
	 */
	private void initChildrenLevelView() {
		for (int i = 0; i < childrenList.length; i++) {
			View v = mChildrenAdapter.getView(i, null, (ViewGroup) getView());
			v.setTag(ITEM_TYPE, FAMILY_MEMBER_ITEM_TYPE);
			v.setTag(FAMILY_MEMBER_TYPE, FAMILY_MEMBER_CHILD_TYPE);
			v.setTag(ITEM_POS, i);
			v.setOnClickListener(this);
			mChildrenHolder.addView(v);
		}
	}

	/**
	 * Initialize the albums list view
	 */
	private void initAlbumsListView() {
		for (int i = 0; i < albumList.length; i++) {
			View v = mAlbumsAdapter.getView(i, null, (ViewGroup) getView());
			ImageButton album = ((ImageButton) v.findViewById(R.id.ib_album_image));
			album.setTag(ITEM_TYPE, ALBUM_ITEM_TYPE);
			album.setTag(ITEM_POS, i);
			album.setOnClickListener(this);
			mPhotoAlbumsHolder.addView(v);
		}
	}

	/**
	 * Initialize the parent's view
	 * @param base - the holder view
	 * @param position - the parent's position (left / right)
	 */
	private void initParentView(LinearLayout base, int position) {
		View v = mParentAdapter.getView(position, null, (ViewGroup) getView());
		v.setTag(ITEM_TYPE, FAMILY_MEMBER_ITEM_TYPE);
		v.setTag(FAMILY_MEMBER_TYPE, FAMILY_MEMBER_PARENT_TYPE);
		v.setTag(ITEM_POS, position);
		v.setOnClickListener(this);
		base.addView(v);
	}

	// TODO remove this and add some real data
	private void initParentAndChildrenListData() {
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
	
	private void initAlbumListData() {
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
		this.albumList = albumList;
	}

	@Override
	public void onClick(View v) {
		//if the currently clicked item is a family member profile
		if (FAMILY_MEMBER_ITEM_TYPE.equals(v.getTag(ITEM_TYPE))) {
			Intent profileIntent = new Intent(getActivity(),
					ProfileScreenActivity.class);
			Bundle args = new Bundle();
			int pos = (Integer) v.getTag(ITEM_POS); 
			//get family members list and currently clicked member
			//TODO: get real data and remove these
			ArrayList<FamilyMemberDetails> familyMembers = 
					createFamilyList(pos, (String) v.getTag(FAMILY_MEMBER_TYPE));
			
			FamilyMemberDetails clickedUserDetails = 
					getMemberItem((String) v.getTag(FAMILY_MEMBER_TYPE), pos);
			
			args.putParcelable(ProfileFragment.MEMBER_ITEM, clickedUserDetails);			
			args.putParcelableArrayList(ProfileScreenActivity.FAMILY_MEMBER_ARRAY_LIST, familyMembers);
			profileIntent.putExtra(ProfileFragment.PROFILE_DATA, args);
			getActivity().startActivity(profileIntent);
		} 
		
		//if the currently clicked item is a photo album
		else if (ALBUM_ITEM_TYPE.equals(v.getTag(ITEM_TYPE))) {
			Intent albumIntent = new Intent(getActivity(),
					PhotoAlbumScreenActivity.class);
			Bundle args = new Bundle();
			int pos = (Integer) v.getTag(ITEM_POS);
			AlbumItem clickedAlbumDetails = albumList[pos];
			
			args.putParcelable(PhotoGridFragment.ALBUM_ITEM, clickedAlbumDetails);
			albumIntent.putExtra(PhotoGridFragment.ALBUM_ITEM, args);
			getActivity().startActivity(albumIntent);
		}
	}

	/**
	 * This will be removed
	 */
	private FamilyMemberDetails getMemberItem(String type, int pos) {
		//TODO: get real data and remove this function
		if (FAMILY_MEMBER_PARENT_TYPE.equals(type)) {
			return parentsList[pos];
		} 
		
		else if (FAMILY_MEMBER_CHILD_TYPE.equals(type)) {
			return childrenList[pos];
		} 
		
		else {
			LogUtils.logWarning(getTag(), "bad family member type entered");
			return null;
		}
	}

	/**
	 * This will be removed
	 * @param pos
	 * @param type
	 * @return
	 */
	private ArrayList<FamilyMemberDetails> createFamilyList(int pos, String type) {
		//TODO: add real data and remove this function
		ArrayList<FamilyMemberDetails> familyMembers = new ArrayList<FamilyMemberDetails>();
		if (FAMILY_MEMBER_PARENT_TYPE.equals(type)) {
			for (int i = 0; i < childrenList.length; i++) {
				familyMembers.add(childrenList[i]);
			}
			
			for (int j = 0; j < parentsList.length; j++) {
				if (j != pos) {
					familyMembers.add(parentsList[j]);
				}
			}
		} 
		
		else if (FAMILY_MEMBER_CHILD_TYPE.equals(type)) {
			for (int i = 0; i < childrenList.length; i++) {
				if (i != pos) {
					familyMembers.add(childrenList[i]);
				}
			}
			
			for (int j = 0; j < parentsList.length; j++) {
				familyMembers.add(parentsList[j]);
			}
		} 
		
		else {
			LogUtils.logWarning(getTag(), "bad family member type entered");
		}
		
		return familyMembers;
	}
}
