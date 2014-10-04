package com.wazapps.familybox;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFeedFragment extends TabsFragment {

	private static final String NEWS = "newsFragment";
	private static final String EVENTS = "eventFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {

		}
		mActionBar = getActivity().getActionBar();
		mActionBar.setTitle(getString(R.string.photo_albums).toUpperCase(
				Locale.getDefault()));
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
				mTabHost.newTabSpec(NEWS).setIndicator(
						makeTabIndicator(R.string.news_feed)),
				NewsFragmentTab.class, this.getArguments());

		mTabHost.addTab(
				mTabHost.newTabSpec(EVENTS).setIndicator(makeTabIndicator(

				R.string.events)), EventsFragmentTab.class, this.getArguments());

		return mTabHost;

	}
}
