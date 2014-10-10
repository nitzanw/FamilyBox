package com.wazapps.familybox.profiles;

import java.util.ArrayList;
import java.util.Arrays;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FamilyProfileParentAdapter extends BaseAdapter {

	private FragmentActivity activity;
	private LayoutInflater linearInflater;
	private FamilyMemberDetails[] parentList;

	public FamilyProfileParentAdapter(FragmentActivity activity,
			FamilyMemberDetails[] parentList) {
		this.activity = activity;
		this.parentList = Arrays.copyOf(parentList, parentList.length,
				FamilyMemberDetails[].class);
	}

	@Override
	public int getCount() {

		return parentList.length;
	}

	@Override
	public Object getItem(int position) {

		return parentList[position];
	}

	@Override
	public long getItemId(int position) {

		return Long.valueOf(parentList[position].getUserId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		linearInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// recycling the view:
		if (v == null) {

			v = linearInflater.inflate(R.layout.family_profile_parent_item,
					parent, false);
		}

		initParentView(position, v);

		return v;
	}

	private void initParentView(int position, View v) {
		((RoundedImageView) v.findViewById(R.id.riv_family_profile_parent))
				.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.profile_pic_example2));
		((TextView) v.findViewById(R.id.tv_family_profile_parent_name))
				.setText(parentList[position].getName());
	}

	
}
