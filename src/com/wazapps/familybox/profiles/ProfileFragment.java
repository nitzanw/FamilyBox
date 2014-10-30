package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;
import com.wazapps.familybox.util.WaveDrawable;

public class ProfileFragment extends Fragment implements OnClickListener {
	public static final String PROFILE_FRAG = "profile fragment";
	public static final String MEMBER_ITEM = "member item";
	public static final String FAMILY_MEMBER_LIST = "family member list";
	public static final String PROFILE_DATA = "profile data";
	private static final String MEMBER_ITEM_TYPE = "family member item";

	private static final int ITEM_TYPE = R.string.type;
	private static final int ITEM_POS = R.string.position;

	private ProfileFamilyListAdapter mFamilyListAdapter;
	private ProfileDetailsAdapter mProfileDetailsAdapter;
	
	//the fragment's profile data
	private UserData[] mFamilyMembersList;
	private UserData mCurrentUserDetails;

	//the fragment's views
	private View root;
	private LinearLayout mFamilyListHolder;
	private ListView mProfileDetailsList;
	private TextView mUserName;
	private RoundedImageView mUserPhoto;
	private MenuItem editItem;
	private TextView mUserStatus;
	private EditText mUserStatusEdit;
	private ImageButton mEditStatusbtn;
	private ImageButton mSubmitStatus;
	AddProfileFragmentListener addProfileCallback = null;
	UpdateProfileStatus updateProfileStatusCallback = null;

	public interface AddProfileFragmentListener {
		void addProfileFragment(Bundle args);
	}
	
	public interface UpdateProfileStatus {
		void updateProfileStatus(String status);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			addProfileCallback = (AddProfileFragmentListener) getActivity();
			updateProfileStatusCallback = (UpdateProfileStatus) getActivity();
		}

		catch (ClassCastException e) {
			LogUtils.logError("ProfileFragment", 
					"Activity should implement " +
					"AddProfileFragmentListener interface" +
					"and UpdateProfileStatus interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);

		mFamilyListHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_members_list_holder);

		mProfileDetailsList = (ListView) root
				.findViewById(R.id.profile_details);

		mUserName = (TextView) root.findViewById(R.id.tv_profile_username);
		mUserStatus = (TextView) root.findViewById(R.id.tv_profile_status);
		mUserStatusEdit = (EditText) root.findViewById(R.id.et_profile_status);
		mEditStatusbtn = (ImageButton) root.findViewById(R.id.ib_edit_status);
		mEditStatusbtn.setOnClickListener(this);
		mSubmitStatus = (ImageButton) root.findViewById(R.id.ib_submit_status);
		mSubmitStatus.setOnClickListener(this);
		mUserPhoto = (RoundedImageView) root
				.findViewById(R.id.riv_profile_image);
		// Clear the listView's top highlight scrolling effect
		int glowDrawableId = root.getResources().getIdentifier(
				"overscroll_glow", "drawable", "android");
		Drawable androidGlow = root.getResources().getDrawable(glowDrawableId);
		androidGlow.setColorFilter(R.color.orange_fb, Mode.CLEAR);

		if (mFamilyMembersList != null) {
			initProfileDetailsViews();
			initFamilyListView();
		}
		
		initAnimations();

		return root;
	}
	
	private void initAnimations() {
		Animation pulse = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.pulse_slow);
		pulse.setInterpolator(new AccelerateInterpolator(3));		
		ImageView animationBackground = 
				(ImageView) root.findViewById(R.id.iv_profile_image_effect);
		
		WaveDrawable waveDrawable = new WaveDrawable(
				Color.WHITE, 200, 3000);
		animationBackground.setBackgroundDrawable(waveDrawable);
		Interpolator interpolator = 
				new AccelerateDecelerateInterpolator();
		
		mUserPhoto.startAnimation(pulse);
		waveDrawable.setWaveInterpolator(interpolator);
		waveDrawable.startAnimation();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle profileArgs = getArguments();
		if (profileArgs != null) {
			// get the data for the profile
			mFamilyMembersList = (UserData[]) profileArgs
					.getParcelableArray(FAMILY_MEMBER_LIST);
			mCurrentUserDetails = (UserData) profileArgs
					.getParcelable(MEMBER_ITEM);
			setHasOptionsMenu(true);
		}

		else {
			LogUtils.logWarning(getTag(), 
					"profile arguments did not pass");
		}
	}

	private void initProfileDetailsViews() {		
		new AsyncTask<Void, Void, Void>() {
			private ProfileFragment frag;
			String userName, status;
			Bitmap profilePic = null;
			
			@Override
			protected Void doInBackground(Void... params) {
				userName = InputHandler.capitalizeFullname(
						frag.mCurrentUserDetails.getName(), 
						frag.mCurrentUserDetails.getLastName());
				status = frag.mCurrentUserDetails.getStatus();
				profilePic = frag.mCurrentUserDetails.getprofilePhoto();
				
				frag.mProfileDetailsAdapter = 
						new ProfileDetailsAdapter(getActivity(),
						frag.mCurrentUserDetails.getUserProfileDetails());
				return null;
			}
			
			protected void onPostExecute(Void result) {
				frag.mProfileDetailsList.setAdapter(frag.mProfileDetailsAdapter);
				frag.mUserName.setText(userName);
				frag.mUserStatus.setText(status);
				frag.mUserStatusEdit.setText(status);
				if (profilePic != null) {
					frag.mUserPhoto.setImageBitmap(profilePic);
					frag.mUserPhoto.setBackgroundColor(getResources().getColor(
							android.R.color.transparent));	
				}
			};
			
			private AsyncTask<Void, Void, Void> init(ProfileFragment frag) {
				this.frag = frag;
				return this;
			}
			
		}.init(this).execute();
	}

	private void initFamilyListView() {
		new AsyncTask<Void, View, Void>() {
			private ProfileFragment frag;

			@Override
			protected Void doInBackground(Void... params) {
				frag.mFamilyListAdapter = new ProfileFamilyListAdapter(
						frag.getActivity(), frag.mFamilyMembersList, 
						frag.mCurrentUserDetails);
				
				for (int i = 0; i < frag.mFamilyListAdapter.getCount(); i++) {
					View v = frag.mFamilyListAdapter.getView(
							i, null, (ViewGroup) getView());
					v.setTag(ITEM_TYPE, MEMBER_ITEM_TYPE);
					v.setTag(ITEM_POS, i);
					v.setOnClickListener(frag);
					publishProgress(v);
				}
				
				return null;
			}
			
			protected void onProgressUpdate(View... v) {
				frag.mFamilyListHolder.addView(v[0]);
			};
			
			
			private AsyncTask<Void, View, Void> init(ProfileFragment frag) {
				this.frag = frag;
				return this;
			}
			
		}.init(this).execute();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_edit_status) {
			mSubmitStatus.setVisibility(View.VISIBLE);
			mEditStatusbtn.setVisibility(View.INVISIBLE);
			mUserStatusEdit.setText(mUserStatus.getText());
			mUserStatusEdit.setVisibility(View.VISIBLE);
			mUserStatus.setVisibility(View.INVISIBLE);
		} else if ((v.getId() == R.id.ib_submit_status)) {
			mEditStatusbtn.setVisibility(View.VISIBLE);
			mSubmitStatus.setVisibility(View.INVISIBLE);
			String oldStatus = mUserStatus.getText().toString();
			String newStatus = mUserStatusEdit.getText().toString();
			if (!oldStatus.equals(newStatus)) {
				mUserStatus.setText(mUserStatusEdit.getText().toString());
				updateProfileStatusCallback.updateProfileStatus(newStatus);
			}
			
			
			mUserStatusEdit.setVisibility(View.INVISIBLE);
			mUserStatus.setVisibility(View.VISIBLE);
		} 
		
//		else {
//			Bundle args = new Bundle();
//			int pos = (Integer) v.getTag(ITEM_POS); // get the current item
//													// position
//													// in family list
//			FamilyMemberDetails[] familyMembers = createFamilyList(pos);
//			FamilyMemberDetails clickedUserDetails = mFamilyMembersList[pos];
//			args.putParcelable(MEMBER_ITEM, clickedUserDetails);
//			args.putParcelableArray(FAMILY_MEMBER_LIST, familyMembers);
//			addProfileCallback.addProfileFragment(args);
//		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu.findItem(R.id.action_edit) == null) {
			inflater.inflate(R.menu.menu_edit_action, menu);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_edit) {
			Intent editIntent = new Intent(getActivity(),
					EditProfileScreenActivity.class);
			Bundle args = getArguments();
			editIntent.putExtra(EditProfileFragment.EDIT_PROFILE_DATA, args);
			getActivity().startActivity(editIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
