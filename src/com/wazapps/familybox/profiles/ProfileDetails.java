package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileDetails implements Parcelable{
	private String detailTitle, detailContents;
	
	public static final Parcelable.Creator<ProfileDetails> CREATOR = 
			new Parcelable.Creator<ProfileDetails>() {
		
		public ProfileDetails createFromParcel(Parcel source) {
			return new ProfileDetails(source);
		}

		public ProfileDetails[] newArray(int size) {
			return new ProfileDetails[size];
		}
	};
	
	public ProfileDetails(String detailTitle, String detailContents) {
		this.detailTitle = detailTitle;
		this.detailContents = detailContents;
	}
	
	public ProfileDetails(Parcel details) {
		this.detailTitle = details.readString();
		this.detailContents = details.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.detailTitle);
		dest.writeString(detailContents);
	}

	public String getDetailTitle() {
		return detailTitle;
	}

	public String getDetailContents() {
		return detailContents;
	}
	
	
}
