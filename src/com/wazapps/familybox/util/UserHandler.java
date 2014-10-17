package com.wazapps.familybox.util;

import com.parse.ParseException;
import com.parse.ParseUser;

public class UserHandler {
	public static boolean checkIfUserLogged() {
		return true;
	}
	
	public static ParseUser createNewUser(String firstName, String lastName, 
			String email, String password, String birthday) throws ParseException {
		ParseUser user = new ParseUser();
		user.setUsername("fb_" + email);
		user.setPassword(password);
		user.setEmail(email);
		user.put("firstName", firstName);
		user.put("lastName", lastName);
		user.put("birthdate", birthday);
		user.put("sex", "m");
		user.put("address", "");
		user.put("middleName", "");
		user.put("prevLastName", "");
		user.put("network", 1); //TODO: replace with real network object ID
		user.signUp();
		return user;
	}
}
