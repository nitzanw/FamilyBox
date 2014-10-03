package com.wazapps.familybox;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class LoginActivity extends FragmentActivity {
	private static final String TAG_EMAIL_FRAG = "emailLogin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, new EmailSignupFragment(), TAG_EMAIL_FRAG);
		ft.commit();		
	}
}



