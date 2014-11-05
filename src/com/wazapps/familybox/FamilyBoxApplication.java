package com.wazapps.familybox;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.wazapps.familybox.newsfeed.NewsItem;
import com.wazapps.familybox.photos.Album;
import com.wazapps.familybox.photos.PhotoItem_ex;
import com.wazapps.familybox.photos.ShareAlbum;

public class FamilyBoxApplication extends Application {
	private static final String appId = "hFLXtlIwku3PGYy0ezKYQf67sRCamG1IvNToz22q";
	private static final String clientKey = "klA7GiTnY25T6ou1aVwFdd4bPrsUBXArFVnBXIw3";
	
	public FamilyBoxApplication() {
		//required empty public constructor
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.enableLocalDatastore(getApplicationContext());
		ParseObject.registerSubclass(Album.class);
		ParseObject.registerSubclass(PhotoItem_ex.class);
		ParseObject.registerSubclass(NewsItem.class);
		ParseObject.registerSubclass(ShareAlbum.class);
		Parse.initialize(this, appId, clientKey);
	}
}
