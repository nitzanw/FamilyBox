package com.wazapps.familybox.familyTree;

import java.util.ArrayList;

import com.wazapps.familybox.R;
import com.wazapps.familybox.profiles.FamilyMemberListDetails;
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
	private ArrayList<FamilyMemberListDetails> familiesList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);
		HeaderListView list = new HeaderListView(root.getContext());
		list.setAdapter(new SectionAdapter() {

            @Override
            public int numberOfSections() {
                return 4;
            }

            @Override
            public int numberOfRows(int section) {
                return 35;
            }

            @Override
            public Object getRowItem(int section, int row) {
                return null;
            }

            @Override
            public boolean hasSectionHeaderView(int section) {
                return true;
            }

            @Override
            public View getRowView(int section, int row, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = (TextView) getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
                }
                ((TextView) convertView).setText("Section " + section + " Row " + row);
                return convertView;
            }

            @Override
            public int getSectionHeaderViewTypeCount() {
                return 2;
            }

            @Override
            public int getSectionHeaderItemViewType(int section) {
                return section % 2;
            }

            @Override
            public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    if (getSectionHeaderItemViewType(section) == 0) {
                        convertView = (TextView)  getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
                    } else {
                        convertView = getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_2), null);
                    }
                }

                if (getSectionHeaderItemViewType(section) == 0) {
                    ((TextView) convertView).setText("Header for section " + section);
                } else {
                    ((TextView) convertView.findViewById(android.R.id.text1)).setText("Header for section " + section);
                    ((TextView) convertView.findViewById(android.R.id.text2)).setText("Has a detail text field");
                }

                switch (section) {
                case 0:
                    convertView.setBackgroundColor(getResources().getColor(R.color.orange_drawer));
                    break;
                case 1:
                    convertView.setBackgroundColor(getResources().getColor(R.color.orange_drawer));
                    break;
                case 2:
                    convertView.setBackgroundColor(getResources().getColor(R.color.orange_drawer));
                    break;
                case 3:
                    convertView.setBackgroundColor(getResources().getColor(R.color.orange_drawer));
                    break;
                }
                return convertView;
            }

            @Override
            public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
                super.onRowItemClick(parent, view, section, row, id);
//                Toast.makeText(DemoActivity.this, "Section " + section + " Row " + row, Toast.LENGTH_SHORT).show();
            }
        });
		return list;
	}
	
	
}
