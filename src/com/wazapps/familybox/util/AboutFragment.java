package com.wazapps.familybox.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wazapps.familybox.R;

public class AboutFragment extends DialogFragment implements OnClickListener {
	public static final String ABOUT_DIALOG_FRAG = "about dialog fragment";
	private View root;
	private TextView mVersion;
	private ImageView mChuck;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		root = inflater.inflate(R.layout.fragment_dialog_about, container,
				false);
		((ImageButton) root.findViewById(R.id.ib_about_exit))
				.setOnClickListener(this);
		((Button) root.findViewById(R.id.button_egg)).setOnClickListener(this);
		mChuck = (ImageView) root.findViewById(R.id.iv_about_chuck);
		mVersion = (TextView) root.findViewById(R.id.tv_about_version);

		String versionName = getVersion();

		mVersion.setText(getString(R.string.version) + " " + versionName);
		return root;
	}

	private String getVersion() {
		Context context = getActivity().getApplicationContext(); // or
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();
		try {
			return packageManager.getPackageInfo(packageName, 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "Version not available";
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_about_exit) {
			dismiss();
		} else if (v.getId() == R.id.button_egg) {
			mChuck.setVisibility(View.VISIBLE);
		}

	}

}
