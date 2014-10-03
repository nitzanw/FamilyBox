package com.wazapps.familybox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class SplashActivity extends Activity{

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 3; // Sleep for some time




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE); 
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.activity_splash);



		// Start timer and launch main activity
		IntentLauncher launcher1 = new IntentLauncher();

		launcher1.start();

		// }
	}

	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
				// Sleeping
				Thread.sleep(SLEEP_TIME * 1000);
				// wait until the ingredient list is loaded

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
