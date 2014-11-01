package com.wazapps.familybox.photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQueryAdapter;
import com.wazapps.familybox.R;

public class PhotoGridAdapter extends ParseQueryAdapter<PhotoItem_ex> {

	LayoutInflater inflater;

	public PhotoGridAdapter(Context context,
			ParseQueryAdapter.QueryFactory<PhotoItem_ex> queryFactory) {
		super(context, queryFactory);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(PhotoItem_ex photoItem, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.album_item, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) view
					.findViewById(R.id.iv_photo_in_album);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ImageView image = holder.image;
		setPhoto(photoItem.getPhotoFile(), image);

		return view;
	}

	private void setPhoto(ParseFile photoFile, ImageView image) {
		if (photoFile != null) {
			photoFile.getDataInBackground(new GetDataCallback() {
				private ImageView image;

				@Override
				public void done(byte[] data, ParseException e) {
					Bitmap bitmap = null;
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
					image.setImageBitmap(bitmap);
				}

				GetDataCallback init(ImageView image) {
					this.image = image;
					return this;
				}
			}.init(image));
		}

	}

	private static class ViewHolder {
		ImageView image;
	}

	// private static final int PHOTO_POS = R.string.photo_albums;
	// // Declare Variables
	// FragmentActivity activity;
	// LayoutInflater inflater;
	// PhotoItem[] photoItemsList;
	//
	// public PhotoGridAdapter(FragmentActivity activity, PhotoItem[] photoUrls)
	// {
	// this.activity = activity;
	// this.photoItemsList = Arrays.copyOf(photoUrls, photoUrls.length,
	// PhotoItem[].class);
	// inflater = (LayoutInflater) activity
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//
	// }
	//
	// @Override
	// public int getCount() {
	// return photoItemsList.length;
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return photoItemsList[position];
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// public View getView(final int position, View view, ViewGroup parent) {
	// View vi = view;
	// if (vi == null) {
	// vi = inflater.inflate(R.layout.image_item, null);
	//
	// }
	// ImageView image = (ImageView) vi.findViewById(R.id.iv_photo_in_album);
	//
	// image.setTag(PHOTO_POS, position);
	// // Set the results into ImageView and textview
	//
	// // Listen for GridView Item Click
	// image.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Intent photoIntent = new Intent(activity,
	// PhotoPagerActivity.class);
	//
	// int photoPos = (Integer) v.getTag(PHOTO_POS);
	// Bundle args = new Bundle();
	// args.putInt(PhotoPagerFragment.PHOTO_FIRST_POS, photoPos);
	// args.putParcelableArray(PhotoPagerFragment.PHOTO_ITEM_LIST,
	// photoItemsList);
	// photoIntent.putExtra(PhotoPagerActivity.PHOTO_BUNDLE, args);
	// activity.startActivity(photoIntent);
	//
	// }
	//
	// });
	//
	// return vi;
	// }
}
