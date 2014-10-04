package com.wazapps.familybox;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

public class EmailLoginDialogueFragment extends DialogFragment implements OnClickListener {
	private View root;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_dialog_email_login, 
				container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		ImageButton exitButton = (ImageButton) root.findViewById(R.id.email_login_exit);
		ImageButton cancelButton = (ImageButton) root.findViewById(R.id.email_login_cancel_button);
		exitButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_login_exit:
			dismiss();
			break;
			
		case R.id.email_login_cancel_button:
			dismiss();
			break;

		default:
			break;
		}
		
	}
}
