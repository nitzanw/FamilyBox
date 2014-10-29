package com.wazapps.familybox.profiles;

import java.util.Arrays;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbstractFamilyListAdapter extends BaseAdapter {
	protected FragmentActivity activity;
	protected UserData userData;
	protected UserData[] familyMembersList;
	protected LayoutInflater linearInflater;

	public AbstractFamilyListAdapter(FragmentActivity activity,
			UserData[] familyMembersList, UserData userData) {
		this.activity = activity;
		this.familyMembersList = Arrays.copyOf(familyMembersList,
				familyMembersList.length, UserData[].class);
		this.userData = userData;
		this.linearInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return this.familyMembersList.length;
	}

	@Override
	public Object getItem(int position) {
		return this.familyMembersList[position];
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(familyMembersList[position].getUserId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		// recycling the view:
		if (v == null) {
			v = getInflatedView(parent);
		}

		initMemberView(position, v);
		return v;
	}

	abstract public View getInflatedView(ViewGroup parent);

	abstract public void initMemberView(int position, View v);

}
