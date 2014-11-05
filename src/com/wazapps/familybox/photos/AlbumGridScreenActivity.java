package com.wazapps.familybox.photos;

import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcelable;

import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.util.AbstractScreenActivity;
import com.wazapps.familybox.util.LogUtils;

public class AlbumGridScreenActivity extends AbstractScreenActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String family = getIntent().getStringExtra(FamilyHandler.FAMILY_ID_KEY);
		String familyName = getIntent().getStringExtra(FamilyHandler.NAME_KEY);
		Bundle args = new Bundle();
		args.putString(FamilyHandler.FAMILY_ID_KEY, family);
		args.putString(FamilyHandler.NAME_KEY, familyName);
		AlbumGridFragment albumGridFrag = new AlbumGridFragment();
		albumGridFrag.setArguments(args);
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, albumGridFrag,
						AlbumGridFragment.ALBUM_GRID_FRAGMENT).commit();

	}

}
