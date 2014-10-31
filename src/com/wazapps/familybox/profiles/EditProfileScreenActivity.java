package com.wazapps.familybox.profiles;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.profiles.EditProfileFragment.EditProfileCallback;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.DateChooserCallback;
import com.wazapps.familybox.util.AbstractScreenActivity;

public class EditProfileScreenActivity extends AbstractScreenActivity 
	implements EditProfileCallback, DateChooserCallback {
	
	private static final String TAG_EDTBIRTHDAY = "edit birthday";
	private static final int SELECT_PICTURE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.enter, R.anim.exit);
		EditProfileFragment editProfileFrag = new EditProfileFragment();
		// set the fragment to the container
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fragment_container, editProfileFrag,
				EditProfileFragment.EDIT_PROFILE_FRAG).commit();
		getActionBar().setTitle(getString(R.string.edit_profile));
	}

	@Override
	public void openBirthdayDialog() {
		BirthdaySignupDialogFragment dialog = 
				new BirthdaySignupDialogFragment();
		dialog.show(getSupportFragmentManager(), TAG_EDTBIRTHDAY);			
	}

	@Override
	public void setDate(String date) {
		EditProfileFragment editProfileFrag = (EditProfileFragment) 
				getSupportFragmentManager()
				.findFragmentByTag(EditProfileFragment.EDIT_PROFILE_FRAG);
		editProfileFrag.setDate(date);
	}

	@Override
	public void openPhonePhotoBrowsing() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri currImageURI = data.getData();
				File file = new File(PhotoHandler.getRealPathFromURI(this,
						currImageURI));

				if (file.exists()) {
					EditProfileFragment frag = (EditProfileFragment) 
							getSupportFragmentManager()
							.findFragmentByTag(EditProfileFragment.EDIT_PROFILE_FRAG);
					Bitmap myBitmap = PhotoHandler.getImageBitmapFromFile(file);
					byte[] fileData = PhotoHandler
							.createDownsampledPictureData(myBitmap);
					String filename = "profilePic.JPEG";
					frag.setProfileImage(myBitmap, fileData, filename);
				}
			}
		}
	}
}
