package com.wazapps.familybox.handlers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parse.ParseUser;
import com.wazapps.familybox.profiles.UserData;

public class InputHandler {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static boolean validateEmailAddress(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
	public static String validateLoginInput(String email, String password) {
		if (email.matches("") || password.matches("")) {
			return "You did not fill all required fields";
		}
		
		if (!validateEmailAddress(email)) {
			return "email address is invalid";
		}
		
		return "";
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
	
	public static String capitalizeName(String name) {
		if (name.length() > 1) {
			return name.substring(0, 1).toUpperCase() + name.substring(1);			
		}
		
		return name;
	}
	
	public static String capitalizeFullname(String firstName, String lastName) {
		if (firstName.length() > 1) {
			firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);	
		}
		
		if (lastName.length() > 1) {
			lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);	
		}
		
		return firstName + " " + lastName;
	}
	
	public static ArrayList<String> generateRelationOptions(
			ParseUser currentUser, 
			UserData currentFamilyMemberDetails,
			boolean isFatherTaken, boolean isMotherTaken) {
		String userGender = currentUser.getString(UserHandler.GENDER_KEY);
		String familyMemberGender = currentFamilyMemberDetails.getGender();
		String familyMemberRole = currentFamilyMemberDetails.getRole();
		boolean isUserMale = 
				(userGender.equals(UserHandler.GENDER_MALE))? true : false;
		boolean	isMemberMale = 
				(familyMemberGender.equals(UserHandler.GENDER_MALE))? true : false;
		ArrayList<String> options = new ArrayList<String>();
		
		//if user is male
		if (isUserMale) {
			//if family member is male
			if (isMemberMale) {
				if (familyMemberRole.equals(UserData.ROLE_PARENT)) {
					options.add(FamilyHandler.RELATION_FATHER);
				} 
				
				else if (familyMemberRole.equals(UserData.ROLE_CHILD)) {
					options.add(FamilyHandler.RELATION_BROTHER);
					if (!isFatherTaken) {						
						options.add(FamilyHandler.RELATION_SON);
					}
				} 
				
				//if family member role is undefined
				else {
					options.add(FamilyHandler.RELATION_BROTHER);
					if (!isFatherTaken) {
						options.add(FamilyHandler.RELATION_SON);
						options.add(FamilyHandler.RELATION_FATHER);						
					}
				}
			}
			
			//if family member is female
			else if (!isMemberMale) {	
				if (familyMemberRole.equals(UserData.ROLE_PARENT)) {
					options.add(FamilyHandler.RELATION_MOTHER);
					if (!isFatherTaken) {
						options.add(FamilyHandler.RELATION_WIFE);						
					}
				} 
				
				else if (familyMemberRole.equals(UserData.ROLE_CHILD)) {
					options.add(FamilyHandler.RELATION_SISTER);
					if (!isFatherTaken) {
						options.add(FamilyHandler.RELATION_DAUGHTER);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(FamilyHandler.RELATION_SISTER);
					if (!isFatherTaken) {
						options.add(FamilyHandler.RELATION_DAUGHTER);
						if (!isMotherTaken) {
							options.add(FamilyHandler.RELATION_WIFE);							
						}
					}
					
					if (!isMotherTaken) {
						options.add(FamilyHandler.RELATION_MOTHER);
					}
				}
			}
		}
		
		//if user is female
		else if (!isUserMale) {
			//if family member is male
			if (isMemberMale) {
				if (familyMemberRole.equals(UserData.ROLE_PARENT)) {
					options.add(FamilyHandler.RELATION_FATHER);
					if (!isMotherTaken) {
						options.add(FamilyHandler.RELATION_HUSBAND);
					}
				}
				
				else if (familyMemberRole.equals(UserData.ROLE_CHILD)) {
					options.add(FamilyHandler.RELATION_BROTHER);
					if (!isMotherTaken) {
						options.add(FamilyHandler.RELATION_SON);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(FamilyHandler.RELATION_BROTHER);
					if (!isFatherTaken) {
						options.add(FamilyHandler.RELATION_FATHER);
						if (!isMotherTaken) {
							options.add(FamilyHandler.RELATION_HUSBAND);
						}
					}
					
					if (!isMotherTaken) {
						options.add(FamilyHandler.RELATION_SON);
					}
				}
			}
			
			//if family member is also a female
			else if (!isMemberMale) {
				if (familyMemberRole.equals(UserData.ROLE_PARENT)) {
					options.add(FamilyHandler.RELATION_MOTHER);
				}
				
				else if (familyMemberRole.equals(UserData.ROLE_CHILD)) {
					options.add(FamilyHandler.RELATION_SISTER);
					if (!isMotherTaken) {
						options.add(FamilyHandler.RELATION_DAUGHTER);
					}
				}
				
				//if family member role is undefined
				else {
					options.add(FamilyHandler.RELATION_SISTER);
					if (!isMotherTaken) {
						options.add(FamilyHandler.RELATION_MOTHER);
						options.add(FamilyHandler.RELATION_DAUGHTER);
					}
				}
			}			
		}
		
		return options;
	}
}
