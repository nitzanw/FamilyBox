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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

public class PhotoGridAdapter extends ParseQueryAdapter<PhotoItem> {

	LayoutInflater inflater;// FavoriteGridAdapter

	public PhotoGridAdapter(Context context,
			ParseQueryAdapter.QueryFactory<PhotoItem> queryFactory) {
		super(context, queryFactory);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public PhotoGridAdapter(Context context) {
		super(context, new PhotoGridAdapter.QueryFactory<PhotoItem>() {
			public ParseQuery<PhotoItem> create() {

				ParseUser user = ParseUser.getCurrentUser();
				ParseRelation<PhotoItem> relation = user
						.getRelation("favorites");
				return relation.getQuery();
			}
		});
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(PhotoItem photoItem, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.image_item, parent, false);
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
					if (e == null) {

						BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
		                options.inPurgeable = true; // inPurgeable is used to free up memory while required
		                Bitmap image1 = BitmapFactory.decodeByteArray(data,0, data.length,options);//Decode image, "data" is the object of image file
		                Bitmap image2 = Bitmap.createScaledBitmap(image1, 100 , 100 , true);// convert decoded bitmap into well scalled Bitmap format.

		                image.setImageBitmap(image2);
	
					} else {
						LogUtils.logError(getClass().getName(), e.getMessage());
					}
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

}
