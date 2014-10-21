package com.wazapps.familybox.photos;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wazapps.familybox.R;

public class PhotoGridAdapter extends BaseAdapter {

	private static final int PHOTO_POS = R.string.photo_albums;
	// Declare Variables
	FragmentActivity activity;
	LayoutInflater inflater;
	PhotoItem[] photoItemsList;

	public PhotoGridAdapter(FragmentActivity activity, PhotoItem[] photoUrls) {
		this.activity = activity;
		this.photoItemsList = Arrays.copyOf(photoUrls, photoUrls.length,
				PhotoItem[].class);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return photoItemsList.length;
	}

	@Override
	public Object getItem(int position) {
		return photoItemsList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		View vi = view;
		if (vi == null) {
			vi = inflater.inflate(R.layout.image_item, null);

		}
		ImageView image = (ImageView) vi.findViewById(R.id.iv_photo_in_album);

		image.setTag(PHOTO_POS, position);
		// Set the results into ImageView and textview

		// Listen for GridView Item Click
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoIntent = new Intent(activity,
						PhotoPagerActivity.class);

				int photoPos = (Integer) v.getTag(PHOTO_POS);
				Bundle args = new Bundle();
				args.putInt(PhotoPagerFragment.PHOTO_FIRST_POS, photoPos);
				args.putParcelableArray(PhotoPagerFragment.PHOTO_ITEM_LIST,
						photoItemsList);
				photoIntent.putExtra(PhotoPagerActivity.PHOTO_BUNDLE, args);
				activity.startActivity(photoIntent);

			}

		});

		return vi;
	}
}
