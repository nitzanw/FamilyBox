package com.wazapps.familybox.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;

public class PhotoHandler {
	/**
	 * Used to decode real path from uri. used by the photo chooser.
	 */
	public static String getRealPathFromURI(Activity activity, Uri contentURI) {
		final String[] imageColumns = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		Cursor cursor = activity.getContentResolver()
				.query(contentURI, imageColumns,
				null, null, null);
		if (cursor == null) { 
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}
	
	public static Bitmap getImageBitmapFromFile(File file) {
		Options options = new Options();
		options.inSampleSize = 4; //downsample factor
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}
	
	public static byte[] createDownsampledPictureData(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		return stream.toByteArray();
	}
}
