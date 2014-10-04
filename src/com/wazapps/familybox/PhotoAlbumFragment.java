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

public class PhotoAlbumFragment extends Fragment {

	private static final String MY_FAMILY = "myFamily";
	private static final String SHARED_ALBUM = "sharedAlbum";
	private static final String FAVORITES = "favorites";

	private FragmentTabHost mTabHost;
	int mCurrentPosition = -1;

	private ActionBar mActionBar;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {

		}
		mActionBar = getActivity().getActionBar();
		mActionBar.setTitle(getString(R.string.photo_albums).toUpperCase(
				Locale.getDefault()));
		setHasOptionsMenu(true);

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
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

	// making the tab view:
	private View makeTabIndicator(int text) {
		TextView Tabimage = new TextView(getActivity());
		LayoutParams LP = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 1);
		LP.setMargins(0, getResources().getDimensionPixelSize(R.dimen.tab_lp),
				0, getResources().getDimensionPixelSize(R.dimen.tab_lp));
		Tabimage.setLayoutParams(LP);
		Tabimage.setText(text);
		Tabimage.setBackgroundColor(getResources().getColor(
				R.color.gray_tab_background));
		Tabimage.setGravity(Gravity.CENTER);
		Tabimage.setTextSize(getResources().getDimension(R.dimen.tab_font_size));
		Tabimage.setMaxHeight(getResources().getDimensionPixelSize(
				R.dimen.tab_height));
		Tabimage.setMinHeight(getResources().getDimensionPixelSize(
				R.dimen.tab_height));
		Tabimage.setPadding(
				getResources().getDimensionPixelSize(R.dimen.tab_left), 0,
				getResources().getDimensionPixelSize(R.dimen.tab_right), 0);

		Tabimage.setTextColor(getResources().getColor(android.R.color.black));

		Tabimage.setBackgroundResource(R.drawable.tab_indicator);
		return Tabimage;

	}

	public void switchTab(int tab) {
		mTabHost.setCurrentTab(tab);
	}

}