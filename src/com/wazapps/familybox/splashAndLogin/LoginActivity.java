package com.wazapps.familybox.splashAndLogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.parse.ParseException;
import com.parse.ParseObject;
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
SignupScreenCallback, EmailLoginScreenCallback, QueryHandlerCallback {
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
	private static final String RELATION_FATHER = "He is my father";
	private static final String RELATION_MOTHER = "She is my mother";
	private static final String RELATION_BROTHER = "He is my brother";
	private static final String RELATION_SISTER = "She is my sister";
	private static final String RELATION_HUSBAND = "He is my husband";
	private static final String RELATION_WIFE = "She is my wife";
	private static final String RELATION_SON = "He is my son";
	private static final String RELATION_DAUGHTER = "She is my daughter";
	
	
	private int familyQueryIndex = 0;
	ArrayList<ParseObject> relatedFamilies = null;
	ParseUser currentUser = null;
	ArrayList<FamilyMemberDetails2> currentRelatedFamily = null;

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
//		Toast toast = Toast.makeText(this, "This feature is not yet available"
//				, Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//		toast.show();
		try {
			currentUser = ParseUser.logIn("fb_test@tester.com", "1111");
			relatedFamilies = new ArrayList<ParseObject>();
			relatedFamilies.add(new ParseObject("LOL"));
			relatedFamilies.add(new ParseObject("ROFL"));
			handleFamilyQuery();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void openSignup() {
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_reverse, R.anim.exit_reverse)
		.replace(R.id.fragment_container, new EmailSignupFragment(),TAG_SGINUP_FRAG)
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
							LogUtils.logError("LoginActivity.class", e.getMessage());
							return;
						}
					}

					EmailSignupFragment frag = (EmailSignupFragment) 
							getSupportFragmentManager().findFragmentByTag(TAG_SGINUP_FRAG);
					Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
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
		}
				
		//else - check the current related family
		//TODO: get real data from parse about current family members
		currentRelatedFamily = generateTempFamilyMembersData();
	
		FamilyMemberDetails2 currUser = 
				new FamilyMemberDetails2(currentUser, "undefined");
		
		//progress to the next family in the related families list
		//in preparation for next iteration
		familyQueryIndex++;
		
		//pass control to family query fragment
		Bundle args = new Bundle();
		args.putParcelable(FamilyQueryFragment.MEMBER_ITEM, currUser);
		args.putParcelableArray(FamilyQueryFragment.QUERY_FAMILIES_LIST,
				currentRelatedFamily.toArray(
						new FamilyMemberDetails2[currentRelatedFamily.size()]));
		
		FamilyQueryFragment familyQueryFrag = new FamilyQueryFragment();
		familyQueryFrag.setArguments(args);
		getSupportFragmentManager().beginTransaction()
		.setCustomAnimations(R.anim.enter, R.anim.exit, 
				R.anim.enter_reverse, R.anim.exit_reverse)
				.replace(R.id.fragment_container, familyQueryFrag, 
						TAG_FAMILYQUERY_FRAG).commit();
	}
	
	@Override
	public void handleMemberQuery() {
		FamilyMemberDetails2 currFamilyMember = 
				currentRelatedFamily.get(0);
		ArrayList<String> relationOptions = 
				generateRelationOptions(currFamilyMember.getRole(), 
						currFamilyMember.getGender(), 
						currentUser.getString("gender"), true, true);
		
		//if only one relation option exists then the answer is already known 
		if (relationOptions.size() == 1) {
			enterApp();
		}
		
		Bundle args = new Bundle();
		args.putParcelable(
				MemberQueryFragment.FAMILY_MEMBER_ITEM, currFamilyMember);
		args.putStringArrayList(
				MemberQueryFragment.FAMILY_MEMBER_RELATION_OPTIONS, relationOptions);
		
		MemberQueryFragment memberQueryFrag = new MemberQueryFragment();
		memberQueryFrag.setArguments(args);
		getSupportFragmentManager().beginTransaction()
		.setCustomAnimations(R.anim.enter, R.anim.exit, 
				R.anim.enter_reverse, R.anim.exit_reverse)
				.replace(R.id.fragment_container, memberQueryFrag, 
						TAG_MEMBERQUERY_FRAG).commit();
	}
	
	public ArrayList<String> generateRelationOptions(
			String familyMemberRole, String familyMemberGender,
			String userGender, boolean isFatherTaken, boolean isMotherTaken) {
		
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
	
	private ArrayList<FamilyMemberDetails2> generateTempFamilyMembersData() {
		ArrayList<FamilyMemberDetails2> familyMembers = 
				new ArrayList<FamilyMemberDetails2>();
		
		FamilyMemberDetails2 arie = new FamilyMemberDetails2
				("1","1","","Arie","Zohar","parent","","","","","","","male","");
		FamilyMemberDetails2 mati = new FamilyMemberDetails2
				("2","1","","Mati","Zohar","parent","","","","","","","female","");
		FamilyMemberDetails2 tal = new FamilyMemberDetails2
				("3","1","","Tal","Zohar","child","","","","","","","female","");
		
		familyMembers.add(arie);
		familyMembers.add(mati);
		familyMembers.add(tal);
		
		return familyMembers;
	}
	
	private Bundle getMemberQueryArgsTemp() {
		ProfileDetails[] profileDetailsData = { null, null, null, null };
		profileDetailsData[0] = (new ProfileDetails("Address",
				"K. yovel, mozkin st."));
		profileDetailsData[1] = (new ProfileDetails("Birthday", "19.10.1987"));
		profileDetailsData[2] = (new ProfileDetails("Previous Family Names",
				"No previous family names"));
		profileDetailsData[3] = (new ProfileDetails("Quotes",
				"For every every there exists exists"));
		
		FamilyMemberDetails dad = new FamilyMemberDetails("0", "1","",
				getString(R.string.father_name), "Zohar",
				getString(R.string.parent), "", "", "", "", "", "",
				"m",profileDetailsData);
		
		ArrayList<String> rofl = new ArrayList<String>();
		rofl.add("He's my dad");
		rofl.add("and he's really awesome");
		rofl.add("and cool");
		rofl.add("and cool2");
		rofl.add("and coo3");
		
		Bundle args = new Bundle();
		args.putParcelable(MemberQueryFragment.FAMILY_MEMBER_ITEM, dad);
		args.putStringArrayList(MemberQueryFragment.FAMILY_MEMBER_RELATION_OPTIONS, rofl);
		return args;
	}
}
