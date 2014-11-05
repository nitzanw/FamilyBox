package com.wazapps.familybox.photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

public class PhotoFragment extends Fragment {

	protected static final String PHOTO_DIALOG_FRAG = "photo dialog fragment";
	protected static final String PHOTO_ITEM = "photo item";
	public static final String PHOTO_ITEM_ID = "photo item id";
	private View root;
	private ImageView mImage;
	private String photoItemId;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_image_dialog, container,
				false);
		
		mImage = (ImageView) root.findViewById(R.id.iv_actual_image);

		ParseQuery<PhotoItem> query = ParseQuery
				.getQuery(PhotoItem.class);
		query.getInBackground(photoItemId, new GetCallback<PhotoItem>() {

			@Override
			public void done(PhotoItem object, ParseException e) {
				if (e == null) {
					object.fetchIfNeededInBackground(new GetCallback<PhotoItem>() {

						@Override
						public void done(PhotoItem object, ParseException e) {
							if (e == null) {
//								caption = object.getCaption();
//								captionCallback.setCaption(object.getCaption(), object.getObjectId());
								ParseFile photoFile = object.getPhotoFile();

								if (photoFile != null) {
									photoFile
											.getDataInBackground(new GetDataCallback() {

												@Override
												public void done(byte[] data,
														ParseException e) {
													if (e == null) {
														Bitmap bitmap = null;
														bitmap = BitmapFactory
																.decodeByteArray(
																		data,
																		0,
																		data.length);
														mImage.setImageBitmap(bitmap);
													}
												}
											});
								}
							}
						}
					});
				}
			}
		});

		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			photoItemId = args.getString(PHOTO_ITEM_ID);

			// TODO load image
			// mImage.setBackground(((PhotoItem)photoList.get(firstPhotoIndex)).getUrl());

		} else {
			LogUtils.logWarning(getTag(),
					"the argument did not pass properlly!");
		}
	}
}
