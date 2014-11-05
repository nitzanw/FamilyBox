package com.wazapps.familybox.photos;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.UserHandler;

public class ShareWithDialogFragment extends DialogFragment implements
		OnClickListener {
	public static final String SHARE_W_DIALOG_FRAG = "share with dialog fragment";
	public static final String SHARE_W_LIST = "share with list";
	private View root;
	private ListView mFamilyListView;
	private ImageButton mExit;
	private Button mAccept;
	private ShareWithListAdapter mAdapter;
	private FamilyShareAlbumCallback shareCallback = null;
	private ProgressBar mProgress;
	private TextView mTextView;
	private ArrayList<String> shareWithSelectedList;

	interface FamilyShareAlbumCallback {
		void setFamilliesToShareWith(ArrayList<String> shareIdList);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.shareCallback = (FamilyShareAlbumCallback) getActivity();
		}

		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement "
					+ "FamilyShareAlbumCallback interface");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			shareWithSelectedList = args.getStringArrayList(SHARE_W_LIST);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_dialog_share_with, container,
				false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		mFamilyListView = (ListView) root
				.findViewById(R.id.lv_share_w_family_list);
		mProgress = (ProgressBar) root.findViewById(android.R.id.progress);
		mTextView = (TextView) root.findViewById(android.R.id.empty);

		ShareWithListAdapter.QueryFactory<ParseObject> factory = new ShareWithListAdapter.QueryFactory<ParseObject>() {
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						FamilyHandler.FAMILY_CLASS_NAME);
				query.whereNotEqualTo("objectId", ParseUser.getCurrentUser()
						.get(UserHandler.FAMILY_KEY));
				query.addAscendingOrder(FamilyHandler.NAME_KEY);
				return query;
			}
		};

		mAdapter = new ShareWithListAdapter(getActivity(), factory, shareWithSelectedList);
		mAdapter.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {
			public void onLoading() {
				// Trigger "loading" UI
				mProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoaded(List<ParseObject> objects, Exception e) {
				if (e == null) {
					mProgress.setVisibility(View.INVISIBLE);
					if (objects.isEmpty()) {
						mTextView.setVisibility(View.VISIBLE);
					} else {

						mFamilyListView.setVisibility(View.VISIBLE);
					}
				}
			}

		});
		mFamilyListView.setAdapter(mAdapter);
		mExit = (ImageButton) root.findViewById(R.id.ib_share_w_exit);
		mExit.setOnClickListener(this);
		mAccept = (Button) root.findViewById(R.id.button_share_w_accept);
		mAccept.setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_share_w_exit) {
			dismiss();
		} else if (v.getId() == R.id.button_share_w_accept) {
			if (!mAdapter.isShareButtonEnabled()) {
				Toast.makeText(getActivity(), R.string.add_album_err_no_share,
						Toast.LENGTH_SHORT).show();
			} else {
				ArrayList<String> familyIdShareList = (ArrayList<String>) mAdapter
						.getFamilyIdShareList();
				shareCallback.setFamilliesToShareWith(familyIdShareList);
			}
			dismiss();
		}
	}
}
