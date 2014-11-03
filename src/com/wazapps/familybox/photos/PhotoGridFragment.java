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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.util.LogUtils;

public class PhotoGridFragment extends Fragment {
	public static final String PHOTO_ALBUM_SCREEN_FRAG = "photo album screen fragment";
	public static final String ALBUM_ITEM = "album item";
	public static final String ALBUM_ITEM_LIST = "album items list";
	protected static final String ALBUM_ITEM_ID = "album id";
	protected static final String ALBUM_PHOTO_COUNT = "album photo count";
	protected static final String ALBUM_SRC = "album source";
	protected static final String ALBUM_NAME = "album name";
	private View root;
	private GridView mGridview;
	private PhotoGridAdapter mAdapter;
	private ProgressBar mProgress;
	private String albumId;
	private int albumSize = -1;
	private String mSource;

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

				ArrayList<String> photoItemsIdList = new ArrayList<String>();
				ArrayList<String> photoItemsCaptionList = new ArrayList<String>();
				if (albumSize == -1) {
					albumSize = mAdapter.getCount();
				}
				for (int i = 0; i < albumSize; i++) {
					photoItemsIdList.add(mAdapter.getItem(i).getObjectId());
					photoItemsCaptionList.add(mAdapter.getItem(i).getCaption());
				}

				args.putStringArrayList(PhotoPagerFragment.PHOTO_ITEM_LIST,
						photoItemsIdList);
				args.putStringArrayList(
						PhotoPagerFragment.PHOTO_ITEM_CAPTION_LIST,
						photoItemsCaptionList);
				args.putString(ALBUM_SRC, mSource);
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
			if (AlbumGridFragment.ALBUM_GRID_FRAGMENT.equals(args
					.getString(ALBUM_SRC))) {
				handleActionbarTitle(args.getString(ALBUM_NAME));
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

			} else if (PhotoAlbumsTabsFragment.PHOTO_ALBUM_TABS_FRAG
					.equals(args.getString(ALBUM_SRC))) {
				mAdapter = new PhotoGridAdapter(getActivity());

			}
			mSource = args.getString(ALBUM_SRC);
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
			
		} else {
			LogUtils.logWarning(getTag(), "these are no valid arguments!");
		}
	}

	private void handleActionbarTitle(String albumName) {

		getActivity().getActionBar().setTitle(albumName);
	}
}
