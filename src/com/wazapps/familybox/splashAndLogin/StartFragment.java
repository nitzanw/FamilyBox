package com.wazapps.familybox.splashAndLogin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wazapps.familybox.R;
import com.wazapps.familybox.R.id;
import com.wazapps.familybox.util.WaveDrawable;

public class StartFragment extends Fragment implements OnClickListener {
	public interface StartScreenCallback {
		public void openEmailLogin();

		public void openFacebookLogin();

		public void openSignup();
	}

	private View root;
	private StartScreenCallback loginCB = null;
	private LinearLayout progressSpinner;
	private Button emailLoginButton, fbLoginButton, signupButton;
	private Animation buttonPress = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.loginCB = (StartScreenCallback) getActivity();
		}

		catch (ClassCastException e) {
			Log.e(getTag(),
					"The activity does not implement LoginCallback interface");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_login_screen, container,
				false);
		progressSpinner = (LinearLayout) root
				.findViewById(R.id.ll_progress_spinner);
	
		emailLoginButton = (Button) root
				.findViewById(R.id.button_login_email);
		fbLoginButton = (Button) root
				.findViewById(R.id.button_login_facebook);
		signupButton = (Button) root.findViewById(R.id.button_signup);

		emailLoginButton.setOnClickListener(this);
		fbLoginButton.setOnClickListener(this);
		signupButton.setOnClickListener(this);

		initAnimations();
		return root;
	}

	private void initAnimations() {
		ImageView fbLogo = (ImageView) root.findViewById(R.id.iv_login_logo);
		Animation pulse = AnimationUtils.loadAnimation(getActivity(),
				R.anim.pulse_slow);
		pulse.setInterpolator(new AccelerateInterpolator(3));
		fbLogo.startAnimation(pulse);
		WaveDrawable waveDrawable = new WaveDrawable(
				Color.parseColor("#F5D0A9"), 500, 3000);
		fbLogo.setBackgroundDrawable(waveDrawable);
		Interpolator interpolator = new AccelerateDecelerateInterpolator();
		waveDrawable.setWaveInterpolator(interpolator);
		waveDrawable.startAnimation();
		
		buttonPress = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.pulse_once);
		buttonPress.setInterpolator(new AccelerateDecelerateInterpolator());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.button_login_email:
			emailLoginButton.startAnimation(buttonPress);
			this.loginCB.openEmailLogin();
			break;

		case id.button_signup:
			signupButton.startAnimation(buttonPress);
			this.loginCB.openSignup();
			break;

		case id.button_login_facebook:
			fbLoginButton.startAnimation(buttonPress);
			this.loginCB.openFacebookLogin();
			break;

		default:
			break;
		}
	}

	public void turnOnProgress() {
		progressSpinner.setVisibility(View.VISIBLE);

	}

	public void turnOffProgress() {
		progressSpinner.setVisibility(View.INVISIBLE);

	}
}
