package com.wazapps.familybox.newsfeed;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("NewsItem")
public class NewsItem extends ParseObject{
	private static final String NEWS_CONTENT = "content";
	private static final String NETWORK_ID = "networkId";
	private static final String USER = "user";
	private static final String USER_FIRST_NAME = "userFirstName";
	private static final String USER_LAST_NAME = "userLastName";
	
	
	public String getContent() {
		return getString(NEWS_CONTENT);
	}
	
	
	public String networkId() {
		return getString(NETWORK_ID);
	}
	
	public String getUserFirstName() {
		return getString(USER_FIRST_NAME);
	}
	
	public String getUserLastName() {
		return getString(USER_LAST_NAME);
	}
	
	public String getUserFullName() {
		return getString(USER_FIRST_NAME) + 
				" " + getString(USER_LAST_NAME);
	}
	
	public ParseUser getUser() {
		return getParseUser(USER);
	}
	
	public Date getDate() {
		return getCreatedAt();
	}
	
	public void setContent(String content) {
		put(NEWS_CONTENT, content);
	}
	
	public void setUser(ParseUser user) {
		put(USER, user);
	}
	
	public void setUserFirstName(String firstName) {
		put(USER_FIRST_NAME, firstName);
	}
	
	public void setUserLastName(String lastName) {
		put(USER_LAST_NAME, lastName);
	}
	
	public void setNetworkId(String networkId) {
		put(NETWORK_ID, networkId);
	}
	
	public static ParseQuery<NewsItem> getQuery() {
        return ParseQuery.getQuery(NewsItem.class);
    }
}
