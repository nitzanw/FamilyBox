package com.wazapps.familybox.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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

public class FamilyHandler {
	public static void fetchRelatedFamilies(String familyName, String networkId, 
			FindCallback<ParseObject> callbackFunc) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
		query.whereEqualTo("name", familyName);
		query.whereEqualTo("network", networkId);
		query.findInBackground(callbackFunc);
	}
		
	public static void createNewFamilyForUser(ParseUser user, 
			final Activity activity, final SaveCallback callbackFunc) {
		user.fetchInBackground(new GetCallback<ParseUser>() {

			@Override
			public void done(ParseUser user, ParseException e) {
				//if data fetching was successful
				if (e == null) {
					String familyName = user.getString("lastName");
					ParseObject newFamily = new ParseObject("Family");
					newFamily.put("name", familyName);
					newFamily.put("network", user.getString("network"));
					newFamily.put("undefined", user);
					user.put("family", newFamily);
					user.put("passFamilyQuery", true);
					user.saveInBackground(callbackFunc);
				} 
				
				//if data fetching failed - cancel the sign up process
				else {
					Toast toast = Toast.makeText(activity, 
							"Error in user creation, please sign up again", 
							Toast.LENGTH_LONG);
					LogUtils.logError("FamilyHandler", e.getMessage());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					ParseUser.logOut();
					user.deleteInBackground();
				}
				
			}
		});
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
