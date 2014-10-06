package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PhotoAlbumsTabsFragment extends TabsFragment {
	private static final String MY_FAMILY = "myFamily";
	private static final String SHARED_ALBUM = "sharedAlbum";
	private static final String FAVORITES = "favorites";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {}
		this.actionBar = getActivity().getActionBar();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		ViewGroup rootView = (ViewGroup) inflater.inflate
				(R.layout.fragment_photo_album_store_tabs, null);
		this.tabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
		this.tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
		
		// add tabs to the tabs storage
		this.tabHost.addTab(this.tabHost.newTabSpec(MY_FAMILY)
				.setIndicator(makeTabIndicator(R.string.photos_tab_my_family)),
				MyFamilyAlbumFragment.class, this.getArguments());

		this.tabHost.addTab(this.tabHost.newTabSpec(SHARED_ALBUM)
				.setIndicator(makeTabIndicator(R.string.photos_tab_shared_album)),
				SharedAlbumFragment.class, this.getArguments());
		
		this.tabHost.addTab(tabHost.newTabSpec(FAVORITES)
				.setIndicator(makeTabIndicator(R.string.photos_tab_favorites)), 
				FavoritesAlbumFragment.class, this.getArguments());
		
		return this.tabHost;
	}



	
}