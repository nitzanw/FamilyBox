package com.wazapps.familybox;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class EmailLoginDialogueFragment extends DialogFragment {
	private View root;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_dialog_email_login, 
				container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return root;
	}
}
