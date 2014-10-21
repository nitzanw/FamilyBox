package com.wazapps.familybox.profiles;

import java.util.Arrays;

import com.parse.ParseUser;
import com.wazapps.familybox.photos.PhotoItem;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyMemberDetails2 implements Parcelable {
	private String userId, networkId, imageURI, firstName, lastName, role, nickname,
			previousLastName, middleName, phoneNumber, birthday, address, gender, quotes;

	public static final Parcelable.Creator<FamilyMemberDetails> CREATOR = new Parcelable.Creator<FamilyMemberDetails>() {

		public FamilyMemberDetails createFromParcel(Parcel source) {
			return new FamilyMemberDetails(source);
		}

		public FamilyMemberDetails[] newArray(int size) {
			return new FamilyMemberDetails[size];
		}
	};

	public FamilyMemberDetails2(String userId, String networkId, String imageURI,
			String firstName, String lastName, String role, String nickname,
			String previousLastName, String middleName, String phoneNumber,
			String birthday, String address, String gender, String quotes) {
		this.userId = userId;
		this.networkId = networkId;
		this.imageURI = imageURI;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.nickname = nickname;
		this.previousLastName = previousLastName;
		this.middleName = middleName;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.address = address;
		this.gender = gender;
		this.quotes = quotes;
	}

	public FamilyMemberDetails2(Parcel details) {
		this.userId = details.readString();
		this.networkId = details.readString();
		this.imageURI = details.readString();
		this.firstName = details.readString();
		this.lastName = details.readString();
		this.role = details.readString();
		this.nickname = details.readString();
		this.previousLastName = details.readString();
		this.middleName = details.readString();
		this.phoneNumber = details.readString();
		this.birthday = details.readString();
		this.address = details.readString();
		this.gender = details.readString();
		this.quotes = details.readString();
	}
	
	public FamilyMemberDetails2(ParseUser user, String role) {
		this.userId = user.getObjectId();
		this.networkId = user.getString("network");
		this.firstName = user.getString("firstName");
		this.lastName = user.getString("lastName");
		this.role = role;
		this.nickname = user.getString("nickname");
		this.previousLastName = user.getString("prevLastName");
		this.middleName = user.getString("middleName");
		this.phoneNumber = user.getString("phoneNumber");
		this.birthday = user.getString("birthdate");
		this.address = user.getString("address");
		this.gender = user.getString("gender");
		this.quotes = user.getString("quotes");
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
		dest.writeString(this.networkId);
		dest.writeString(this.imageURI);
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.role);
		dest.writeString(this.nickname);
		dest.writeString(this.previousLastName);
		dest.writeString(this.middleName);
		dest.writeString(this.phoneNumber);
		dest.writeString(this.birthday);
		dest.writeString(this.address);
		dest.writeString(this.gender);
		dest.writeString(this.quotes);
	}

	public String getLastName() {
		return lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPreviousLastName() {
		return previousLastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getAddress() {
		return address;
	}
	
	public String getNetworkId() {
		return networkId;
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getQuotes() {
		return quotes;
	}

	//TODO: remove this
	public void setRole(String role) {
		this.role = role;
	}
}
