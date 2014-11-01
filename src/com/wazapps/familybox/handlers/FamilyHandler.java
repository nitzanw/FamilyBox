package com.wazapps.familybox.handlers;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
	
	public static void getFamilyById(String familyId, GetCallback<ParseObject> callbackFunc) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(FAMILY_CLASS_NAME);
		query.getInBackground(familyId, callbackFunc);
	}
		
	public static void createNewFamilyForUser(ParseUser user,
			final boolean isCurrentFamily, ParseObject newFamily, 
			final SaveCallback callbackFunc) {
		user.fetchInBackground(new GetCallback<ParseUser>() {
			private ParseObject newFamily;
			
			@Override
			public void done(ParseUser user, ParseException e) {
				
				//if data fetching was successful
				if (e == null) {
					String familyName = (isCurrentFamily)?
							user.getString(UserHandler.LAST_NAME_KEY) :
							user.getString(UserHandler.PREV_LAST_NAME_KEY);
					newFamily.put(NAME_KEY, familyName);
					newFamily.put(NETWORK_KEY, user.getString(NETWORK_KEY));
					newFamily.put(UNDEFINED_KEY, user);
					newFamily.saveInBackground(new SaveCallback() {
						private ParseUser user;
						private ParseObject newFamily;
						
						@Override
						public void done(ParseException e) {
							if (e == null) {
								if (isCurrentFamily) {
									user.put(UserHandler.FAMILY_KEY, 
											newFamily.getObjectId());									
								} else {
									user.put(UserHandler.PREV_FAMILY_KEY, 
											newFamily.getObjectId());
								}
								
								if (isCurrentFamily) {
									user.put(UserHandler.PASS_QUERY_KEY, true);									
								}
								
								user.saveInBackground(callbackFunc);								
							} 
							
							else {
								callbackFunc.done(e);
							}
						}
						
						private SaveCallback init(ParseUser user, ParseObject newFamily) {
							this.user = user;
							this.newFamily = newFamily;
							return this;
						}
					}.init(user, newFamily));
				} 
				
				//if data fetching failed - cancel the sign up process
				else {
					callbackFunc.done(e);
				}	
			}
			
			private GetCallback<ParseUser> init(ParseObject newFamily) {
				this.newFamily = newFamily;
				return this;
			}
		}.init(newFamily));
	}
	
	public static void updateUsersAndFamilyRelation(ParseUser currentUser, 
			ParseUser currentFamilyMember, ParseObject currentFamily, 
			String relation, boolean isMemberUndefined, boolean isUserMale, 
			boolean isMemberMale, boolean isCurrentFamily,
			final SaveCallback callbackFunc) {
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
		
		if (isCurrentFamily) {
			currentUser.put(UserHandler.PASS_QUERY_KEY, true);
			currentUser.put(UserHandler.FAMILY_KEY, currentFamily.getObjectId());			
		} 
		
		else {
			currentUser.put(UserHandler.PREV_FAMILY_KEY, currentFamily.getObjectId());
		}
		
		currentFamily.saveInBackground(new SaveCallback() {
			private ParseUser currentUser;
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					if (currentUser.isDirty()) {
						currentUser.saveInBackground(callbackFunc);
					} else {
						callbackFunc.done(null);
					}
				} else {
					callbackFunc.done(e);
				}				
			}
			
			private SaveCallback init(ParseUser currentUser) {
				this.currentUser = currentUser;
				return this;
			}
		}.init(currentUser));
	}
}
