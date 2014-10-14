package com.wazapps.familybox.familyProfiles;

import java.util.Arrays;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.AlbumItem;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FamilyProfileAlbumAdapter extends BaseAdapter {
	private FragmentActivity activity;
	private AlbumItem[] albumList;

	public FamilyProfileAlbumAdapter(FragmentActivity activity,
			AlbumItem[] albumList) {
		this.activity = activity;
		this.albumList = Arrays.copyOf(albumList, albumList.length,
				AlbumItem[].class);
	}

	@Override
	public int getCount() {
		return albumList.length;
	}

	@Override
	public Object getItem(int position) {
		return albumList[position];
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(albumList[position].getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		LayoutInflater linearInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// recycling the view:
		if (v == null) {
			v = linearInflater.inflate(R.layout.family_profile_album_item,
					parent, false);
		}

		initAlbumView(position, v);
		return v;
	}

	private void initAlbumView(int position, View v) {
		ImageButton image = (ImageButton) v.findViewById(R.id.ib_album_image);
		((TextView) v.findViewById(R.id.tv_album_title))
				.setText(albumList[position].getAlbumName());
		((TextView) v.findViewById(R.id.tv_album_date))
				.setText(albumList[position].getAlbumDate());
	}
}
