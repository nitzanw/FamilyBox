package com.wazapps.familybox.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.wazapps.familybox.familyTree.BasicFamilyListFragment;
import com.wazapps.familybox.familyTree.FamiliesListItem;

public class SharedAlbumFragment extends BasicFamilyListFragment {
	SharedAlbumsListAdapter sharedAlbumsAdapter;

	@Override
	public void onResume() {
		super.onResume();
		sharedAlbumsAdapter = new SharedAlbumsListAdapter(getActivity(),
				familiesListData);
		familiesList.setAdapter(sharedAlbumsAdapter);
	}

	@Override
	protected void createOnClickOperation(View v) {
		FamiliesListItem currFamily = (FamiliesListItem) v.getTag(FAMILT_ITEM);
		Intent familyAlbumsIntent = new Intent(getActivity(),
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
		getActivity().startActivity(familyAlbumsIntent);

	}

}
