package com.wazapps.familybox.splashAndLogin;

import com.wazapps.familybox.R;
import com.wazapps.familybox.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class SplashActivity extends Activity{
	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 3; // Sleep for some time
	private static int SEC_FACTOR = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		overridePendingTransition(R.anim.enter, R.anim.exit); //TODO: handle transition animation in a better way
		// Start timer and launch main activity
		IntentLauncher appLaunch = new IntentLauncher();
		appLaunch.start();
	}

	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
				// delay splash screen
				Thread.sleep(SLEEP_TIME * SEC_FACTOR);

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			// Start main activity
			Intent intent = new Intent(SplashActivity.this,
					LoginActivity.class);

			startActivity(intent);
			finish();
		}
	}
}
