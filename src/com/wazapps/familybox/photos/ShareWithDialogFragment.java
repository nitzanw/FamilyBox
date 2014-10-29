package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.wazapps.familybox.R;

public class ShareWithDialogFragment extends DialogFragment implements
		OnClickListener {
	public static final String SHARE_W_DIALOG_FRAG = "share with dialog fragment";
	private View root;
	private ListView mFamilyListView;
	private ImageButton mExit;
	private Button mAccept;
	private ArrayList<String> connectedFamilliesList;
	private ShareWithListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_dialog_share_with, container,
				false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		mFamilyListView = (ListView) root
				.findViewById(R.id.lv_share_w_family_list);
		createData();
		mAdapter = new ShareWithListAdapter(getActivity(),
				connectedFamilliesList);
		mFamilyListView.setAdapter(mAdapter);
		mExit = (ImageButton) root.findViewById(R.id.ib_share_w_exit);
		mExit.setOnClickListener(this);
		mAccept = (Button) root.findViewById(R.id.button_share_w_accept);
		mAccept.setOnClickListener(this);
		return root;
	}

	private void createData() {
		connectedFamilliesList = new ArrayList<String>();
		connectedFamilliesList.add("Zohar");
		connectedFamilliesList.add("Kimhi");
		connectedFamilliesList.add("Cohen");
		connectedFamilliesList.add("Zur");
		connectedFamilliesList.add("Jorden");
		connectedFamilliesList.add("Hirsh");
		connectedFamilliesList.add("York");
		Collections.sort(connectedFamilliesList);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_share_w_exit) {
			dismiss();
		} else if (v.getId() == R.id.button_share_w_accept) {
			// TODO get all the checked family names ids
			dismiss();

		}

	}
}
