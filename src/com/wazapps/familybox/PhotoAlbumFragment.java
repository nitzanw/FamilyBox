package com.wazapps.familybox;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PhotoAlbumFragment extends TabsFragment {

	private static final String MY_FAMILY = "myFamily";
	private static final String SHARED_ALBUM = "sharedAlbum";
	private static final String FAVORITES = "favorites";





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {

		}
		
		mActionBar = getActivity().getActionBar();
		
		setHasOptionsMenu(true);

	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_photo_album_store_tabs, null);
		mTabHost = (FragmentTabHost) rootView
				.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);
		// add tabs to the tabs storage
		mTabHost.addTab(
				mTabHost.newTabSpec(MY_FAMILY).setIndicator(
						makeTabIndicator(R.string.photos_tab_my_family)),
				MyFamilyMainAlbumFragment.class, this.getArguments());

		mTabHost.addTab(
				mTabHost.newTabSpec(SHARED_ALBUM).setIndicator(
						makeTabIndicator(

						R.string.photos_tab_shared_album)),
				SharedAlbumFragment.class, this.getArguments());
		mTabHost.addTab(
				mTabHost.newTabSpec(FAVORITES).setIndicator(makeTabIndicator(

				R.string.photos_tab_favorites)), FavoritesAlbumFragment.class,
				this.getArguments());
		return mTabHost;

	}



	
}