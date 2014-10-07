package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.HorizontialListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {
	private View root;
	private HorizontialListView familyList;
	private ProfileFamilyListAdapter familyAdapter;
	private ArrayList<FamilyMemberListDetails> familyListData;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);
		this.familyList = (HorizontialListView) root.findViewById(R.id.familyMembersList);
		this.familyListData = new ArrayList<FamilyMemberListDetails>();
		this.familyAdapter = new ProfileFamilyListAdapter(this.getActivity(), 
				this.familyListData);
		this.familyList.setAdapter(this.familyAdapter);
		getFamilyList(); //TODO: do it in a proper way
		return root;
	}
	
	private void getFamilyList() {
		//TODO: add actual data
		this.familyListData.add(new FamilyMemberListDetails("F1U1", "", "Arie", "Father"));
		this.familyListData.add(new FamilyMemberListDetails("F1U2", "", "Mati", "Mother"));
		this.familyListData.add(new FamilyMemberListDetails("F1U3", "", "Tal" , "Sister"));
		this.familyAdapter.notifyDataSetChanged();
	}

}
