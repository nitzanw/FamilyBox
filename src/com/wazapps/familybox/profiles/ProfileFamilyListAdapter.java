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

public class ProfileFamilyListAdapter extends AbstractFamilyListAdapter {


	public ProfileFamilyListAdapter(FragmentActivity activity,
			FamilyMemberDetails[] familyMembersList) {
		super(activity, familyMembersList);
	}

	public void initMemberView(int position, View v) {
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

	@Override
	public View getInflatedView(ViewGroup parent) {
		return linearInflater.inflate(R.layout.family_members_list_item,
				parent, false);
	}

}
