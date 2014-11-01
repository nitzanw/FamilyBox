package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.photos.AddAlbumFragment.AddAlbumScreenCallback;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.DateChooserCallback;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class AddAlbumScreenActivity extends AbstractScreenActivity implements
		AddAlbumScreenCallback, DateChooserCallback {
	private static final String TAG_ALBUM_DATE = "add album date";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, new AddAlbumFragment(),
						AddAlbumFragment.ADD_ALBUM_FRAG).commit();
	}

	@Override
	public void openDateInputDialog() {

		BirthdaySignupDialogFragment dialog = new BirthdaySignupDialogFragment();
		dialog.show(getSupportFragmentManager(), TAG_ALBUM_DATE);

	}

	@Override
	public void setDate(String date) {
		AddAlbumFragment addAlbum = (AddAlbumFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
		if (addAlbum != null) {
			addAlbum.setAlbumDate(date);
		}
	}

	@Override
	public void uploadPhotosToAlbum(String albumName, String albumDate,
			String albumDesc, ArrayList<String> photoUrls) {
		Intent photoIntent = new Intent();
		photoIntent.putExtra(PhotoHandler.ALBUM_NAME, albumName);
		photoIntent.putExtra(PhotoHandler.ALBUM_DATE, albumDate);
		photoIntent.putExtra(PhotoHandler.ALBUM_DESCRIPTION, albumDesc);
		photoIntent.putExtra(PhotoHandler.PHOTO_URLS, photoUrls);
		setResult(RESULT_OK, photoIntent);
	}
}
