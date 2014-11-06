package com.wazapps.familybox.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

public class AlbumSharedGridAdapter extends ParseQueryAdapter<ShareAlbum> {

	private static final int ALBUM_ITEM = R.string.album_title;
	LayoutInflater inflater;
	private Context context;

	public AlbumSharedGridAdapter(Context context,
			ParseQueryAdapter.QueryFactory<ShareAlbum> queryFactory) {
		super(context, queryFactory);
		LogUtils.logTemp("AlbumSharedGridAdapter", "ctor");
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(ShareAlbum sharealbum, View view, ViewGroup parent) {
		ViewHolder holder;
		LogUtils.logTemp("getItemView", sharealbum.getAlbumId());
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
			holder.albumFrame = (ImageButton) view
					.findViewById(R.id.ib_album_image);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ParseQuery<Album> query = ParseQuery.getQuery(Album.class);
		query.getInBackground(sharealbum.getAlbumId(),
				new GetCallback<Album>() {
					ViewHolder holder;

					@Override
					public void done(Album album, ParseException e) {
						if (e == null) {
							album.fetchIfNeededInBackground(new GetCallback<Album>() {

								@Override
								public void done(Album album, ParseException e) {
									LogUtils.logTemp("in done! got album!!",
											album.getAlbumName());
									TextView titleAlbum = holder.titleAlbum;

									TextView dateAlbum = holder.dateAlbum;

									FrameLayout imageAlbum = holder.imageAlbum;

									ImageButton albumFrame = holder.albumFrame;

									titleAlbum.setText(album.getAlbumName());
									dateAlbum.setText(album.getAlbumDate());
									setCoverPhoto(album.getAlbumCover(),
											imageAlbum);
									albumFrame.setTag(ALBUM_ITEM, album);
									setAlbumFrameClick(albumFrame);
								}
							});

						}
					}

					GetCallback<Album> init(ViewHolder holder) {
						this.holder = holder;
						return this;
					}
				}.init(holder));

		return view;
	}

	private void setAlbumFrameClick(ImageButton albumFrame) {
		// Listen for GridView Item Click
		albumFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (context != null) {
					Intent i = new Intent(context,
							PhotoAlbumScreenActivity.class);
					Bundle args = new Bundle();
					Album album = (Album) v.getTag(ALBUM_ITEM);
					args.putInt(PhotoGridFragment.ALBUM_PHOTO_COUNT,
							album.getAlbumPhotoCount());
					args.putString(PhotoGridFragment.ALBUM_ITEM_ID,
							album.getObjectId());
					args.putString(PhotoGridFragment.ALBUM_SRC,
							AlbumGridFragment.ALBUM_GRID_FRAGMENT);
					args.putString(PhotoGridFragment.ALBUM_NAME,
							album.getAlbumName());
					i.putExtra(PhotoGridFragment.ALBUM_ITEM, args);

					context.startActivity(i);
				}
			}
		});
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
