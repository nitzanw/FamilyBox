package com.wazapps.familybox.newsfeed;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wazapps.familybox.ActivityWithDrawer;
import com.wazapps.familybox.R;
import com.wazapps.familybox.TabsFragment;
import com.wazapps.familybox.familyProfiles.FamilyProfileActivity;
import com.wazapps.familybox.familyTree.FamilyTreeActivity;
import com.wazapps.familybox.photos.PhotoAlbumsActivity;
import com.wazapps.familybox.profiles.ProfileActivity;
import com.wazapps.familybox.util.JSONParser;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;

public class NewsfeedActivity extends ActivityWithDrawer {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.news_feed_title);
		overridePendingTransition(R.anim.enter, R.anim.exit); // TODO: handle
																// transition
																// animation in
		NewsFeedTabsFragment newsTabs = new NewsFeedTabsFragment();
		Bundle args = new Bundle();
		updateNewsPosts(args);
		newsTabs.setArguments(args);
		// a better way
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, newsTabs, TAG_NEWS_FEED);
		ft.commit();
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
	public void selectItem(int position) {
		mPosition = position;
		switch (position) {
		case MY_PROFILE_POS:
			Intent profileIntent = new Intent(this, ProfileActivity.class);
			startActivity(profileIntent);
			break;
		case MY_FAMILY_PROFILE_POS:
			Intent familyProfileIntent = new Intent(this,
					FamilyProfileActivity.class);
			startActivity(familyProfileIntent);
			break;
		case FAMILY_TREE_POS:
			Intent familyTreeIntent = new Intent(this, FamilyTreeActivity.class);
			startActivity(familyTreeIntent);
			break;

		case PHOTOS_POS:
			Intent photosIntent = new Intent(this, PhotoAlbumsActivity.class);
			startActivity(photosIntent);
			break;
		case NEWS_POS:
			TabsFragment frag = (TabsFragment) getSupportFragmentManager()
					.findFragmentByTag(TAG_NEWS_FEED);
			frag.switchTab(0);
			break;

		case EXPAND_NETWORK_POS:

			break;

		default:
			break;
		}

		this.mDrawerLayout.closeDrawer(this.mDrawerList);
	}

}
