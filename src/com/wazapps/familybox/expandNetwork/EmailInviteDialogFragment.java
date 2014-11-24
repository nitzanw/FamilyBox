package com.wazapps.familybox.expandNetwork;

import com.wazapps.familybox.R;
import com.wazapps.familybox.expandNetwork.ExpandNetworkFragment.InviteCallback;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.util.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EmailInviteDialogFragment extends DialogFragment 
implements OnClickListener {
	private View root;
	private InviteCallback inviteHandler = null;
	private EditText emailAddress;
	private ImageButton exitButton, sendButton;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			inviteHandler = (InviteCallback) getActivity();
		} catch (ClassCastException e) {
			LogUtils.logError(getTag(), "activity does not implement " +
					"InviteHandler interface");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_dialog_email_invite, 
				container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		emailAddress = (EditText) 
				root.findViewById(R.id.email_invite_adress_input);
		exitButton = (ImageButton)
				root.findViewById(R.id.email_invite_exit);
		sendButton = (ImageButton)
				root.findViewById(R.id.email_invite_send_button);
		
		exitButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_invite_exit:
			dismiss();			
			break;
			
		case R.id.email_invite_send_button:
			handleEmailInvite();
			break;

		default:
			break;
		}		
	}
	
	private void handleEmailInvite() {
		String email = emailAddress.getText().toString().trim().toLowerCase();
		if (email.isEmpty()) {
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), 
					"Please write an email address", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}
		
		if (!InputHandler.validateEmailAddress(email)) {
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), 
					"Invalid email address", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}
		
		inviteHandler.emailInvite(email);
		dismiss();
	}
}
