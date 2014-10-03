package com.wazapps.familybox;

import com.wazapps.familybox.LoginFragment.LoginCallback;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class LoginActivity extends FragmentActivity implements LoginCallback{
	private static final String TAG_EMAIL_FRAG = "emailLogin";
	private static final String TAG_LOGIN_SCR = "loginScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, new LoginFragment(), TAG_LOGIN_SCR);
		ft.commit();		
	}

	@Override
	public void emailLogin() {		
		Log.d("ROFL", "LOLZ LOLZ LOLZ");
		EmailLoginDialogueFragment frag = new EmailLoginDialogueFragment();
		frag.show(getSupportFragmentManager(), TAG_EMAIL_FRAG);
	}

	@Override
	public void facebookLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signup() {
		// TODO Auto-generated method stub
		
	}
}



