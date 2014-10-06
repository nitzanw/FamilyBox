package com.wazapps.familybox.newsfeed;

import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFeedTabsFragment extends TabsFragment {

	private static final String NEWS = "newsFragment";
	private static final String EVENTS = "eventFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {}
		
		actionBar = getActivity().getActionBar();
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
		this.tabHost.addTab(this.tabHost.newTabSpec(NEWS)
				.setIndicator(makeTabIndicator(R.string.news)),
				NewsFragment.class, this.getArguments());

		this.tabHost.addTab(this.tabHost.newTabSpec(EVENTS)
				.setIndicator(makeTabIndicator(R.string.events)), 
				EventsFragment.class, this.getArguments());

		return tabHost;
	}
}
