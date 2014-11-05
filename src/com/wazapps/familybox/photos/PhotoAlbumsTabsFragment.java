package com.wazapps.familybox.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.UserHandler;

public class PhotoAlbumsTabsFragment extends TabsFragment {
	private static final String MY_FAMILY = "myFamily";
	private static final String SHARED_ALBUM = "sharedAlbum";
	private static final String FAVORITES = "favorites";
	public static final String PHOTO_ALBUM_TABS_FRAG = "photo album tabs fragment";
	public static final int MY_FAMILY_POS = 0;
	public static final int SHARED_ALBUM_POS = 1;
	public static final int FAVORITES_POS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
		}
		this.actionBar = getActivity().getActionBar();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		Bundle args = new Bundle();
		args.putString(FamilyHandler.FAMILY_ID_KEY, ParseUser.getCurrentUser()
				.get(UserHandler.FAMILY_KEY).toString());
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_photo_album_store_tabs, null);
		PhotoAlbumsTabsFragment.tabHost = (FragmentTabHost) rootView
				.findViewById(android.R.id.tabhost);
		PhotoAlbumsTabsFragment.tabHost.setup(getActivity(),
				getChildFragmentManager(), R.id.realtabcontent);

		// add tabs to the tabs storage
		TabsFragment.tabHost.addTab(TabsFragment.tabHost.newTabSpec(MY_FAMILY)
				.setIndicator(makeTabIndicator(R.string.photos_tab_my_family)),
				AlbumGridFragment.class, args);

		TabsFragment.tabHost.addTab(
				TabsFragment.tabHost.newTabSpec(SHARED_ALBUM).setIndicator(
						makeTabIndicator(R.string.photos_tab_shared_album)),
				SharedAlbumFragment.class, args);

		args.putString(PhotoGridFragment.ALBUM_SRC, PHOTO_ALBUM_TABS_FRAG);
		TabsFragment.tabHost.addTab(
				tabHost.newTabSpec(FAVORITES).setIndicator(
						makeTabIndicator(R.string.photos_tab_favorites)),
				PhotoGridFragment.class, args);

		return TabsFragment.tabHost;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_add_album, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add_album) {

			Intent addAlbum = new Intent(getActivity(),
					AddAlbumScreenActivity.class);
			getActivity().startActivityForResult(addAlbum,
					MainActivity.ADD_ALBUM_SCREEN_ACTIVITY);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void refreshMyFamilyAlbums() {
		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(0);

	}

	public int getCurrentTabHostPos() {
		String currTag = tabHost.getCurrentTabTag();
		if (MY_FAMILY.equals(currTag)) {
			return MY_FAMILY_POS;
		} else if (SHARED_ALBUM.equals(currTag)) {
			return SHARED_ALBUM_POS;
		} else if (FAVORITES.equals(currTag)) {
			return FAVORITES_POS;
		}
		return 0;
	}

}