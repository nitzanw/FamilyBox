package com.wazapps.familybox.splashAndLogin;

import com.wazapps.familybox.MainActivity;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.WaveDrawable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class SplashActivity extends Activity{
	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 4; // Sleep for some time
	private static int SEC_FACTOR = 1000;
	
	public static final String SPLASH_ACTION = "splash";
	public static final String HANDLE_QUERY = "handle query";
	public static final String USER_LOGGED = "user logged";
	
	private WaveDrawable waveDrawable;
	private ImageView welcomeImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		overridePendingTransition(R.anim.enter, R.anim.exit);
		initAnimation();
		
		// Start timer and launch main activity
		IntentLauncher appLaunch = new IntentLauncher();
		appLaunch.start();
	}
	
	private void initAnimation() {
		welcomeImage = (ImageView) findViewById(R.id.welcome_elipse);
		Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
		pulse.setInterpolator(new AccelerateInterpolator());
		welcomeImage.startAnimation(pulse);
		waveDrawable = new WaveDrawable(Color.parseColor("#fbfbfb"), 550);
		welcomeImage.setBackgroundDrawable(waveDrawable);
		Interpolator interpolator = new AccelerateDecelerateInterpolator();
		waveDrawable.setWaveInterpolator(interpolator);
		waveDrawable.startAnimation();
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
			
			ParseUser currUser = ParseUser.getCurrentUser();
			
			//if user is logged in - get to main screen or to family query
			if (currUser != null) {
				try {
					currUser.fetchIfNeeded();
					//TODO: pin data to local datastore
					
					boolean passedQuery = 
							currUser.getBoolean(UserHandler.PASS_QUERY_KEY);
					
					if (passedQuery) {
						launchMainActivity();
					} else {
						launchLoginActivity(true);
					}
				} 
				
				catch (ParseException e) {
					UserHandler.userLogout();
					launchLoginActivity(false);
				}
			} 
			
			//user is not logged in, enter login screen
			else {
				launchLoginActivity(false);
			}	
			
			finish();
		}
		
		private void launchLoginActivity(boolean handleQuery) {
			Intent intent = new Intent(SplashActivity.this,
					LoginActivity.class);
			intent.putExtra(SPLASH_ACTION, SPLASH_ACTION);
			intent.putExtra(HANDLE_QUERY, handleQuery);
			startActivity(intent);
		}
		
		private void launchMainActivity() {
			Intent intent = new Intent(SplashActivity.this, 
					MainActivity.class);
			startActivity(intent);
		}
	}
}
