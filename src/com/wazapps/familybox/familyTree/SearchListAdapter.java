package com.wazapps.familybox.familyTree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.InputHandler;

public class SearchListAdapter extends BaseAdapter {

	private static final int POSITION = R.string.position;
	private ArrayList<FamiliesListItem> familliesList;
	private Context context;

	public SearchListAdapter(Context context, List<FamiliesListItem> objects) {

		this.familliesList = new ArrayList<FamiliesListItem>(objects);
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.searchable_family_list_item, parent,
					false);
		}

		FamiliesListItem currentFamily = familliesList.get(position);

		if (currentFamily != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView familyName = (TextView) v
					.findViewById(R.id.tv_search_families_list_item_name);
			familyName.setText(InputHandler.capitalizeName(currentFamily
					.getFamilyName()));

		}

		return v;

	}

	@Override
	public int getCount() {

		return familliesList.size();
	}

	@Override
	public Object getItem(int position) {

		return familliesList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public void setData(ArrayList<FamiliesListItem> filteredList) {
		familliesList = new ArrayList<FamiliesListItem>(filteredList);

	}
}
