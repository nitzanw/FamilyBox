package com.wazapps.familybox;

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
	private birthdayCallbackListener birthdayCallback = null;
	private DatePicker mDatePicker;

	public interface birthdayCallbackListener {
		public void setDate(String date);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			birthdayCallback = (birthdayCallbackListener) getActivity();
		} catch (ClassCastException e) {
			Log.e(getTag(),
					"the activity did not implement birthdayCallbackListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		root = inflater.inflate(R.layout.fragment_dialog_birthday, container,
				false);
		ImageButton exitBtn = (ImageButton) root
				.findViewById(R.id.ib_exit_signup_birthday);
		exitBtn.setOnClickListener(this);
		mDatePicker = (DatePicker) root
				.findViewById(R.id.datePicker_signup_birthday);
		mDatePicker.setCalendarViewShown(false);
		Button okBtn = (Button) root.findViewById(R.id.button_signup_birthday);
		okBtn.setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_signup_birthday) {
			int day = mDatePicker.getDayOfMonth();
			int month = mDatePicker.getMonth();
			int year = mDatePicker.getYear();
			birthdayCallback.setDate(String.valueOf(month) + "/"
					+ String.valueOf(day) + "/" + String.valueOf(year));
			this.dismiss();
		} else if (v.getId() == R.id.ib_exit_signup_birthday) {
			this.dismiss();
		}
	}
}
