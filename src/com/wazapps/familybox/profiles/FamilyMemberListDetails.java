package com.wazapps.familybox.profiles;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyMemberListDetails implements Parcelable{
	private String userId, imageURI, name, role;
	
	public static final Parcelable.Creator<FamilyMemberListDetails> CREATOR = 
			new Parcelable.Creator<FamilyMemberListDetails>() {
		
		public FamilyMemberListDetails createFromParcel(Parcel source) {
			return new FamilyMemberListDetails(source);
		}

		public FamilyMemberListDetails[] newArray(int size) {
			return new FamilyMemberListDetails[size];
		}
	};
	
	public FamilyMemberListDetails(String userId, String imageURI, 
			String name, String role) {
		this.userId = userId;
		this.imageURI = imageURI;
		this.name = name;
		this.role = role;
	}
	
	public FamilyMemberListDetails(Parcel details) {
		this.userId = details.readString();
		this.imageURI = details.readString();
		this.name = details.readString();
		this.role = details.readString();
	}

	public String getUserId() {
		return this.userId;
	}

	public String getImageURI() {
		return this.imageURI;
	}

	public String getName() {
		return this.name;
	}

	public String getRole() {
		return this.role;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.userId);
		dest.writeString(this.imageURI);
		dest.writeString(this.name);
		dest.writeString(this.role);
	}
}
