package com.wazapps.familybox;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	private Activity activity;
	ArrayList<NewsItem> newsArr;
	
	
	public NewsAdapter(Activity activity, ArrayList<NewsItem> newsPosts) {
		this.activity = activity;
		this.newsArr = newsPosts;
	}

	@Override
	public int getCount() {
		return newsArr.size();
	}

	@Override
	public Object getItem(int position) {
		return this.newsArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.newsArr.get(position).getPostid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {			
			LayoutInflater vi;
			vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.news_feed_item, parent, false);	
		}
		
		NewsItem item = this.newsArr.get(position);
		TextView userName, youTag, updateMessage, date, time;
		userName = (TextView) v.findViewById(R.id.tv_news_feed_item_username);
		youTag = (TextView) v.findViewById(R.id.tv_news_feed_item_username_you_tag);
		updateMessage = (TextView) v.findViewById(R.id.tv_news_feed_item_update_msg);
		date = (TextView) v.findViewById(R.id.tv_news_feed_item_date);
		time = (TextView) v.findViewById(R.id.tv_news_feed_item_time);
		
		//TODO: extract information from json and stuff
		return v;
	}
}