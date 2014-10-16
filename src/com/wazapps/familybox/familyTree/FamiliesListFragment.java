package com.wazapps.familybox.familyTree;

import java.util.ArrayList;
import java.util.Locale;

import android.text.TextUtils;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.SharedAlbumsListAdapter;

public class FamiliesListFragment extends BasicFamilyListFragment {
	FamiliesListAdapter familiesListAdapater;

	@Override
	public void onResume() {
		super.onResume();
		familiesListAdapater = new FamiliesListAdapter(getActivity(),
				familiesListData);
		familiesList.setAdapter(familiesListAdapater);
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
					familiesListAdapater = new FamiliesListAdapter(
							getActivity(), familiesListData);
					familiesList.setAdapter(familiesListAdapater);
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
				familiesListAdapater.clearData();
				familiesListAdapater = new FamiliesListAdapter(getActivity(),
						filteredFamilyList);
				familiesList.setAdapter(familiesListAdapater);
				return true;
			}
		});
	}
}
