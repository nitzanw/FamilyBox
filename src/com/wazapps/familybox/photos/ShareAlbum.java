package com.wazapps.familybox.photos;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ShareAlbum")
public class ShareAlbum extends ParseObject{

	
	public String getAlbumOwner() {
		return getString("albumOwnerId");
	}
	
	public String getAlbumId() {
		return getString("albumId");
	}
	
	public String getSharedWithId() {
		return getString("sharedWithId");
	}
	public void setAlbumId(String albumId) {
		put("albumId", albumId);
	}
	public void setAlbumOwner(String albumOwnerId) {
		put("albumOwnerId", albumOwnerId);
	}
	public void setSharedWithId(String sharedWithId) {
		put("sharedWithId", sharedWithId);
	}
}
