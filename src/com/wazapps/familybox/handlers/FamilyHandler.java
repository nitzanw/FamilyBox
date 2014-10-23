package com.wazapps.familybox.handlers;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wazapps.familybox.splashAndLogin.LoginActivity;
import com.wazapps.familybox.util.LogUtils;

public class FamilyHandler {
	public static final String FAMILY_CLASS_NAME = "Family";
	public static final String NAME_KEY = "name";
	public static final String NETWORK_KEY = "network";
	public static final String GENDER_KEY = "gender";
	public static final String CHILDREN_KEY = "children";
	public static final String UNDEFINED_KEY = "undefined";
	public static final String FATHER_KEY = "father";
	public static final String MOTHER_KEY = "mother";
	
	public static final String RELATION_FATHER = "father";
	public static final String RELATION_MOTHER = "mother";
	public static final String RELATION_HUSBAND = "husband";
	public static final String RELATION_WIFE = "wife";
	public static final String RELATION_SON = "son";
	public static final String RELATION_DAUGHTER = "daughter";
	public static final String RELATION_BROTHER = "brother";
	public static final String RELATION_SISTER = "sister";
	
	public static void fetchRelatedFamilies(String familyName, String networkId, 
			FindCallback<ParseObject> callbackFunc) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(FAMILY_CLASS_NAME);
		query.whereEqualTo(NAME_KEY, familyName);
		query.whereEqualTo(NETWORK_KEY, networkId);
		query.findInBackground(callbackFunc);
	}
		
	public static void createNewFamilyForUser(ParseUser user, 
			final LoginActivity activity, final SaveCallback callbackFunc) {
		user.fetchInBackground(new GetCallback<ParseUser>() {

			@Override
			public void done(ParseUser user, ParseException e) {
				//if data fetching was successful
				if (e == null) {
					String familyName = user.getString(UserHandler.LAST_NAME_KEY);
					ParseObject newFamily = new ParseObject(FAMILY_CLASS_NAME);
					newFamily.put(NAME_KEY, familyName);
					newFamily.put(NETWORK_KEY, user.getString(NETWORK_KEY));
					newFamily.put(UNDEFINED_KEY, user);
					user.put(UserHandler.FAMILY_KEY, newFamily);
					user.put(UserHandler.PASS_QUERY_KEY, true);
					user.saveInBackground(callbackFunc);
				} 
				
				//if data fetching failed - cancel the sign up process
				else {
					activity.handleUserCreationError(e, true);
				}	
			}
		});
	}
	
	public static void updateUsersAndFamilyRelation(ParseUser currentUser, 
			ParseUser currentFamilyMember, ParseObject currentFamily, 
			String relation, boolean isMemberUndefined, boolean isUserMale, 
			boolean isMemberMale, SaveCallback callbackFunc) {
		if (relation.equals(RELATION_FATHER) 
				|| relation.equals(RELATION_MOTHER)) {
			ParseRelation<ParseUser> children = 
					currentFamily.getRelation(CHILDREN_KEY);
			children.add(currentUser);
			if (isMemberUndefined) {
				currentFamily.put(relation, currentFamilyMember);
				currentFamily.remove(UNDEFINED_KEY);
			}
		} 
		
		else if (relation.equals(RELATION_HUSBAND) 
				|| relation.equals(RELATION_WIFE)) {
			if (isUserMale) {
				currentFamily.put(RELATION_FATHER, currentUser);
			} else {
				currentFamily.put(RELATION_MOTHER, currentUser);
			}
			
			if (isMemberUndefined) {
				if (isMemberMale) {
					currentFamily.put(RELATION_FATHER, currentFamilyMember);					
				} else {
					currentFamily.put(RELATION_MOTHER, currentFamilyMember);
				}
				currentFamily.remove(UNDEFINED_KEY);
			}
		}
		
		else if (relation.equals(RELATION_SON) 
				|| relation.equals(RELATION_DAUGHTER)) {
			if (isUserMale) {
				currentFamily.put(RELATION_FATHER, currentUser);
			} else {
				currentFamily.put(RELATION_MOTHER, currentUser);
			}
			
			if (isMemberUndefined) {
				ParseRelation<ParseUser> children = 
						currentFamily.getRelation(CHILDREN_KEY);
				children.add(currentFamilyMember);
				currentFamily.remove(UNDEFINED_KEY);
			}
		}
		
		//if the user and family member are brothers\sisters:
		else {
			ParseRelation<ParseUser> children = 
					currentFamily.getRelation(CHILDREN_KEY);
			children.add(currentUser);
			if (isMemberUndefined) {
				children.add(currentFamilyMember);
				currentFamily.remove(UNDEFINED_KEY);
			}
		}
		
		currentUser.put(UserHandler.PASS_QUERY_KEY, true);
		currentUser.put(UserHandler.FAMILY_KEY, currentFamily);
		currentUser.saveInBackground(callbackFunc);
	}
}
