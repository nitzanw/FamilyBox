package com.wazapps.familybox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

public class EmailLoginDialogueFragment extends DialogFragment implements OnClickListener {
	public interface LoginActionCallbackListener {
		public void loginAction();
	}
	
	private LoginActionCallbackListener loginCallback;
	private View root;	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			loginCallback = (LoginActionCallbackListener) getActivity();			
		} catch(ClassCastException e) {
			Log.e("loginCallbackErr", "Activity should implement LogActionCallbackListener interface");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_dialog_email_login, 
				container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		ImageButton exitButton = (ImageButton) root.findViewById(R.id.email_login_exit);
		ImageButton signButton = (ImageButton) root.findViewById(R.id.email_login_log_button);
		ImageButton cancelButton = (ImageButton) root.findViewById(R.id.email_login_cancel_button);
		exitButton.setOnClickListener(this);
		signButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_login_exit:
			dismiss();
			break;
			
		case R.id.email_login_log_button:
			loginCallback.loginAction();
			break;
			
		case R.id.email_login_cancel_button:
			dismiss();
			break;

		default:
			break;
		}
		
	}
}
