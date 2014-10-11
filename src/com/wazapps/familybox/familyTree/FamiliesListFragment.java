package com.wazapps.familybox.familyTree;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.profiles.FamilyMemberDetails;
import com.wazapps.familybox.util.HeaderListView;
import com.wazapps.familybox.util.SectionAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class FamiliesListFragment extends Fragment {
	private View root;
	private ArrayList<FamilyMemberDetails> familiesList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);
		HeaderListView familiesList = new HeaderListView(root.getContext());
		familiesList.setBackgroundColor(getResources().getColor(R.color.white_cream_ab));
		ArrayList<FamiliesListItem> familiesListData = new ArrayList<FamiliesListItem>();
		testFunc(familiesListData);
		FamiliesListAdapter familiesListAdapater = new FamiliesListAdapter(getActivity(), familiesListData);
		familiesList.setAdapter(familiesListAdapater);
		return familiesList;
	}
	
	private void testFunc(ArrayList<FamiliesListItem> familiesListData) {
		familiesListData.add(new FamiliesListItem("f1u1", "Aluf"));
		familiesListData.add(new FamiliesListItem("f1u1", "Alon"));
		familiesListData.add(new FamiliesListItem("f1u1", "Alran"));
		familiesListData.add(new FamiliesListItem("f1u1", "Ander"));
		familiesListData.add(new FamiliesListItem("f1u1", "Arbel"));
		familiesListData.add(new FamiliesListItem("f1u1", "����"));
		familiesListData.add(new FamiliesListItem("f1u1", "�����"));
		
		familiesListData.add(new FamiliesListItem("f1u1", "Ben-lulu"));
		familiesListData.add(new FamiliesListItem("f1u1", "Barak"));
		familiesListData.add(new FamiliesListItem("f1u1", "Biton"));
		familiesListData.add(new FamiliesListItem("f1u1", "Berger"));
		familiesListData.add(new FamiliesListItem("f1u1", "Buzaglo"));
		familiesListData.add(new FamiliesListItem("f1u1", "Brit"));
		familiesListData.add(new FamiliesListItem("f1u1", "Binder"));
		
		familiesListData.add(new FamiliesListItem("f1u1", "Friedman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Frenkel"));
		familiesListData.add(new FamiliesListItem("f1u1", "Frueman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Folman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Feldman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Fargun"));
		familiesListData.add(new FamiliesListItem("f1u1", "Fox"));
		
		familiesListData.add(new FamiliesListItem("f1u1", "Ginor"));
		familiesListData.add(new FamiliesListItem("f1u1", "Giat"));
		familiesListData.add(new FamiliesListItem("f1u1", "Ginat"));
		familiesListData.add(new FamiliesListItem("f1u1", "Galili"));
		familiesListData.add(new FamiliesListItem("f1u1", "Gal"));
		familiesListData.add(new FamiliesListItem("f1u1", "GoldStone"));
		familiesListData.add(new FamiliesListItem("f1u1", "GoldStein"));
		familiesListData.add(new FamiliesListItem("f1u1", "Goldberg"));
		familiesListData.add(new FamiliesListItem("f1u1", "Gabay"));
		
		familiesListData.add(new FamiliesListItem("f1u1", "Klein"));
		familiesListData.add(new FamiliesListItem("f1u1", "Kadosh"));
		familiesListData.add(new FamiliesListItem("f1u1", "Karmon"));
		
		familiesListData.add(new FamiliesListItem("f1u1", "Shapira"));
		familiesListData.add(new FamiliesListItem("f1u1", "Shemesh"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sharon"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sherman"));
		familiesListData.add(new FamiliesListItem("f1u1", "Stav"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sela"));
		familiesListData.add(new FamiliesListItem("f1u1", "Shmuelli"));
		familiesListData.add(new FamiliesListItem("f1u1", "Saban"));
		familiesListData.add(new FamiliesListItem("f1u1", "Sabag"));
		
		familiesListData.add(new FamiliesListItem("f1u1", "Zohar"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zaguri"));
		familiesListData.add(new FamiliesListItem("f1u1", "Ziv"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zar"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zamir"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zachs"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zach"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zaken"));
		familiesListData.add(new FamiliesListItem("f1u1", "Zilber"));
	}
}
