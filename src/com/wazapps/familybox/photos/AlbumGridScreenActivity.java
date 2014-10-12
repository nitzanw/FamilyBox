package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class AlbumGridScreenActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		//get the activity arguments (album item) and pass them to the fragment 
		AlbumGridFragment albumGridFrag = new AlbumGridFragment();
//		albumGridFrag.setArguments(args);
		
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.fragment_container, albumGridFrag,
				AlbumGridFragment.ALBUM_GRID_FRAGMENT).commit();	
		
		initActionBar();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initActionBar() {
		getActionBar().setIcon(
				getResources().getDrawable(
						R.drawable.action_bar_fc_icon_with_arrow));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}
}
