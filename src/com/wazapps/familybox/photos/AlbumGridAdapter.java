package com.wazapps.familybox.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQueryAdapter;
import com.wazapps.familybox.R;

public class AlbumGridAdapter extends ParseQueryAdapter<Album> {

	private static final int ALBUM_ITEM = R.string.album_title;
	LayoutInflater inflater;
	private Context context;

	public AlbumGridAdapter(Context context,
			ParseQueryAdapter.QueryFactory<Album> queryFactory) {
		super(context, queryFactory);
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(Album album, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.album_item, parent, false);
			holder = new ViewHolder();
			holder.imageAlbum = (FrameLayout) view
					.findViewById(R.id.fl_album_item);
			holder.titleAlbum = (TextView) view
					.findViewById(R.id.tv_album_title);
			holder.dateAlbum = (TextView) view.findViewById(R.id.tv_album_date);
			holder.albumFrame = (ImageButton) view
					.findViewById(R.id.ib_album_image);
			holder.albumFrame = (ImageButton) view.findViewById(R.id.ib_album_image);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		

		TextView titleAlbum = holder.titleAlbum;
		titleAlbum.setText(album.getAlbumName());

		TextView dateAlbum = holder.dateAlbum;
		dateAlbum.setText(album.getAlbumDate());

		FrameLayout imageAlbum = holder.imageAlbum;
		setCoverPhoto(album.getAlbumCover(), imageAlbum);
		
		ImageButton albumFrame = holder.albumFrame;
		albumFrame.setTag(ALBUM_ITEM,album);
		// Listen for GridView Item Click
		albumFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, PhotoAlbumScreenActivity.class);
				Bundle args = new Bundle();
				Album album = (Album) v.getTag(ALBUM_ITEM);
				args.putInt(PhotoGridFragment.ALBUM_PHOTO_COUNT, album.getAlbumPhotoCount());
				args.putString(PhotoGridFragment.ALBUM_ITEM_ID, album.getObjectId());
				args.putString(PhotoGridFragment.ALBUM_SRC, AlbumGridFragment.ALBUM_GRID_FRAGMENT);
				i.putExtra(PhotoGridFragment.ALBUM_ITEM, args);
				
				context.startActivity(i);
			}
		});

		return view;
	}

	public void setCoverPhoto(ParseFile coverPhoto, FrameLayout imageAlbum) {
		if (coverPhoto != null) {

			coverPhoto.getDataInBackground(new GetDataCallback() {
				private FrameLayout imageAlbum;

				@Override
				public void done(byte[] data, ParseException e) {
					// set the cover photo
					Bitmap bitmap = null;
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					imageAlbum.setBackground(drawable);
					// make the progress disappear
					LinearLayout progress = (LinearLayout) imageAlbum
							.findViewById(R.id.ll_pb_album_cover);
					progress.setVisibility(View.INVISIBLE);

				}

				GetDataCallback init(FrameLayout imageAlbum) {
					this.imageAlbum = imageAlbum;
					return this;
				}

			}.init(imageAlbum));

		} else {
			LinearLayout progress = (LinearLayout) imageAlbum
					.findViewById(R.id.ll_pb_album_cover);
			progress.setVisibility(View.INVISIBLE);
		}
	}

	private static class ViewHolder {
		FrameLayout imageAlbum;
		TextView titleAlbum;
		TextView dateAlbum;
		ImageButton albumFrame;

		
	}
}

// public AlbumGridAdapter(Activity activity, AlbumItem[] albumList) {
// this.activity = activity;
// this.albumList = Arrays.copyOf(albumList, albumList.length,
// AlbumItem[].class);
//
// inflater = (LayoutInflater) activity
// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
// }
//
// @Override
// public int getCount() {
// return albumList.length;
// }
//
// @Override
// public Object getItem(int position) {
// return albumList[position];
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
// vi = inflater.inflate(R.layout.album_item, null);
//
// }
// FrameLayout imageAlbum = (FrameLayout) vi
// .findViewById(R.id.fl_album_item);
// // create an imageloader and load image from db or web
// imageAlbum.setBackground(activity.getResources().getDrawable(
// R.drawable.image6));
// TextView titleAlbum = (TextView) vi.findViewById(R.id.tv_album_title);
// titleAlbum.setText(albumList[position].getAlbumName());
// TextView dateAlbum = (TextView) vi.findViewById(R.id.tv_album_date);
// dateAlbum.setText(albumList[position].getAlbumDate());
// ImageButton albumFrame = (ImageButton) vi
// .findViewById(R.id.ib_album_image);
// albumFrame.setTag(ALBUM_ITEM_POS, albumList[position]);
// // Set the results into ImageView and textview
//
// // Listen for GridView Item Click
// albumFrame.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Intent i = new Intent(activity, PhotoAlbumScreenActivity.class);
// Bundle args = new Bundle();
// AlbumItem item = (AlbumItem) v.getTag(ALBUM_ITEM_POS);
// args.putParcelable(PhotoGridFragment.ALBUM_ITEM, item);
// i.putExtra(PhotoGridFragment.ALBUM_ITEM, args);
// activity.startActivity(i);
// }
// });
//
// return vi;
// }
