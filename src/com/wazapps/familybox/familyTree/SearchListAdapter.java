package com.wazapps.familybox.familyTree;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SearchListAdapter extends ArrayAdapter<FamiliesListItem> {

	public SearchListAdapter(Context context, int resource,
			int textViewResourceId, List<FamiliesListItem> objects) {
		super(context, resource, textViewResourceId, objects);

	}
}
