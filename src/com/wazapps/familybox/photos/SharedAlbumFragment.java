package com.wazapps.familybox.photos;

import com.wazapps.familybox.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class SharedAlbumFragment extends Fragment {
private View root;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	root = inflater.inflate(R.layout.fragment_shared_album, container, false);
	
	return root;
}
}
