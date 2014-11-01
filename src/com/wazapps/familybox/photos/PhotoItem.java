package com.wazapps.familybox.photos;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable{
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	private String date;
	private String url;
	private String caption;

	public PhotoItem(String date, String url, String caption) {
		this.date = date;
		this.url = url;
		this.caption = caption;
	}

	public PhotoItem(Parcel in) {
		this.date = in.readString();
		this.url = in.readString();
		this.caption = in.readString();
	}

	public static final Parcelable.Creator<PhotoItem> CREATOR = new Parcelable.Creator<PhotoItem>() {

		public PhotoItem createFromParcel(Parcel source) {
			return new PhotoItem(source);
		}

		public PhotoItem[] newArray(int size) {
			return new PhotoItem[size];
		}
	};

	@Override
	public int describeContents() {// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.date);
		dest.writeString(this.url);
		dest.writeString(this.caption);

	}
}
