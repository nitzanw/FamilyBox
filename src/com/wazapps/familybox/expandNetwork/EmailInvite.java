package com.wazapps.familybox.expandNetwork;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("EmailInvite")
public class EmailInvite extends ParseObject {
	public static final String NETWORK_ID = "networkId";
	public static final String EMAIL_ADDR = "emailAddress";
	public static final String INVITER_ID = "inviterId";
	
	public String getNetworkId() {
		return getString(NETWORK_ID);
	}
	
	public String getEmailAddress() {
		return getString(EMAIL_ADDR);
	}
	
	public String getInviterId() {
		return getString(INVITER_ID);
	}
	
	public void setNetworkId(String networkId) {
		put(NETWORK_ID, networkId);
	}
	
	public void setEmailAddress(String emailAddress) {
		put(EMAIL_ADDR, emailAddress);
	}
	
	public void setInviterId(String inviterId) {
		put(INVITER_ID, inviterId);
	}
	
	public static ParseQuery<EmailInvite> getQuery() {
        return ParseQuery.getQuery(EmailInvite.class);
    }
}
