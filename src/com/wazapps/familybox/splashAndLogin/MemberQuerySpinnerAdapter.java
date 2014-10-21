package com.wazapps.familybox.splashAndLogin;

import java.util.ArrayList;

import com.wazapps.familybox.R;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemberQuerySpinnerAdapter extends BaseAdapter {
	private FragmentActivity activity;
	private ArrayList<String> options;	
	private LayoutInflater layoutInflater;
	private boolean isMemberMale;
	
	private static final String MALE_REFERENCE = "He is my ";
	private static final String FEMALE_REFERENCE = "She is my ";
	
	public MemberQuerySpinnerAdapter(FragmentActivity activity, 
			ArrayList<String> options, boolean isMemberMale) {
		this.activity = activity;
		this.options = options;
		this.isMemberMale = isMemberMale;
	}

	@Override
	public int getCount() {
		return this.options.size();
	}

	@Override
	public Object getItem(int position) {
		return this.options.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// recycling the view:
		if (v == null) {
			v = layoutInflater.inflate(R.layout.member_query_spinner_item,
					parent, false);
		}
		TextView option = (TextView) 
				v.findViewById(R.id.tv_member_query_spinner_item_text);
		
		if (isMemberMale) {			
			option.setText(MALE_REFERENCE + this.options.get(position));
		} else {
			option.setText(FEMALE_REFERENCE + this.options.get(position));			
		}
		
		return v;		
	}

}
