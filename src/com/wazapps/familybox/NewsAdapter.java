package com.wazapps.familybox;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
		return v;
	}
}