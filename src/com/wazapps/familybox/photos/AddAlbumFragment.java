package com.wazapps.familybox.photos;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.splashAndLogin.EmailSignupFragment.SignupScreenCallback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddAlbumFragment extends Fragment implements OnClickListener,
		OnFocusChangeListener {
	public static final String ADD_ALBUM_FRAG = "add album fragment";
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
			shareWith.show(getChildFragmentManager(), ShareWithDialogFragment.SHARE_W_DIALOG_FRAG);
		} else if (v.getId() == R.id.rl_add_photos_btn_empty) {
			// TODO get real data:
			ArrayList<String> urlPhotoList = new ArrayList<String>();
			urlPhotoList.add("");
			urlPhotoList.add("");
			urlPhotoList.add("");
			urlPhotoList.add("");
			mAdapter = new AddPhotoAdapter(getActivity(), urlPhotoList);
			for (int i = 0; i < urlPhotoList.size(); i++) {
				if (i % 2 == 0) {
					mPhotosInputSectionRow1.addView(mAdapter.getView(i, null,
							(ViewGroup) getView()));
				} else {
					mPhotosInputSectionRow2.addView(mAdapter.getView(i, null,
							(ViewGroup) getView()));
				}
			}
			mAddPhotosEmpty.setVisibility(View.INVISIBLE);
			mAddPhotoBtn.setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.tv_add_photos_btn) {
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
}
