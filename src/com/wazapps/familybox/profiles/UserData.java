package com.wazapps.familybox.profiles;

import java.util.Arrays;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.util.LogUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {
	public static abstract class DownloadCallback {
		public abstract void done(ParseException e);
	}
	
	public static String ROLE_UNDEFINED = "undefined";
	public static String ROLE_PARENT = "parent";
	public static String ROLE_CHILD = "child";
	
	private String userId, networkId, firstName, lastName, role, nickname,
			previousLastName, middleName, phoneNumber, birthday, 
			address, gender, quotes;
	private byte[] profilePic;

	public static final Parcelable.Creator<FamilyMemberDetails> CREATOR = 
			new Parcelable.Creator<FamilyMemberDetails>() {

		public FamilyMemberDetails createFromParcel(Parcel source) {
			return new FamilyMemberDetails(source);
		}

		public FamilyMemberDetails[] newArray(int size) {
			return new FamilyMemberDetails[size];
		}
	};

	public UserData(String userId, String networkId,
			String firstName, String lastName, String role, String nickname,
			String previousLastName, String middleName, String phoneNumber,
			String birthday, String address, String gender, String quotes, 
			byte[] profilePic) {
		this.userId = userId;
		this.networkId = networkId;
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
		if (profilePic != null) {
			this.profilePic = Arrays.copyOf(profilePic, profilePic.length);			
		} else {
			this.profilePic = null;
		}
	}

	public UserData(Parcel details) {
		this.userId = details.readString();
		this.networkId = details.readString();
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
		int profilePicLength = details.readInt();
		if (profilePicLength > 0) {
			this.profilePic = new byte[profilePicLength];
			details.readByteArray(this.profilePic);
		} else {
			this.profilePic = null;
		}
	}
	
	public UserData(ParseUser user, String role) {
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
		this.profilePic = null; //set individualy through dedicated function
	}
	
	public void downloadProfilePicAsync(ParseUser user, 
			DownloadCallback callbackFunc) {
		if (!user.has(UserHandler.PROFILE_PICTURE_KEY)) {
			this.profilePic = null;
			callbackFunc.done(null);
			return;
		}
		
		ParseFile profilePic = (ParseFile) 
				user.get(UserHandler.PROFILE_PICTURE_KEY);
		
		profilePic.getDataInBackground(new GetDataCallback() {
			UserData userDetails;
			DownloadCallback callbackFunc;
			
			@Override
			public void done(byte[] data, ParseException e) {
				if (e == null) {
					this.userDetails.setProfilePic(data);
				} else {
					LogUtils.logError("FamilyMemberDetails", e.getMessage());
				}
				
				this.callbackFunc.done(e);
			}
			
			private GetDataCallback init(UserData userDetails,
					DownloadCallback callbackFunc) {
				this.userDetails = userDetails;
				this.callbackFunc = callbackFunc;
				return this;
			}
		}.init(this, callbackFunc));
	}
	
	public boolean downloadProfilePicSync(ParseUser user) {
		if (!user.has(UserHandler.PROFILE_PICTURE_KEY)) {
			return false;
		}
		
		ParseFile profilePic = (ParseFile) 
				user.get(UserHandler.PROFILE_PICTURE_KEY);
		try {
			this.profilePic = profilePic.getData();
		} 
		
		catch (ParseException e) {
			LogUtils.logError("FamilyMemberDetails", e.getMessage());
			this.profilePic = null;
			return false;
		}
		
		return true;
	}

	public String getUserId() {
		return this.userId;
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
		if (this.profilePic != null) {
			dest.writeInt(this.profilePic.length);
			dest.writeByteArray(this.profilePic);			
		} else {
			dest.writeInt(0);
		}
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
	
	public Bitmap getprofilePhoto() {
		Bitmap bitmap = null;
		if (this.profilePic != null) {
			bitmap = BitmapFactory.decodeByteArray(
					this.profilePic, 0, this.profilePic.length);			
		}
		
		return bitmap;
	}
	
	public void setProfilePic(byte[] profilePic) {
		this.profilePic = Arrays.copyOf(profilePic, profilePic.length);
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
