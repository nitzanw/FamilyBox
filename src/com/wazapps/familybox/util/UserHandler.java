package com.wazapps.familybox.util;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

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
		user.put("network", 1); //TODO: replace with real network object ID
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
}
