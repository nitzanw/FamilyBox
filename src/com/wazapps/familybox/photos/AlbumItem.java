package com.wazapps.familybox.photos;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

//a class that hold all photo albums data
public class AlbumItem implements Parcelable {
	private String id;
	private PhotoItem[] photoList;
	private String albumName;
	private String albumDate;

	public AlbumItem(String id, PhotoItem[] photoList, String albumName,
			String albumDate) {
		this.id = id;
		this.photoList = photoList;
		this.albumName = albumName;
		this.albumDate = albumDate;
	}

	public AlbumItem(Parcel source) {
		this.id = source.readString();
		Parcelable[] parcelableArray = source
				.readParcelableArray(PhotoItem.class.getClassLoader());
		this.photoList = null;
		if (parcelableArray != null) {
			this.photoList = Arrays.copyOf(parcelableArray,
					parcelableArray.length, PhotoItem[].class);
		}
		this.albumName = source.readString();
		this.albumDate = source.readString();
	}

	public PhotoItem[] getPhotosList() {
		return photoList;
	}

	public void setPhotosUrls(PhotoItem[] photosUrls) {
		this.photoList = photosUrls;
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
		dest.writeString(id);
		dest.writeParcelableArray(photoList, flags);
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

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}
}
