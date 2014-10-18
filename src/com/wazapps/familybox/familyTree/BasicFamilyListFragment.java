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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

abstract public class BasicFamilyListFragment extends Fragment {
	protected ViewGroup root;
	protected ArrayList<FamiliesListItem> familiesListData;
	protected HeaderListView familiesList;
	protected SearchView search;
	protected ListView searchableList;
	private LinearLayout emptyFamily;
	protected SearchListAdapter searchableListAdapter;
	protected FrameLayout totalLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = (ViewGroup) inflater.inflate(R.layout.fragment_families_list,
				container, false);
		totalLayout = (FrameLayout) root
				.findViewById(R.id.fragment_families_list);
		emptyFamily = (LinearLayout) root
				.findViewById(R.id.ll_families_list_empty);
		searchableList = (ListView) root.findViewById(R.id.lv_searchable_list);
		familiesList = new HeaderListView(getActivity());

		this.familiesListData = new ArrayList<FamiliesListItem>();
		makeTempData();
		Collections.sort(this.familiesListData);
		searchableList.setVisibility(View.INVISIBLE);
		if (this.familiesListData.isEmpty()) {
			emptyFamily.setVisibility(View.VISIBLE);
			familiesList.setVisibility(View.INVISIBLE);

		} else {
			totalLayout.addView(familiesList);	
			emptyFamily.setVisibility(View.INVISIBLE);
			familiesList.setVisibility(View.VISIBLE);
			setHasOptionsMenu(true);
		}
		return root;
	}

	@Override
	public void onDestroy() {
		root.removeAllViewsInLayout();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		searchableListAdapter = new SearchListAdapter(getActivity(),
				R.layout.searchable_family_list_item,
				R.id.tv_search_families_list_item_name, familiesListData);
		searchableList.setAdapter(searchableListAdapter);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		search = (SearchView) menu.findItem(R.id.action_search).getActionView();
		search.setQueryHint(getString(R.string.search_family));
		int id = search.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.BLACK);

		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				if (newText == null || TextUtils.isEmpty(newText)) {
					searchableList.setVisibility(View.INVISIBLE);
					familiesList.setVisibility(View.VISIBLE);
					return true;
				}
				searchableList.setVisibility(View.VISIBLE);
				familiesList.setVisibility(View.INVISIBLE);

				searchableListAdapter.getFilter().filter(newText);
				return true;

			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_search_family, menu);
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
