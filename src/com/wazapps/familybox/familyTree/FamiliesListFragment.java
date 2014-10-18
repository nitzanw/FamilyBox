package com.wazapps.familybox.familyTree;

public class FamiliesListFragment extends BasicFamilyListFragment {
	public static final String FAMILY_TREE_FRAG = "family tree fragment";
	FamiliesListAdapter familiesListAdapater;

	@Override
	public void onResume() {
		super.onResume();
		familiesListAdapater = new FamiliesListAdapter(getActivity(),
				familiesListData);
		familiesList.setAdapter(familiesListAdapater);

	}

}
