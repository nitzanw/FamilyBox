package com.wazapps.familybox.photos;

import com.wazapps.familybox.familyTree.BasicFamilyListFragment;

public class SharedAlbumFragment extends BasicFamilyListFragment {
	SharedAlbumsListAdapter sharedAlbumsAdapter;

	@Override
	public void onResume() {
		super.onResume();
		sharedAlbumsAdapter = new SharedAlbumsListAdapter(getActivity(),
				familiesListData);
		familiesList.setAdapter(sharedAlbumsAdapter);
	}



}
