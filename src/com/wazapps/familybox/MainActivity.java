package com.wazapps.familybox;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseUser;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment;
import com.wazapps.familybox.familyTree.FamiliesListFragment;
import com.wazapps.familybox.newsfeed.NewsFeedTabsFragment;
import com.wazapps.familybox.newsfeed.NewsFragment;
import com.wazapps.familybox.newsfeed.NewsItem;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoAlbumsTabsFragment;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.profiles.ProfileDetails;
import com.wazapps.familybox.profiles.ProfileFragment;
import com.wazapps.familybox.profiles.ProfileFragment.AddProfileFragmentListener;
import com.wazapps.familybox.splashAndLogin.ChangePasswordDialogFragment;
import com.wazapps.familybox.splashAndLogin.LoginActivity;
import com.wazapps.familybox.util.JSONParser;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.MenuListAdapter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements
		AddProfileFragmentListener {

	public static final int MY_PROFILE_POS = 0;
	public static final int MY_FAMILY_PROFILE_POS = 1;
	public static final int FAMILY_TREE_POS = 2;
	public static final int PHOTOS_POS = 3;
	public static final int NEWS_POS = 4;
	public static final int EXPAND_NETWORK_POS = 5;

	public static final String LOG_OUT_ACTION = "logout";
	private static final String DRAWER_POSITION = "drawerPos";

	// Declare Variable
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	protected int mPosition = NEWS_POS;
	String[] title;
	String[] subtitle;
	int[] icon;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main);
		initDrawer();
		selectItem(mPosition);
		getActionBar().setTitle(getString(R.string.news_feed_title));
		overridePendingTransition(R.anim.enter, R.anim.exit);
	}

	public void initDrawer() {
		// Generate title
		title = new String[] { getString(R.string.drawer_my_profile),
				getString(R.string.drawer_family_profile),
				getString(R.string.drawer_family_tree),
				getString(R.string.drawer_photos),
				getString(R.string.drawer_news),
				getString(R.string.drawer_expand_network) };

		// TODO: add icons
		// Generate icon
		icon = new int[] { R.drawable.my_profile_drawer_icon,
				R.drawable.family_profile_drawer_icon,
				R.drawable.family_tree_drawer_icon,
				R.drawable.photos_drawer_icon, R.drawable.news_drawer_icon,
				R.drawable.expand_drawer_icon };

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				Fragment frag = getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

				if (PhotoAlbumsTabsFragment.PHOTO_ALBUM_TABS_FRAG.equals(frag
						.getTag()))
					getActionBar().setTitle(R.string.photo_albums);
				else if (NewsFeedTabsFragment.NEW_FEED_TAB_FRAG.equals(frag
						.getTag()))
					getActionBar().setTitle(R.string.news_feed_title);
				else if (ProfileFragment.PROFILE_FRAG.equals(frag.getTag()))
					getActionBar().setTitle(R.string.profile_title);
				else if (FamilyProfileFragment.FAMILY_PROFILE_FRAGMENT
						.equals(frag.getTag()))
					getActionBar().setTitle(R.string.family_profile_title);
				else if (FamiliesListFragment.FAMILY_TREE_FRAG.equals(frag
						.getTag()))
					getActionBar().setTitle(R.string.family_tree_title);

				// creates call to onPrepareOptionsMenu()
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(R.string.drawer_menu); // TODO: maybe
																// remove
				// creates call to onPrepareOptionsMenu()
				invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setIcon(
				getResources().getDrawable(R.drawable.drawer_app_icon));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set a custom shadow that overlays the main
		// content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass results to MenuListAdapter Class
		mMenuAdapter = new MenuListAdapter(this, title, icon, subtitle);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		// Capture button clicks on side menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		// getActionBar().setIcon(android.R.color.transparent);

		mDrawerLayout.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
					return true;
				}
				return false;
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		if (item.getItemId() == R.id.logout) {
			ParseUser currUser = ParseUser.getCurrentUser();
			if (currUser != null) {
				ParseUser.logOut();
			}

			Intent logoutIntent = new Intent(this, LoginActivity.class);
			logoutIntent.putExtra(LOG_OUT_ACTION, LOG_OUT_ACTION);
			startActivity(logoutIntent);
			finish();
		} else if (item.getItemId() == R.id.password) {

			ChangePasswordDialogFragment changePw = new ChangePasswordDialogFragment();
			changePw.show(getSupportFragmentManager(),
					ChangePasswordDialogFragment.CHANGE_PASSWORD_DIALOG_FRAG);
		} else if (item.getItemId() == R.id.about) {

		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(DRAWER_POSITION, mPosition);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();

	}

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FragmentManager mngr = getSupportFragmentManager();
			// make sure there is only one fragment on the backstack:
			while (mngr.getBackStackEntryCount() > 0) {
				mngr.popBackStackImmediate();
			}
			selectItem(position);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_extra_actions, menu);
		final MenuItem searchMenuItem = menu.findItem(R.id.action_extra);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action
		// items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_extra).setVisible(!drawerOpen);
		MenuItem searchFamily = menu.findItem(R.id.action_search);
		if (searchFamily != null) {
			searchFamily.setVisible(!drawerOpen);
		}
		MenuItem editProfile = menu.findItem(R.id.action_edit);
		if (editProfile != null) {
			editProfile.setVisible(!drawerOpen);
		}
		return true;
	}

	public void selectItem(int position) {
		mPosition = position;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (position) {
		case MY_PROFILE_POS:
			ProfileFragment profileFrag = new ProfileFragment();
			profileFrag.setArguments(getProfileArgsTemp());
			ft.replace(R.id.fragment_container, profileFrag,
					ProfileFragment.PROFILE_FRAG);
			break;
		case MY_FAMILY_PROFILE_POS:
			ft.replace(R.id.fragment_container, new FamilyProfileFragment(),
					FamilyProfileFragment.FAMILY_PROFILE_FRAG);

			break;
		case FAMILY_TREE_POS:
			ft.replace(R.id.fragment_container, new FamiliesListFragment(),
					FamiliesListFragment.FAMILY_TREE_FRAG);
			break;

		case PHOTOS_POS:
			ft.replace(R.id.fragment_container, new PhotoAlbumsTabsFragment(),
					PhotoAlbumsTabsFragment.PHOTO_ALBUM_TABS_FRAG);

			break;

		case NEWS_POS:
			NewsFeedTabsFragment newsTabs = new NewsFeedTabsFragment();
			Bundle args = new Bundle();
			updateNewsPosts(args);
			newsTabs.setArguments(args);
			// a better way

			ft.replace(R.id.fragment_container, newsTabs,
					NewsFeedTabsFragment.NEW_FEED_TAB_FRAG);

			break;

		case EXPAND_NETWORK_POS:

			break;
		}

		ft.commit();
		this.mDrawerLayout.closeDrawer(this.mDrawerList);
	}

	// TODO remove when real data comes
	private Bundle getProfileArgsTemp() {
		ProfileDetails[] profileDetailsData = { null, null, null, null };
		profileDetailsData[0] = (new ProfileDetails("Address",
				"K. yovel, mozkin st."));
		profileDetailsData[1] = (new ProfileDetails("Birthday", "19.10.1987"));
		profileDetailsData[2] = (new ProfileDetails("Previous Family Names",
				"No previous family names"));
		profileDetailsData[3] = (new ProfileDetails("Quotes",
				"For every every there exists exists"));

		FamilyMemberDetails dad = new FamilyMemberDetails("0", "1", "",
				getString(R.string.father_name), "Zohar",
				getString(R.string.parent), "", "", "", "", "", "", "m",
				profileDetailsData);
		FamilyMemberDetails mom = new FamilyMemberDetails("1", "1", "",
				getString(R.string.mother_name), "Zohar",
				getString(R.string.parent), "", "", "", "", "", "", "f",
				profileDetailsData);
		FamilyMemberDetails child1 = new FamilyMemberDetails("2", "1", "",
				getString(R.string.name) + " 1", "Zohar",
				getString(R.string.child), "", "", "", "", "", "", "f",
				profileDetailsData);
		FamilyMemberDetails child2 = new FamilyMemberDetails("3", "1", "",
				getString(R.string.name) + " 1", "Zohar",
				getString(R.string.child), "", "", "", "", "", "", "f",
				profileDetailsData);
		FamilyMemberDetails child3 = new FamilyMemberDetails("4", "1", "",
				getString(R.string.name) + " 1", "Zohar",
				getString(R.string.child), "", "", "", "", "", "", "f",
				profileDetailsData);

		FamilyMemberDetails child4 = new FamilyMemberDetails("5", "1", "",
				getString(R.string.name) + " 1", "Zohar",
				getString(R.string.child), "", "", "", "", "", "", "f",
				profileDetailsData);

		FamilyMemberDetails child5 = new FamilyMemberDetails("6", "1", "",
				getString(R.string.name) + " 1", "Zohar",
				getString(R.string.child), "", "", "", "", "", "", "f",
				profileDetailsData);

		final FamilyMemberDetails[] parentsList = { dad, mom };
		final FamilyMemberDetails[] childrenList = { child1, child2, child3,
				child4, child5 };
		final FamilyMemberDetails[] child1Family = { dad, mom, child2, child3,
				child4, child5 };

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
		args.putParcelable(ProfileFragment.MEMBER_ITEM, child1);
		args.putParcelableArray(ProfileFragment.FAMILY_MEMBER_LIST,
				child1Family);
		return args;
	}

	private void updateNewsPosts(Bundle args) {
		String jsonStr = null;
		JSONObject jsonObj = null;
		JSONArray jsonArr = null;

		jsonStr = JSONParser.loadJSONFromAsset(this, "familyBox.json");
		if (jsonStr == null)
			return;

		ArrayList<NewsItem> newsPosts = new ArrayList<NewsItem>();
		try {
			jsonObj = new JSONObject(jsonStr);
			jsonArr = jsonObj.getJSONArray("news_posts");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject post = jsonArr.getJSONObject(i);

				String userid = post.getString("user_id");
				long postid = post.getLong("post_id");
				String actionType = post.getString("action_type");
				ArrayList<String> extraInfo = new ArrayList<String>();
				JSONArray extraItems = post.getJSONArray("extra_info");
				for (int j = 0; j < extraItems.length(); j++) {
					extraInfo.add(extraItems.getString(j));
				}

				NewsItem postItem = new NewsItem(userid, postid, actionType,
						extraInfo);
				newsPosts.add(postItem);
			}
			args.putParcelableArrayList(NewsFragment.NEWS_ITEM_LIST, newsPosts);

		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

	}

	@Override
	public void addProfileFragment(Bundle args) {
		ProfileFragment profileFrag = new ProfileFragment();
		profileFrag.setArguments(args);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, profileFrag,
				ProfileFragment.PROFILE_FRAG).addToBackStack(null);
		ft.commit();

	}
}
