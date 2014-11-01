package com.wazapps.familybox.photos;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//a class that hold all photo albums data
@ParseClassName("Album")
public class Album extends ParseObject {

	public String getAlbumFamily() {
		return getString("family");
	}
	public String getAlbumName() {
		return getString("albumName");
	}

	public void setAlbumName(String albumName) {
		put("albumName", albumName);
	}

	public String getAlbumDate() {
		return getString("albumDate");
	}

	public void setAlbumDate(String albumDate) {
		put("albumDate", albumDate);
	}
	
	public String getAlbumDescription() {
		return getString("albumDescription");
	}

	public void setAlbumDescription(String albumDescription) {
		put("albumDescription", albumDescription);
	}
	
	public ParseFile getAlbumCover() {
		
		return (ParseFile) get("albumCover");
	}
	
    public static ParseQuery<Album> getQuery() {
        return ParseQuery.getQuery(Album.class);
    }
}
