package com.wazapps.familybox.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class FamilyHandler {
	/**
	 * Finds related families to a newly created parseUser. 
	 */
	public static ArrayList<ParseObject> getRelatedFamilies(int networkId, 
			String familyName) throws ParseException {
		ArrayList<ParseObject> familiesList = null;
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
		query.whereEqualTo("name", familyName);
		query.whereEqualTo("network", networkId);
		familiesList = new ArrayList<ParseObject>(query.find());
		return familiesList;
	}
	
	public static void createNewFamilyForUser(ParseUser user) 
			throws ParseException {
		String familyName = user.getString("lastName");
		ParseObject newFamily = new ParseObject("Family");
		newFamily.put("name", familyName);
		//TODO: replace with real network object ID
		newFamily.put("network", user.getInt("network"));
		//by default we assume that the user who created the new family
		//is one of the parents
		if (user.getString("gender").equals("male")) {
			newFamily.put("father", user);
		} else {
			newFamily.put("mother", user);
		}
		
		user.put("family", newFamily);
		user.save();
	}
	
	public static void createNewPrevFamilyForUser(ParseUser user) throws ParseException {
		String prevFamilyName = user.getString("prevLastName");
		ParseObject prevFamily = new ParseObject("Family");
		prevFamily.put("name", prevFamilyName);
		//TODO: replace with real network object ID
		prevFamily.put("network", user.getString("network"));
		//by default we assume that the user who created the family 
		//is one of the children
		ParseRelation<ParseUser> children = prevFamily.getRelation("children");
		children.add(user);
		user.put("prevFamily", prevFamily);
		prevFamily.save();
	}
}
