package com.wazapps.familybox.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

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
		
		Bundle args = getArguments();
		if (args != null) {
			AlbumItem[] albumList = (AlbumItem[]) args
					.getParcelableArray(PhotoAlbumScreenFragment.ALBUM_ITEM_LIST);

			mAdapter = new AlbumGridAdapter(getActivity(), albumList);
			mGridview.setAdapter(mAdapter);
		} else {
			LogUtils.logWarning(getTag(), "the args did not pass!!");
		}
	}
}
