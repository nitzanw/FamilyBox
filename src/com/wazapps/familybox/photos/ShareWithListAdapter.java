package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
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
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.handlers.UserHandler;

public class ShareWithListAdapter extends ParseQueryAdapter<ParseObject>
		implements OnCheckedChangeListener {

	private static final int FAMILY_ID = R.string.id;
	private Context context;
	private LayoutInflater inflater;
	private HashMap<String, Boolean> checkedMap;
	private ArrayList<String> shareWithSelectedList;

	public ShareWithListAdapter(Context context,
			ParseQueryAdapter.QueryFactory<ParseObject> queryFactory,
			ArrayList<String> shareWithSelectedList) {

		super(context, queryFactory);
		this.context = context;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		checkedMap = new HashMap<String, Boolean>();
		this.shareWithSelectedList = shareWithSelectedList;
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

		if (shareWithSelectedList.contains(family.getFamilyId())) {
			checkBox.setChecked(true);
		}

		return view;
	}

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
