package com.wazapps.familybox;

import com.wazapps.familybox.BirthdaySignupDialogFragment.birthdayCallbackListener;
import com.wazapps.familybox.EmailSignupFragment.signupCallbackListener;
import com.wazapps.familybox.LoginFragment.LoginCallback;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class LoginActivity extends FragmentActivity implements LoginCallback,
		birthdayCallbackListener, signupCallbackListener {
	private static final String TAG_EMAIL_FRAG = "emailLogin";
	private static final String TAG_LOGIN_SCR = "loginScreen";
	private static final String TAG_SIGNBIRTHDAY = "birthdayDialog";
	private static final String TAG_SGINUP_FRAG = "signupScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_login_screen);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, new LoginFragment(), TAG_LOGIN_SCR);
		ft.commit();
	}

	@Override
	public void emailLogin() {
		EmailLoginDialogueFragment frag = new EmailLoginDialogueFragment();
		frag.show(getSupportFragmentManager(), TAG_EMAIL_FRAG);
	}

	@Override
	public void facebookLogin() {

	}

	@Override
	public void signup() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, new EmailSignupFragment(), TAG_SGINUP_FRAG);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void setDate(String date) {
		EmailSignupFragment frag = (EmailSignupFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_SGINUP_FRAG);
		frag.setBirthday(date);

	}

	@Override
	public void openBirthdayInputDialog() {
		BirthdaySignupDialogFragment dialog = new BirthdaySignupDialogFragment();
		dialog.show(getSupportFragmentManager(), TAG_SIGNBIRTHDAY);

	}
}
