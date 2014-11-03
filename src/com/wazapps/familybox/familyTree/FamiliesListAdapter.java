package com.wazapps.familybox.familyTree;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.parse.ParseUser;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment.AddFamilyProfileFragmentListener;
import com.wazapps.familybox.familyProfiles.FamilyProfileScreenActivity;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.util.LogUtils;

public class FamiliesListAdapter extends BasicFamilliesListAdapter {
	private AddFamilyProfileFragmentListener profileAdder;
	
	public FamiliesListAdapter(Activity activity,
			ArrayList<FamiliesListItem> familiesList) {
		super(activity, familiesList);
		try {
			profileAdder = (AddFamilyProfileFragmentListener) activity;			
		} 
		
		catch(ClassCastException e) {
			LogUtils.logWarning("FamiliesListAdapter", "activity does not" +
					"implement AddFamilyProfileFragmentListener");
		}
	}

	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		super.onRowItemClick(parent, view, section, row, id);
		
		Bundle args = new Bundle();
		FamiliesListItem clickedFamily = (FamiliesListItem) 
				getRowItem(section, row);
		args.putBoolean(FamilyProfileFragment.USER_FAMILY, false);
		args.putString(FamilyProfileFragment.FAMILY_ID, 
				clickedFamily.getFamilyId());
		args.putString(FamilyProfileFragment.FAMILY_NAME, 
				clickedFamily.getFamilyName());
		
		Intent familyProfileIntent = new Intent(activity, 
				FamilyProfileScreenActivity.class);
		familyProfileIntent.putExtra(
				FamilyProfileScreenActivity.FAMILY_PROFILE_ARGS, args);
		activity.startActivity(familyProfileIntent);
	}
}
