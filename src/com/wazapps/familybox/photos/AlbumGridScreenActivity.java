package com.wazapps.familybox.photos;

import java.util.Arrays;

import com.wazapps.familybox.R;
import com.wazapps.familybox.familyTree.FamiliesListItem;
import com.wazapps.familybox.util.AbstractScreenActivity;
import com.wazapps.familybox.util.LogUtils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.text.style.ParagraphStyle;
import android.view.MenuItem;

public class AlbumGridScreenActivity extends AbstractScreenActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getIntent().getBundleExtra(PhotoGridFragment.ALBUM_ITEM_LIST);
		Parcelable[] parcelableArray = args.getParcelableArray(PhotoGridFragment.ALBUM_ITEM_LIST);
		String familyName = args.getString(FamiliesListItem.FAMILY_NAME);
		AlbumItem[] albumItems = Arrays.copyOf(parcelableArray, parcelableArray.length, AlbumItem[].class);
		args.putParcelableArray(PhotoGridFragment.ALBUM_ITEM_LIST, albumItems);
		args.putString(FamiliesListItem.FAMILY_NAME, familyName);
		
		//get the activity arguments (album item) and pass them to the fragment 
		AlbumGridFragment albumGridFrag = new AlbumGridFragment();
		albumGridFrag.setArguments(args);
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.fragment_container, albumGridFrag,
				AlbumGridFragment.ALBUM_GRID_FRAGMENT).commit();	
		
	}

}
