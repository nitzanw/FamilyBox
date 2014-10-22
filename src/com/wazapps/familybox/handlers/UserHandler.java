package com.wazapps.familybox.handlers;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.wazapps.familybox.profiles.FamilyMemberDetails2;

public class UserHandler {
	
	public static abstract class FamilyMembersFetchCallback {
		public abstract void done(ParseException e);
	}
	
	private class AsyncFamilyMemberFetch extends 
	AsyncTask<Void, Void, ParseException> {
		private ArrayList<ParseUser> familyMembers; 
		private ArrayList<FamilyMemberDetails2> familyMembersDetails;
		private ParseObject family;
		private FamilyMembersFetchCallback callbackFunc;
		
		public AsyncFamilyMemberFetch(ArrayList<ParseUser> familyMembers, 
				ArrayList<FamilyMemberDetails2> familyMembersDetails, 
				ParseObject family, FamilyMembersFetchCallback callbackFunc) {
			this.familyMembers = familyMembers;
			this.familyMembersDetails = familyMembersDetails;
			this.family = family;
			this.callbackFunc = callbackFunc;
		}
		
		@Override
		protected ParseException doInBackground(Void... params) {
			try {
				family.fetchIfNeeded();
				
				if (family.has("undefined")) {
					ParseUser undef = family.getParseUser("undefined");
					undef.fetchIfNeeded();
					FamilyMemberDetails2 memberDetails = 
							new FamilyMemberDetails2(undef, "undefined");
					
					familyMembers.add(undef);
					familyMembersDetails.add(memberDetails);
				}
				
				if (family.has("father")) {
					ParseUser father = family.getParseUser("father");
					father.fetchIfNeeded();
					FamilyMemberDetails2 memberDetails = 
							new FamilyMemberDetails2(father, "parent");
					
					familyMembers.add(father);
					familyMembersDetails.add(memberDetails);
				}
				
				if (family.has("mother")) {
					ParseUser mother = family.getParseUser("mother");
					mother.fetchIfNeeded();
					FamilyMemberDetails2 memberDetails = 
							new FamilyMemberDetails2(mother, "parent");
					
					familyMembers.add(mother);
					familyMembersDetails.add(memberDetails);
				}
				
				if (family.has("children")) {
					ParseRelation<ParseUser> children = 
							family.getRelation("children");
					List<ParseUser> childrenList = children.getQuery().find();
					for (ParseUser child : childrenList) {
						child.fetchIfNeeded();
						FamilyMemberDetails2 memberDetails = 
								new FamilyMemberDetails2(child, "child");
						
						familyMembers.add(child);
						familyMembersDetails.add(memberDetails);
					}
				}
			} 
			
			catch (ParseException e) {
				return e;
			}
			
			return null;
		}
		
		protected void onPostExecute(ParseException e) {
			callbackFunc.done(e);
	    }
	}
	
	public ParseUser createNewUser(String firstName, String lastName, 
			String email, String gender, String password, 
			String birthday, byte[] profilePictureData, 
			String profilePictureName, SignUpCallback callbackFunc) {
		
		ParseUser user = new ParseUser();
		user.setUsername("fb_" + email);
		user.setPassword(password);
		user.setEmail(email);
		user.put("firstName", firstName);
		user.put("lastName", lastName);
		user.put("birthdate", birthday);
		user.put("gender", gender);
		user.put("address", "");
		user.put("middleName", "");
		user.put("prevLastName", "");
		user.put("nickname", "");
		user.put("phoneNumber", "");
		user.put("network", "1"); //TODO: replace with real network object ID
		user.put("quotes", "");
		user.put("passFamilyQuery", false);
		
		//upload profile picture to server and link it to user
		if (!(profilePictureName.equals("")) && (profilePictureData != null)) {
			ParseFile profilePic = new ParseFile(profilePictureName, 
					profilePictureData);
			user.put("profilePic", profilePic);			
//			profilePic.saveInBackground(); //TODO: maybe handle exceptions?
		}
		
		user.signUpInBackground(callbackFunc);
		return user;
	}
	
	public void fetchFamilyMembers(ArrayList<ParseUser> familyMembers, 
			ArrayList<FamilyMemberDetails2> familyMembersDetails, 
			ParseObject family, FamilyMembersFetchCallback callbackFunc) {
		new AsyncFamilyMemberFetch(familyMembers, 
				familyMembersDetails, family, callbackFunc).execute();
	}
}
