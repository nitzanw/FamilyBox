package com.wazapps.familybox.util;

import java.util.HashSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wazapps.familybox.R;

public class ImageAdapter extends BaseAdapter {
	/**
	 * 
	 */
	private final MultiImageChooserActivity multiImageChooserActivity;
	private final Bitmap mPlaceHolderBitmap;
	private LayoutInflater inflater;

	public ImageAdapter(MultiImageChooserActivity multiImageChooserActivity, Context c) {
		this.multiImageChooserActivity = multiImageChooserActivity;
		if (this.multiImageChooserActivity.photoIdList == null) {
			this.multiImageChooserActivity.photoIdList = new HashSet<Integer>();
		} else {
			this.multiImageChooserActivity.photoIdList.clear();
		}
		Bitmap tmpHolderBitmap = BitmapFactory.decodeResource(
				this.multiImageChooserActivity.getResources(), R.drawable.stub);
		mPlaceHolderBitmap = Bitmap.createScaledBitmap(tmpHolderBitmap,
				this.multiImageChooserActivity.colWidth, this.multiImageChooserActivity.colWidth, false);
		if (tmpHolderBitmap != mPlaceHolderBitmap) {
			tmpHolderBitmap.recycle();
			tmpHolderBitmap = null;
		}
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		if (this.multiImageChooserActivity.imagecursor != null) {
			return this.multiImageChooserActivity.imagecursor.getCount();
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int pos, View vi, ViewGroup parent) {
		ImageView imageView = (ImageView) vi;

		if (imageView == null) {
			imageView = (ImageView) inflater.inflate(
					R.layout.image_picker_item, parent, false);

		}

		imageView.setImageBitmap(null);

		final int position = pos;

		if (!this.multiImageChooserActivity.imagecursor.moveToPosition(position)) {
			return imageView;
		}

		if (this.multiImageChooserActivity.image_column_index == -1) {
			return imageView;
		}

		final int id = this.multiImageChooserActivity.imagecursor.getInt(this.multiImageChooserActivity.image_column_index);
		this.multiImageChooserActivity.photoIdList.add(Integer.valueOf(id));
		if (this.multiImageChooserActivity.isChecked(pos)) {
			imageView.setBackgroundColor(this.multiImageChooserActivity.selectedColor);
		} else {
			imageView.setBackgroundColor(Color.TRANSPARENT);
		}
		if (this.multiImageChooserActivity.shouldRequestThumb) {
			this.multiImageChooserActivity.fetcher.fetch(Integer.valueOf(id), imageView, this.multiImageChooserActivity.colWidth);
		}

		return imageView;
	}
}