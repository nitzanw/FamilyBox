package com.wazapps.familybox;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_news_feed, container, false);
		TextView emptyText = (TextView)root.findViewById(R.id.news_feed_empty_click);
		emptyText.setOnClickListener(this);
		
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
		ListView news = (ListView) root.findViewById(android.R.id.list);
		LinearLayout emptyLayout = (LinearLayout) root.findViewById(R.id.ll_news_feed_empty);
		
		emptyLayout.setVisibility(View.INVISIBLE);
		news.setVisibility(View.VISIBLE);
	}
}
