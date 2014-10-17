package com.wazapps.familybox.splashAndLogin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.misc.InputException;
import com.wazapps.familybox.newsfeed.NewsfeedActivity;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.BirthdayChooserCallback;
import com.wazapps.familybox.splashAndLogin.EmailLoginDialogueFragment.EmailLoginScreenCallback;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment.SignupScreenCallback;
import com.wazapps.familybox.splashAndLogin.StartFragment.StartScreenCallback;
import com.wazapps.familybox.util.FamilyHandler;
import com.wazapps.familybox.util.InputValidator;
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
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity 
implements StartScreenCallback, BirthdayChooserCallback, 
SignupScreenCallback, EmailLoginScreenCallback {
	private static final String TAG_EMAIL_FRAG = "emailLogin";
	private static final String TAG_LOGIN_SCR = "loginScreen";
	private static final String TAG_SIGNBIRTHDAY = "birthdayDialog";
	private static final String TAG_SGINUP_FRAG = "signupScreen";
	private static final int SELECT_PICTURE = 0;
	
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
			if (extras.containsKey(ActivityWithDrawer.LOG_OUT_ACTION))
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
		Intent intent = new Intent(this, NewsfeedActivity.class);
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDate(String date) {
		EmailSignupFragment frag = (EmailSignupFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_SGINUP_FRAG);
		frag.setBirthday(date);
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
				File file = null;
				file = new File(getRealPathFromURI(currImageURI));

				if (file.exists()) {
					EmailSignupFragment frag = (EmailSignupFragment) 
							getSupportFragmentManager().findFragmentByTag(TAG_SGINUP_FRAG);
					Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
					frag.setBitmap(myBitmap);
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
			String birthday, String password, String passwordConfirm) {
		ParseUser newUser = null;
		ArrayList<ParseObject> relatedFamilies = null;
		
		try {
			InputValidator.validateSignupInput(firstName, lastName, email, 
					birthday, password, passwordConfirm);
			
			newUser = UserHandler.createNewUser(firstName, lastName, email, 
					password, birthday);
				
			relatedFamilies = FamilyHandler.getRelatedFamilies (
					newUser.getInt("network"), newUser.getString("lastName"));
			
			if (relatedFamilies.size() == 0) {
				Toast.makeText(this,"creating new family!", Toast.LENGTH_LONG).show();
				//if the user has no related families in network
				//create new family and jump to main application screen
				FamilyHandler.createNewFamilyForUser(newUser);
				enterApp();
			} else {
				//the user has related families - open family selection menu
				Toast.makeText(this,"family do exist!", Toast.LENGTH_LONG).show();
			}
		} catch (InputException e) {
			Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		} catch (ParseException e) {
			//TODO: maybe handle parseExceptions in a better way
			String errMsg = e.getMessage();		
			Toast toast = Toast.makeText(this, errMsg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}
	}
}
