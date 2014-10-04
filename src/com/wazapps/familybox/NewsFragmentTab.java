package com.wazapps.familybox;

import java.util.ArrayList;

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
		emptyText.setOnClickListener(this);
		
		this.newsList = (ListView) root.findViewById(R.id.news_posts_list);
		this.newsPosts = new ArrayList<NewsItem>();
		this.newsPosts.add(new NewsItem("lol",1,"lol",new ArrayList<String>()));
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
	}
}
