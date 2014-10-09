package com.wazapps.familybox.photos;

import java.util.ArrayList;


import android.os.Parcel;
import android.os.Parcelable;

//a class that hold all photo albums data
public class AlbumItem implements Parcelable {

	private ArrayList<PhotoItem> photosUrls;
	private String albumName;
	private String albumDate;

	public AlbumItem(ArrayList<PhotoItem> photosUrls, String albumName,
			String albumDate) {
		this.photosUrls = photosUrls;
		this.albumName = albumName;
		this.albumDate = albumDate;
	}

	public AlbumItem(Parcel source) {
		this.photosUrls = new ArrayList<PhotoItem>();
	    source.readList(this.photosUrls, getClass().getClassLoader());
		this.albumName = source.readString();
		this.albumDate = source.readString();
	}

	public ArrayList<PhotoItem> getPhotosUrls() {
		return photosUrls;
	}

	public void setPhotosUrls(ArrayList<PhotoItem> photosUrls) {
		this.photosUrls = photosUrls;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumDate() {
		return albumDate;
	}

	public void setAlbumDate(String albumDate) {
		this.albumDate = albumDate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(photosUrls);
		dest.writeString(albumName);
		dest.writeString(albumDate);
	}
	


	public static final Parcelable.Creator<AlbumItem> CREATOR = new Parcelable.Creator<AlbumItem>() {

		public AlbumItem createFromParcel(Parcel source) {
			return new AlbumItem(source);
		}

		public AlbumItem[] newArray(int size) {
			return new AlbumItem[size];
		}
	};

}
