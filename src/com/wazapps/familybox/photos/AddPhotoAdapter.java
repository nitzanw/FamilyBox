package com.wazapps.familybox.photos;

import java.util.ArrayList;

import com.wazapps.familybox.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AddPhotoAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> photoUrlList;
	LayoutInflater inflater;

	public AddPhotoAdapter(Context context, ArrayList<String> photoUrlList) {
		this.context = context;
		this.photoUrlList = photoUrlList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return photoUrlList.size();
	}

	@Override
	public Object getItem(int position) {

		return photoUrlList.get(position);
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

		return vi;
	}

}
