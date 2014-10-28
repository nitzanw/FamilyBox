package com.wazapps.familybox.splashAndLogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.wazapps.familybox.R;

public class GenderSignupDialogFragment extends DialogFragment 
		implements OnClickListener {
	private View root;
	private GenderChooserCallback genderCallback = null;
	
	public interface GenderChooserCallback {
		public void setGender(String gender);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.genderCallback = (GenderChooserCallback) getActivity();
		}
		
		catch(ClassCastException e) {
			Log.e(getTag(),
			"the activity does not implement GenderCallbackListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		root = inflater.inflate(R.layout.fragment_dialog_gender, container, false);
		
		ImageButton exitButton = (ImageButton) root.findViewById(R.id.ib_exit_signup_gender);
		Button maleButton = (Button) root.findViewById(R.id.button_signup_male);
		Button femaleButton = (Button) root.findViewById(R.id.button_signup_female);
		
		exitButton.setOnClickListener(this);
		maleButton.setOnClickListener(this);
		femaleButton.setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_exit_signup_gender:
			this.dismiss();
			break;
			
		case R.id.button_signup_male:
			this.genderCallback.setGender("Male");
			this.dismiss();
			break;
			
		case R.id.button_signup_female:
			this.genderCallback.setGender("Female");
			this.dismiss();
			break;

		default:
			break;
		}		
	}

}
