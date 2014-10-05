package com.wazapps.familybox.splashAndLogin;

import com.wazapps.familybox.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

public class EmailLoginDialogueFragment extends DialogFragment 
implements OnClickListener {
	
	public interface EmailLoginScreenCallback {
		public void emailLoginAction();
	}
	
	private EmailLoginScreenCallback loginCallback;
	private View root;	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			loginCallback = (EmailLoginScreenCallback) getActivity();			
		} 
		
		catch(ClassCastException e) {
			Log.e("loginCallbackErr", 
					"Activity should implement LogActionCallbackListener interface");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		root = inflater.inflate(R.layout.fragment_dialog_email_login, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		root.findViewById(R.id.email_login_exit).setOnClickListener(this);
		root.findViewById(R.id.email_login_log_button).setOnClickListener(this);
		root.findViewById(R.id.email_login_cancel_button).setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_login_exit:
			dismiss();
			break;
			
		case R.id.email_login_log_button:
			loginCallback.emailLoginAction();
			break;
			
		case R.id.email_login_cancel_button:
			dismiss();
			break;

		default:
			break;
		}
		
	}
}
