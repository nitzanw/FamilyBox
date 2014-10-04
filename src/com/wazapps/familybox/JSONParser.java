package com.wazapps.familybox;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;

public class JSONParser {
	
	public static String loadJSONFromAsset(Activity activity, String filename) {
	    String json = null;
	    
	    try {
	        InputStream is = activity.getAssets().open(filename);
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        json = new String(buffer, "UTF-8");
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    
	    return json;
	}
}
