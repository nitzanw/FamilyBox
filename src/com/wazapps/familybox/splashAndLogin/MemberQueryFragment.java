package com.wazapps.familybox.splashAndLogin;

import java.util.ArrayList;

import com.parse.ParseException;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.UserData;
import com.wazapps.familybox.splashAndLogin.FamilyQueryFragment.QueryHandlerCallback;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;
import com.wazapps.familybox.util.WaveDrawable;

import com.wazapps.familybox.R;
import android.app.Activity;
import android.graphics.Bitmap;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;
import com.wazapps.familybox.util.WaveDrawable;

public class MemberQueryFragment extends Fragment implements OnClickListener {
	private View root;
	private RoundedImageView mMemberProfilePic;
	private Spinner mRelationPicker;
	private TextView mFamilyMemberName, mFamilyMemberQuestion;
	private Button mAcceptButton;
	private LinearLayout mLoadingSpinner; 
	private UserData mFamilyMember;
	private ArrayList<String> mSpinnerOptions;
	private QueryAnswerHandlerCallback mQueryAnswerHandler;
	private boolean mIsMemberMale;
	
	public static final String FAMILY_MEMBER_ITEM = 
			"family member item";
	public static final String FAMILY_MEMBER_RELATION_OPTIONS = 
			"feamily member relation options";
	public static final String FAMILY_MEMBER_GENDER = "family member gender";
	
	public interface QueryAnswerHandlerCallback {
		public void handleMemberQueryAnswer(String currOption);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.mQueryAnswerHandler = (QueryAnswerHandlerCallback) getActivity();
		} 
		
		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement " +
					"QueryAnswerHandlerCallback interface");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_member_query, 
				container, false);
		mMemberProfilePic = (RoundedImageView) 
				root.findViewById(R.id.riv_member_query_profile_picture);
		mRelationPicker = (Spinner) 
				root.findViewById(R.id.sp_member_query_relation_options);
		mFamilyMemberName = (TextView) 
				root.findViewById(R.id.tv_member_query_name);
		mFamilyMemberQuestion = (TextView) 
				root.findViewById(R.id.tv_member_query_family_question);
		mLoadingSpinner = (LinearLayout) 
				root.findViewById(R.id.ll_member_query_progress_spinner);
		mAcceptButton = (Button) 
				root.findViewById(R.id.button_member_query_accept);
		mAcceptButton.setOnClickListener(this);
		initAnimations();
		
		return root;
	}
	
	private void initAnimations() {
		Animation pulse = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.pulse_slow);
		pulse.setInterpolator(new AccelerateInterpolator(4));		
		ImageView animationBackground = 
				(ImageView) root.findViewById(R.id.iv_member_query_profile_effect);
		
		WaveDrawable waveDrawable = new WaveDrawable(
				Color.parseColor("#F5D0A9"), 400, 3000);
		animationBackground.setBackgroundDrawable(waveDrawable);
		Interpolator interpolator = 
				new AccelerateDecelerateInterpolator();
		
		mMemberProfilePic.startAnimation(pulse);
		waveDrawable.setWaveInterpolator(interpolator);
		waveDrawable.startAnimation();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mFamilyMember = args.getParcelable(FAMILY_MEMBER_ITEM);
			mSpinnerOptions = args.getStringArrayList(FAMILY_MEMBER_RELATION_OPTIONS);
			mIsMemberMale = args.getBoolean(FAMILY_MEMBER_GENDER);
		} else {
			LogUtils.logWarning(getTag(), "member query arguments did not pass");
		}		
	}
	
	public void onResume() {
		MemberQuerySpinnerAdapter querySpinnerAdapter = 
				new MemberQuerySpinnerAdapter(getActivity(), 
						mSpinnerOptions, mIsMemberMale);
		mRelationPicker.setAdapter(querySpinnerAdapter);
		String memberName = InputHandler.capitalizeFullname(
				mFamilyMember.getName(), mFamilyMember.getLastName());
		mFamilyMemberName.setText(memberName);
		
		if (!mIsMemberMale) {
			mFamilyMemberQuestion.setText("What is your family relation with her?");
		}
		
		Bitmap profilePic = mFamilyMember.getprofilePhoto();
		if (profilePic != null) {
			mMemberProfilePic.setImageBitmap(mFamilyMember.getprofilePhoto());
			mMemberProfilePic.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_member_query_accept:
			//add loading animation upon processing data
			mLoadingSpinner.setVisibility(View.VISIBLE);
			String currOption = mRelationPicker.getSelectedItem().toString();
			mQueryAnswerHandler.handleMemberQueryAnswer(currOption);
			break;

		default:
			break;
		}
	}
}
