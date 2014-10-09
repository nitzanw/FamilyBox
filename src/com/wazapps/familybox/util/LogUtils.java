package com.wazapps.familybox.util;

import android.util.Log;

public class LogUtils {
	private static final String FAMILY_BOX = "FamilyBox";

	public static void logDebug(String className, String msg) {
		Log.d(FAMILY_BOX, ">>>" + className + " >>> " + msg);
	}
	
	public static void logWarning(String className, String msg) {
		Log.w(FAMILY_BOX, "@@@" + className + " @@@ " + msg);
	}
	
	public static void logError(String className, String msg) {
		Log.e(FAMILY_BOX, "!!!" + className + " !!! " + msg);
	}
}
