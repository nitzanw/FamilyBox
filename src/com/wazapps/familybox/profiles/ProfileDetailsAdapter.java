package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ProfileDetailsAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<ProfileDetails> profileDetails; 
	
	
	@Override
	public int getCount() {
		return this.profileDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return this.profileDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {			
			LayoutInflater vi;
			vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.profile_details_item, parent, false);	
		}
		
		return v;
	}

}
