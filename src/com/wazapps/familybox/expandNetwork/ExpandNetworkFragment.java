package com.wazapps.familybox.expandNetwork;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;


public class ExpandNetworkFragment extends Fragment implements OnClickListener {
	public interface InviteCallback {
		public void launchEmailInviteDialog();
		public void emailInvite(String email);
	}
	
	private View root;
	private ImageButton facebookInvite, emailInvite;
	private Animation buttonPress;
	private InviteCallback inviteHandler = null;
	public static final String EXPAND_NETWORK_FRAG = 
			"expand network fragment";
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			inviteHandler = (InviteCallback) getActivity();
		} catch (ClassCastException e) {
			LogUtils.logError(getTag(), 
					"activity does not implement InviteCallback interface");
		}		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_expand_network, 
				container, false);
		
		facebookInvite = (ImageButton) 
				root.findViewById(R.id.expand_network_facebook_button);
		
		emailInvite = (ImageButton)
				root.findViewById(R.id.expand_network_email_button);
		
		facebookInvite.setOnClickListener(this);
		emailInvite.setOnClickListener(this);
		
		initAnimations();
		return root;
	}
	
	private void initAnimations() {
		buttonPress = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.pulse_once);
		buttonPress.setInterpolator(new AccelerateDecelerateInterpolator());
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.expand_network_facebook_button:
			facebookInvite.startAnimation(buttonPress);
			
			
			Toast toast = Toast.makeText(getActivity(), 
					"Feature not yet available", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			break;
			
		case R.id.expand_network_email_button:
			emailInvite.startAnimation(buttonPress);
			inviteHandler.launchEmailInviteDialog();
			break;

		default:
			break;
		}		
	}	
}
