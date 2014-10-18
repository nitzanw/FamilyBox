package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.BasicFamilliesListAdapter;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.photos.AlbumGridScreenActivity;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoGridFragment;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.SectionAdapter;

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
		// generate temp data for album intent
		// TODO: create real data
		AlbumItem[] albumList = { null, null, null, null, null, null };
		String albumName = "Temp Album Name ";
		PhotoItem[] tempData = { null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null };

		for (int i = 0; i < 18; i++) {
			tempData[i] = new PhotoItem("11.2.201" + i, "www.bla.com",
					"This is me and my friend Dan " + i);
		}

		for (int i = 0; i < 6; i++) {
			albumList[i] = new AlbumItem(String.valueOf(i), tempData, albumName
					+ i, "December 201" + i);
		}

		Bundle args = new Bundle();
		args.putParcelableArray(PhotoGridFragment.ALBUM_ITEM_LIST, albumList);
		args.putString(FamiliesListItem.FAMILY_NAME, currFamily.getFamilyName());
		familyAlbumsIntent.putExtra(PhotoGridFragment.ALBUM_ITEM_LIST, args);
		activity.startActivity(familyAlbumsIntent);
	}
}
