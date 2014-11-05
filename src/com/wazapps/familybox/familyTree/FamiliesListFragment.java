package com.wazapps.familybox.familyTree;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment;
import com.wazapps.familybox.familyProfiles.FamilyProfileScreenActivity;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.LogUtils;

public class FamiliesListFragment extends BasicFamilyListFragment {
	public static final String FAMILY_TREE_FRAG = "family tree fragment";
	FamiliesListAdapter familiesListAdapater;

	@Override
	protected void createOnClickOperation(View v) {
		FamiliesListItem clickedFamily = (FamiliesListItem) 
				v.getTag(BasicFamilyListFragment.FAMILT_ITEM);
		Bundle args = new Bundle();
		args.putBoolean(FamilyProfileFragment.USER_FAMILY, false);
		args.putString(FamilyProfileFragment.FAMILY_ID, 
				clickedFamily.getFamilyId());
		args.putString(FamilyProfileFragment.FAMILY_NAME, 
				clickedFamily.getFamilyName());
		
		Intent familyProfileIntent = new Intent(getActivity(), 
				FamilyProfileScreenActivity.class);
		familyProfileIntent.putExtra(
				FamilyProfileScreenActivity.FAMILY_PROFILE_ARGS, args);
		getActivity().startActivity(familyProfileIntent);
	}

	@Override
	public void createAdapter() {
		familiesListAdapater = new FamiliesListAdapter(
				getActivity(), familiesListData);
		familiesList.setAdapter(familiesListAdapater);
		loadingSpinner.setVisibility(View.GONE);
		
	}
	
}
