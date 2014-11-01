package com.wazapps.familybox.familyProfiles;

import java.util.ArrayList;

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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.handlers.UserHandler.FamilyMembersFetchCallback;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoAlbumScreenActivity;
import com.wazapps.familybox.photos.PhotoGridFragment;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.ProfileDetails;
import com.wazapps.familybox.profiles.ProfileFragment;
import com.wazapps.familybox.profiles.ProfileScreenActivity;
import com.wazapps.familybox.profiles.UserData;
import com.wazapps.familybox.util.LogUtils;

public class FamilyProfileFragment extends Fragment implements OnClickListener {
	private static final int ITEM_TYPE = R.string.type;
	private static final int ITEM_POS = R.string.position;
	private static final int FAMILY_MEMBER_TYPE = R.string.family;

	public static final String FAMILY_PROFILE_FRAGMENT = "family profile fragment";
	public static final String USER_FAMILY = "user family";
	
	private static final String FAMILY_MEMBER_CHILD_TYPE = "child";
	private static final String FAMILY_MEMBER_PARENT_TYPE = "parent";
	private static final String FAMILY_MEMBER_ITEM_TYPE = "family member item";
	private static final String ALBUM_ITEM_TYPE = "album item";
	public static final String FAMILY_PROFILE_DATA = "family profile data";
	public static final String FAMILY_PROFILE_FRAG = "family profile fragment";
	private static final String FAMILY_ID = "family id";
	private static final String FAMILY_NAME = "family name";

	private View root;
	private FamilyProfileParentAdapter mParentAdapter;
	private LinearLayout mParentLayoutRight, mParentLayoutLeft, 
							mPhotoAlbumsHolder, mChildrenHolder;
	private FamilyProfileChildAdapter mChildrenAdapter;
	private FamilyProfileAlbumAdapter mAlbumsAdapter;
	private TextView mFamilyTitle;
	
	//family data
	private FamilyMemberDetails[] mParentsList2, mChildrenList2;
	private ArrayList<ParseUser> mFamilyMembers = null;
	private ArrayList<UserData> mFamilyMembersData = null, 
			mChildrenList = null, mParentsList = null;
	private AlbumItem[] mAlbumList;
	private UserHandler mUserhandler = null;
	private boolean mIsUserFamily = false;
	private ParseUser mLoggedUser = null;
	private ParseObject mCurrentFamily = null;
	private String mFamilyId, mFamilyName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserhandler = new UserHandler();
		mFamilyMembers = new ArrayList<ParseUser>();
		mFamilyMembersData = new ArrayList<UserData>();
		mLoggedUser = ParseUser.getCurrentUser();
		
		if (mLoggedUser == null) {
			//TODO: handle error
		}
		
		Bundle args = getArguments();
		if (args != null) {
			mIsUserFamily = args.getBoolean(USER_FAMILY);
			
			if (args.containsKey(FAMILY_ID)) {
				mFamilyId = args.getString(FAMILY_ID);
			}
			
			if (args.containsKey(FAMILY_NAME)) {
				mFamilyName = args.getString(FAMILY_NAME);
			}
		} 
		
		else {
			LogUtils.logWarning(getTag(), 
					"family profile arguments did not pass");
		}
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_family_profile, 
				container, false);
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
		
		if (mIsUserFamily) {
			mFamilyId = mLoggedUser.getString(UserHandler.FAMILY_KEY);
			mFamilyName = mLoggedUser.getString(UserHandler.LAST_NAME_KEY);
		} 
		
		mFamilyName = InputHandler.capitalizeName(mFamilyName);
		mFamilyTitle.setText(mFamilyName + " Family");
		
		FamilyHandler.getFamilyById(mFamilyId, true,
				new GetCallback<ParseObject>() {
			FamilyProfileFragment frag;
			
			@Override
			public void done(ParseObject family, ParseException e) {
				if (e != null) {
					//TODO: handle error
					return;
				} 
				
				frag.mCurrentFamily = family;
				if (mIsUserFamily) {
					frag.mUserhandler.fetchFamilyMembersLocally(
							mFamilyMembers, mFamilyMembersData, 
							mCurrentFamily, mLoggedUser.getObjectId(), 
							true, new FamilyMembersFetchCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e != null) {
										//TODO: handle error
									} else {
										frag.initDataViews();
									}
								}
							});
				} 
				
				else {
					frag.mUserhandler.fetchFamilyMembers(
							mFamilyMembers, mFamilyMembersData, 
							mCurrentFamily, mLoggedUser.getObjectId(),
							true, new FamilyMembersFetchCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e != null) {
										//TODO: handle error
									} else {
										frag.initDataViews();
									}
								}
							});
				}
			}
			
			GetCallback<ParseObject> init(FamilyProfileFragment frag) {
				this.frag = frag;
				return this;
			}
		}.init(this));
		
		return root;
	}

	private void initDataViews() {
		//init the data
		mParentsList = new ArrayList<UserData>();
		mChildrenList = new ArrayList<UserData>();
		
		for (UserData member : mFamilyMembersData) {
			if (member.getRole().equals(UserData.ROLE_PARENT)) {
				mParentsList.add(member);
			} else {
				mChildrenList.add(member);
			}
		}
		
		initParentsLevelView();
		initChildrenLevelView();
		initAlbumsListView();
	}

	private void initParentsLevelView() {
		//init the parents list view
		mParentAdapter = new FamilyProfileParentAdapter(
				getActivity(), mParentsList.toArray(
						new UserData[mParentsList.size()]));
		int mParentsListSize = mParentsList.size();
		if (mParentsListSize > 0) {
			initParentView(mParentLayoutRight, 0);			
		}
		
		if (mParentsListSize > 1) {
			initParentView(mParentLayoutLeft, 1);			
		}
	}
	
	private void initChildrenLevelView() {
		// init the children list view
		mChildrenAdapter = new FamilyProfileChildAdapter(
				getActivity(), mChildrenList.toArray(
						new UserData[mChildrenList.size()]));

		for (int i = 0; i < mChildrenList.size(); i++) {
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
		initAlbumListData();
		mAlbumsAdapter = new FamilyProfileAlbumAdapter(
				getActivity(), mAlbumList);
		
		for (int i = 0; i < mAlbumList.length; i++) {
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
			AlbumItem clickedAlbumDetails = mAlbumList[pos];

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
			return mParentsList2[pos];
		} 

		else if (FAMILY_MEMBER_CHILD_TYPE.equals(type)) {
			return mChildrenList2[pos];
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
			for (int i = 0; i < mChildrenList2.length; i++) {
				familyMembers.add(mChildrenList2[i]);
			}

			for (int j = 0; j < mParentsList2.length; j++) {
				if (j != pos) {
					familyMembers.add(mParentsList2[j]);
				}
			}
		} 

		else if (FAMILY_MEMBER_CHILD_TYPE.equals(type)) {
			for (int i = 0; i < mChildrenList2.length; i++) {
				if (i != pos) {
					familyMembers.add(mChildrenList2[i]);
				}
			}

			for (int j = 0; j < mParentsList2.length; j++) {
				familyMembers.add(mParentsList2[j]);
			}
		} 

		else {
			LogUtils.logWarning(getTag(), "bad family member type entered");
		}

		return familyMembers;
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
		this.mAlbumList = albumList;
	}
}
