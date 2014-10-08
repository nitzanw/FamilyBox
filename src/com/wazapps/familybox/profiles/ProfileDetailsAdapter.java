package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProfileDetailsAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<ProfileDetails> profileDetails; 
	
	public ProfileDetailsAdapter(Activity activity, 
			ArrayList<ProfileDetails> profileDetails) {
		this.activity = activity;
		this.profileDetails = profileDetails;
	}
	
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
		
		TextView detailsTitle = (TextView) v.findViewById(R.id.tv_profile_detail_header);
		TextView detailsContents = (TextView) v.findViewById(R.id.tv_profile_detail_data);
		ProfileDetails details = this.profileDetails.get(position);
		String title = details.getDetailTitle();
		String data = details.getDetailContents();
		
		detailsTitle.setText(title);
		detailsContents.setText(data);	
		
		//if this is the last element - add some padding to the bottom of it
		//to improve the listView's visuals
		if (position == this.getCount() - 1) {
			detailsContents.setPadding(0, 0, 0, 50);
		}
		
		return v;
		
	}

}
