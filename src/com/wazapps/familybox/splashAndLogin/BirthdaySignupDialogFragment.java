package com.wazapps.familybox.splashAndLogin;

import com.wazapps.familybox.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

public class BirthdaySignupDialogFragment extends DialogFragment implements
		OnClickListener {
	private View root;
	private BirthdayChooserCallback birthdayCallback = null;
	private DatePicker picker;

	public interface BirthdayChooserCallback {
		public void setDate(String date);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			birthdayCallback = (BirthdayChooserCallback) getActivity();
		} 
		
		catch (ClassCastException e) {
			Log.e(getTag(),
					"the activity does not implement birthdayCallbackListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		root = inflater.inflate(R.layout.fragment_dialog_birthday, container, false);
		
		ImageButton exitButton = (ImageButton) root.findViewById(R.id.ib_exit_signup_birthday);
		exitButton.setOnClickListener(this);
		
		this.picker = (DatePicker) root.findViewById(R.id.datePicker_signup_birthday);
		this.picker.setCalendarViewShown(false);
		
		Button okButton = (Button) root.findViewById(R.id.button_signup_birthday);
		okButton.setOnClickListener(this);
		
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_signup_birthday:
			int day = picker.getDayOfMonth();
			int month = picker.getMonth();
			int year = picker.getYear();
			this.birthdayCallback.setDate(String.valueOf(month + 1) + "/"
					+ String.valueOf(day) + "/" + String.valueOf(year));
			this.dismiss();
			break;
			
		case R.id.ib_exit_signup_birthday:
			this.dismiss();
			break;

		default:
			break;
		}
	}
}