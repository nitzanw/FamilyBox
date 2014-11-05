package com.wazapps.familybox.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wazapps.familybox.familyTree.BasicFamilyListFragment;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.util.LogUtils;

public class SharedAlbumFragment extends BasicFamilyListFragment {
	SharedAlbumsListAdapter sharedAlbumsAdapter;

	@Override
	protected void createOnClickOperation(View v) {
		FamiliesListItem currFamily = (FamiliesListItem) v.getTag(FAMILT_ITEM);
		Intent familyAlbumsIntent = new Intent(getActivity(),
				AlbumGridScreenActivity.class);
		familyAlbumsIntent.putExtra(FamilyHandler.FAMILY_ID_KEY,
				currFamily.getFamilyId());
		getActivity().startActivity(familyAlbumsIntent);

	}

	@Override
	public void createAdapter() {
		sharedAlbumsAdapter = new SharedAlbumsListAdapter(getActivity(),
				familiesListData);
		familiesList.setAdapter(sharedAlbumsAdapter);
		loadingSpinner.setVisibility(View.GONE);
	}

}
