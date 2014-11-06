package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.util.MultiImageChooserActivity;

public class AddAlbumFragment extends Fragment implements OnClickListener,
		OnFocusChangeListener {
	public static final String ADD_ALBUM_FRAG = "add album fragment";
	protected static final int PHOTO_CHOOSER = 0;
	protected static final int PHOTO_CHOOSER_ADDITION = 1;
	private static final int NAME_ERR = 1;
	private static final int DATE_ERR = 10;
	private static final int DESC_ERR = 100;
	private static final int PHOTOS_ERR = 1000;
	private ViewGroup rootView;
	private EditText mAlbumName;
	private EditText mAlbumDate;
	private EditText mAlbumDesc;
	private TextView mShareBtn;
	private RelativeLayout mAddPhotosEmpty;
	private TextView mAddPhotoBtn;
	private AddAlbumScreenCallback addAlbumCallback = null;
	private AddPhotoAdapter mAdapter;
	private LinearLayout mPhotosInputSectionRow1;
	private LinearLayout mPhotosInputSectionRow2;
	private ArrayList<Integer> currentSelectedPhotos;
	private ArrayList<String> photoUrls;
	private ArrayList<String> shareWithList = new ArrayList<String>();
	private LinearLayout mProgress;

	public interface AddAlbumScreenCallback {
		public void openDateInputDialog();

		public void uploadPhotosToAlbum(String albumName, String albumDate,
				String albumDesc, ArrayList<String> photoUrls,
				ArrayList<String> shareWithList);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.addAlbumCallback = (AddAlbumScreenCallback) getActivity();
		}

		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement "
					+ "AddAlbumScreenCallback interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_album,
				null);
		initViews();
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_accept, menu);

	}

	private void initViews() {
		mAlbumName = (EditText) rootView.findViewById(R.id.et_add_album_name);
		mAlbumDate = (EditText) rootView.findViewById(R.id.et_add_album_date);
		mAlbumDate.setOnFocusChangeListener(this);

		mAlbumDate.setOnClickListener(this);
		mAlbumDesc = (EditText) rootView
				.findViewById(R.id.et_add_album_description);
		mShareBtn = (TextView) rootView
				.findViewById(R.id.tv_add_album_share_with_btn);
		mShareBtn.setOnClickListener(this);
		mPhotosInputSectionRow1 = (LinearLayout) rootView
				.findViewById(R.id.ll_input_section_row_1);
		mPhotosInputSectionRow2 = (LinearLayout) rootView
				.findViewById(R.id.ll_input_section_row_2);
		mAddPhotosEmpty = (RelativeLayout) rootView
				.findViewById(R.id.rl_add_photos_btn_empty);
		mProgress = (LinearLayout) rootView
				.findViewById(R.id.ll_progress_spinner);
		mAddPhotosEmpty.setOnClickListener(this);
		mAddPhotoBtn = (TextView) rootView.findViewById(R.id.tv_add_photos_btn);
		mAddPhotoBtn.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setTitle(
				getString(R.string.add_album_title));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.et_add_album_date) {
			addAlbumCallback.openDateInputDialog();
			InputMethodManager bimm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			bimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} else if (v.getId() == R.id.tv_add_album_share_with_btn) {
			ShareWithDialogFragment shareWith = new ShareWithDialogFragment();
			Bundle args = new Bundle();
			args.putStringArrayList(ShareWithDialogFragment.SHARE_W_LIST,
					shareWithList);
			shareWith.setArguments(args);
			shareWith.show(getChildFragmentManager(),
					ShareWithDialogFragment.SHARE_W_DIALOG_FRAG);
		} else if (v.getId() == R.id.rl_add_photos_btn_empty) {
			startPhotoPicker(PHOTO_CHOOSER);

		} else if (v.getId() == R.id.tv_add_photos_btn) {
			startPhotoPicker(PHOTO_CHOOSER_ADDITION);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			// upload the album to the server
			if (checkFields() == 0) {

				addAlbumCallback.uploadPhotosToAlbum(mAlbumName.getText()
						.toString(), mAlbumDate.getText().toString(),
						mAlbumDesc.getText().toString(), photoUrls,
						shareWithList);

				mProgress.setVisibility(View.VISIBLE);
				
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private int checkFields() {
		int errType = 0;
		if (TextUtils.isEmpty(mAlbumName.getText().toString())) {

			errType += NAME_ERR;
		}
		if (TextUtils.isEmpty(mAlbumDate.getText().toString())) {
			errType += DATE_ERR;
		}
		if (TextUtils.isEmpty(mAlbumDesc.getText().toString())) {
			errType += DESC_ERR;
		}
		if (photoUrls == null || photoUrls.isEmpty()) {
			errType += PHOTOS_ERR;
		}

		if (errType == NAME_ERR) {
			createToast(R.string.add_album_err_no_name);
		} else if (errType == DATE_ERR) {
			createToast(R.string.add_album_err_no_date);
		} else if (errType == DESC_ERR) {
			createToast(R.string.add_album_err_no_description);
		} else if (errType == 101) {
			createToast(R.string.add_album_err_no_name_desc);
		} else if (errType == 11) {
			createToast(R.string.add_album_err_no_name_date);
		} else if (errType == 111) {
			createToast(R.string.add_album_err_no_fields);
		} else if (errType >= 1000) {
			createToast(R.string.add_album_err_no_photos);

		}
		return errType;

	}

	private void createToast(int resource) {
		Toast toast = Toast.makeText(getActivity(), getString(resource),
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

	}

	private void createToast(String text) {
		Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

	}

	public void setAlbumDate(String date) {
		mAlbumDate.setText(date);

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			addAlbumCallback.openDateInputDialog();
			InputMethodManager bimm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			bimm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}

	}

	public void setPhotosToUpload(ArrayList<Integer> integerArrayListExtra) {
		mAddPhotosEmpty.setVisibility(View.INVISIBLE);
		mAddPhotoBtn.setVisibility(View.VISIBLE);
		currentSelectedPhotos = integerArrayListExtra;
		mAdapter = new AddPhotoAdapter(getActivity(), integerArrayListExtra);

		for (int i = 0; i < integerArrayListExtra.size(); i++) {
			if (i % 2 == 0) {
				mPhotosInputSectionRow2.addView(mAdapter.getView(i, null,
						(ViewGroup) getView()));
			} else {
				mPhotosInputSectionRow1.addView(mAdapter.getView(i, null,
						(ViewGroup) getView()));
			}
		}

	}

	public void startPhotoPicker(int requestCode) {
		Intent getPhotos = new Intent(getActivity(),
				MultiImageChooserActivity.class);
		getPhotos.putExtra(MultiImageChooserActivity.COL_WIDTH_KEY, 200);
		startActivityForResult(getPhotos, requestCode);
	}

	public void addPhotosToUpload(ArrayList<Integer> integerArrayList) {
		if (!integerArrayList.isEmpty()) {
			int currentSize = currentSelectedPhotos.size();
			// if the added photo id's are not already uploading, add it to the
			// list
			for (Integer i : integerArrayList) {
				if (!currentSelectedPhotos.contains(i)) {
					currentSelectedPhotos.add(i);
				}
			}

			mAdapter.updateData(currentSelectedPhotos);
			// add the added photos to the view
			for (int i = currentSize; i < currentSelectedPhotos.size(); i++) {
				if (i % 2 == 0) {
					mPhotosInputSectionRow2.addView(mAdapter.getView(i, null,
							(ViewGroup) getView()));
				} else {
					mPhotosInputSectionRow1.addView(mAdapter.getView(i, null,
							(ViewGroup) getView()));
				}
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		ArrayList<Integer> selected = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == AddAlbumFragment.PHOTO_CHOOSER) {
				selected = data
						.getIntegerArrayListExtra(MultiImageChooserActivity.MULTIPLE_FILE_IDS);
				photoUrls = data
						.getStringArrayListExtra(MultiImageChooserActivity.MULTIPLE_FILE_URL);
				if (selected != null && !selected.isEmpty()) {
					setPhotosToUpload(selected);
				}
			} else if (requestCode == AddAlbumFragment.PHOTO_CHOOSER_ADDITION) {
				selected = data
						.getIntegerArrayListExtra(MultiImageChooserActivity.MULTIPLE_FILE_IDS);
				ArrayList<String> addedPhotoUrls = data
						.getStringArrayListExtra(MultiImageChooserActivity.MULTIPLE_FILE_URL);
				if (selected != null && !selected.isEmpty()) {
					addPhotosToUpload(selected);
				}
				if (addedPhotoUrls != null) {
					addPhotoUrls(addedPhotoUrls);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addPhotoUrls(ArrayList<String> addedPhotoUrls) {
		if (!addedPhotoUrls.isEmpty()) {

			// if the added photo url are not already uploading, add it to the
			// list
			for (String s : addedPhotoUrls) {
				if (!photoUrls.contains(s)) {
					photoUrls.add(s);
				}
			}
		}
	}

	public void setSharedWithList(ArrayList<String> shareWithList) {
		this.shareWithList = shareWithList;

	}
}
