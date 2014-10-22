package com.wazapps.familybox.util;

import android.util.Log;

public class LogUtils {
	private static final String FAMILY_BOX = "FamilyBox";

	@Deprecated
	public static void logTemp(String className, String msg) {
		Log.d(FAMILY_BOX, "***" + className + " *** " + msg);
	}

	@Deprecated
	public static void logTemp(String className, int value) {
		Log.d(FAMILY_BOX, "***" + className + " *** " + value);
	}
	
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
