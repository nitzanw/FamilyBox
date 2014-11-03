package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamiliesListItem;

public class ShareWithListAdapter extends ParseQueryAdapter<ParseObject>
		implements OnCheckedChangeListener {

	private static final int FAMILY_ID = R.string.id;
	private Context context;
	// private ArrayList<String> familyNames;
	private LayoutInflater inflater;
	static private boolean shareEnabled;
	private HashMap<String, Boolean> checkedMap;

	public ShareWithListAdapter(Context context,
			ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {// TODO
																		// //
																		// change
		super(context, queryFactory);
		this.context = context;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		checkedMap = new HashMap<String, Boolean>();
	}

	@Override
	public View getItemView(ParseObject familyObj, View view, ViewGroup parent) {
		FamiliesListItem family = new FamiliesListItem(familyObj);
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.share_w_item, parent, false);
			holder = new ViewHolder();
			holder.familyName = (TextView) view
					.findViewById(R.id.tv_families_list_item_name);
			holder.checkBox = (CheckBox) view
					.findViewById(R.id.cb_families_list_item_check_box);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		TextView familyName = holder.familyName;

		familyName.setText(family.getFamilyName());
		CheckBox checkBox = holder.checkBox;
		checkBox.setTag(FAMILY_ID, family.getFamilyId());
		checkBox.setOnCheckedChangeListener(this);

		return view;
	}

	// @Override
	// public int getCount() {
	// return familyNames.size();
	// }

	// @Override
	// public Object getItem(int position) {
	//
	// return familyNames.get(position);
	// }

	// @Override
	// public long getItemId(int arg0) {
	// return 0;
	// }
	//
	// @Override
	// public View getView(final int position, View view, ViewGroup parent) {
	// View vi = view;
	// if (vi == null) {
	// vi = inflater.inflate(R.layout.share_w_item, null);
	//
	// }
	//
	// TextView familyName = (TextView) vi
	// .findViewById(R.id.tv_families_list_item_name);
	// familyName.setText(familyNames.get(position));
	// CheckBox checkBox = (CheckBox) vi
	// .findViewById(R.id.cb_families_list_item_check_box);
	// checkBox.setOnCheckedChangeListener(this);
	//
	// return vi;
	// }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String familyId = (String) buttonView.getTag(FAMILY_ID);
		checkedMap.put(familyId, isChecked);
	}

	public boolean isShareButtonEnabled() {
		boolean isEnabled = false;
		for (Boolean b : checkedMap.values()) {
			isEnabled |= b;
		}
		return isEnabled;

	}

	private static class ViewHolder {
		TextView familyName;
		CheckBox checkBox;
	}

	public List<String> getFamilyIdShareList() {
		List<String> familyIdShareList = new ArrayList<String>();
		for (String id : checkedMap.keySet()) {
			if (checkedMap.get(id)) {
				familyIdShareList.add(id);
			}
		}
		return familyIdShareList;

	}
}
