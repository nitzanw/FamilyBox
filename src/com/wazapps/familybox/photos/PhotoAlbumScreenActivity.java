package com.wazapps.familybox.photos;

import java.util.ArrayList;

import com.wazapps.familybox.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

public class PhotoAlbumScreenActivity extends FragmentActivity {
	protected static final String ALBUM_ITEM = "album item";
	private AlbumItem albumItem;
	private PhotoGridAdapter mAdpter;
	private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_album_screen);
		mGridView = (GridView) findViewById(R.id.gv_photo_album);
		Bundle args = getIntent().getBundleExtra(ALBUM_ITEM);
		albumItem = args.getParcelable(ALBUM_ITEM);
		initActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<PhotoItem> tempData = new ArrayList<PhotoItem>();
		for (int i = 0; i < 18; i++) {
			tempData.add(new PhotoItem("11.2.201" + i, "www.bla.com",
					"This is me and my friend Dan"));
		}
		mAdpter = new PhotoGridAdapter(this, tempData);
		mGridView.setAdapter(mAdpter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		getActionBar().setTitle(albumItem.getAlbumName());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}
}
