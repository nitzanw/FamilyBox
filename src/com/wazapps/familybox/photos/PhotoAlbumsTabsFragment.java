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

import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;
import com.wazapps.familybox.familyTree.FamiliesListItem;

public class PhotoAlbumsTabsFragment extends TabsFragment {
	private static final String MY_FAMILY = "myFamily";
	private static final String SHARED_ALBUM = "sharedAlbum";
	private static final String FAVORITES = "favorites";
	public static final String PHOTO_ALBUM_TABS_FRAG = "photo album tabs fragment";

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

		Bundle args = makeTempData();
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_photo_album_store_tabs, null);
		this.tabHost = (FragmentTabHost) rootView
				.findViewById(android.R.id.tabhost);
		this.tabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);

		// add tabs to the tabs storage
		this.tabHost.addTab(
				this.tabHost.newTabSpec(MY_FAMILY).setIndicator(
						makeTabIndicator(R.string.photos_tab_my_family)),
				AlbumGridFragment.class, args);

		this.tabHost.addTab(
				this.tabHost.newTabSpec(SHARED_ALBUM).setIndicator(
						makeTabIndicator(R.string.photos_tab_shared_album)),
				SharedAlbumFragment.class, args);

		this.tabHost.addTab(
				tabHost.newTabSpec(FAVORITES).setIndicator(
						makeTabIndicator(R.string.photos_tab_favorites)),
				PhotoGridFragment.class, args);

		return this.tabHost;
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

	// TODO: get real data
	/**
	 * Get temporary data for testing purposes
	 * 
	 * @return
	 */
	private Bundle makeTempData() {
		AlbumItem[] albumList = { null, null, null, null, null, null };
		String albumName = "Temp Album Name ";
		PhotoItem[] tempData = { null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null };

		for (int i = 0; i < 18; i++) {
			tempData[i] = new PhotoItem("11.2.201" + i, "www.bla.com",
					"This is me and my friend Dan " + i);
		}

		for (int i = 0; i < 6; i++) {
			albumList[i] = new AlbumItem(String.valueOf(i), tempData, albumName
					+ i, "December 201" + i);
		}

		Bundle args = new Bundle();
		args.putParcelableArray(PhotoGridFragment.ALBUM_ITEM_LIST, albumList);
		args.putParcelable(PhotoGridFragment.ALBUM_ITEM, albumList[0]);
		args.putString(FamiliesListItem.FAMILY_NAME, "My");
		return args;
	}

}