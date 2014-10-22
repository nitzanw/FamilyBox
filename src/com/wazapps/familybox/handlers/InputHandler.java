package com.wazapps.familybox.handlers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parse.ParseUser;
import com.wazapps.familybox.misc.InputException;
import com.wazapps.familybox.profiles.FamilyMemberDetails2;

public class InputHandler {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String GENDER_MALE = "male";
	private static final String ROLE_PARENT = "parent";
	private static final String ROLE_CHILD = "child";
	
	private static final String RELATION_FATHER = "father";
	private static final String RELATION_MOTHER = "mother";
	private static final String RELATION_BROTHER = "brother";
	private static final String RELATION_SISTER = "sister";
	private static final String RELATION_HUSBAND = "husband";
	private static final String RELATION_WIFE = "wife";
	private static final String RELATION_SON = "son";
	private static final String RELATION_DAUGHTER = "daughter";
	
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

	
	public static String validateSignupInput(String firstName, String lastName, 
			String email, String birthday, String gender, String password, 
			String passwordConfirm) {
		
		if (firstName.matches("") || lastName.matches("") || email.matches("") 
				|| gender.matches("") || birthday.matches("") || password.matches("")) {
			return "You did not fill all required fields";
		}
		
		if (password.length() < 4) {
			return "password is too short. " +
					"must be at least 4 chars";
		}
		
		if (!password.equals(passwordConfirm)) {
			return "password does not match password confirm";
		}
		
		if (!validateEmailAddress(email)) {
			return "email address is invalid";
		}
		
		return "";
	}
	
	public static ArrayList<String> generateRelationOptions(
			ParseUser currentUser, 
			FamilyMemberDetails2 currentFamilyMemberDetails,
			boolean isFatherTaken, boolean isMotherTaken) {
		String userGender = currentUser.getString("gender");
		String familyMemberGender = currentFamilyMemberDetails.getGender();
		String familyMemberRole = currentFamilyMemberDetails.getRole();
		boolean isUserMale = (userGender.equals(GENDER_MALE))? true : false,
				isMemberMale = (familyMemberGender.equals(GENDER_MALE))? true : false;
		ArrayList<String> options = new ArrayList<String>();
		
		//if user is male
		if (isUserMale) {
			//if family member is male
			if (isMemberMale) {
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_FATHER);
				} 
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_BROTHER);
					if (!isFatherTaken) {						
						options.add(RELATION_SON);
					}
				} 
				
				//if family member role is undefined
				else {
					options.add(RELATION_BROTHER);
					if (!isFatherTaken) {
						options.add(RELATION_SON);
						options.add(RELATION_FATHER);						
					}
				}
			}
			
			//if family member is female
			else if (!isMemberMale) {	
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_MOTHER);
					if (!isFatherTaken) {
						options.add(RELATION_WIFE);						
					}
				} 
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_SISTER);
					if (!isFatherTaken) {
						options.add(RELATION_DAUGHTER);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(RELATION_SISTER);
					if (!isFatherTaken) {
						options.add(RELATION_DAUGHTER);
						if (!isMotherTaken) {
							options.add(RELATION_WIFE);							
						}
					}
					
					if (!isMotherTaken) {
						options.add(RELATION_MOTHER);
					}
				}
			}
		}
		
		//if user is female
		else if (!isUserMale) {
			//if family member is male
			if (isMemberMale) {
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_FATHER);
					if (!isMotherTaken) {
						options.add(RELATION_HUSBAND);
					}
				}
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_BROTHER);
					if (!isMotherTaken) {
						options.add(RELATION_SON);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(RELATION_BROTHER);
					if (!isFatherTaken) {
						options.add(RELATION_FATHER);
						if (!isMotherTaken) {
							options.add(RELATION_HUSBAND);
						}
					}
					
					if (!isMotherTaken) {
						options.add(RELATION_SON);
					}
				}
			}
			
			//if family member is also a female
			else if (!isMemberMale) {
				if (familyMemberRole.equals(ROLE_PARENT)) {
					options.add(RELATION_MOTHER);
				}
				
				else if (familyMemberRole.equals(ROLE_CHILD)) {
					options.add(RELATION_SISTER);
					if (!isMotherTaken) {
						options.add(RELATION_DAUGHTER);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(RELATION_SISTER);
					if (!isMotherTaken) {
						options.add(RELATION_MOTHER);
						options.add(RELATION_DAUGHTER);
					}
				}
			}			
		}
		
		return options;
	}
}
