package com.wazapps.familybox.profiles;

import java.util.Arrays;

import com.wazapps.familybox.photos.PhotoItem;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyMemberDetails implements Parcelable {
	private String userId, imageURI, firstName, lastName, role;
	ProfileDetails[] details;

	public static final Parcelable.Creator<FamilyMemberDetails> CREATOR = new Parcelable.Creator<FamilyMemberDetails>() {

		public FamilyMemberDetails createFromParcel(Parcel source) {
			return new FamilyMemberDetails(source);
		}

		public FamilyMemberDetails[] newArray(int size) {
			return new FamilyMemberDetails[size];
		}
	};

	public FamilyMemberDetails(String userId, String imageURI,
			String firstName, String lastName, String role,
			ProfileDetails[] details) {
		this.userId = userId;
		this.imageURI = imageURI;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.details = Arrays.copyOf(details, details.length,
				ProfileDetails[].class);
	}

	public FamilyMemberDetails(Parcel details) {
		this.userId = details.readString();
		this.imageURI = details.readString();
		this.firstName = details.readString();
		this.lastName = details.readString();
		this.role = details.readString();
		Parcelable[] parcelableArray = details
				.readParcelableArray(ProfileDetails.class.getClassLoader());
		this.details = null;
		if (parcelableArray != null) {
			this.details = Arrays.copyOf(parcelableArray,
					parcelableArray.length, ProfileDetails[].class);
		}
	}

	public ProfileDetails[] getDetails() {
		return this.details;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getImageURI() {
		return this.imageURI;
	}

	public String getName() {
		return this.firstName;
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
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.role);
		dest.writeParcelableArray(this.details, flags);
	}

	public String getLastName() {

		return lastName;
	}
}