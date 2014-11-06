package com.wazapps.familybox.photos;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.splunk.mint.Mint;
import com.wazapps.familybox.R;

import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.newsfeed.NewsItem;
import com.wazapps.familybox.photos.AddAlbumFragment.AddAlbumScreenCallback;
import com.wazapps.familybox.photos.ShareWithDialogFragment.FamilyShareAlbumCallback;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment;
import com.wazapps.familybox.splashAndLogin.BirthdaySignupDialogFragment.DateChooserCallback;
import com.wazapps.familybox.util.AbstractScreenActivity;
import com.wazapps.familybox.util.LogUtils;

public class AddAlbumScreenActivity extends AbstractScreenActivity implements
		AddAlbumScreenCallback, DateChooserCallback, FamilyShareAlbumCallback {
	private static final String TAG_ALBUM_DATE = "add album date";
	private ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(AddAlbumScreenActivity.this, "ad50ec84");
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
			String albumDesc, ArrayList<String> photoUrls,
			ArrayList<String> shareWithList) {
		uploadAlbum(albumName, albumDate, albumDesc, photoUrls, shareWithList);
	}

	private void uploadAlbum(String albumName, String albumDate,
			String albumDesc, ArrayList<String> photoUrls,
			final ArrayList<String> shareWith) {

		Album album = new Album(this, photoUrls.size());
		if (currentUser == null) {
			currentUser = ParseUser.getCurrentUser();
		}

		album.setFamily((String) currentUser.get(UserHandler.FAMILY_KEY));
		album.setAlbumName(albumName);
		album.setAlbumDate(albumDate);
		album.setAlbumDescription(albumDesc);
		album.saveInBackground(new SaveCallback() {
			FragmentActivity activity = null;
			Album album = null;
			ArrayList<String> photoUrls = null;

			@Override
			public void done(ParseException e) {
				if (e == null) {
					
					new SetAlbumShareWithList(album, shareWith).run();
					new UploadPhotosToAlbum(album, photoUrls).run();
				} else {
					Toast toast = Toast.makeText(activity,
							getString(R.string.add_album_err_not_uploaded)
									+ " " + e.toString(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
				}
			}

			SaveCallback init(FragmentActivity activity, Album album,
					ArrayList<String> photoUrls) {
				this.activity = activity;
				this.album = album;
				this.photoUrls = photoUrls;
				return this;
			}

		}.init(this, album, photoUrls));

	}

	class UploadPhotosToAlbum extends Thread {
		private Album album;
		private ArrayList<String> photoUrlList;

		UploadPhotosToAlbum(Album album, ArrayList<String> photoUrlList) {
			this.album = album;
			this.photoUrlList = photoUrlList;
		}

		@Override
		public void run() {
			uploadPhotosToAlbum(album, photoUrlList);
			super.run();
		}

	}

	protected void uploadPhotosToAlbum(Album album, ArrayList<String> photoUrls) {
		for (int i = 0; i < photoUrls.size(); i++) {
			Uri currImageURI = Uri.parse(photoUrls.get(i));

			File file = new File(PhotoHandler.getRealPathFromURI(this,
					currImageURI));

			if (file.exists()) {
				Bitmap myBitmap = PhotoHandler.getImageBitmapFromFile(file);
				byte[] fileData = PhotoHandler
						.createDownsampledPictureData(myBitmap);

				PhotoItem photoItem = new PhotoItem();
				photoItem.setCaption("");
				photoItem.setAlbum(album.getObjectId());

				ParseFile photoFile = new ParseFile(file.getName(), fileData);
				photoFile.saveInBackground(new SaveCallback() {
					private PhotoItem photoItem;
					private Album album;
					private ParseFile photoFile;

					@Override
					public void done(ParseException e) {
						if (e == null) {

							photoItem.setPhotoFile(photoFile);
							photoItem.saveEventually(new SaveCallback() {
								private Album album;
								private ParseFile photoFile;

								@Override
								public void done(ParseException e) {
									// add the cover photo to the album (if the
									// counter is 0)

									album.incrementAlbumCounter(photoFile);
								}

								SaveCallback init(Album album,
										ParseFile photoFile) {

									this.album = album;
									this.photoFile = photoFile;
									return this;
								}

							}.init(album, photoFile));
						} else {
							album.incrementAlbumCounterForErrorUplaod();
							LogUtils.logError(getLocalClassName(),
									"something went wrong with the album uplaod "
											+ e.getMessage());
						}
					}

					private SaveCallback init(FragmentActivity activity,
							Album album, ParseFile albumPic, PhotoItem photoItem) {
						this.album = album;
						this.photoFile = albumPic;
						this.photoItem = photoItem;
						return this;
					}
				}.init(this, album, photoFile, photoItem));

			}
		}
	}



	class SetAlbumShareWithList extends Thread {
		private Album album;
		private ArrayList<String> shareWithList;

		public SetAlbumShareWithList(final Album album,
				ArrayList<String> shareWithList) {
			this.album = album;
			this.shareWithList = shareWithList;
		}

		@Override
		public void run() {
			setAlbumShareWithList(album, shareWithList);
			super.run();
		}
	}

	private void setAlbumShareWithList(final Album album,
			ArrayList<String> shareWithList) {
		for (String familyid : shareWithList) {

			ShareAlbum share = new ShareAlbum();
			share.setAlbumId(album.getObjectId());
			share.setAlbumOwner(ParseUser.getCurrentUser()
					.get(UserHandler.FAMILY_KEY).toString());
			share.setSharedWithId(familyid);
			share.saveEventually();

		}
	}

	@Override
	public void setFamilliesToShareWith(ArrayList<String> shareIdList) {
		AddAlbumFragment addAlbum = (AddAlbumFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
		if (addAlbum != null) {
			addAlbum.setSharedWithList(shareIdList);
		}

	}
}
