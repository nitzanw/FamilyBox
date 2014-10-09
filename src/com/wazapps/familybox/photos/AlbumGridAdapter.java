package com.wazapps.familybox.photos;

import java.util.ArrayList;
import com.wazapps.familybox.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class AlbumGridAdapter extends BaseAdapter {

	
	private static final int ALBUM_ITEM_POS = R.string.photos_tab_my_family;
	// Declare Variables
	Activity activity;
	LayoutInflater inflater;
	ArrayList<AlbumItem> albumList;

	public AlbumGridAdapter(Activity activity, ArrayList<AlbumItem> albumList) {
		this.activity = activity;
		this.albumList = albumList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return albumList.size();
	}

	@Override
	public Object getItem(int position) {
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		View vi = view;
		if (vi == null) {
			vi = inflater.inflate(R.layout.album_item, null);

		}
		FrameLayout imageAlbum = (FrameLayout) vi
				.findViewById(R.id.fl_album_item);
		// create an imageloader and load image from db or web
		imageAlbum.setBackground(activity.getResources().getDrawable(
				R.drawable.image6));
		TextView titleAlbum = (TextView) vi.findViewById(R.id.tv_album_title);
		titleAlbum.setText(albumList.get(position).getAlbumName());
		TextView dateAlbum = (TextView) vi.findViewById(R.id.tv_album_date);
		dateAlbum.setText(albumList.get(position).getAlbumDate());
		ImageButton albumFrame = (ImageButton) vi
				.findViewById(R.id.ib_album_image);
		albumFrame.setTag(ALBUM_ITEM_POS, albumList.get(position));
		// Set the results into ImageView and textview

		// Listen for GridView Item Click
		albumFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, PhotoAlbumScreenActivity.class);
				Bundle args = new Bundle();
				AlbumItem item = (AlbumItem) v.getTag(ALBUM_ITEM_POS);
				args.putParcelable(PhotoAlbumScreenActivity.ALBUM_ITEM, item);
				i.putExtra(PhotoAlbumScreenActivity.ALBUM_ITEM, args);
				activity.startActivity(i);

			}

		});

		return vi;
	}
}