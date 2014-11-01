package com.wazapps.familybox.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.parse.ParseQuery;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.util.LogUtils;

public class PhotoGridFragment extends Fragment {
	public static final String PHOTO_ALBUM_SCREEN_FRAG = "photo album screen fragment";
	public static final String ALBUM_ITEM = "album item";
	public static final String ALBUM_ITEM_LIST = "album items list";
	private View root;
	private GridView mGridview;
	private Album album;
	private PhotoGridAdapter mAdpter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_photo_album_screen,
				container, false);
		mGridview = (GridView) root.findViewById(R.id.gv_photo_album);
		return root;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			album = (Album) args.getSerializable(ALBUM_ITEM);
			// Set up the Parse query to use in the adapter
			PhotoGridAdapter.QueryFactory<PhotoItem_ex> factory = new PhotoGridAdapter.QueryFactory<PhotoItem_ex>() {
				public ParseQuery<PhotoItem_ex> create() {
					ParseQuery<PhotoItem_ex> query = ParseQuery
							.getQuery("PhotoItem");
					query.whereEqualTo(PhotoHandler.ALBUM_KEY,
							album.getAlbumId());

					return query;
				}
			};

			mAdpter = new PhotoGridAdapter(getActivity(), factory);
			mGridview.setAdapter(mAdpter);
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
