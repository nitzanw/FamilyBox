package com.wazapps.familybox.familyTree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wazapps.familybox.familyProfiles.FamilyProfileScreenActivity;
import com.wazapps.familybox.photos.AlbumItem;

public class FamiliesListFragment extends BasicFamilyListFragment {
	public static final String FAMILY_TREE_FRAG = "family tree fragment";
	FamiliesListAdapter familiesListAdapater;

	@Override
	public void onResume() {
		super.onResume();
		familiesListAdapater = new FamiliesListAdapter(getActivity(),
				familiesListData);
		familiesList.setAdapter(familiesListAdapater);

	}

	@Override
	protected void createOnClickOperation(View v) {
		Intent familyAlbumsIntent = new Intent(getActivity(),
				FamilyProfileScreenActivity.class);
		AlbumItem[] albumList = { null, null, null, null, null, null };
		Bundle args = new Bundle();
		args.putParcelableArray("test1", albumList);
		familyAlbumsIntent.putExtra("test2", args);
		getActivity().startActivity(familyAlbumsIntent);
		
	}

}
