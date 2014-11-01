package com.wazapps.familybox.photos;

import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.LogUtils;

public class AlbumGridFragment extends Fragment {
	public static final String ALBUM_GRID_FRAGMENT = "album grid fragment";
	private View root;
	private GridView mGridview;
	AlbumGridAdapter mAdapter;
	private ParseUser currentUser = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.logTemp(getClass().getName(), "in the on create!");
		if (currentUser == null) {
			currentUser = ParseUser.getCurrentUser();
		}
        // Set up the Parse query to use in the adapter
		AlbumGridAdapter.QueryFactory<Album> factory = new AlbumGridAdapter.QueryFactory<Album>() {
            public ParseQuery<Album> create() {
                ParseQuery<Album> query = ParseQuery.getQuery("Album");
                query.whereEqualTo(PhotoHandler.ALBUM_FAMILY_KEY, currentUser.get(UserHandler.FAMILY_KEY));
                query.orderByDescending("createdAt");
                return query;
            }
        };
        mAdapter = new AlbumGridAdapter(getActivity(), factory);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_my_family, container, false);
		mGridview = (GridView) root.findViewById(R.id.gv_my_family);
		mGridview.setAdapter(mAdapter);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();

	}
	
	private void handleActionbarTitle(String familyName) {
//		if (getActivity() instanceof PhotoAlbumsActivity) {
//			getActivity().getActionBar().setTitle("Photo Albums");
//		} else {
//			getActivity().getActionBar().setTitle(familyName + " Family's Albums");
//		}
	}
}


