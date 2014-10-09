package com.wazapps.familybox.photos;

import java.util.ArrayList;

import com.wazapps.familybox.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class MyFamilyAlbumFragment extends Fragment {
	private View root;
	private GridView mGridview;
	AlbumGridAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_my_family, container, false);
		mGridview = (GridView) root.findViewById(R.id.gv_my_family);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		ArrayList<AlbumItem> albumList = new ArrayList<AlbumItem>();
		String albumName = "Temp Album Name ";
		for (int i = 0; i < 6; i++) {
			ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
			albumList.add(new AlbumItem(photos, albumName + i, "December 201"
					+ i));
		}
		mAdapter = new AlbumGridAdapter(getActivity(), albumList);
		mGridview.setAdapter(mAdapter);
	}
}
