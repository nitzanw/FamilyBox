package com.wazapps.familybox;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;

public class NewsFragmentTab extends Fragment implements OnClickListener{
	
	private View root;
	private ArrayList<NewsItem> newsPosts;
	private NewsAdapter newsAdapter;
	private ListView newsList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_news_feed, container, false);
		
		TextView emptyText = (TextView)root.findViewById(R.id.news_feed_empty_click);
		this.newsList = (ListView) root.findViewById(R.id.news_posts_list);
		emptyText.setOnClickListener(this);
		this.newsPosts = new ArrayList<NewsItem>();
		this.newsAdapter = new NewsAdapter(this.getActivity(), this.newsPosts);
		this.newsList.setAdapter(this.newsAdapter);
		
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_feed_empty_click:
			viewNewsFeed();
			break;

		default:
			break;
		}		
	}
	
	private void viewNewsFeed() {
		LinearLayout emptyLayout = (LinearLayout) root.findViewById(R.id.ll_news_feed_empty);
		emptyLayout.setVisibility(View.INVISIBLE);
		this.newsList.setVisibility(View.VISIBLE);
		updateNewsPosts();
	}
	
	private void updateNewsPosts() {
		String jsonStr = null;
		JSONObject jsonObj = null;
		JSONArray jsonArr = null;
		
		jsonStr = JSONParser.loadJSONFromAsset(getActivity(), "familyBox.json");
		if (jsonStr == null) return;
		
		try {
			jsonObj = new JSONObject(jsonStr);
			jsonArr = jsonObj.getJSONArray("news_posts");
			for (int i=0; i<jsonArr.length(); i++) {
				JSONObject post = jsonArr.getJSONObject(i);
				
				String userid = post.getString("user_id");
				long postid = post.getLong("post_id");
				String actionType =  post.getString("action_type");
				ArrayList<String> extraInfo = new ArrayList<String>();
				JSONArray extraItems = post.getJSONArray("extra_info");
				for (int j=0; j<extraItems.length(); j++) {
					extraInfo.add(extraItems.getString(j));
				}
				
				NewsItem postItem = new NewsItem(userid, postid, actionType, extraInfo);
				this.newsPosts.add(postItem);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}	
		
		this.newsAdapter.notifyDataSetChanged();
	}
}
