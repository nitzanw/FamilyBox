package com.wazapps.familybox.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.handlers.UserHandler.FamilyMembersFetchCallback;
import com.wazapps.familybox.newsfeed.NewsItem;
import com.wazapps.familybox.profiles.EditProfileFragment.EditProfileCallback;
import com.wazapps.familybox.profiles.UserData.DownloadCallback;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment;
import com.wazapps.familybox.splashAndLogin.FamilyQueryFragment;
import com.wazapps.familybox.splashAndLogin.LoginActivity;
import com.wazapps.familybox.splashAndLogin.FamilyQueryFragment.QueryHandlerCallback;
import com.wazapps.familybox.splashAndLogin.MemberQueryFragment.QueryAnswerHandlerCallback;
import com.wazapps.familybox.splashAndLogin.MemberQueryFragment;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.DateChooserCallback;
import com.wazapps.familybox.util.AbstractScreenActivity;
import com.wazapps.familybox.util.LogUtils;

public class EditProfileScreenActivity extends AbstractScreenActivity 
	implements EditProfileCallback, DateChooserCallback, QueryHandlerCallback,
	QueryAnswerHandlerCallback {
	
	public abstract class PrevFamilyQueryCallback {
		public abstract void done(Exception e);
	}

	private static final String TAG_EDTBIRTHDAY = "edit birthday";
	private static final int SELECT_PICTURE = 0;
	private static final String TAG_MEMBERQUERY_FRAG = "member query fragment";
	protected static final String TAG_FAMILYQUERY_FRAG = "family query fragment";
	
	//families query data
	private ParseUser mCurrentUser = null, mCurrentFamilyMember = null;
	private UserData mCurrentFamilyMemberData = null;
	private ArrayList<ParseObject> mPrevFamiliesList = null;
	private ParseObject mPrevFamily = null;
	private ArrayList<ParseUser> mPrevFamilyMembers = null;
	private ArrayList<UserData> mPrevFamilyMembersData = null;
	private int familyQueryIndex = 0;
	private PrevFamilyQueryCallback queryCallback = null;
	private UserHandler userHandler = null;
	
	private void iniCallbackFunctions() {
		this.queryCallback = new PrevFamilyQueryCallback() {
			
			@Override
			public void done(Exception e) {
				if (e == null) {
					mCurrentUser.saveEventually(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast toast = Toast.makeText(getApplicationContext(), 
										"user details updated", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
								toast.show();
							}
							
							else {
								LogUtils.logError("EditProfileScreenActivity", 
										e.getMessage());
								Toast toast = Toast.makeText(getApplicationContext(), 
										"error saving user details", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
								toast.show();
							}
						}
					});		
					
					NewsItem userUpdate = new NewsItem();
					userUpdate.setNetworkId(mCurrentUser.getString(UserHandler.NETWORK_KEY));
					userUpdate.setContent("Updated profile details");
					userUpdate.setUser(mCurrentUser);
					userUpdate.setUserFirstName(mCurrentUser.getString(UserHandler.FIRST_NAME_KEY));
					userUpdate.setUserLastName(mCurrentUser.getString(UserHandler.LAST_NAME_KEY));
					userUpdate.saveEventually();
				} 
				
				else {
					LogUtils.logError("EditProfileScreenActivity", 
							e.getMessage());
					Toast toast = Toast.makeText(getApplicationContext(), 
							"error updating user details", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
				}
				
				finish();
			}
		};
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iniCallbackFunctions();
		userHandler = new UserHandler();
		overridePendingTransition(R.anim.enter, R.anim.exit);
		EditProfileFragment editProfileFrag = new EditProfileFragment();
		// set the fragment to the container
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fragment_container, editProfileFrag,
				EditProfileFragment.EDIT_PROFILE_FRAG).commit();
		getActionBar().setTitle(getString(R.string.edit_profile));
	}

	@Override
	public void openBirthdayDialog() {
		BirthdaySignupDialogFragment dialog = 
				new BirthdaySignupDialogFragment();
		dialog.show(getSupportFragmentManager(), TAG_EDTBIRTHDAY);			
	}

	@Override
	public void setDate(String date) {
		EditProfileFragment editProfileFrag = (EditProfileFragment) 
				getSupportFragmentManager()
				.findFragmentByTag(EditProfileFragment.EDIT_PROFILE_FRAG);
		editProfileFrag.setDate(date);
	}

	@Override
	public void openPhonePhotoBrowsing() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri currImageURI = data.getData();
				File file = new File(PhotoHandler.getRealPathFromURI(this,
						currImageURI));

				if (file.exists()) {
					EditProfileFragment frag = (EditProfileFragment) 
							getSupportFragmentManager()
							.findFragmentByTag(EditProfileFragment.EDIT_PROFILE_FRAG);
					Bitmap myBitmap = PhotoHandler.getImageBitmapFromFile(file);
					byte[] fileData = PhotoHandler
							.createDownsampledPictureData(myBitmap);
					String filename = "profilePic.JPEG";
					frag.setProfileImage(myBitmap, fileData, filename);
				}
			}
		}
	}

	@Override
	public void updateProfile(boolean prevFamilyUpdated, ParseUser user) {
		//fetch the edit profile fragment in case we want to start progress
		//bar animation
		final EditProfileFragment editFrag = (EditProfileFragment) 
				getSupportFragmentManager().findFragmentByTag(
						EditProfileFragment.EDIT_PROFILE_FRAG);
		mCurrentUser = user;
		
		if (!prevFamilyUpdated) {
			queryCallback.done(null);
			return;
		} 
		
		editFrag.turnOnProgress(); //start loading spinner
		
		String familyName = mCurrentUser
				.getString(UserHandler.PREV_LAST_NAME_KEY);
		
		String userNetwork = mCurrentUser
				.getString(UserHandler.NETWORK_KEY);
		
		FamilyHandler.fetchRelatedFamilies(familyName, userNetwork, 
				new FindCallback<ParseObject>() {
			EditProfileScreenActivity activity;
			
			@Override
			public void done(List<ParseObject> families, 
					ParseException e) {
				if (e == null) {
					activity.mPrevFamiliesList = 
							new ArrayList<ParseObject>(families);
					activity.familyQueryIndex = 0;
					activity.handleFamilyQuery();
				} 
				
				else {
					activity.queryCallback.done(e);
				}
			}
			
			private FindCallback<ParseObject> init(
					EditProfileScreenActivity activity) {
				this.activity = activity;
				return this;
			}
		}.init(this));
	}
	
	public void handleFamilyQuery() {
		// if no related families were found for the user
		// create a new family for him/her
		if (familyQueryIndex >= mPrevFamiliesList.size()) {
			mPrevFamily = new ParseObject(FamilyHandler.FAMILY_CLASS_NAME);
			mPrevFamilyMembers = new ArrayList<ParseUser>();
			FamilyHandler.createNewFamilyForUser(mCurrentUser, 
					false, mPrevFamily, new SaveCallback() {
				EditProfileScreenActivity activity;
				
				@Override
				public void done(ParseException e) {
					activity.queryCallback.done(e);
				}
				
				private SaveCallback init(
						EditProfileScreenActivity activity) {
					this.activity = activity;
					return this;
				}
			}.init(this));
			
			return ;
		}
		
		// else - check the current related family
		// fetch its members and show to user
		String userId = mCurrentUser.getObjectId();
		mPrevFamilyMembers = new ArrayList<ParseUser>();
		mPrevFamilyMembersData = new ArrayList<UserData>();
		mPrevFamily = mPrevFamiliesList.get(familyQueryIndex);
		userHandler.fetchFamilyMembers(mPrevFamilyMembers, 
				mPrevFamilyMembersData, mPrevFamily, userId, 
				false, new FamilyMembersFetchCallback() {
				EditProfileScreenActivity activity;
				
				@Override
				public void done(ParseException e) {
					if (e != null) {
						activity.queryCallback.done(e);
						return;
					}
					
					activity.launchFamilyQueryFragment();
				}
				
				private FamilyMembersFetchCallback init(
						EditProfileScreenActivity activity){
					this.activity = activity;
					return this;
					
				}
			}.init(this));
	}
	
	/**
	 * Launches the family query fragment, with the fetched families list
	 */
	protected void launchFamilyQueryFragment() {
		// progress to the next family in the related families list
		// in preparation for next iteration
		familyQueryIndex++;

		// pass arguments and control to family query fragment
		UserData userDetails = new UserData(
				mCurrentUser, UserData.ROLE_UNDEFINED);
		
		userDetails.downloadProfilePicAsync(
				mCurrentUser, new DownloadCallback() {
				private UserData userDetails;

				@Override
				public void done(ParseException e) {
					// we can ignore error message
					Bundle args = new Bundle();
					args.putParcelable(FamilyQueryFragment.MEMBER_ITEM,
							userDetails);
					
					args.putParcelableArray(
							FamilyQueryFragment.QUERY_FAMILIES_LIST,
							mPrevFamilyMembersData.toArray(
									new UserData[mPrevFamilyMembersData.size()]));
					
					args.putBoolean(FamilyQueryFragment.CURRENT_FAMILY, false);

					FamilyQueryFragment familyQueryFrag = 
							new FamilyQueryFragment();
					familyQueryFrag.setArguments(args);
					
					// TODO: fix backstack bug
					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.setCustomAnimations(R.anim.enter, R.anim.exit,
							R.anim.enter_reverse, R.anim.exit_reverse);
					ft.replace(R.id.fragment_container, familyQueryFrag,
							TAG_FAMILYQUERY_FRAG).commit();
				}
				
				private DownloadCallback init(UserData userDetails) {
					this.userDetails = userDetails;
					return this;
				}
			}.init(userDetails));
	}

	@Override
	public void handleMemberQuery() {
		// fetch the familyQueryFragment in case we want to start loading
		// animation
		final FamilyQueryFragment familyQueryFrag = (FamilyQueryFragment) 
				getSupportFragmentManager()
				.findFragmentByTag(TAG_FAMILYQUERY_FRAG);
	
		mPrevFamily.fetchIfNeededInBackground(
				new GetCallback<ParseObject>() {
			private EditProfileScreenActivity activity;
	
			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					activity.mCurrentFamilyMember = mPrevFamilyMembers.get(0);
					activity.mCurrentFamilyMemberData = mPrevFamilyMembersData.get(0);
	
					boolean isFatherTaken = activity.mPrevFamily
							.has(FamilyHandler.RELATION_FATHER);
					boolean isMotherTaken = activity.mPrevFamily
							.has(FamilyHandler.RELATION_MOTHER);
					boolean isMemberMale = activity.mCurrentFamilyMember
							.getString(UserHandler.GENDER_KEY).equals(
									UserHandler.GENDER_MALE) ? true : false;
	
					ArrayList<String> relationOptions = InputHandler
							.generateRelationOptions(activity.mCurrentUser,
									activity.mCurrentFamilyMemberData,
									isFatherTaken, isMotherTaken);
	
					// if only one relation option exists then the answer is
					// already known
					if (relationOptions.size() == 1) {
						familyQueryFrag.startLoadingSpinner();
						activity.handleMemberQueryAnswer(relationOptions.get(0));
						return;
					}
	
					// else open the member query screen
					activity.launchMemberQueryFragment(relationOptions,
							isMemberMale);
				} else {
					activity.queryCallback.done(e);
				}
			}
	
			private GetCallback<ParseObject> init(EditProfileScreenActivity activity) {
				this.activity = activity;
				return this;
			}
		}.init(this));
	}
	
	@Override
	public void handleMemberQueryAnswer(String relation) {
		String userGender = mCurrentUser.getString(UserHandler.GENDER_KEY);
		String familyMemberGender = mCurrentFamilyMemberData.getGender();
		String familyMemberRole = mCurrentFamilyMemberData.getRole();
		boolean isMemberUndefined = familyMemberRole
				.equals(UserData.ROLE_UNDEFINED) ? true : false;
		boolean isUserMale = userGender.equals(UserHandler.GENDER_MALE) ? true
				: false;
		boolean isMemberMale = familyMemberGender
				.equals(UserHandler.GENDER_MALE) ? true : false;

		FamilyHandler.updateUsersAndFamilyRelation(mCurrentUser,
				mCurrentFamilyMember, mPrevFamily, relation,
				isMemberUndefined, isUserMale, isMemberMale,
				false, new SaveCallback() {
					private EditProfileScreenActivity activity;

					@Override
					public void done(ParseException e) {
						activity.queryCallback.done(e);
					}

					private SaveCallback init(EditProfileScreenActivity activity) {
						this.activity = activity;
						return this;
					}
				}.init(this));
	}
	
	protected void launchMemberQueryFragment(ArrayList<String> relationOptions,
			boolean isMemberMale) {
		Bundle args = new Bundle();
		args.putParcelable(MemberQueryFragment.FAMILY_MEMBER_ITEM,
				mCurrentFamilyMemberData);
		args.putStringArrayList(
				MemberQueryFragment.FAMILY_MEMBER_RELATION_OPTIONS,
				relationOptions);
		args.putBoolean(MemberQueryFragment.FAMILY_MEMBER_GENDER, isMemberMale);

		MemberQueryFragment memberQueryFrag = new MemberQueryFragment();
		memberQueryFrag.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.enter, R.anim.exit,
						R.anim.enter_reverse, R.anim.exit_reverse)
				.replace(R.id.fragment_container, memberQueryFrag,
						TAG_MEMBERQUERY_FRAG).commit();
	}
}
