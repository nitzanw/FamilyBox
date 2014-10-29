package com.wazapps.familybox.familyTree;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wazapps.familybox.familyProfiles.FamilyProfileScreenActivity;
import com.wazapps.familybox.photos.AlbumItem;

public class FamiliesListAdapter extends BasicFamilliesListAdapter {

	public FamiliesListAdapter(Activity activity,
			ArrayList<FamiliesListItem> familiesList) {
		super(activity, familiesList);
	}

	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		super.onRowItemClick(parent, view, section, row, id);
		Intent familyAlbumsIntent = new Intent(activity,
				FamilyProfileScreenActivity.class);
		AlbumItem[] albumList = { null, null, null, null, null, null };
		Bundle args = new Bundle();
		args.putParcelableArray("test1", albumList);
		familyAlbumsIntent.putExtra("test2", args);
		activity.startActivity(familyAlbumsIntent);
	}



}
