package com.wazapps.familybox.profiles;

import java.util.Arrays;

import com.wazapps.familybox.R;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProfileFamilyListAdapter extends BaseAdapter {
	private FragmentActivity activity;
	private FamilyMemberDetails[] familyMembersList;
	private LayoutInflater linearInflater;
	
	public ProfileFamilyListAdapter(FragmentActivity activity, 
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
			v = linearInflater.inflate(R.layout.family_members_list_item,
					parent, false);
		}
		
		initMemberView(position, v);
		return v;
	}
	
	private void initMemberView(int position, View v) {
		switch (this.getCount()) {
		case 1:
			v.setPadding(0, 0, 260, 0);
			break;

		case 2:
			if (position == 1)
				v.setPadding(0, 0, 240, 0);
			break;

		case 3:
			if (position == 2)
				v.setPadding(0, 0, 45, 0);
		default:
			break;
		}

		// TODO: add profile picture image handling
		FamilyMemberDetails member = this.familyMembersList[position];
		TextView name = (TextView) v
				.findViewById(R.id.tv_close_family_member_name);
		TextView role = (TextView) v.findViewById(R.id.tv_close_family_role);

		name.setText(member.getName());
		role.setText(member.getRole());
	}

}
