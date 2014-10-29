package com.wazapps.familybox.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.AddAlbumFragment.AddAlbumScreenCallback;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.DateChooserCallback;
import com.wazapps.familybox.util.AbstractScreenActivity;
import com.wazapps.familybox.util.MultiImageChooserActivity;

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
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			// TODO upload the album to the server
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_accept, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == AddAlbumFragment.PHOTO_CHOOSER) {
				AddAlbumFragment frag = (AddAlbumFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container);
				if (frag != null) {
					frag.setPhotosToUpload(data
							.getIntegerArrayListExtra(MultiImageChooserActivity.MULTIPLE_FILE_IDS));
				}
			}else if(requestCode == AddAlbumFragment.PHOTO_CHOOSER_ADDITION){
				AddAlbumFragment frag = (AddAlbumFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container);
				if (frag != null) {
					frag.addPhotosToUpload(data
							.getIntegerArrayListExtra(MultiImageChooserActivity.MULTIPLE_FILE_IDS));
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}