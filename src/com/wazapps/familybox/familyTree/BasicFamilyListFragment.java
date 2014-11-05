package com.wazapps.familybox.familyTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment.AddFamilyProfileFragmentListener;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.HeaderListView;
import com.wazapps.familybox.util.LogUtils;

public abstract class BasicFamilyListFragment extends Fragment {
	protected static final int FAMILT_ITEM = R.string.family;
	public static final String IS_FAMILY_TREE = "is family tree";
	private static final String FAMILY_LIST_DATA = "family list";
	protected ViewGroup root;
	protected ArrayList<FamiliesListItem> familiesListData;
	protected HeaderListView familiesList;
	protected SearchView search;
	private LinearLayout emptyFamily;
	protected SearchListAdapter searchableListAdapter;
	protected FrameLayout totalLayout;
	private LinearLayout searchresult;
	protected LinearLayout loadingSpinner;
	protected boolean isFamilyTree = true;
	protected boolean isDataInit = false;
	protected ParseUser loggedUser = null;
	protected AddFamilyProfileFragmentListener addFamilyProfile = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			addFamilyProfile = (AddFamilyProfileFragmentListener) getActivity();
		}

		catch (ClassCastException e) {
			LogUtils.logWarning("FamiliesListAdapter", "activity does not"
					+ "implement AddFamilyProfileFragmentListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loggedUser = ParseUser.getCurrentUser();
		if (loggedUser == null) {
			// TODO: handle error
		}

		Bundle args = getArguments();
		if (args != null) {
			isFamilyTree = args.getBoolean(IS_FAMILY_TREE);
		}

		else {
			LogUtils.logWarning(getTag(),
					"family profile arguments did not pass");
		}
		// if (savedInstanceState == null) {
		// this.familiesListData = new ArrayList<FamiliesListItem>();
		// getAllFamilies();
		if (savedInstanceState != null) {
			this.familiesListData = savedInstanceState
					.getParcelableArrayList(FAMILY_LIST_DATA);
			createAdapter();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (familiesListData != null && !familiesListData.isEmpty()) {
			setupList();
		} else {
			this.familiesListData = new ArrayList<FamiliesListItem>();
			getAllFamilies();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(FAMILY_LIST_DATA, familiesListData);
	}

	private void getAllFamilies() {
		ParseQuery<ParseObject> familiesQuery = ParseQuery
				.getQuery(FamilyHandler.FAMILY_CLASS_NAME);
		familiesQuery.whereEqualTo(FamilyHandler.NETWORK_KEY,
				loggedUser.getString(UserHandler.NETWORK_KEY));
		familiesQuery.orderByAscending(FamilyHandler.NAME_KEY);
		familiesQuery.findInBackground(new FindCallback<ParseObject>() {
			BasicFamilyListFragment frag;

			@Override
			public void done(List<ParseObject> families, ParseException e) {

				if (e == null) {
					for (ParseObject family : families) {
						FamiliesListItem familyData = new FamiliesListItem(
								family);
						familiesListData.add(familyData);
					}
				}

				else {
					LogUtils.logError("BasicFamilyListFragment", e.getMessage());
				}

				frag.setupList();
			}

			private FindCallback<ParseObject> init(BasicFamilyListFragment frag) {
				this.frag = frag;
				return this;
			}
		}.init(this));
	}

	private void setupList() {
		searchableListAdapter = new SearchListAdapter(getActivity(),
				familiesListData);

		// searchableList.setAdapter(searchableListAdapter);
		familiesList = new HeaderListView(getActivity());
		familiesList.setBackgroundColor(getResources().getColor(
				R.color.white_cream_ab));

		if (this.familiesListData.isEmpty()) {
			emptyFamily.setVisibility(View.VISIBLE);
			familiesList.setVisibility(View.INVISIBLE);
		}

		else {
			totalLayout.addView(familiesList);
			emptyFamily.setVisibility(View.INVISIBLE);
			familiesList.setVisibility(View.VISIBLE);
			setHasOptionsMenu(true);
			isDataInit = true;
			getActivity().invalidateOptionsMenu();
		}
		createAdapter();

	}

	abstract public void createAdapter();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = (ViewGroup) inflater.inflate(R.layout.fragment_families_list,
				container, false);
		totalLayout = (FrameLayout) root
				.findViewById(R.id.fragment_families_list);
		emptyFamily = (LinearLayout) root
				.findViewById(R.id.ll_families_list_empty);
		searchresult = (LinearLayout) root
				.findViewById(R.id.ll_search_results_layout);
		loadingSpinner = (LinearLayout) root
				.findViewById(R.id.ll_families_list_spinner);
		return root;
	}

	@Override
	public void onDestroy() {
		root.removeAllViewsInLayout();
		super.onDestroy();
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (isDataInit) {
			search = (SearchView) menu.findItem(R.id.action_search)
					.getActionView();
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

					searchresult.removeAllViews();
					if (newText == null || TextUtils.isEmpty(newText)) {
						searchresult.setVisibility(View.GONE);
						familiesList.setVisibility(View.VISIBLE);
						return true;
					}

					ArrayList<FamiliesListItem> filteredList = new ArrayList<FamiliesListItem>(
							filter(newText));
					searchableListAdapter.setData(filteredList);
					searchableListAdapter.notifyDataSetChanged();
					for (int i = 0; i < filteredList.size(); i++) {

						View v = searchableListAdapter.getView(i, null,
								(ViewGroup) getView());
						v.setTag(FAMILT_ITEM, filteredList.get(i));
						v.setClickable(true);
						v.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								createOnClickOperation(v);
							}
						});
						searchresult.addView(v);
					}

					searchresult.setVisibility(View.VISIBLE);
					familiesList.setVisibility(View.GONE);
					return true;
				}
			});
		}
	}

	abstract protected void createOnClickOperation(View v);

	public ArrayList<FamiliesListItem> filter(String constraint) {
		if (constraint == null || constraint.length() == 0) {
			// No filter implemented we return all the list
			return familiesListData;
		} else {
			// We perform filtering operation
			ArrayList<FamiliesListItem> currfamilliesList = new ArrayList<FamiliesListItem>();

			for (FamiliesListItem f : familiesListData) {
				if (f.getFamilyName()
						.toLowerCase(Locale.getDefault())
						.startsWith(
								constraint.toString().toLowerCase(
										Locale.getDefault())))
					currfamilliesList.add(f);
			}

			return currfamilliesList;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_search_family, menu);
	}
}
