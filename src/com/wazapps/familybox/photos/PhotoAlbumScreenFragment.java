package com.wazapps.familybox.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

public class PhotoAlbumScreenFragment extends Fragment {
	public static final String PHOTO_ALBUM_SCREEN_FRAG = "photo album screen fragment";
	protected static final String ALBUM_ITEM = "album item";
	protected static final String ALBUM_ITEM_LIST = "album items list";
	private View root;
	private GridView mGridview;
	private AlbumItem albumItem;
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
	public void onResume() {
		super.onResume();
		Bundle args = getArguments();
		if (args != null) {
			albumItem = args.getParcelable(ALBUM_ITEM);
			PhotoItem[] photoList = albumItem.getPhotosList();
			mAdpter = new PhotoGridAdapter(getActivity(), photoList);
			mGridview.setAdapter(mAdpter);
			getActivity().getActionBar().setTitle(albumItem.getAlbumName());
		} else {
			LogUtils.logWarning(getTag(), "the are no valid argument!");
		}
	}
}
