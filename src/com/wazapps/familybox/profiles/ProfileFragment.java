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
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment.AddFamilyProfileFragmentListener;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.handlers.UserHandler.FamilyMembersFetchCallback;
import com.wazapps.familybox.splashAndLogin.LoginActivity;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;
import com.wazapps.familybox.util.WaveDrawable;

public class ProfileFragment extends Fragment 
implements OnClickListener, OnLongClickListener {
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
	public static final String USER_PROFILE = "user profile";
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
	protected boolean mIsUserProfile = true;
	protected ParseUser loggedUser;
	
	//the fragment's callback functions
	protected MembersFetchCallback membersFetchCallback = null;

	//the fragment's views
	private View root;
	private LinearLayout mFamilyListHolder; 
	private RelativeLayout mSpinner, emptyText;
	private HorizontalScrollView membersList;
	private ListView mProfileDetailsList;
	private TextView mUserName;
	private TextView familyTitle;
	private RoundedImageView mUserPhoto;
	private MenuItem editItem;
	private TextView mUserStatus;
	private EditText mUserStatusEdit;
	private ImageButton mEditStatusbtn;
	private ImageButton mSubmitStatus;
	private Animation statusJump = null;
	private MenuItem editButton;
	private ImageButton mPrevFamilybutton, mCurrFamilybutton;
	AddProfileFragmentListener addProfileCallback = null;
	AddFamilyProfileFragmentListener addFamilyProfileCallback = null;

	public interface AddProfileFragmentListener {
		void addProfileFragment(Bundle args);
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
			addProfileCallback = (AddProfileFragmentListener) 
					getActivity();
			addFamilyProfileCallback = (AddFamilyProfileFragmentListener) 
					getActivity();
		}

		catch (ClassCastException e) {
			LogUtils.logError("ProfileFragment", 
					"Activity should implement " +
					"AddProfileFragmentListener and" +
					"AddFamilyProfileFragmentListener interfaces");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userHandler = new UserHandler();
		initCallbackFuncs();
		loggedUser = ParseUser.getCurrentUser();
		if (loggedUser == null) {
			//TODO: handle error
			LogUtils.logWarning(getTag(), 
					"user is not logged in!");
		}
		
		Bundle profileArgs = getArguments();
		if (profileArgs != null) {
			mIsUserProfile = profileArgs.getBoolean(USER_PROFILE);
			// get the data for the profile
			if (profileArgs.containsKey(MEMBER_ITEM)) {
				mCurrentUser = (UserData) profileArgs
						.getParcelable(MEMBER_ITEM);				
			}	
		}

		else {
			LogUtils.logWarning(getTag(), 
					"profile arguments did not pass");
		}
		
		setHasOptionsMenu(true);
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
		mUserPhoto = (RoundedImageView) root.findViewById(R.id.riv_profile_image);
		mUserStatusEdit = (EditText) root.findViewById(R.id.et_profile_status);
		mEditStatusbtn = (ImageButton) root.findViewById(R.id.ib_edit_status);
		mSubmitStatus = (ImageButton) root.findViewById(R.id.ib_submit_status);
		mSpinner = (RelativeLayout) root.findViewById(R.id.rl_family_members_list_spinner);
		mPrevFamilybutton = (ImageButton) root.findViewById(R.id.ib_prev_family);
		mCurrFamilybutton = (ImageButton) root.findViewById(R.id.ib_curr_family);
		mPrevFamilybutton.setOnClickListener(this);
		mCurrFamilybutton.setOnClickListener(this);
		emptyText = (RelativeLayout) root.findViewById(R.id.rl_family_members_list_empty);
		familyTitle = (TextView) root.findViewById(R.id.tv_close_family_title);
		membersList = (HorizontalScrollView) root.findViewById(R.id.hsv_family_members_list);
		
		if (mIsUserProfile) {
			mEditStatusbtn.setOnClickListener(this);			
			mSubmitStatus.setOnClickListener(this);
		} else {
			mEditStatusbtn.setVisibility(View.INVISIBLE);
			mSubmitStatus.setVisibility(View.INVISIBLE);
			mSpinner.setVisibility(View.VISIBLE);
		}
		
		// Clear the listView's top highlight scrolling effect
		int glowDrawableId = root.getResources().getIdentifier(
				"overscroll_glow", "drawable", "android");
		Drawable androidGlow = root.getResources().getDrawable(glowDrawableId);
		androidGlow.setColorFilter(R.color.orange_fb, Mode.CLEAR);
		initAnimations();
		return root;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//if this is the user's profile - 
		//load its data manually from local datastore
		if (mIsUserProfile) {
			//refetch loggedUser just to be sure
			loggedUser = ParseUser.getCurrentUser();
			
			if (loggedUser == null) {
				//TODO: handle error
			} 
			
			else {
				mCurrentUser = new UserData(loggedUser, 
						UserData.ROLE_UNDEFINED);				
			}
		}

		if (!mCurrentUser.getPrevFamilyId().equals("") 
				&& !mCurrentUser.getPreviousLastName().equals("")) {
			mPrevFamilybutton.setVisibility(View.VISIBLE);
		}
		
		//inflate profile data into the screen
		initProfileDetailsViews();
		
		//fetch family members data and inflate it
		fetchFamilyData(mIsUserProfile, true, 
				new ProfileFragmentCallback() {
			
			@Override
			public void done(Exception e) {
				if (e == null) {
					mSpinner.setVisibility(View.GONE);
					initFamilyListView();					
				} 
				
				//if an error happened - inflate error text onscreen
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
				if (frag.mIsUserProfile) {
					frag.mCurrentUser.downloadProfilePicSync(loggedUser);
				}
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
		mFamilyMembers = new ArrayList<ParseUser>();
		mFamilyMembersData = new ArrayList<UserData>();
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
		mFamilyListHolder.removeAllViews();
		membersList.setVisibility(View.VISIBLE);
		
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
					v.setOnLongClickListener(frag);
					publishProgress(v);
				}
				
				return null;
			}
			
			protected void onProgressUpdate(View... v) {
				frag.mFamilyListHolder.addView(v[0]);
			};
			
			protected void onPostExecute(Void result) {
				if (frag.mFamilyMembersData.size() == 0) {
					frag.emptyText.setVisibility(View.VISIBLE);
					frag.membersList.setVisibility(View.GONE);
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
		switch (v.getId()) {
		case R.id.ib_edit_status:
			mSubmitStatus.setVisibility(View.VISIBLE);
			mEditStatusbtn.setVisibility(View.INVISIBLE);
			mUserStatusEdit.setText(mUserStatus.getText());
			mUserStatusEdit.setVisibility(View.VISIBLE);
			mUserStatus.setVisibility(View.INVISIBLE);
			mUserStatusEdit.startAnimation(statusJump);
			break;
			
		case R.id.ib_submit_status:
			mEditStatusbtn.setVisibility(View.VISIBLE);
			mSubmitStatus.setVisibility(View.INVISIBLE);
			mUserStatus.startAnimation(statusJump);
			final String oldStatus = mUserStatus.getText().toString();
			final String newStatus = mUserStatusEdit.getText().toString();
			if (!oldStatus.equals(newStatus)) {
				mUserStatus.setText(mUserStatusEdit.getText().toString());
				loggedUser.put(UserHandler.STATUS_KEY, newStatus);
				loggedUser.saveEventually(
				new SaveCallback() {
				FragmentActivity activity;
				
				@Override
				public void done(ParseException e) {
					if (e == null) {
						Toast toast = Toast.makeText(activity, 
								"Status updated", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
					} 
					
					else {
						LogUtils.logError("FragmentActivity", 
								e.getMessage());
						Toast toast = Toast.makeText(activity, 
								"Failed to update status", 
								Toast.LENGTH_SHORT);
						
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
					}
				}
				
				private SaveCallback init(FragmentActivity activity) {
					this.activity = activity;
					return this;
				}
			}.init(getActivity()));
			}
						
			mUserStatusEdit.setVisibility(View.INVISIBLE);
			mUserStatus.setVisibility(View.VISIBLE);
			break;
			
		case R.id.ib_prev_family:
			mSpinner.setVisibility(View.VISIBLE);
			emptyText.setVisibility(View.GONE);
			mPrevFamilybutton.setVisibility(View.INVISIBLE);
			familyTitle.startAnimation(statusJump);
			familyTitle.setText("Previous Family");
			//re-init user data in case of data refresh
			fetchFamilyData(false, false, new ProfileFragmentCallback() {
				private ProfileFragment frag;

				@Override
				public void done(Exception e) {
					if (e == null) {
						frag.mSpinner.setVisibility(View.GONE);						
						frag.mCurrFamilybutton.setVisibility(View.VISIBLE);
						frag.initFamilyListView();						
					}
					
					//if an error happened - inflate error text onscreen
					else {
						LogUtils.logError("ProfileFragment", e.getMessage());
						TextView errorText = (TextView) emptyText.findViewById(R.id.tv_family_members_list_empty);
						errorText.setText("Error in loading family members");
						emptyText.setVisibility(View.VISIBLE);	
					}
				}
				
				private ProfileFragmentCallback init(ProfileFragment frag) {
					this.frag = frag;
					return this;
				}
			}.init(this));
			break;
			
		case R.id.ib_curr_family:
			if (!mIsUserProfile) {
				mSpinner.setVisibility(View.VISIBLE);				
			}
			
			emptyText.setVisibility(View.GONE);
			mCurrFamilybutton.setVisibility(View.INVISIBLE);
			familyTitle.startAnimation(statusJump);
			familyTitle.setText("Close Family");
			
			//re-init user data in case of data refresh
			fetchFamilyData(mIsUserProfile, true, 
					new ProfileFragmentCallback() {
				private ProfileFragment frag;

				@Override
				public void done(Exception e) {
					if (e == null) {
						frag.mSpinner.setVisibility(View.GONE);						
						frag.mPrevFamilybutton.setVisibility(View.VISIBLE);
						frag.initFamilyListView();						
					}
					
					//if an error happened - inflate error text onscreen
					else {
						LogUtils.logError("ProfileFragment", e.getMessage());
						TextView errorText = (TextView) emptyText.findViewById(
								R.id.tv_family_members_list_empty);
						errorText.setText("Error in loading family members");
						emptyText.setVisibility(View.VISIBLE);	
					}
				}
				
				private ProfileFragmentCallback init(ProfileFragment frag) {
					this.frag = frag;
					return this;
				}
			}.init(this));
			
			break;

		default:
			//default case is user profile click
			Bundle args = new Bundle();
			//get the current item position in family members list
			int pos = (Integer) v.getTag(ITEM_POS); 
			UserData clickedUserDetails = mFamilyMembersData.get(pos);
			boolean isUserProfile = (clickedUserDetails.getUserId()
					.equals(loggedUser.getObjectId()));
			args.putBoolean(USER_PROFILE, isUserProfile);
			if (!isUserProfile) {
				args.putParcelable(MEMBER_ITEM, clickedUserDetails);				
			}
			
			addProfileCallback.addProfileFragment(args);
			break;
		}
	}
	

	@Override
	public boolean onLongClick(View v) {
		if (MEMBER_ITEM_TYPE.equals(v.getTag(ITEM_TYPE))) {
			Bundle args = new Bundle();
			int pos = (Integer) v.getTag(ITEM_POS);
			UserData clickedUserDetails = mFamilyMembersData.get(pos);
			boolean isUserProfile = (clickedUserDetails.getUserId()
					.equals(loggedUser.getObjectId()));
			args.putBoolean(FamilyProfileFragment.USER_FAMILY, isUserProfile);
			if (!isUserProfile) {
				args.putString(FamilyProfileFragment.FAMILY_ID, 
						clickedUserDetails.getFamilyId());
				args.putString(FamilyProfileFragment.FAMILY_NAME, 
						clickedUserDetails.getLastName());
			}
			
			addFamilyProfileCallback.addFamilyProfileFragment(args);
			return true;
		} 
		
		else {
			return false;			
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		editButton = menu.findItem(R.id.action_edit);
		if (!mIsUserProfile) {
			editButton.setVisible(false);
		} else {
			editButton.setVisible(true);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { 
		editButton = menu.findItem(R.id.action_edit);
		if (editButton == null)
			inflater.inflate(R.menu.menu_edit_action, menu);
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
}
