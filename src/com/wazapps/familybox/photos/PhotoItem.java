package com.wazapps.familybox.photos;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.os.Parcel;
import android.os.Parcelable;
@ParseClassName("PhotoItem")
public class PhotoItem extends ParseObject {// implements Parcelable {
	public String getCaption() {
		return getString("caption");
	}
	
	public void setCaption(String caption) {
		put("caption", caption);
	}
	
	public ParseFile getPhotoFile() {
		return getParseFile("photo");
	}
	
	public void setPhotoFile(ParseFile photo) {
		put("photo", photo);
	}

	public void setAlbum(String objectId) {
		put("album", objectId);
		
	}

	// public String getDate() {
	// return date;
	// }
	//
	// public void setDate(String date) {
	// this.date = date;
	// }
	//
	// public String getUrl() {
	// return url;
	// }
	//
	// public void setUrl(String url) {
	// this.url = url;
	// }
	//
	// public String getCaption() {
	// return caption;
	// }
	//
	// public void setCaption(String caption) {
	// this.caption = caption;
	// }
	//
	// private String date;
	// private String url;
	// private String caption;
	//
	// public PhotoItem(String date, String url, String caption) {
	// this.date = date;
	// this.url = url;
	// this.caption = caption;
	// }
	//
	// public PhotoItem(Parcel in) {
	// this.date = in.readString();
	// this.url = in.readString();
	// this.caption = in.readString();
	// }
	//
	// public static final Parcelable.Creator<PhotoItem> CREATOR = new
	// Parcelable.Creator<PhotoItem>() {
	//
	// public PhotoItem createFromParcel(Parcel source) {
	// return new PhotoItem(source);
	// }
	//
	// public PhotoItem[] newArray(int size) {
	// return new PhotoItem[size];
	// }
	// };
	//
	// @Override
	// public int describeContents() {// TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeString(this.date);
	// dest.writeString(this.url);
	// dest.writeString(this.caption);
	//
	// }

}
