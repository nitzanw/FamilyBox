package com.wazapps.familybox.familyTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.SharedAlbumsListAdapter;
import com.wazapps.familybox.util.HeaderListView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

abstract public class BasicFamilyListFragment extends Fragment {
	protected View root;
	protected ArrayList<FamiliesListItem> familiesListData;
	protected HeaderListView familiesList;
	protected SearchView search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_families_list, container,
				false);
		familiesList = new HeaderListView(root.getContext());
		familiesList.setBackgroundColor(getResources().getColor(
				R.color.white_cream_ab));
		this.familiesListData = new ArrayList<FamiliesListItem>();
		makeTempData();
		Collections.sort(this.familiesListData);
		if (this.familiesListData.isEmpty()) {
			return root;
		} else {
			setHasOptionsMenu(true);
			return familiesList;
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		search = (SearchView) menu.findItem(R.id.action_search).getActionView();
		search.setQueryHint(getString(R.string.search_family));
		int id = search.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.BLACK);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_search_family, menu);
	}

	class FamilySearchTextListener implements OnQueryTextListener {
		public FamilySearchTextListener(BasicFamilliesListAdapter adapter) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	/**
	 * Adding temporary data for test purposes
	 */
	private void makeTempData() {
		familiesListData.add(new FamiliesListItem("f1u1", "Aluf"));
		familiesListData.add(new FamiliesListItem("f1u1", "Alon"));
		familiesListData.add(new FamiliesListItem("f1u1", "Alran"));
		familiesListData.add(new FamiliesListItem("f1u1", "Ander"));
		familiesListData.add(new FamiliesListItem("f1u1", "Arbel"));

		familiesListData.add(new FamiliesListItem("f1u1", "Ben-lulu"));
		familiesListData.add(new FamiliesListItem("f1u1", "Barak"));
		familiesListData.add(new FamiliesListItem("f1u1", "Biton"));
		familiesListData.add(new FamiliesListItem("f1u1", "Berger"));
		familiesListData.add(new FamiliesListItem("f1u1", "Buzaglo"));
		familiesListData.add(new FamiliesListItem("f1u1", "Brit"));
		familiesListData.add(new FamiliesListItem("f1u1", "Binder"));

		familiesListData.add(new FamiliesListItem("f1u1", "Friedman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Frenkel"));
		familiesListData.add(new FamiliesListItem("f1u1", "Frueman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Folman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Feldman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Fargun"));
		familiesListData.add(new FamiliesListItem("f1u1", "Fox"));

		familiesListData.add(new FamiliesListItem("f1u1", "Ginor"));
		familiesListData.add(new FamiliesListItem("f1u1", "Giat"));
		familiesListData.add(new FamiliesListItem("f1u1", "Ginat"));
		familiesListData.add(new FamiliesListItem("f1u1", "Galili"));
		familiesListData.add(new FamiliesListItem("f1u1", "Gal"));
		familiesListData.add(new FamiliesListItem("f1u1", "Goldstone"));
		familiesListData.add(new FamiliesListItem("f1u1", "Goldstein"));
		familiesListData.add(new FamiliesListItem("f1u1", "Goldberg"));
		familiesListData.add(new FamiliesListItem("f1u1", "Gabay"));

		familiesListData.add(new FamiliesListItem("f1u1", "Klein"));
		familiesListData.add(new FamiliesListItem("f1u1", "Kadosh"));
		familiesListData.add(new FamiliesListItem("f1u1", "Karmon"));

		familiesListData.add(new FamiliesListItem("f1u1", "Shapira"));
		familiesListData.add(new FamiliesListItem("f1u1", "Shemesh"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sharon"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sherman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Stav"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sela"));
		familiesListData.add(new FamiliesListItem("f1u1", "Shmuelli"));
		familiesListData.add(new FamiliesListItem("f1u1", "Saban"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sabag"));

		familiesListData.add(new FamiliesListItem("f1u1", "Zohar"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zaguri"));
		familiesListData.add(new FamiliesListItem("f1u1", "Ziv"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zar"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zamir"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zachs"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zach"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zaken"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zilber"));
	}
}
