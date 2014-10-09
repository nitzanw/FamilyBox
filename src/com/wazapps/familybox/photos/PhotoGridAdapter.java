package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.support.v4.app.FragmentActivity;
import com.wazapps.familybox.R;

public class PhotoGridAdapter extends BaseAdapter{

	
	private static final int PHOTO_POS = R.string.photo_albums;
	// Declare Variables
	FragmentActivity activity;
	LayoutInflater inflater;
	ArrayList<PhotoItem> photoItemsList;

	public PhotoGridAdapter(FragmentActivity activity, ArrayList<PhotoItem> photoUrls) {
		this.activity = activity;
		this.photoItemsList = photoUrls;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return photoItemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return photoItemsList.get(position);
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
				PhotoDialogFragment photoDialog = new PhotoDialogFragment();
				int photoPos = (Integer) v.getTag(PHOTO_POS);
				Bundle args = new Bundle();
				args.putInt(PhotoDialogFragment.PHOTO_FIRST_POS, photoPos);
				args.putParcelableArrayList(PhotoDialogFragment.PHOTO_ALBUM_DATA, photoItemsList);
				photoDialog.setArguments(args);
				photoDialog.show(activity.getSupportFragmentManager(), PhotoDialogFragment.PHOTO_DIALOG_FRAG);

			}

		});

		return vi;
	}
}
