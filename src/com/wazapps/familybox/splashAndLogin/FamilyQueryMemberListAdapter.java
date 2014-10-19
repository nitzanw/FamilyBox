package com.wazapps.familybox.splashAndLogin;

import java.util.Arrays;

import com.wazapps.familybox.R;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.util.RoundedImageView;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FamilyQueryMemberListAdapter extends BaseAdapter {
	private FragmentActivity activity;
	private FamilyMemberDetails[] familyMembersList;
	private LayoutInflater linearInflater;
	
	public FamilyQueryMemberListAdapter(FragmentActivity activity, 
			FamilyMemberDetails[] familyMembersList) {
		this.activity = activity;
		this.familyMembersList = Arrays.copyOf(familyMembersList,
				familyMembersList.length, FamilyMemberDetails[].class);
	}

	@Override
	public int getCount() {
		return this.familyMembersList.length;
	}

	@Override
	public Object getItem(int position) {
		return this.familyMembersList[position];
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(familyMembersList[position].getUserId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		linearInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// recycling the view:
		if (v == null) {
			v = linearInflater.inflate(R.layout.family_query_members_list_item,
					parent, false);
		}
		
		initMemberView(position, v);
		return v;		
	}
	
	private void initMemberView(int position, View v) {
		int size = this.getCount();
		
		switch (size) {
		case 1:
			v.setPadding(270, 0, 0, 0);
			break;

		case 2:
			if (position == 0)
				v.setPadding(170, 0, 0, 0);
			break;

		case 3:
			if (position == 0)
				v.setPadding(70, 0, 0, 0);
			break;
			
		default:
			if (position == 0) {
				v.setPadding(40, 0, 0, 0);
			}
			
			if (position == size - 1) {
				v.setPadding(0, 0, 40, 0);
			}
			break;
		}

		// TODO: add profile picture image handling
		FamilyMemberDetails member = this.familyMembersList[position];
		TextView name = (TextView) v
				.findViewById(R.id.tv_query_family_member_name);
		String memberName = member.getName() + " " + member.getLastName();
		name.setText(memberName);
	}

}
