package com.wazapps.familybox.photos;

import java.io.File;
import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment;
import com.wazapps.familybox.util.ImageFetcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AddPhotoAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Integer> photoIdList;
	LayoutInflater inflater;
	private final ImageFetcher fetcher = new ImageFetcher();

	public AddPhotoAdapter(Context context, ArrayList<Integer> photoIdList) {
		this.context = context;
		this.photoIdList = photoIdList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return photoIdList.size();
	}

	@Override
	public Object getItem(int position) {

		return photoIdList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) {
			vi = inflater.inflate(R.layout.add_album_item, null);

		}

		ImageView image = (ImageView) vi.findViewById(R.id.iv_add_album_item);
		fetcher.fetch(photoIdList.get(position), image, 125);
		return vi;
	}

	public void updateData(ArrayList<Integer> currentSelectedPhotos) {
		photoIdList = currentSelectedPhotos;
		notifyDataSetChanged();

	}

}
