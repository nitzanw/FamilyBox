package com.wazapps.familybox.splashAndLogin;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wazapps.familybox.R;

public class ChangePasswordDialogFragment extends DialogFragment implements
		OnClickListener {
	public static final String CHANGE_PASSWORD_DIALOG_FRAG = "change password dialog frag";
	private View root;
	private EditText mCurrentPw;
	private EditText mNewPw;
	private EditText mConfirmtPw;
	private ImageButton mChangeButtonCancel;
	private ImageButton mChangeButton;
	private TextView mErrorMsg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_dialog_change_password,
				container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		// set the error msg to visible and set appropriate error msg:
		mErrorMsg = (TextView) root.findViewById(R.id.tv_change_pw_error_msg);
		mCurrentPw = (EditText) root.findViewById(R.id.et_current_pw);
		mNewPw = (EditText) root.findViewById(R.id.et_new_pw);
		mConfirmtPw = (EditText) root.findViewById(R.id.et_confirm_pw);
		((ImageButton) root.findViewById(R.id.ib_change_pw_exit))
				.setOnClickListener(this);
		((ImageButton) root.findViewById(R.id.ib_change_pw))
				.setOnClickListener(this);

		return root;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_change_pw_exit) {
			dismiss();
		} else if (v.getId() == R.id.ib_change_pw) {
			// TODO change password here
		}

	}
}
