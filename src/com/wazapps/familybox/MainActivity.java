package com.wazapps.familybox;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.splunk.mint.Mint;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment;
import com.wazapps.familybox.familyProfiles.FamilyProfileFragment.AddFamilyProfileFragmentListener;
import com.wazapps.familybox.familyTree.BasicFamilyListFragment;
import com.wazapps.familybox.familyTree.FamiliesListFragment;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.newsfeed.NewsFeedTabsFragment;
import com.wazapps.familybox.newsfeed.NewsFragment;
import com.wazapps.familybox.newsfeed.NewsItemToRemove;
import com.wazapps.familybox.photos.Album;
import com.wazapps.familybox.photos.PhotoAlbumsTabsFragment;
import com.wazapps.familybox.photos.PhotoItem_ex;
import com.wazapps.familybox.photos.ShareAlbum;
import com.wazapps.familybox.profiles.ProfileFragment;
import com.wazapps.familybox.profiles.ProfileFragment.AddProfileFragmentListener;
import com.wazapps.familybox.profiles.UserData;
import com.wazapps.familybox.splashAndLogin.ChangePasswordDialogFragment;
import com.wazapps.familybox.splashAndLogin.LoginActivity;
import com.wazapps.familybox.util.AboutFragment;
import com.wazapps.familybox.util.JSONParser;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.MenuListAdapter;

public class MainActivity extends FragmentActivity implements
		AddProfileFragmentListener, AddFamilyProfileFragmentListener {

	public static abstract class MainActivityCallback {
		public abstract void done(Exception e);
	}

	public static abstract class GetDataCallback {
		public abstract void done(Bundle data, Exception e);
	}

	public static final int ADD_ALBUM_SCREEN_ACTIVITY = 5;

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
	protected int mPosition = NEWS_POS;
	MenuListAdapter mMenuAdapter;
	String[] title;
	String[] subtitle;
	int[] icon;

	// cached user data variables and handlers
	private ParseUser currentUser = null;
	private UserData userData = null;
	private ParseObject currentFamily = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(MainActivity.this, "ad50ec84");
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
			logUserOut();
		}

		else if (item.getItemId() == R.id.password) {
			ChangePasswordDialogFragment changePw = new ChangePasswordDialogFragment();
			changePw.show(getSupportFragmentManager(),
					ChangePasswordDialogFragment.CHANGE_PASSWORD_DIALOG_FRAG);
		}

		else if (item.getItemId() == R.id.about) {
			AboutFragment aboutFrag = new AboutFragment();
			aboutFrag.show(getSupportFragmentManager(),
					AboutFragment.ABOUT_DIALOG_FRAG);
		}
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
		final MenuItem extraMenuItem = menu.findItem(R.id.action_extra);
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
		MenuItem addAlbum = menu.findItem(R.id.action_add_album);
		if (addAlbum != null) {
			addAlbum.setVisible(!drawerOpen);
		}
		return true;
	}

	public void selectItem(int position) {
		mPosition = position;
		switch (position) {

		case MY_PROFILE_POS:
			fetchProfileLocalData(new MainActivityCallback() {
				MainActivity activity;

				@Override
				public void done(Exception e) {
					if (e == null) {
						Bundle data = new Bundle();
						data.putBoolean(ProfileFragment.USER_PROFILE, false);
						data.putBoolean(ProfileFragment.IS_FROM_DRAWER, true);
						ProfileFragment profileFrag = new ProfileFragment();
						profileFrag.setArguments(data);
						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();
						ft.setCustomAnimations(R.anim.fade_in_fast,
								R.anim.fade_out_fast);
						ft.replace(R.id.fragment_container, profileFrag,
								ProfileFragment.PROFILE_FRAG);
						ft.commit();													
					} 
					
					else {
						LogUtils.logError("MainActivity", e.getMessage());

						Toast.makeText(getApplicationContext(),
								"connection error. log in again",
								Toast.LENGTH_SHORT).show();

						logUserOut();
					}

				}

				private MainActivityCallback init(MainActivity activity) {
					this.activity = activity;
					return this;
				}
			}.init(this));

			mDrawerLayout.closeDrawer(mDrawerList);
			break;

		case MY_FAMILY_PROFILE_POS:
			fetchProfileLocalData(new MainActivityCallback() {
				MainActivity activity;

				@Override
				public void done(Exception e) {
					if (e == null) {
						Bundle data = new Bundle();
						data.putBoolean(FamilyProfileFragment.USER_FAMILY,
								false);
						data.putString(FamilyProfileFragment.FAMILY_NAME,
								currentUser
										.getString(UserHandler.LAST_NAME_KEY));
						data.putString(FamilyProfileFragment.FAMILY_ID,
								currentUser.getString(UserHandler.FAMILY_KEY));

						FamilyProfileFragment frag = new FamilyProfileFragment();
						frag.setArguments(data);

						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();
						ft.setCustomAnimations(R.anim.fade_in_fast,
								R.anim.fade_out_fast);
						ft.replace(R.id.fragment_container, frag,
								FamilyProfileFragment.FAMILY_PROFILE_FRAG);
						ft.commit();
					} else {
						LogUtils.logError("MainActivity", e.getMessage());

						Toast.makeText(getApplicationContext(),
								"connection error. log in again",
								Toast.LENGTH_SHORT).show();
						logUserOut();
					}
				}

				private MainActivityCallback init(MainActivity activity) {
					this.activity = activity;
					return this;
				}
			}.init(this));

			mDrawerLayout.closeDrawer(mDrawerList);
			break;

		case FAMILY_TREE_POS:
			Bundle familyTreeArgs = new Bundle();
			familyTreeArgs.putBoolean(BasicFamilyListFragment.IS_FAMILY_TREE,
					true);
			FamiliesListFragment familyTreeFragment = new FamiliesListFragment();
			familyTreeFragment.setArguments(familyTreeArgs);

			FragmentTransaction ft2 = getSupportFragmentManager()
					.beginTransaction();
			ft2.setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_fast);
			ft2.replace(R.id.fragment_container, familyTreeFragment,
					FamiliesListFragment.FAMILY_TREE_FRAG);
			ft2.commit();
			this.mDrawerLayout.closeDrawer(this.mDrawerList);

			break;

		case PHOTOS_POS:
			FragmentTransaction ft3 = getSupportFragmentManager()
					.beginTransaction();
			ft3.setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_fast);
			ft3.replace(R.id.fragment_container, new PhotoAlbumsTabsFragment(),
					PhotoAlbumsTabsFragment.PHOTO_ALBUM_TABS_FRAG);
			ft3.commit();
			this.mDrawerLayout.closeDrawer(this.mDrawerList);

			break;

		case NEWS_POS:
			FragmentTransaction ft4 = getSupportFragmentManager()
					.beginTransaction();
			ft4.setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_fast);
			NewsFeedTabsFragment newsTabs = new NewsFeedTabsFragment();
			Bundle args = new Bundle();
			updateNewsPosts(args);
			newsTabs.setArguments(args);
			// a better way

			ft4.replace(R.id.fragment_container, newsTabs,
					NewsFeedTabsFragment.NEW_FEED_TAB_FRAG);
			ft4.commit();
			this.mDrawerLayout.closeDrawer(this.mDrawerList);

			break;

		case EXPAND_NETWORK_POS:
			FragmentTransaction ft5 = getSupportFragmentManager()
					.beginTransaction();
			ft5.setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_fast);
			this.mDrawerLayout.closeDrawer(this.mDrawerList);
			ft5.commit();

			break;
		}
	}

	private void updateNewsPosts(Bundle args) {
		String jsonStr = null;
		JSONObject jsonObj = null;
		JSONArray jsonArr = null;

		jsonStr = JSONParser.loadJSONFromAsset(this, "familyBox.json");
		if (jsonStr == null)
			return;

		ArrayList<NewsItemToRemove> newsPosts = new ArrayList<NewsItemToRemove>();
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

				NewsItemToRemove postItem = new NewsItemToRemove(userid,
						postid, actionType, extraInfo);
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
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_reverse,
				R.anim.fade_out_fast);
		ft.add(R.id.fragment_container, profileFrag,
				ProfileFragment.PROFILE_FRAG).addToBackStack(null);
		ft.commit();
	}

	@Override
	public void addFamilyProfileFragment(Bundle args) {
		FamilyProfileFragment familyProfileFrag = new FamilyProfileFragment();
		familyProfileFrag.setArguments(args);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_reverse,
				R.anim.fade_out_fast);
		ft.add(R.id.fragment_container, familyProfileFrag,
				ProfileFragment.PROFILE_FRAG).addToBackStack(null);
		ft.commit();
	}

	public void fetchProfileLocalData(final MainActivityCallback callbackFunc) {
		// if profile data has already been fetched before
		if (currentUser != null && userData != null) {
			callbackFunc.done(null);
			return;
		}

		// else start fetching profile data
		currentUser = ParseUser.getCurrentUser();

		// if no user is logged in
		if (currentUser == null) {
			callbackFunc.done(new Exception("no user is logged in"));
		}

		currentUser
				.fetchFromLocalDatastoreInBackground(new GetCallback<ParseUser>() {
					@Override
					public void done(ParseUser user, ParseException e) {
						callbackFunc.done(e);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_ALBUM_SCREEN_ACTIVITY && resultCode == RESULT_OK) {
			String albumName = data.getStringExtra(PhotoHandler.ALBUM_NAME);
			String albumDate = data.getStringExtra(PhotoHandler.ALBUM_DATE);
			String albumDesc = data
					.getStringExtra(PhotoHandler.ALBUM_DESCRIPTION);
			ArrayList<String> photoUrls = data
					.getStringArrayListExtra(PhotoHandler.PHOTO_URLS);
			ArrayList<String> shareWith = data
					.getStringArrayListExtra(PhotoHandler.SHARE_WITH);

			uploadAlbum(albumName, albumDate, albumDesc, photoUrls, shareWith);
		}
	}

	private void setAlbumShareWithList(final Album album,
			ArrayList<String> shareWithList) {
		for (String familyid : shareWithList) {
				
						ShareAlbum share = new ShareAlbum();
						share.setAlbumId(album.getObjectId());
						share.setAlbumOwner(ParseUser.getCurrentUser().get(UserHandler.FAMILY_KEY).toString());
						share.setSharedWithId(familyid);
						share.saveEventually();

		}
	}

	private void uploadAlbum(String albumName, String albumDate,
			String albumDesc, ArrayList<String> photoUrls,
			final ArrayList<String> shareWith) {

		Album album = new Album(this, photoUrls.size());
		if (currentUser == null) {
			currentUser = ParseUser.getCurrentUser();
		}


		album.setAlbumPhotoCount(photoUrls.size());
		album.setFamily((String) currentUser.get(UserHandler.FAMILY_KEY));
		album.setAlbumName(albumName);
		album.setAlbumDate(albumDate);
		album.setAlbumDescription(albumDesc);
		album.saveInBackground(new SaveCallback() {
			FragmentActivity activity = null;
			Album album = null;
			ArrayList<String> photoUrls = null;

			@Override
			public void done(ParseException e) {
				if (e == null) {
					setAlbumShareWithList(album, shareWith);
					((MainActivity) activity).uploadPhotosToAlbum(album,
							photoUrls);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							getString(R.string.add_album_err_not_uploaded)
									+ " " + e.toString(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
				}
			}

			SaveCallback init(FragmentActivity activity, Album album,
					ArrayList<String> photoUrls) {
				this.activity = activity;
				this.album = album;
				this.photoUrls = photoUrls;
				return this;
			}

		}.init(this, album, photoUrls));

	}

	protected void uploadPhotosToAlbum(Album album, ArrayList<String> photoUrls) {
		for (int i = 0; i < photoUrls.size(); i++) {
			Uri currImageURI = Uri.parse(photoUrls.get(i));

			File file = new File(PhotoHandler.getRealPathFromURI(this,
					currImageURI));

			if (file.exists()) {
				Bitmap myBitmap = PhotoHandler.getImageBitmapFromFile(file);
				byte[] fileData = PhotoHandler
						.createDownsampledPictureData(myBitmap);

				PhotoItem_ex photoItem = new PhotoItem_ex();
				photoItem.setCaption("");
				photoItem.setAlbum(album.getObjectId());

				ParseFile photoFile = new ParseFile(file.getName(), fileData);
				photoFile.saveInBackground(new SaveCallback() {
					private PhotoItem_ex photoItem;
					private Album album;
					private ParseFile photoFile;

					@Override
					public void done(ParseException e) {
						if (e == null) {
							photoItem.setPhotoFile(photoFile);
							photoItem.saveEventually(new SaveCallback() {
								private Album album;
								private ParseFile photoFile;

								@Override
								public void done(ParseException e) {
									// add the cover photo to the album (if the
									// counter is 0)

									album.incrementAlbumCounter(photoFile);
								}

								SaveCallback init(Album album,
										ParseFile photoFile) {

									this.album = album;
									this.photoFile = photoFile;
									return this;
								}

							}.init(album, photoFile));
						} else {
							LogUtils.logError(getLocalClassName(),
									"something went wrong with the album uplaod "
											+ e.getMessage());
						}
					}

					private SaveCallback init(FragmentActivity activity,
							Album album, ParseFile albumPic,
							PhotoItem_ex photoItem) {
						this.album = album;
						this.photoFile = albumPic;
						this.photoItem = photoItem;
						return this;
					}
				}.init(this, album, photoFile, photoItem));

			}
		}
	}

	public void logUserOut() {
		ParseUser currUser = ParseUser.getCurrentUser();
		if (currUser != null) {
			ParseUser.logOut();
			ParseUser.unpinAllInBackground("UserFamilyMembers");
			ParseObject.unpinAllInBackground("UserFamily");
		}

		Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
		logoutIntent.putExtra(LOG_OUT_ACTION, LOG_OUT_ACTION);
		startActivity(logoutIntent);
		finish();
	}
}
