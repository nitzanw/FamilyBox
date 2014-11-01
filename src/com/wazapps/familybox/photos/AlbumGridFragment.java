package com.wazapps.familybox.photos;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;

public class AlbumGridFragment extends Fragment implements OnClickListener {
	public static final String ALBUM_GRID_FRAGMENT = "album grid fragment";
	private View root;
	private GridView mGridview;
	AlbumGridAdapter mAdapter;
	private ParseUser currentUser = null;
	private LinearLayout mEmptyLayout;
	private ProgressBar mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (currentUser == null) {
			currentUser = ParseUser.getCurrentUser();
		}
		// Set up the Parse query to use in the adapter
		AlbumGridAdapter.QueryFactory<Album> factory = new AlbumGridAdapter.QueryFactory<Album>() {
			public ParseQuery<Album> create() {
				ParseQuery<Album> query = ParseQuery.getQuery("Album");
				query.whereEqualTo(PhotoHandler.ALBUM_FAMILY_KEY,
						currentUser.get(UserHandler.FAMILY_KEY));
				query.orderByDescending("createdAt");
				return query;
			}
		};
		mAdapter = new AlbumGridAdapter(getActivity(), factory);
		mAdapter.addOnQueryLoadListener(new OnQueryLoadListener<Album>() {
			public void onLoading() {
				// Trigger "loading" UI
				mEmptyLayout.setVisibility(View.INVISIBLE);
				mProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoaded(List<Album> objects, Exception e) {
				if (e == null) {
					mProgress.setVisibility(View.INVISIBLE);
					if (objects.isEmpty()) {
						mEmptyLayout.setVisibility(View.VISIBLE);
					} else {

						mGridview.setVisibility(View.VISIBLE);
					}
				}
			}

		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_my_family, container, false);
		mGridview = (GridView) root.findViewById(R.id.gv_my_family);
		mEmptyLayout = (LinearLayout) root.findViewById(R.id.ll_myfamily_empty);
		((Button) root.findViewById(R.id.my_family_album_empty_click))
				.setOnClickListener(this);
		mProgress = (ProgressBar) root.findViewById(R.id.pb_myfamily);
		mGridview.setAdapter(mAdapter);
//		mGridview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Album album = mAdapter.getItem(position);
//				openAlbum(album);
//
//			}
//		});
		return root;
	}

//	protected void openAlbum(Album album) {
//		
//		Intent i = new Intent(getActivity(), PhotoAlbumScreenActivity.class);
//		Bundle args = new Bundle();
//		args.putSerializable(PhotoGridFragment.ALBUM_ITEM, album);
//		i.putExtra(PhotoGridFragment.ALBUM_ITEM, args);
//		startActivity(i);
//	}

	private void handleActionbarTitle(String familyName) {
		// if (getActivity() instanceof PhotoAlbumsActivity) {
		// getActivity().getActionBar().setTitle("Photo Albums");
		// } else {
		// getActivity().getActionBar().setTitle(familyName +
		// " Family's Albums");
		// }
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.my_family_album_empty_click) {
			Intent addAlbum = new Intent(getActivity(),
					AddAlbumScreenActivity.class);
			getActivity().startActivityForResult(addAlbum,
					MainActivity.ADD_ALBUM_SCREEN_ACTIVITY);

		}
	}
}
