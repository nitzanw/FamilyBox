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
import android.os.Parcelable;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.handlers.UserHandler.FamilyMembersFetchCallback;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;
import com.wazapps.familybox.util.WaveDrawable;

public class ProfileFragment extends Fragment implements OnClickListener {
	private abstract class ProfileFragmentCallback {
		public abstract void done(Exception e);
	}
	
	private abstract class MembersFetchCallback extends GetCallback<ParseObject> {
		public abstract MembersFetchCallback init(ProfileFragment frag, 
				boolean fetchOffline, boolean isCurrFamily, 
				ProfileFragmentCallback fetchCallbackFunc);
	}

	public static final String PROFILE_FRAG = "profile fragment";
	public static final String MEMBER_ITEM = "member item";
	public static final String FAMILY_MEMBER_LIST = "family member list";
	public static final String PROFILE_DATA = "profile data";
	private static final String MEMBER_ITEM_TYPE = "family member item";

	private static final int ITEM_TYPE = R.string.type;
	private static final int ITEM_POS = R.string.position;

	private ProfileFamilyListAdapter mFamilyListAdapter;
	private ProfileDetailsAdapter mProfileDetailsAdapter;
	private UserHandler userHandler = null;
	
	//the fragment's profile data
	protected UserData mCurrentUser;
	protected ArrayList<ParseUser> mFamilyMembers = null;
	protected ArrayList<UserData> mFamilyMembersData = null;
	protected boolean mIsLocalProfile;
	
	//the fragment's callback functions
	protected MembersFetchCallback membersFetchCallback = null;

	//the fragment's views
	private View root;
	private LinearLayout mFamilyListHolder; 
	private RelativeLayout spinner;
	private ListView mProfileDetailsList;
	private TextView mUserName;
	private RoundedImageView mUserPhoto;
	private MenuItem editItem;
	private TextView mUserStatus;
	private EditText mUserStatusEdit;
	private ImageButton mEditStatusbtn;
	private ImageButton mSubmitStatus;
	private Animation statusJump = null;
	AddProfileFragmentListener addProfileCallback = null;
	UpdateProfileStatus updateProfileStatusCallback = null;

	public interface AddProfileFragmentListener {
		void addProfileFragment(Bundle args);
	}
	
	public interface UpdateProfileStatus {
		void updateProfileStatus(String status);
	}
	
	private void initCallbackFuncs() {
		//in charge if handling the retrieved members data
		this.membersFetchCallback = new MembersFetchCallback(){
			boolean fetchOffline, isCurrFamily;
			ProfileFragmentCallback callbackFunc;
			ProfileFragment frag;
			
			@Override
			public void done(ParseObject family, ParseException e) {
				final ParseObject currFamily = family;
				//if query fetching failed
				if (e != null) {
					callbackFunc.done(e);
					return;
				}
				
				FamilyMembersFetchCallback fetchCallback = 
						new FamilyMembersFetchCallback() {
					@Override
					public void done(ParseException e) {
						//if family members data fetching has failed
						if (e != null) {
							callbackFunc.done(e);
							return;
						}
						
						//get the user's role in the family
						try {
							String userRole = frag.userHandler.getUserRole(
									frag.mCurrentUser, currFamily, !isCurrFamily);
							frag.mCurrentUser.setRole(userRole);
						} 
						
						catch (Exception roleError) {
							callbackFunc.done(roleError);
							return;
						}
						
						callbackFunc.done(null);
					}
				};
				
				String userId = frag.mCurrentUser.getUserId();
				//if members are cached in local data store 
				//then fetch them from it
				if (fetchOffline) {
					frag.userHandler.fetchFamilyMembersLocally(
							frag.mFamilyMembers, frag.mFamilyMembersData, 
							family, userId, false, fetchCallback);
				} 
				
				//else - fetch from parse servers
				else {
					frag.userHandler.fetchFamilyMembers(
							frag.mFamilyMembers, frag.mFamilyMembersData, 
							family, userId, false, fetchCallback);
				}
			}
			
			public MembersFetchCallback init(ProfileFragment frag, 
					boolean fetchOffline, boolean isCurrFamily, 
					ProfileFragmentCallback callbackFunc) {
				this.frag = frag;
				this.fetchOffline = fetchOffline;
				this.callbackFunc = callbackFunc;
				this.isCurrFamily = isCurrFamily;
				return this;
			}
		};
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userHandler = new UserHandler();
		mFamilyMembers = new ArrayList<ParseUser>();
		mFamilyMembersData = new ArrayList<UserData>();
		initCallbackFuncs();
		
		Bundle profileArgs = getArguments();
		if (profileArgs != null) {
			// get the data for the profile
			mCurrentUser = (UserData) profileArgs
					.getParcelable(MEMBER_ITEM);
			
			//check if this profile is the user's profile.
			//(used to indicate wheter to load data from cache
			//or from server)
			ParseUser loggedUser = ParseUser.getCurrentUser();
			if (loggedUser != null) {
				mIsLocalProfile = (loggedUser.getObjectId()
						.equals(mCurrentUser.getUserId()));
			} else {
				mIsLocalProfile = false;
			}
			
			setHasOptionsMenu(true);
		}

		else {
			LogUtils.logWarning(getTag(), 
					"profile arguments did not pass");
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
		mSubmitStatus = (ImageButton) root.findViewById(R.id.ib_submit_status);
		spinner = (RelativeLayout) root.findViewById(R.id.rl_family_members_list_spinner);
		
		if (mIsLocalProfile) {
			mEditStatusbtn.setOnClickListener(this);			
			mSubmitStatus.setOnClickListener(this);
		} else {
			mEditStatusbtn.setVisibility(View.INVISIBLE);
			mSubmitStatus.setVisibility(View.INVISIBLE);
			spinner.setVisibility(View.VISIBLE);
		}
		
		mUserPhoto = (RoundedImageView) root
				.findViewById(R.id.riv_profile_image);
		
		// Clear the listView's top highlight scrolling effect
		int glowDrawableId = root.getResources().getIdentifier(
				"overscroll_glow", "drawable", "android");
		Drawable androidGlow = root.getResources().getDrawable(glowDrawableId);
		androidGlow.setColorFilter(R.color.orange_fb, Mode.CLEAR);
		initAnimations();
		
		//inflate profile data into the screen
		initProfileDetailsViews();
		
		//fetch family members data and inflate it
		fetchFamilyData(mIsLocalProfile, true, 
				new ProfileFragmentCallback() {
			
			@Override
			public void done(Exception e) {
				if (e == null) {
					spinner.setVisibility(View.GONE);
					initFamilyListView();					
				} 
				
				else {
					LogUtils.logError("ProfileFragment", e.getMessage());
					RelativeLayout errorTextLayout = (RelativeLayout) getActivity()
							.findViewById(R.id.rl_family_members_list_empty);
					TextView errorText = (TextView) errorTextLayout
							.findViewById(R.id.tv_family_members_list_empty);
					errorText.setText("Error in loading family members");
					errorTextLayout.setVisibility(View.VISIBLE);	
				}
			}
		});
		
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
		
		statusJump = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.pulse_once);
		statusJump.setInterpolator(new AccelerateDecelerateInterpolator());
	}

	private void initProfileDetailsViews() {		
		new AsyncTask<Void, Void, Void>() {
			private ProfileFragment frag;
			String userName, status;
			Bitmap profilePic = null;
			
			@Override
			protected Void doInBackground(Void... params) {
				userName = InputHandler.capitalizeFullname(
						frag.mCurrentUser.getName(), 
						frag.mCurrentUser.getLastName());
				status = frag.mCurrentUser.getStatus();
				profilePic = frag.mCurrentUser.getprofilePhoto();
				
				frag.mProfileDetailsAdapter = 
						new ProfileDetailsAdapter(getActivity(),
						frag.mCurrentUser.getUserProfileDetails());
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
	
	private void fetchFamilyData(
			boolean isFromLocalDB, boolean isCurrFamily,
			final ProfileFragmentCallback callbackFunc) {
		//fetch the user's family from online source or local datatore
		//if currFamily is true - fetch the user's current family
		//otherwise fetch previous family.
		String familyId = isCurrFamily? mCurrentUser.getFamilyId() : 
			mCurrentUser.getPrevFamilyId();
		ParseQuery<ParseObject> familyQuery = 
				ParseQuery.getQuery(FamilyHandler.FAMILY_CLASS_NAME);
		
		//if the family members are cached in the local datastore 
		//then do an offline query
		if (isFromLocalDB) {
			familyQuery.fromLocalDatastore();			
		}
		
		//start fetching the members
		familyQuery.getInBackground(familyId, 
				membersFetchCallback.init(this, isFromLocalDB, 
						isCurrFamily, callbackFunc));
	}
	
	private void initFamilyListView() {
		new AsyncTask<Void, View, Void>() {
			private ProfileFragment frag;

			@Override
			protected Void doInBackground(Void... params) {
				frag.mFamilyListAdapter = new ProfileFamilyListAdapter(
						frag.getActivity(), frag.mFamilyMembersData.toArray(
								new UserData[frag.mFamilyMembersData.size()]), 
						frag.mCurrentUser);
				
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
			
			protected void onPostExecute(Void result) {
				if (frag.mFamilyMembersData.size() == 0) {
					RelativeLayout emptyText = (RelativeLayout) 
							frag.getActivity()
							.findViewById(R.id.rl_family_members_list_empty);
					emptyText.setVisibility(View.VISIBLE);
				}
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
			mUserStatusEdit.startAnimation(statusJump);
		} else if ((v.getId() == R.id.ib_submit_status)) {
			mEditStatusbtn.setVisibility(View.VISIBLE);
			mSubmitStatus.setVisibility(View.INVISIBLE);
			mUserStatus.startAnimation(statusJump);
			String oldStatus = mUserStatus.getText().toString();
			String newStatus = mUserStatusEdit.getText().toString();
			if (!oldStatus.equals(newStatus)) {
				mUserStatus.setText(mUserStatusEdit.getText().toString());
				updateProfileStatusCallback.updateProfileStatus(newStatus);
			}
			
			
			mUserStatusEdit.setVisibility(View.INVISIBLE);
			mUserStatus.setVisibility(View.VISIBLE);
		} 
		
		else {
			Bundle args = new Bundle();
			//get the current item position in family members list
			int pos = (Integer) v.getTag(ITEM_POS); 
			UserData clickedUserDetails = mFamilyMembersData.get(pos);
			args.putParcelable(MEMBER_ITEM, clickedUserDetails);
			addProfileCallback.addProfileFragment(args);
		}
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
			getActivity().startActivity(editIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
