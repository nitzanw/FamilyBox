package com.wazapps.familybox.splashAndLogin;


import com.parse.ParseException;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.FamilyMemberDetails2;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FamilyQueryFragment extends Fragment implements OnClickListener {
	private View root;
	private QueryHandlerCallback queryHandlerCallback;
	private FamilyMemberDetails2 mCurrentUser;
	private FamilyMemberDetails2[] mFamilyList;
	private TextView mFragTitle, mFragMsg, mFragMembers;
	private LinearLayout mFamilyMembersHolder;
	private FamilyQueryMemberListAdapter mFamilyListAdapter;
	private Button yesButton, noButton;
	
	public static final String QUERY_FAMILIES_LIST = "query families list";
	public static final String MEMBER_ITEM = "member item";
	
	public interface QueryHandlerCallback {
		public void handleFamilyQuery() throws ParseException;
		public void handleMemberQuery();
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			this.queryHandlerCallback = (QueryHandlerCallback) 
					getActivity();
		} 
		
		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement " +
					"QueryHandlerCallback interface");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_family_query, container, false);
		mFamilyMembersHolder = (LinearLayout) 
				root.findViewById(R.id.ll_family_query_members_list_holder);
		mFragTitle = (TextView) root.findViewById(R.id.tv_family_query_title);
		mFragMsg = (TextView) root.findViewById(R.id.tv_family_query_family_name);
		mFragMembers = (TextView) root.findViewById(R.id.tv_family_query_members);
		yesButton = (Button) root.findViewById(R.id.button_family_query_yes);
		noButton = (Button) root.findViewById(R.id.button_family_query_no);
		
		yesButton.setOnClickListener(this);
		noButton.setOnClickListener(this);
		
		return root;
	}
	
	//TODO: either fix or delete
	private void initAnimations() {
		Animation profileJump = AnimationUtils
				.loadAnimation(getActivity(), R.anim.pulse_strong);
		profileJump.setStartOffset(1);
		profileJump.setRepeatCount(1);
		profileJump.setInterpolator(new AccelerateDecelerateInterpolator());
		RoundedImageView profilePic = (RoundedImageView) root
				.findViewById(R.id.riv_query_profile_picture);
		profilePic.startAnimation(profileJump);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mFamilyList = (FamilyMemberDetails2[]) args.getParcelableArray(QUERY_FAMILIES_LIST);	
			mCurrentUser = args.getParcelable(MEMBER_ITEM);
		} else {
			LogUtils.logWarning(getTag(), "family query arguments did not pass");
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		String firstName = mCurrentUser.getName();
		String lastName = mCurrentUser.getLastName();
		firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
		lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
		mFragTitle.setText("Hi " + firstName + "!");
		mFragMsg.setText("Your family name is " + lastName + ",");
		if (mFamilyList.length == 1) {
			mFragMembers.setText("Is this your family member?");
		}
		
		initFamilyMembersListView();		
	}
	
	private void initFamilyMembersListView() {
		mFamilyListAdapter = new FamilyQueryMemberListAdapter(this.getActivity(),
				mFamilyList);
		for (int i = 0; i < mFamilyListAdapter.getCount(); i++) {
			View v = mFamilyListAdapter.getView(i, null, (ViewGroup) getView());
			v.setClickable(false);
			mFamilyMembersHolder.addView(v);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_family_query_yes:
			queryHandlerCallback.handleMemberQuery();
			break;
			
		case R.id.button_family_query_no:
			try {
				queryHandlerCallback.handleFamilyQuery();
			} 
			
			catch (ParseException e) {
				//TODO: handle exception in a proper way
				Toast.makeText(getActivity(), "error in parse", 
						Toast.LENGTH_SHORT);
			}
			
			break;

		default:
			break;
		}
		
	}
}
