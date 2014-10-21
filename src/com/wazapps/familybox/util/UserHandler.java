package com.wazapps.familybox.util;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.wazapps.familybox.profiles.FamilyMemberDetails2;

public class UserHandler {
	public static boolean checkIfUserLogged() {
		return true;
	}
	
	public static ParseUser createNewUser(String firstName, String lastName, 
			String email, String gender, String password, 
			String birthday, byte[] profilePictureData, 
			String profilePictureName) throws ParseException {
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
			profilePic.save();
			user.put("profilePic", profilePic);			
		}
		
		user.signUp();
		return user;
	}
	
	public static void fetchFamilyMembers(ArrayList<ParseUser> familyMembers, 
			ArrayList<FamilyMemberDetails2> familyMembersDetails, 
			ParseObject family) throws ParseException {
		family.fetchIfNeeded();
		if (family.has("undefinedFamilyMember")) {
			ParseUser undef = family.getParseUser("undefinedFamilyMember");
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
			ParseRelation<ParseUser> children = family.getRelation("children");
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
	
	public static ArrayList<ParseUser> getFamilyMembers(ParseObject family) 
			throws ParseException {
		ArrayList<ParseUser> familyMembers = new ArrayList<ParseUser>();
		family.fetchIfNeeded();
		if (family.has("undefinedFamilyMember")) {
			familyMembers.add(family.getParseUser("undefinedFamilyMember"));
		}
		
		if (family.has("father")) {
			familyMembers.add(family.getParseUser("father"));
		}
		
		if (family.has("mother")) {
			familyMembers.add(family.getParseUser("mother"));
		}
		
		if (family.has("children")) {
			ParseRelation<ParseUser> children = family.getRelation("children");
			List<ParseUser> childrenList = children.getQuery().find();
			for (ParseUser child : childrenList) {
				familyMembers.add(child);
			}
		}
		
		return familyMembers;
	}
}
