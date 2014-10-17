package com.wazapps.familybox.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wazapps.familybox.misc.InputException;

public class InputValidator {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static boolean validateEmailAddress(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
	public static void validateLoginInput(String email, String password) throws InputException {
		if (email.matches("") || password.matches("")) {
			throw new InputException("You did not fill all required fields");
		}
		
		if (!validateEmailAddress(email)) {
			throw new InputException("email address is invalid");
		}
	}

	
	public static void validateSignupInput(String firstName, String lastName, 
			String email, String birthday, String gender, String password, 
			String passwordConfirm) throws InputException {
		
		if (firstName.matches("") || lastName.matches("") || email.matches("") 
				|| gender.matches("") || birthday.matches("") || password.matches("")) {
			throw new InputException("You did not fill all required fields");
		}
		
		if (password.length() < 4) {
			throw new InputException("password is too short. " +
					"must be at least 4 characters");
		}
		
		if (!password.equals(passwordConfirm)) {
			throw new InputException("Password field does not match password" +
					" confirm field\n" + "please make sure that you enter " +
					"the same password twice");
		}
		
		if (!validateEmailAddress(email)) {
			throw new InputException("email address is invalid");
		}
	}
}
