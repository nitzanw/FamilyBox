package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.util.LogUtils;

public class PhotoGridFragment extends Fragment {
	public static final String PHOTO_ALBUM_SCREEN_FRAG = "photo album screen fragment";
	public static final String ALBUM_ITEM = "album item";
	public static final String ALBUM_ITEM_LIST = "album items list";
	protected static final String ALBUM_ITEM_ID = "album id";
	protected static final String ALBUM_PHOTO_COUNT = "album photo count";
	private View root;
	private GridView mGridview;
	private PhotoGridAdapter mAdapter;
	private ProgressBar mProgress;
	private String albumId;
	private int albumSize;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_photo_album_screen,
				container, false);
		mGridview = (GridView) root.findViewById(R.id.gv_photo_album);
		mProgress = (ProgressBar) root.findViewById(R.id.pb_photo_album);
		mGridview.setAdapter(mAdapter);
		mGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Album album = mAdapter.getItem(position);
				// openAlbum(album);

				Intent photoIntent = new Intent(getActivity(),
						PhotoPagerActivity.class);
				//
				// int photoPos = (Integer) v.getTag(PHOTO_POS);
				Bundle args = new Bundle();
				args.putInt(PhotoPagerFragment.PHOTO_FIRST_POS, position);
				args.putString(PhotoPagerFragment.PHOTO_ID,
						mAdapter.getItem(position).getObjectId());
				ArrayList<String> photoItemsIdList = new ArrayList<String>();
				for (int i = 0; i < albumSize; i++) {
					photoItemsIdList.add(mAdapter.getItem(i).getObjectId());
				}
				args.putString(PhotoPagerFragment.PHOTO_ALBUM_ID, albumId);
				args.putStringArrayList(PhotoPagerFragment.PHOTO_ITEM_LIST,
						photoItemsIdList);
				photoIntent.putExtra(PhotoPagerActivity.PHOTO_BUNDLE, args);
				startActivity(photoIntent);

			}
		});
		return root;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			albumSize = args.getInt(ALBUM_PHOTO_COUNT);
			albumId = args.getString(ALBUM_ITEM_ID);
			// Set up the Parse query to use in the adapter
			PhotoGridAdapter.QueryFactory<PhotoItem_ex> factory = new PhotoGridAdapter.QueryFactory<PhotoItem_ex>() {
				public ParseQuery<PhotoItem_ex> create() {
					ParseQuery<PhotoItem_ex> query = ParseQuery
							.getQuery("PhotoItem");
					query.whereEqualTo(PhotoHandler.ALBUM_KEY, albumId);

					return query;
				}
			};

			mAdapter = new PhotoGridAdapter(getActivity(), factory);
			mAdapter.addOnQueryLoadListener(new OnQueryLoadListener<PhotoItem_ex>() {
				public void onLoading() {
					// Trigger "loading" UI
					mProgress.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoaded(List<PhotoItem_ex> objects, Exception e) {
					if (e == null) {
						mProgress.setVisibility(View.INVISIBLE);
						mGridview.setVisibility(View.VISIBLE);
					}
				}

			});
			handleActionbarTitle();
		} else {
			LogUtils.logWarning(getTag(), "these are no valid arguments!");
		}
	}

	private void handleActionbarTitle() {
		// if (getActivity() instanceof PhotoAlbumsActivity) {
		// getActivity().getActionBar().setTitle("Photo Albums");
		// } else {
		// getActivity().getActionBar().setTitle(albumItem.getAlbumName());
		// }
	}
}
