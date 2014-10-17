package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.Locale;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.BasicFamilyListFragment;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.util.HeaderListView;

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
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				if (newText == null || TextUtils.isEmpty(newText)) {
					sharedAlbumsAdapter = new SharedAlbumsListAdapter(
							getActivity(), familiesListData);
					familiesList.setAdapter(sharedAlbumsAdapter);
					return true;
				}
				newText = newText.toLowerCase(Locale.getDefault());
				ArrayList<FamiliesListItem> filteredFamilyList = new ArrayList<FamiliesListItem>();
				for (FamiliesListItem family : familiesListData) {
					if (family.getFamilyName().toLowerCase(Locale.getDefault())
							.startsWith(newText)) {
						filteredFamilyList.add(family);
					}
				}
				sharedAlbumsAdapter.clearData();
				sharedAlbumsAdapter = new SharedAlbumsListAdapter(
						getActivity(), filteredFamilyList);
				familiesList.setAdapter(sharedAlbumsAdapter);
				return true;
			}
		});
	}

}
