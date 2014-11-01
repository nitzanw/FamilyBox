package com.wazapps.familybox.photos;

import java.io.Serializable;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;

//a class that hold all photo albums data
@ParseClassName("Album")
public class Album extends ParseObject{

	int photoListSize;
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
			this.saveEventually();
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
					PhotoAlbumsTabsFragment.refreshMyFamilyAlbums();
				}
			}

			toast.show();
		}
	}
}
