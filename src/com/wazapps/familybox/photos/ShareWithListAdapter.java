package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.wazapps.familybox.R;

public class ShareWithListAdapter extends BaseAdapter implements
		OnCheckedChangeListener {

	private Context context;
	private ArrayList<String> familyNames;
	private LayoutInflater inflater;

	public ShareWithListAdapter(Context context, ArrayList<String> familyNames) {// TODO
																					// change
																					// to
																					// ids
		this.context = context;
		this.familyNames = familyNames;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return familyNames.size();
	}

	@Override
	public Object getItem(int position) {

		return familyNames.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		View vi = view;
		if (vi == null) {
			vi = inflater.inflate(R.layout.share_w_item, null);

		}

		TextView familyName = (TextView) vi
				.findViewById(R.id.tv_families_list_item_name);
		familyName.setText(familyNames.get(position));
		CheckBox checkBox = (CheckBox) vi
				.findViewById(R.id.cb_families_list_item_check_box);
		checkBox.setOnCheckedChangeListener(this);

		return vi;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

}
