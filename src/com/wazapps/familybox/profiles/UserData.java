package com.wazapps.familybox.profiles;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.UserHandler;
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
			address, gender, quotes, status, famildId, prevFamilyId, email;
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
			String status, String familyId, String prevFamilyId, String email,
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
		this.status = status;
		this.famildId = familyId;
		this.prevFamilyId = prevFamilyId;
		this.email = email;
		
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
		this.status = details.readString();
		this.famildId = details.readString();
		this.prevFamilyId = details.readString();
		this.email = details.readString();
		
		int profilePicLength = details.readInt();
		if (profilePicLength > 0) {
			this.profilePic = new byte[profilePicLength];
			details.readByteArray(this.profilePic);
		} else {
			this.profilePic = null;
		}
	}
	
	/**
	 * Constructs a new UserData object from a ParseUser object.
	 * assumes that the ParseUser has all dats fetched, otherwise
	 * the results may be unexpected.
	 * 
	 * This constructor does not retrieve profile picture data, must
	 * be done manually through dedicated methods in this class
	 */
	public UserData(ParseUser user, String role) {
		this.userId = user.getObjectId();
		this.networkId = user.getString(UserHandler.NETWORK_KEY);
		this.firstName = user.getString(UserHandler.FIRST_NAME_KEY);
		this.lastName = user.getString(UserHandler.LAST_NAME_KEY);
		this.role = role;
		this.nickname = user.getString(UserHandler.NICKNAME_KEY);
		this.previousLastName = user.getString(UserHandler.PREV_LAST_NAME_KEY);
		this.middleName = user.getString(UserHandler.MIDDLE_NAME_KEY);
		this.phoneNumber = user.getString(UserHandler.PHONE_NUMBER_KEY);
		this.birthday = user.getString(UserHandler.BIRTHDATE_KEY);
		this.address = user.getString(UserHandler.ADDRESS_KEY);
		this.gender = user.getString(UserHandler.GENDER_KEY);
		this.quotes = user.getString(UserHandler.QUOTES_KEY);
		this.status = user.getString(UserHandler.STATUS_KEY);
		this.famildId = user.getString(UserHandler.FAMILY_KEY);
		this.prevFamilyId = user.getString(UserHandler.PREV_FAMILY_KEY);
		this.email = user.getString(UserHandler.EMAIL_KEY);
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
		dest.writeString(this.status);
		dest.writeString(this.famildId);
		dest.writeString(this.prevFamilyId);
		dest.writeString(this.email);
		
		if (this.profilePic != null) {
			dest.writeInt(this.profilePic.length);
			dest.writeByteArray(this.profilePic);			
		} else {
			dest.writeInt(0);
		}
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
	
	public String getStatus() {
		return status;
	}
	
	public String getFamilyId() {
		return famildId;
	}
	
	public String getPrevFamilyId() {
		return prevFamilyId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Bitmap getprofilePhoto() {
		Bitmap bitmap = null;
		if (this.profilePic != null) {
			bitmap = BitmapFactory.decodeByteArray(
					this.profilePic, 0, this.profilePic.length);			
		}
		
		return bitmap;
	}
	
	/**
	 * Returns the user's data in the format of profileDetails array
	 * this is used by the ProfileDetailsAdapter in the ProfileFragment
	 * (profile screen)
	 */
	public ProfileDetails[] getUserProfileDetails() {
		ArrayList<ProfileDetails> profileDetails = 
				new ArrayList<ProfileDetails>();
		profileDetails.add(new ProfileDetails("Details",""));
		if (hasProperty(email))
			profileDetails.add(new ProfileDetails("Email", email));
		
		if (hasProperty(nickname))
			profileDetails.add(new ProfileDetails("Nickname", 
					InputHandler.capitalizeName(nickname)));
		
		if (hasProperty(previousLastName))
			profileDetails.add(new ProfileDetails("Previous family name", 
					InputHandler.capitalizeName(previousLastName)));
		
		if (hasProperty(middleName))
			profileDetails.add(new ProfileDetails("Middle name", 
					InputHandler.capitalizeName(middleName)));
		
		if (hasProperty(phoneNumber))
			profileDetails.add(new ProfileDetails("Phone number", phoneNumber));
		
		if (hasProperty(birthday))
			profileDetails.add(new ProfileDetails("Birthdate", birthday));
		
		if (hasProperty(address))
			profileDetails.add(new ProfileDetails("Address", address));
		
		if (hasProperty(gender))
			profileDetails.add(new ProfileDetails("Gender", 
					InputHandler.capitalizeName(gender)));
		
		if (hasProperty(quotes))
			profileDetails.add(new ProfileDetails("Quote", quotes));
		
		return profileDetails.toArray(
				new ProfileDetails[profileDetails.size()]);
	}
	
	
	public void setProfilePic(byte[] profilePic) {
		this.profilePic = Arrays.copyOf(profilePic, profilePic.length);
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	private boolean hasProperty(String property) {
		return (!property.equals(""));
	}
}
