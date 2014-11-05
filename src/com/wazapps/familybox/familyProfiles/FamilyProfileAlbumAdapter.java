package com.wazapps.familybox.familyProfiles;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.Album;

public class FamilyProfileAlbumAdapter extends BaseAdapter {
	private FragmentActivity activity;
	private List<Album> albumList;

	public FamilyProfileAlbumAdapter(FragmentActivity activity,
			List<Album> albumList) {
		this.activity = activity;
		this.albumList = albumList;
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
		return Long.valueOf(albumList.get(position).getObjectId());
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

	private void initAlbumView(int position, final View v) {
		albumList.get(position).fetchIfNeededInBackground(
				new GetCallback<Album>() {

					@Override
					public void done(Album object, ParseException e) {
						if (e == null) {
							ImageButton image = (ImageButton) v
									.findViewById(R.id.ib_album_image);
							setCoverPhoto(object.getAlbumCover(), image);
							((TextView) v.findViewById(R.id.tv_album_title))
									.setText(object.getAlbumName());
							((TextView) v.findViewById(R.id.tv_album_date))
									.setText(object.getAlbumDate());
						}
					}
				});
	}

	public void setCoverPhoto(ParseFile coverPhoto, ImageButton imageAlbum) {
		if (coverPhoto != null) {

			coverPhoto.getDataInBackground(new GetDataCallback() {
				private ImageButton imageAlbum;

				@Override
				public void done(byte[] data, ParseException e) {
					// set the cover photo
					Bitmap bitmap = null;
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					imageAlbum.setBackground(drawable);

				}

				GetDataCallback init(ImageButton imageAlbum) {
					this.imageAlbum = imageAlbum;
					return this;
				}

			}.init(imageAlbum));

		}
	}
}
