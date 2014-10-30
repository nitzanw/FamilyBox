package com.wazapps.familybox.profiles;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.RoundedImageView;

public class ProfileFamilyListAdapter extends AbstractFamilyListAdapter {

	public ProfileFamilyListAdapter(FragmentActivity activity,
			UserData[] familyMembersList, UserData userData) {
		super(activity, familyMembersList, userData);
	}

	public void initMemberView(int position, View v) {
		switch (this.getCount()) {
		case 0:
			//do nothing
			break;
			
		case 1:
			v.setPadding(250, 0, 0, 0);
			break;

		case 2:
			if (position == 0)
				v.setPadding(175, 0, 0, 0);
			else
				v.setPadding(10, 0, 0, 0);
			break;

		case 3:
			if (position == 0)
				v.setPadding(70, 0, 0, 0);
			else 
				v.setPadding(10, 0, 0, 0);
			break;
		
		default:
			if (position == this.getCount() - 1)
				v.setPadding(0, 0, 10, 0);
			else 
				v.setPadding(10, 0, 0, 0);
			break;
		}
		
		UserData member = this.familyMembersList[position];
		TextView name = (TextView) v
				.findViewById(R.id.tv_close_family_member_name);
		TextView role = (TextView) 
				v.findViewById(R.id.tv_close_family_role);
		RoundedImageView photo = (RoundedImageView) 
				v.findViewById(R.id.riv_profile_family_member);
		
		String memberName = InputHandler.capitalizeName(member.getName());
		String memberRole = setRelativeRole(member);
		Bitmap memberPic = member.getprofilePhoto();

		name.setText(memberName);
		role.setText(memberRole);
		if (memberPic != null) {
			photo.setImageBitmap(memberPic);
			photo.setBackgroundColor(activity.getResources().getColor(
					android.R.color.transparent));	
		}
	}

	@Override
	public View getInflatedView(ViewGroup parent) {
		return linearInflater.inflate(R.layout.family_members_list_item,
				parent, false);
	}
	
	private String setRelativeRole(UserData member) {
		String userRole = userData.getRole();
		String memberRole = member.getRole();
		String memberGender = member.getGender();
		String role = "";
		
		//if user is a parent
		if (userRole.equals(UserData.ROLE_PARENT)) {
			//if member is also a parent
			if (memberRole.equals(UserData.ROLE_PARENT)) {
				if (memberGender.equals(UserHandler.GENDER_MALE)) {
					role =  "Husband";
				} else {
					role = "Wife";
				}
			} 
			//if member is a child
			else {
				role = "Child";
			}
		}
		
		//if user is child
		else {
			if (memberRole.equals(UserData.ROLE_PARENT)) {
				if (memberGender.equals(UserHandler.GENDER_MALE)) {
					role = "Father";
				} else {
					role = "Mother";
				}
			} else {
				if (memberGender.equals(UserHandler.GENDER_MALE)) {
					role = "Brother";
				} else {
					role = "Sister";
				}
			}
		}
		
		return role;
	}
}
