package com.wazapps.familybox.photos;

import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.util.LogUtils;

public class AlbumGridFragment extends Fragment {
	public static final String ALBUM_GRID_FRAGMENT = "album grid fragment";
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
			Parcelable[] parcelableArray = args.getParcelableArray(PhotoGridFragment.ALBUM_ITEM_LIST);
			AlbumItem[] albumList = Arrays.copyOf(parcelableArray, parcelableArray.length, AlbumItem[].class);
			String familyName = args.getString(FamiliesListItem.FAMILY_NAME);
			handleActionbarTitle(familyName);
			mAdapter = new AlbumGridAdapter(getActivity(), albumList);
			mGridview.setAdapter(mAdapter);
		} else {
			LogUtils.logWarning(getTag(), "the args did not pass!!");
		}
	}
	
	private void handleActionbarTitle(String familyName) {
//		if (getActivity() instanceof PhotoAlbumsActivity) {
//			getActivity().getActionBar().setTitle("Photo Albums");
//		} else {
//			getActivity().getActionBar().setTitle(familyName + " Family's Albums");
//		}
	}
}


