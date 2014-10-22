package com.wazapps.familybox.splashAndLogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.misc.InputException;
import com.wazapps.familybox.profiles.FamilyMemberDetails2;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.BirthdayChooserCallback;
import com.wazapps.familybox.splashAndLogin.EmailLoginDialogueFragment.EmailLoginScreenCallback;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment.SignupScreenCallback;
import com.wazapps.familybox.splashAndLogin.FamilyQueryFragment.QueryHandlerCallback;
import com.wazapps.familybox.splashAndLogin.GenderSignupDialogFragment.GenderChooserCallback;
import com.wazapps.familybox.splashAndLogin.MemberQueryFragment.QueryAnswerHandlerCallback;
import com.wazapps.familybox.splashAndLogin.StartFragment.StartScreenCallback;
import com.wazapps.familybox.util.LogUtils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity 
implements StartScreenCallback, BirthdayChooserCallback, GenderChooserCallback,
SignupScreenCallback, EmailLoginScreenCallback, QueryHandlerCallback,
QueryAnswerHandlerCallback {
	private static final String TAG_EMAIL_FRAG = "emailLogin";
	private static final String TAG_LOGIN_SCR = "loginScreen";
	private static final String TAG_SIGNBIRTHDAY = "birthdayDialog";
	private static final String TAG_SIGNGENDER = "genderDialog";
	private static final String TAG_SGINUP_FRAG = "signupScreen";
	private static final String TAG_FAMILYQUERY_FRAG = "familyQueryScreen";
	private static final String TAG_MEMBERQUERY_FRAG = "memberQueryScreen";
	private static final int SELECT_PICTURE = 0;
	
	private static final String GENDER_MALE = "male";
	private static final String ROLE_UNDEFINED = "undefined";
	private static final String RELATION_FATHER = "father";
	private static final String RELATION_MOTHER = "mother";
	private static final String RELATION_HUSBAND = "husband";
	private static final String RELATION_WIFE = "wife";
	private static final String RELATION_SON = "son";
	private static final String RELATION_DAUGHTER = "daughter";
	private static final String GENDER_KEY = "gender";
	private static final String CHILDREN_KEY = "children";
	
	public int familyQueryIndex = 0;
	public ArrayList<ParseObject> relatedFamilies = null;
	public ParseUser currentUser = null, currentFamilyMember = null;
	public FamilyMemberDetails2 currentFamilyMemberDetails = null;
	public ParseObject currentFamily = null;
	public ArrayList<ParseUser> relatedFamilyMembers = null;
	public ArrayList<FamilyMemberDetails2> relatedFamilyMemberDetails = null;

	private UserHandler userHandler = null;
	
	//pre-creating the callback functions used by parse to make code more readable
	//all these callbacks are defined in the "setupCallbackFunctions" method
	private SignUpCallback userCreationCallback = null;
	private SaveCallback familyCreationCallback = null;
	private FindCallback<ParseObject> familiesListFetchCallback = null;
	private UserHandler.FamilyMembersFetchCallback familyMembersFetchCallback = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		// Hide the status bar.
		getActionBar().hide();
		//checking if loginActivity was called by a sign out action or by
		//splash screen and determine the transition animations accordingly.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.containsKey(MainActivity.LOG_OUT_ACTION))
				overridePendingTransition(R.anim.enter_reverse, R.anim.exit_reverse); 

			else if (extras.containsKey(SplashActivity.SPLASH_ACTION))
				overridePendingTransition(R.anim.enter, R.anim.exit);
		}
		
		userHandler = new UserHandler();
		setUpCallbackFunctions();

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.fragment_container, new StartFragment(), TAG_LOGIN_SCR)
		.commit();
	}
	
	private void setUpCallbackFunctions() {
		//handles the sign up process after registering the user on parse
		userCreationCallback = new SignUpCallback() {
			private LoginActivity loginActivity;
			
			@Override
			public void done(ParseException e) {
				//if sign up succeeded - fetch all families
				//that might be related to user
				if (e == null) {
					String userNetwork = 
							loginActivity.currentUser.getString("network");
					String userFamilyName = 
							loginActivity.currentUser.getString("lastName");
					
					FamilyHandler.fetchRelatedFamilies(userFamilyName, 
							userNetwork, loginActivity.familiesListFetchCallback);
				} 
				
				//if sign up failed
				else {
					loginActivity.handleUserCreationError(e, false);
				}	
			}
			
			private SignUpCallback init(LoginActivity loginActivity) {
				this.loginActivity = loginActivity;
				return this;
			}
		}.init(this);
		
		//handles the beginning of the families list query after
		//fetching the related families list
		familiesListFetchCallback = new FindCallback<ParseObject>() {
			private LoginActivity loginActivity;
			
			@Override
			public void done(List<ParseObject> fetchedFamilies, 
					ParseException e) {
				
				//if fetching succeeded
				if (e == null) {
					loginActivity.relatedFamilies = 
							new ArrayList<ParseObject>(fetchedFamilies);
					
					loginActivity.familyQueryIndex = 0;
					loginActivity.handleFamilyQuery();
				} 
				
				//if fetching failed
				else {
					loginActivity.handleUserCreationError(e, true);
				}
			}
			
			private FindCallback<ParseObject> init(
					LoginActivity loginActivity) {
				this.loginActivity = loginActivity;
				return this;
			}
		}.init(this);
		
		//handles the launching process of the application
		//after creating a new family for the new user
		familyCreationCallback = new SaveCallback() {
			private LoginActivity loginActivity;
			
			@Override
			public void done(ParseException e) {
				//if family creation for the new user was successful
				//then user can enter the main application
				if (e == null) {
					loginActivity.enterApp();							
				} 
				
				//if family creation failed
				else {
					loginActivity.handleUserCreationError(e, true);
				}
			}
			
			private SaveCallback init(LoginActivity loginActivity) {
				this.loginActivity = loginActivity;
				return this;
			}
		}.init(this);
		
		//opens the family members query fragment after fetching 
		//the family members data
		familyMembersFetchCallback = new UserHandler.FamilyMembersFetchCallback() {
			private LoginActivity loginActivity;
			
			@Override
			public void done(ParseException e) {
				//if fetching was successful
				if (e == null) {
					loginActivity.launchFamilyQueryFragment();
				} 
				
				else {
					//if fetching failed
					loginActivity.handleUserCreationError(e, true);
				}
			}
			
			private UserHandler.FamilyMembersFetchCallback init(
					LoginActivity loginActivity) {
				this.loginActivity = loginActivity;
				return this;
			}
		}.init(this);
	}

	/**
	 * Launches the family query fragment, with the fetched
	 * families list
	 */
	protected void launchFamilyQueryFragment() {
		//progress to the next family in the related families list
		//in preparation for next iteration
		familyQueryIndex++;
		
		//pass arguments and control to family query fragment
		Bundle args = new Bundle();
		args.putParcelable(FamilyQueryFragment.MEMBER_ITEM, 
				new FamilyMemberDetails2(currentUser, ROLE_UNDEFINED));
		args.putParcelableArray(FamilyQueryFragment.QUERY_FAMILIES_LIST,
				relatedFamilyMemberDetails.toArray(new FamilyMemberDetails2
						[relatedFamilyMemberDetails.size()]));
		
		FamilyQueryFragment familyQueryFrag = new FamilyQueryFragment();
		familyQueryFrag.setArguments(args);
		//TODO: fix backstack bug
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, 
				R.anim.enter_reverse, R.anim.exit_reverse);
		ft.replace(R.id.fragment_container, familyQueryFrag, 
						TAG_FAMILYQUERY_FRAG).commit();
	}
	
	/**
	 * Launches the main activity, 
	 * call this function when you want to pass to
	 * the application's actual main screen
	 */
	private void enterApp() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * Used to decode real path from uri. used by the photo chooser.
	 */
	private String getRealPathFromURI(Uri contentURI) {
		final String[] imageColumns = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentURI, imageColumns,
				null, null, null);
		if (cursor == null) { 
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}

	@Override
	public void openEmailLogin() {
		EmailLoginDialogueFragment frag = new EmailLoginDialogueFragment();
		frag.show(getSupportFragmentManager(), TAG_EMAIL_FRAG);
	}

	@Override
	public void openFacebookLogin() {
		//right now we are not going to implement this feature
		Toast toast = Toast.makeText(this, "This feature is not yet available"
				, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	@Override
	public void openSignup() {
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.enter, R.anim.exit, 
				R.anim.enter_reverse, R.anim.exit_reverse)
		.replace(R.id.fragment_container, new EmailSignupFragment(), 
				TAG_SGINUP_FRAG)
		.addToBackStack(null).commit();
	}

	@Override
	public void openBirthdayInputDialog() {
		BirthdaySignupDialogFragment dialog = new BirthdaySignupDialogFragment();
		dialog.show(getSupportFragmentManager(), TAG_SIGNBIRTHDAY);
	}

	@Override
	public void openGenderInputDialog() {
		GenderSignupDialogFragment dialog = new GenderSignupDialogFragment();
		dialog.show(getSupportFragmentManager(), TAG_SIGNGENDER);
	}

	@Override
	public void setDate(String date) {
		EmailSignupFragment frag = (EmailSignupFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_SGINUP_FRAG);
		frag.setBirthday(date);
	}

	@Override
	public void setGender(String gender) {
		EmailSignupFragment frag = (EmailSignupFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_SGINUP_FRAG);
		frag.setGender(gender);
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
				FileInputStream fileInputStream = null;
				File file = null;
				String filename;
				byte[] fileData;
				Uri currImageURI = data.getData();
				file = new File(getRealPathFromURI(currImageURI));

				if (file.exists()) {
					try {
						filename = file.getName();
						fileData = new byte[(int)file.length()];
						fileInputStream = new FileInputStream(file);
						fileInputStream.read(fileData);
						fileInputStream.close();
					} 

					catch (FileNotFoundException e) {
						LogUtils.logError("LoginActivity.class", e.getMessage());
						return;

					} catch (IOException e) {
						LogUtils.logError("LoginActivity.class", e.getMessage());
						return;

					} finally {
						try {
							if (fileInputStream != null)
								fileInputStream.close();
						} catch (IOException e) {
							LogUtils.logError("LoginActivity.class", 
									e.getMessage());
							return;
						}
					}

					EmailSignupFragment frag = (EmailSignupFragment) 
							getSupportFragmentManager()
							.findFragmentByTag(TAG_SGINUP_FRAG);
					Bitmap myBitmap = BitmapFactory.decodeFile(
							file.getAbsolutePath());
					frag.setProfileImage(myBitmap, fileData, filename);
				}
			}
		}
	}

	@Override
	public void emailLoginAction(String email, String password) {
		try {
			InputHandler.validateLoginInput(email, password);
			ParseUser.logIn("fb_" + email, password);
			enterApp();
			//TODO: validate that user has passed family query
		} 

		catch (InputException e) {
			Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
		}

		catch (ParseException e) {
			Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
		}
	}

	@Override
	public void signUp(String firstName, String lastName, String email,
			String birthday, String gender, String password, 
			String passwordConfirm, byte[] profilePictureData, 
			String profilePictureName) {	

		String errMsg = InputHandler.validateSignupInput(firstName, lastName, email, 
				birthday, gender, password, passwordConfirm);
		if (!errMsg.equals("")) {	
			Toast toast = Toast.makeText(this, errMsg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}

		currentUser = userHandler.createNewUser(firstName, lastName, email, 
				gender, password, birthday, profilePictureData, 
				profilePictureName, userCreationCallback);
	} 
	
	@Override
	public void handleFamilyQuery() {
		//if no related families were found for the user 
		//create a new family for him/her
		if (familyQueryIndex >= relatedFamilies.size()) {
			FamilyHandler.createNewFamilyForUser(currentUser, 
					this, familyCreationCallback);
			return;
		}
						
		//else - check the current related family
		//fetch its members and show to user
		relatedFamilyMembers = new ArrayList<ParseUser>();
		relatedFamilyMemberDetails = new ArrayList<FamilyMemberDetails2>();
		currentFamily = relatedFamilies.get(familyQueryIndex);
		
		userHandler.fetchFamilyMembers(relatedFamilyMembers, 
				relatedFamilyMemberDetails, currentFamily, 
				familyMembersFetchCallback);
	}
	
	@Override
	public void handleMemberQuery() throws ParseException {
		currentFamilyMember = relatedFamilyMembers.get(0);
		currentFamily.fetchIfNeeded();
		currentFamilyMemberDetails = relatedFamilyMemberDetails.get(0);
		boolean isFatherTaken = currentFamily.has(RELATION_FATHER);
		boolean isMotherTaken = currentFamily.has(RELATION_MOTHER);
		boolean isMemberMale = currentFamilyMember.getString(GENDER_KEY)
				.equals(GENDER_MALE)? true : false;
		
		ArrayList<String> relationOptions = InputHandler
				.generateRelationOptions(currentUser, 
						currentFamilyMemberDetails, 
						isFatherTaken, isMotherTaken);
		
		//if only one relation option exists then the answer is already known 
		if (relationOptions.size() == 1) {
			handleMemberQueryAnswer(relationOptions.get(0));
		}
		
		Bundle args = new Bundle();
		args.putParcelable(
				MemberQueryFragment.FAMILY_MEMBER_ITEM, 
				currentFamilyMemberDetails);
		args.putStringArrayList(
				MemberQueryFragment.FAMILY_MEMBER_RELATION_OPTIONS, 
				relationOptions);
		args.putBoolean(MemberQueryFragment.FAMILY_MEMBER_GENDER, 
				isMemberMale);
		
		MemberQueryFragment memberQueryFrag = new MemberQueryFragment();
		memberQueryFrag.setArguments(args);
		getSupportFragmentManager().beginTransaction()
		.setCustomAnimations(R.anim.enter, R.anim.exit, 
				R.anim.enter_reverse, R.anim.exit_reverse)
				.replace(R.id.fragment_container, memberQueryFrag, 
						TAG_MEMBERQUERY_FRAG).commit();
	}
	
	@Override
	public void handleMemberQueryAnswer(String relation) throws ParseException {
		String userGender = currentUser.getString(GENDER_KEY);
		String familyMemberGender = currentFamilyMemberDetails.getGender();
		String familyMemberRole = currentFamilyMemberDetails.getRole();
		boolean isMemberUndefined = familyMemberRole
				.equals(ROLE_UNDEFINED)? true : false;
		boolean isUserMale = userGender
				.equals(GENDER_MALE)? true : false;
		boolean isMemberMale = familyMemberGender
				.equals(GENDER_MALE)? true : false;
		
		if (relation.equals(RELATION_FATHER) 
				|| relation.equals(RELATION_MOTHER)) {
			ParseRelation<ParseUser> children = 
					currentFamily.getRelation(CHILDREN_KEY);
			children.add(currentUser);
			if (isMemberUndefined) {
				currentFamily.put(relation, currentFamilyMember);
				currentFamily.remove(ROLE_UNDEFINED);
			}
		} 
		
		else if (relation.equals(RELATION_HUSBAND) 
				|| relation.equals(RELATION_WIFE)) {
			if (isUserMale) {
				currentFamily.put(RELATION_FATHER, currentUser);
			} else {
				currentFamily.put(RELATION_MOTHER, currentUser);
			}
			
			if (isMemberUndefined) {
				if (isMemberMale) {
					currentFamily.put(RELATION_FATHER, currentFamilyMember);					
				} else {
					currentFamily.put(RELATION_MOTHER, currentFamilyMember);
				}
				currentFamily.remove(ROLE_UNDEFINED);
			}
		}
		
		else if (relation.equals(RELATION_SON) 
				|| relation.equals(RELATION_DAUGHTER)) {
			if (isUserMale) {
				currentFamily.put(RELATION_FATHER, currentUser);
			} else {
				currentFamily.put(RELATION_MOTHER, currentUser);
			}
			
			if (isMemberUndefined) {
				ParseRelation<ParseUser> children = 
						currentFamily.getRelation(CHILDREN_KEY);
				children.add(currentFamilyMember);
				currentFamily.remove(ROLE_UNDEFINED);
			}
		}
		
		//if the user and family member are brothers\sisters:
		else {
			ParseRelation<ParseUser> children = 
					currentFamily.getRelation(CHILDREN_KEY);
			children.add(currentUser);
			if (isMemberUndefined) {
				children.add(currentFamilyMember);
				currentFamily.remove(ROLE_UNDEFINED);
			}
		}
		
		currentUser.put("passFamilyQuery", true);
		currentUser.put("family", currentFamily);
		currentUser.save();
		enterApp();
	}
	
	public void handleUserCreationError(ParseException e, boolean wasUserCreated) {
		String errMsg = (e.getMessage().startsWith("username"))? 
			"Email already taken" : 
			"Error in user creation, please sign up again";
		Toast toast = Toast.makeText(this, errMsg, Toast.LENGTH_LONG);
		LogUtils.logError("LoginActivity", e.getMessage());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
		
		if (wasUserCreated) {
			ParseUser.logOut();
			currentUser.deleteInBackground();			
		}
	}
}
