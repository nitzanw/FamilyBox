package com.wazapps.familybox.photos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.MultiImageChooserActivity;

public class AddAlbumFragment extends Fragment implements OnClickListener,
		OnFocusChangeListener {
	public static final String ADD_ALBUM_FRAG = "add album fragment";
	protected static final int PHOTO_CHOOSER = 0;
	protected static final int PHOTO_CHOOSER_ADDITION = 1;
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

	public interface AddAlbumScreenCallback {
		public void openDateInputDialog();
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
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_album,
				null);
		initViews();
		return rootView;
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
			shareWith.show(getChildFragmentManager(),
					ShareWithDialogFragment.SHARE_W_DIALOG_FRAG);
		} else if (v.getId() == R.id.rl_add_photos_btn_empty) {
			Intent getPhotos = new Intent(getActivity(),
					MultiImageChooserActivity.class);
			getPhotos.putExtra(MultiImageChooserActivity.COL_WIDTH_KEY, 200);
			getActivity().startActivityForResult(getPhotos, PHOTO_CHOOSER);

			mAddPhotosEmpty.setVisibility(View.INVISIBLE);
			mAddPhotoBtn.setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.tv_add_photos_btn) {
			Intent getPhotos = new Intent(getActivity(),
					MultiImageChooserActivity.class);
			getPhotos.putExtra(MultiImageChooserActivity.COL_WIDTH_KEY, 200);
			getActivity().startActivityForResult(getPhotos,
					PHOTO_CHOOSER_ADDITION);
		}

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
			//add the added photos to the view
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
}
