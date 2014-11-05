package com.wazapps.familybox.photos;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;

public class AlbumGridFragment extends Fragment implements OnClickListener {
	public static final String ALBUM_GRID_FRAGMENT = "album grid fragment";
	private View root;
	private GridView mGridview;
	AlbumGridAdapter mMyFamilyAdapter;
	AlbumSharedGridAdapter mSharedAdapter;
	private ParseUser currentUser = null;
	private LinearLayout mEmptyLayout;
	private ProgressBar mProgress;
	private boolean isMyFamily = false;
	private String familyIdForAlbum;
	AlbumGridAdapter.QueryFactory<Album> myFamilyFactory = new AlbumGridAdapter.QueryFactory<Album>() {

		public ParseQuery<Album> create() {
			ParseQuery<Album> query = ParseQuery.getQuery("Album");
			query.whereEqualTo(PhotoHandler.ALBUM_FAMILY_KEY, familyIdForAlbum);

			query.orderByDescending("createdAt");
			return query;
		}
	};

	AlbumSharedGridAdapter.QueryFactory<ShareAlbum> otherFamilyFactory = new AlbumSharedGridAdapter.QueryFactory<ShareAlbum>() {
		public ParseQuery<ShareAlbum> create() {

			ParseQuery<ShareAlbum> query = ParseQuery.getQuery("ShareAlbum");
			query.whereEqualTo("albumOwnerId", familyIdForAlbum);
			query.whereEqualTo("sharedWithId",
					ParseUser.getCurrentUser().get(UserHandler.FAMILY_KEY));
			query.orderByDescending("createdAt");
			return query;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (currentUser == null) {
			currentUser = ParseUser.getCurrentUser();
		}

		setUpAdapters();

	}

	private void setUpAdapters() {
		Bundle args = getArguments();
		if (args != null) {
			familyIdForAlbum = args.getString(FamilyHandler.FAMILY_ID_KEY);
			if (ParseUser.getCurrentUser().get(UserHandler.FAMILY_KEY)
					.toString().equals(familyIdForAlbum)) {
				isMyFamily = true;
				mMyFamilyAdapter = new AlbumGridAdapter(getActivity(),
						myFamilyFactory);
				setMyAdapterQueryLoadListener();

			} else {
				isMyFamily = false;
				String familyName = args.getString(FamilyHandler.NAME_KEY);
				getActivity().getActionBar().setTitle(familyName);

				mSharedAdapter = new AlbumSharedGridAdapter(getActivity(),
						otherFamilyFactory);
				setShareAdapterQueryLoadListener();
			}
		}
	}

	public void setMyAdapterQueryLoadListener() {
		mMyFamilyAdapter
				.addOnQueryLoadListener(new OnQueryLoadListener<Album>() {
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

	public void setShareAdapterQueryLoadListener() {
		mSharedAdapter
				.addOnQueryLoadListener(new OnQueryLoadListener<ShareAlbum>() {
					public void onLoading() {
						// Trigger "loading" UI
						mEmptyLayout.setVisibility(View.INVISIBLE);
						mProgress.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoaded(List<ShareAlbum> objects, Exception e) {
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
	public void onResume() {
		super.onResume();
		if (isMyFamily) {
			if (mMyFamilyAdapter != null) {
				mProgress.setVisibility(View.INVISIBLE);
			}
			// mGridview.setAdapter(mMyFamilyAdapter);
		} else {
			if (mSharedAdapter != null) {
				mProgress.setVisibility(View.INVISIBLE);
			}
			// mGridview.setAdapter(mSharedAdapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_my_family, container, false);
		mGridview = (GridView) root.findViewById(R.id.gv_my_family);
		mEmptyLayout = (LinearLayout) root.findViewById(R.id.ll_myfamily_empty);
		if (isMyFamily) {
			((Button) root.findViewById(R.id.my_family_album_empty_click))
					.setOnClickListener(this);
		} else {
			mEmptyLayout.findViewById(R.id.informative_msg).setVisibility(
					View.INVISIBLE);
			((Button) root.findViewById(R.id.my_family_album_empty_click))
					.setVisibility(View.INVISIBLE);
		}
		mProgress = (ProgressBar) root.findViewById(R.id.pb_myfamily);
		if (isMyFamily) {
//			if (mMyFamilyAdapter != null) {
//				mProgress.setVisibility(View.INVISIBLE);
//			}
			mGridview.setAdapter(mMyFamilyAdapter);
		} else {
//			if (mSharedAdapter != null) {
//				mProgress.setVisibility(View.INVISIBLE);
//			}
			mGridview.setAdapter(mSharedAdapter);
		}
		return root;
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
