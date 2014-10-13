package com.wazapps.familybox.profiles;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.RoundedImageView;

public class FamilyProfileChildAdapter extends BaseAdapter {
	private static final int CHILD_POS = R.string.child;
	private FragmentActivity activity;
	private LayoutInflater linearInflater;
	private FamilyMemberDetails[] childrenList;

	public FamilyProfileChildAdapter(FragmentActivity activity,
			FamilyMemberDetails[] childrenList) {
		this.activity = activity;
		this.childrenList = Arrays.copyOf(childrenList, childrenList.length,
				FamilyMemberDetails[].class);

	}

	@Override
	public int getCount() {

		return childrenList.length;
	}

	@Override
	public Object getItem(int position) {
		return childrenList[position];
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(childrenList[position].getUserId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		linearInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// recycling the view:
		if (v == null) {
			v = linearInflater.inflate(R.layout.family_profile_child_item,
					parent, false);
		}

		initChildView(position, v);
		return v;
	}

	private void initChildView(int position, View v) {
		RoundedImageView image = (RoundedImageView) v
				.findViewById(R.id.riv_family_profile_child);

		image.setImageDrawable(activity.getResources().getDrawable(
				R.drawable.profile_pic_example));// TODO
		// setSomthing(memberList[position].getURI()

		TextView name = (TextView) v
				.findViewById(R.id.tv_family_profile_child_name);
		name.setText(childrenList[position].getName() + " "
				+ childrenList[position].getLastName());

		ImageView connector1 = (ImageView) v
				.findViewById(R.id.iv_family_profile_children_connector_1);
		ImageView connector2 = (ImageView) v
				.findViewById(R.id.iv_family_profile_children_connector_2);

		// remove the connectors according to the position in the list
		if (position == 0) {
			connector1.setVisibility(View.GONE);
		} else {
			connector1.setVisibility(View.VISIBLE);
		}
		if (position == childrenList.length - 1) {
			connector2.setVisibility(View.GONE);
		} else {
			connector2.setVisibility(View.VISIBLE);

		}
		v.setTag(CHILD_POS, position);
	}
}
