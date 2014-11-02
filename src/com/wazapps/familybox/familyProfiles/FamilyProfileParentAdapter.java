package com.wazapps.familybox.familyProfiles;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.UserData;
import com.wazapps.familybox.util.RoundedImageView;

public class FamilyProfileParentAdapter extends BaseAdapter {

	private FragmentActivity activity;
	private LayoutInflater linearInflater;
	private UserData[] parentList;

	public FamilyProfileParentAdapter(FragmentActivity activity,
			UserData[] parentList) {
		this.activity = activity;
		this.parentList = Arrays.copyOf(parentList, parentList.length,
				UserData[].class);
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
		TextView name = (TextView) 
				v.findViewById(R.id.tv_family_profile_parent_name);
		RoundedImageView photo = (RoundedImageView) 
				v.findViewById(R.id.riv_family_profile_parent);
		
		UserData member = parentList[position];
		String memberName = InputHandler.capitalizeFullname(
				member.getName(), member.getLastName());
		Bitmap memberPhoto = member.getprofilePhoto();
		
		name.setText(memberName);
		if (memberPhoto != null) {
			photo.setImageBitmap(memberPhoto);
			photo.setBackgroundColor(activity.getResources().getColor(
					android.R.color.transparent));	
		}
		
	}
}
