package com.wazapps.familybox.photos;

import java.io.Serializable;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

//a class that hold all photo albums data
@ParseClassName("Album")
public class Album extends ParseObject {

	private int photoListSize;
	private int upLoadPhotoCounter;
	private Context context;

	public Album() {
	}

	public Album(Context context, int photoListSize) {
		super();
		this.context = context;
		this.photoListSize = photoListSize;
	}

	public String getAlbumFamily() {
		return getString("family");
	}

	public int getAlbumPhotoCount() {
		return getInt("albumPhotoCount");
	}

	public void setAlbumPhotoCount(int albumPhotoCount) {
		put("albumPhotoCount", albumPhotoCount);
	}

	public String getAlbumName() {
		return getString("albumName");
	}

	public void setAlbumName(String albumName) {
		put("albumName", albumName);
	}

	public String getAlbumDate() {
		return getString("albumDate");
	}

	public void setAlbumDate(String albumDate) {
		put("albumDate", albumDate);
	}

	public String getAlbumDescription() {
		return getString("albumDescription");
	}

	public void setAlbumDescription(String albumDescription) {
		put("albumDescription", albumDescription);
	}

	public ParseFile getAlbumCover() {
		return getParseFile("albumCover");
	}

	public void setCoverPhoto(ParseFile albumCover) {
		// albumCover.getDataInBackground(new GetDataCallback() {
		//
		// @Override
		// public void done(byte[] data, ParseException e) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		put("albumCover", albumCover);
	}

	public static ParseQuery<Album> getQuery() {
		return ParseQuery.getQuery(Album.class);
	}

	public void setFamily(String family) {
		put("family", family);

	}

	public synchronized void incrementAlbumCounter(ParseFile coverFile) {
		if (upLoadPhotoCounter == 0) {

			this.setCoverPhoto(coverFile);
			this.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
						LogUtils.logTemp("album class", "cover saved");
					}

				}
			});
		}
		upLoadPhotoCounter++;
		// if the number of uploaded photos is equal to the number of photos
		// user selected - show a toast
		if (upLoadPhotoCounter == photoListSize) {
			Toast toast = Toast.makeText(context,
					context.getString(R.string.add_album_success),
					Toast.LENGTH_LONG);

			if (context instanceof MainActivity) {
				Fragment frag = ((MainActivity) context)
						.getSupportFragmentManager().findFragmentById(
								R.id.fragment_container);
				if (frag instanceof PhotoAlbumsTabsFragment) {
					if (((PhotoAlbumsTabsFragment) frag).getCurrentTabHostPos() == PhotoAlbumsTabsFragment.MY_FAMILY_POS) {
						PhotoAlbumsTabsFragment.refreshMyFamilyAlbums();
					}
				}
			}

			toast.show();
		}
	}

}
