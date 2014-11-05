package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.wazapps.familybox.familyTree.BasicFamilliesListAdapter;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.handlers.FamilyHandler;

public class SharedAlbumsListAdapter extends BasicFamilliesListAdapter {

	public SharedAlbumsListAdapter(Activity activity,
			ArrayList<FamiliesListItem> familiesList) {
		super(activity, familiesList);

	}

	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		super.onRowItemClick(parent, view, section, row, id);

		FamiliesListItem currFamily = (FamiliesListItem) getRowItem(section,
				row);

		Intent familyAlbumsIntent = new Intent(activity,
				AlbumGridScreenActivity.class);
		familyAlbumsIntent.putExtra(FamilyHandler.FAMILY_ID_KEY,
				currFamily.getFamilyId());
		familyAlbumsIntent.putExtra(FamilyHandler.NAME_KEY,
				currFamily.getFamilyName());
		activity.startActivity(familyAlbumsIntent);
	}
}
