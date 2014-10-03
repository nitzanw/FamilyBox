package com.wazapps.familybox;

import com.wazapps.familybox.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginFragment extends Fragment implements OnClickListener {
	public interface LoginCallback {
		public void emailLogin();
		public void facebookLogin();
		public void signup();
	}

	private View root;
	private LoginCallback loginCB = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.loginCB = (LoginCallback) getActivity();
		} catch(ClassCastException e){
			Log.e(getTag(), 
					"The activity does implement LoginCallback interface");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_login_screen, container, false);
		
		Button emailLoginButton = (Button) root.findViewById(R.id.button_login_email);
		Button fbLoginButton = (Button) root.findViewById(R.id.button_login_facebook);
		Button signupButton = (Button) root.findViewById(R.id.button_signup);
		
		emailLoginButton.setOnClickListener(this);
		fbLoginButton.setOnClickListener(this);
		signupButton.setOnClickListener(this);
		
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.button_login_email:
			this.loginCB.emailLogin();
			break;

		case id.button_signup:
			this.loginCB.signup();
			
		default:
			break;
		}
	}	
}
