package com.wazapps.familybox.splashAndLogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.misc.InputException;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.FamilyMemberDetails2;
import com.wazapps.familybox.profiles.ProfileDetails;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.BirthdayChooserCallback;
import com.wazapps.familybox.splashAndLogin.EmailLoginDialogueFragment.EmailLoginScreenCallback;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment.SignupScreenCallback;
import com.wazapps.familybox.splashAndLogin.FamilyQueryFragment.QueryHandlerCallback;
import com.wazapps.familybox.splashAndLogin.GenderSignupDialogFragment.GenderChooserCallback;
import com.wazapps.familybox.splashAndLogin.MemberQueryFragment.QueryAnswerHandlerCallback;
import com.wazapps.familybox.splashAndLogin.StartFragment.StartScreenCallback;
import com.wazapps.familybox.util.FamilyHandler;
import com.wazapps.familybox.util.InputValidator;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.UserHandler;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
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
	private static final String GENDER_FEMALE = "female";
	private static final String ROLE_PARENT = "parent";
	private static final String ROLE_CHILD = "child";
	private static final String RELATION_FATHER = "father";
	private static final String RELATION_MOTHER = "mother";
	private static final String RELATION_BROTHER = "brother";
	private static final String RELATION_SISTER = "sister";
	private static final String RELATION_HUSBAND = "husband";
	private static final String RELATION_WIFE = "wife";
	private static final String RELATION_SON = "son";
	private static final String RELATION_DAUGHTER = "daughter";
	
	private int familyQueryIndex = 0;
	ArrayList<ParseObject> relatedFamilies = null;
	ParseUser currentUser = null, currentFamilyMember = null;
	FamilyMemberDetails2 currentFamilyMemberDetails = null;
	ParseObject currentFamily = null;
	ArrayList<ParseUser> relatedFamilyMembers = null;
	ArrayList<FamilyMemberDetails2> relatedFamilyMemberDetails = null;

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

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.fragment_container, new StartFragment(), TAG_LOGIN_SCR)
		.commit();
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
			InputValidator.validateLoginInput(email, password);
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

		try {
			InputValidator.validateSignupInput(firstName, lastName, email, 
					birthday, gender, password, passwordConfirm);

			currentUser = UserHandler.createNewUser(firstName, lastName, email, 
					gender, password, birthday, profilePictureData, 
					profilePictureName);

			relatedFamilies = FamilyHandler.getRelatedFamilies (
					currentUser.getString("network"), 
					currentUser.getString("lastName"));
			
			familyQueryIndex = 0;
			handleFamilyQuery();
		} 

		catch (InputException e) {
			Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		} 
		
		catch (ParseException e) {
			//TODO: maybe handle parseExceptions in a better way
			String errMsg = e.getMessage();		
			Toast toast = Toast.makeText(this, errMsg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}
	}
	
	@Override
	public void handleFamilyQuery() throws ParseException {
		//if no related families were found for the user 
		//create a new family for him/her
		if (familyQueryIndex >= relatedFamilies.size()) {
			FamilyHandler.createNewFamilyForUser(currentUser);
			enterApp();
			return;
		}
						
		//else - check the current related family
		relatedFamilyMembers = new ArrayList<ParseUser>();
		relatedFamilyMemberDetails = new ArrayList<FamilyMemberDetails2>();
		currentFamily = relatedFamilies.get(familyQueryIndex);
		UserHandler.fetchFamilyMembers(relatedFamilyMembers, 
				relatedFamilyMemberDetails, currentFamily);
		
		//progress to the next family in the related families list
		//in preparation for next iteration
		familyQueryIndex++;
		
		//pass arguments and control to family query fragment
		Bundle args = new Bundle();
		args.putParcelable(FamilyQueryFragment.MEMBER_ITEM, 
				new FamilyMemberDetails2(currentUser, "undefined"));
		
		args.putParcelableArray(FamilyQueryFragment.QUERY_FAMILIES_LIST,
				relatedFamilyMemberDetails.toArray(
						new FamilyMemberDetails2
						[relatedFamilyMemberDetails.size()]));
		
		FamilyQueryFragment familyQueryFrag = new FamilyQueryFragment();
		familyQueryFrag.setArguments(args);
		getSupportFragmentManager().beginTransaction()
		.setCustomAnimations(R.anim.enter, R.anim.exit, 
				R.anim.enter_reverse, R.anim.exit_reverse)
				.replace(R.id.fragment_container, familyQueryFrag, 
						TAG_FAMILYQUERY_FRAG).commit();
	}
	
	@Override
	public void handleMemberQuery() throws ParseException {
		currentFamilyMember = relatedFamilyMembers.get(0);
		currentFamily.fetchIfNeeded();
		currentFamilyMemberDetails = relatedFamilyMemberDetails.get(0);
		boolean isFatherTaken = currentFamily.has("father");
		boolean isMotherTaken = currentFamily.has("mother");
		boolean isMemberMale = currentFamilyMember.getString("gender")
				.equals("male")? true : false;
		
		ArrayList<String> relationOptions = 
				generateRelationOptions(isFatherTaken, isMotherTaken);
		
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
		args.putBoolean(MemberQueryFragment.FAMILY_MEMBER_GENDER, isMemberMale);
		
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
		String userGender = currentUser.getString("gender");
		String familyMemberGender = currentFamilyMemberDetails.getGender();
		String familyMemberRole = currentFamilyMemberDetails.getRole();
		
		if (relation.equals("father") || relation.equals("mother")) {
			ParseRelation<ParseUser> children = 
					currentFamily.getRelation("children");
			children.add(currentUser);
			if (familyMemberRole.equals("undefined")) {
				currentFamily.put(relation, currentFamilyMember);
				currentFamily.remove("undefinedFamilyMember");
			}
		} 
		
		else if (relation.equals("husband") || relation.equals("wife")) {
			if (userGender.equals(GENDER_MALE)) {
				currentFamily.put("father", currentUser);
			} else {
				currentFamily.put("mother", currentUser);
			}
			
			if (familyMemberRole.equals("undefined")) {
				if (familyMemberGender.equals(GENDER_MALE)) {
					currentFamily.put("father", currentFamilyMember);					
				} else {
					currentFamily.put("mother", currentFamilyMember);
				}
				currentFamily.remove("undefinedFamilyMember");
			}
		}
		
		else if (relation.equals("son") || relation.equals("daughter")) {
			if (userGender.equals(GENDER_MALE)) {
				currentFamily.put("father", currentUser);
			} else {
				currentFamily.put("mother", currentUser);
			}
			
			if (familyMemberRole.equals("undefined")) {
				ParseRelation<ParseUser> children = 
						currentFamily.getRelation("children");
				children.add(currentFamilyMember);
				currentFamily.remove("undefinedFamilyMember");
			}
		}
		
		//if the user and family member are brothers\sisters:
		else {
			ParseRelation<ParseUser> children = 
					currentFamily.getRelation("children");
			children.add(currentUser);
			if (familyMemberRole.equals("undefined")) {
				children.add(currentFamilyMember);
				currentFamily.remove("undefinedFamilyMember");
			}
		}
		
		currentUser.put("passFamilyQuery", true);
		currentUser.put("family", currentFamily);
		currentUser.save();
		enterApp();
	}
	
	public ArrayList<String> generateRelationOptions(
			boolean isFatherTaken, boolean isMotherTaken) {
		String userGender = currentUser.getString("gender");
		String familyMemberGender = currentFamilyMemberDetails.getGender();
		String familyMemberRole = currentFamilyMemberDetails.getRole();
		ArrayList<String> options = new ArrayList<String>();
		
		//if user is male
		if (userGender.equals(GENDER_MALE)) {
			//if family member is male
			if (familyMemberGender.equals(GENDER_MALE)) {
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_FATHER);
				} 
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_BROTHER);
					if (!isFatherTaken) {						
						options.add(RELATION_SON);
					}
				} 
				
				//if family member role is undefined
				else {
					options.add(RELATION_BROTHER);
					if (!isFatherTaken) {
						options.add(RELATION_SON);
						options.add(RELATION_FATHER);						
					}
				}
			}
			
			//if family member is female
			else if (familyMemberGender.equals(GENDER_FEMALE)) {	
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_MOTHER);
					if (!isFatherTaken) {
						options.add(RELATION_WIFE);						
					}
				} 
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_SISTER);
					if (!isFatherTaken) {
						options.add(RELATION_DAUGHTER);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(RELATION_SISTER);
					if (!isFatherTaken) {
						options.add(RELATION_DAUGHTER);
						if (!isMotherTaken) {
							options.add(RELATION_WIFE);							
						}
					}
					
					if (!isMotherTaken) {
						options.add(RELATION_MOTHER);
					}
				}
			}
		}
		
		//if user is female
		else if (userGender.equals(GENDER_FEMALE)) {
			//if family member is male
			if (familyMemberGender.equals(GENDER_MALE)) {
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_FATHER);
					if (!isMotherTaken) {
						options.add(RELATION_HUSBAND);
					}
				}
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_BROTHER);
					if (!isMotherTaken) {
						options.add(RELATION_SON);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(RELATION_BROTHER);
					if (!isFatherTaken) {
						options.add(RELATION_FATHER);
						if (!isMotherTaken) {
							options.add(RELATION_HUSBAND);
						}
					}
					
					if (!isMotherTaken) {
						options.add(RELATION_SON);
					}
				}
			}
			
			//if family member is also a female
			else if (familyMemberGender.equals(GENDER_FEMALE)) {
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_MOTHER);
				}
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_SISTER);
					if (!isMotherTaken) {
						options.add(RELATION_DAUGHTER);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(RELATION_SISTER);
					if (!isMotherTaken) {
						options.add(RELATION_MOTHER);
						options.add(RELATION_DAUGHTER);
					}
				}
			}			
		}
		
		return options;
	}
}
