package com.wazapps.familybox.profiles;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.util.HorizontialListView;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ProfileFragment extends Fragment {
	private View root;
	private HorizontialListView familyList;
	private ListView profileDetailsList;
	private ProfileFamilyListAdapter familyListAdapter;
	private ProfileDetailsAdapter profileDetailsAdapter; 
	private ArrayList<FamilyMemberListDetails> familyListData;
	private ArrayList<ProfileDetails> profileDetailsData; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);
		setUpProfileDetails();
		setUpFamilyList();
		
		//Clear the listView's top highlight scrolling effect
		//TODO: maybe handle it in a better way (that will give prettier results)
		int glowDrawableId = root.getResources().getIdentifier("overscroll_glow", "drawable", "android");
		Drawable androidGlow = root.getResources().getDrawable(glowDrawableId);
		androidGlow.setColorFilter(R.color.orange_fb, Mode.CLEAR);
		return root;
	}
	
	private void setUpProfileDetails() {
		this.profileDetailsList = (ListView) root.findViewById(R.id.profile_details);		
		//set profile details list header
		//TODO: THIS DOESNT WORK. find a way to add a header to listview
//		LayoutInflater lf = getActivity().getLayoutInflater();
//		ViewGroup headerView = (ViewGroup) lf.inflate(R.id.ll_profile_details_header_container, this.profileDetailsList, false);
//		this.profileDetailsList.addHeaderView(headerView, null, false);
		
		this.profileDetailsData = new ArrayList<ProfileDetails>();
		this.profileDetailsAdapter = new ProfileDetailsAdapter(this.getActivity(),
				this.profileDetailsData);
		this.profileDetailsList.setAdapter(this.profileDetailsAdapter);
		
		//TODO: set up real data
		this.profileDetailsData.add(new ProfileDetails("Address", "K. yovel, mozkin st."));
		this.profileDetailsData.add(new ProfileDetails("Birthday","19.10.1987"));
		this.profileDetailsData.add(new ProfileDetails("Previous Family Names", "No previous family names"));
		this.profileDetailsData.add(new ProfileDetails("Quotes", "For every every there exists exists"));
		this.profileDetailsAdapter.notifyDataSetChanged();
	}
	
	private void setUpFamilyList() {
		this.familyList = (HorizontialListView) root.findViewById(R.id.family_members_list);
		this.familyListData = new ArrayList<FamilyMemberListDetails>();
		this.familyListAdapter = new ProfileFamilyListAdapter(this.getActivity(), 
				this.familyListData);
		this.familyList.setAdapter(this.familyListAdapter);
		
		//TODO: set up real data
		this.familyListData.add(new FamilyMemberListDetails("F1U1", "", "Arie", "Father"));
		this.familyListData.add(new FamilyMemberListDetails("F1U2", "", "Mati", "Mother"));
		this.familyListData.add(new FamilyMemberListDetails("F1U3", "", "Tal" , "Sister"));
		this.familyListAdapter.notifyDataSetChanged();
	}
}
