package com.wazapps.familybox.familyTree;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import com.wazapps.familybox.R;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.SectionAdapter;

public class FamiliesListAdapter extends SectionAdapter {
	Activity activity;
	ArrayList<FamiliesListItem> familiesList;
	ArrayList<String> sectionLetters;
	ArrayList<Integer> sectionIndexes, sectionSizes;
	String currLetter;
	int numberOfSections, currSectionSize;
	
	public FamiliesListAdapter(Activity activity, ArrayList<FamiliesListItem> familiesList) {
		this.activity = activity;
		this.familiesList = familiesList;
		this.sectionLetters = new ArrayList<String>();
		this.sectionIndexes = new ArrayList<Integer>();
		this.sectionSizes = new ArrayList<Integer>();
		this.currLetter = "";
		this.numberOfSections = 0;
		this.currSectionSize = 0;
		Collections.sort(this.familiesList);
		
		for (int i = 0; i < this.familiesList.size(); i++) {
			String familyLetter = this.familiesList.get(i).getFamilyName().substring(0,1);
			//TODO: if we want to support other languages we should change the comparison method 
			if (!(this.currLetter.equals(familyLetter.toLowerCase()))) {
				this.currLetter = familyLetter.toLowerCase();
				this.numberOfSections++;
				this.sectionLetters.add(this.currLetter);
				this.sectionIndexes.add(i);
				if (this.currSectionSize != 0) {
					this.sectionSizes.add(this.currSectionSize);
				}
				
				currSectionSize = 1;
			} else {
				this.currSectionSize++;
			}
		}
		
		if (this.currSectionSize != 0) {
			this.sectionSizes.add(this.currSectionSize);
		}
	}
	
	@Override
    public int numberOfSections() {
        return this.numberOfSections;
    }

    @Override
    public int numberOfRows(int section) {	
    	//this code part is for handling a rare error in the headerListView
    	if (section == -1) {
    		return 0;
    	}
    	
    	return this.sectionSizes.get(section);
    }

    @Override
    public Object getRowItem(int section, int row) {
    	//this code part is for handling a rare error in the headerListView
    	if (section == -1) {
    		return null;
    	}
    	
        int index = this.sectionIndexes.get(section);
        index += row;
        return this.familiesList.get(index);
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (TextView) this.activity.getLayoutInflater().inflate(this.activity.getResources().getLayout(android.R.layout.simple_list_item_1), null);
        }
        FamiliesListItem currItem = (FamiliesListItem)getRowItem(section, row);
        ((TextView) convertView).setText(currItem.getFamilyName());
        return convertView;
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return this.numberOfSections;
    }

    @Override
    public int getSectionHeaderItemViewType(int section) {
        return section;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
    	if (convertView == null) {
    		convertView = (TextView) this.activity.getLayoutInflater().inflate(this.activity.getResources().getLayout(android.R.layout.simple_list_item_1), null);
    	}
    	 ((TextView) convertView).setText("Letter " + this.sectionLetters.get(section).toUpperCase());
    	 convertView.setBackgroundColor(this.activity.getResources().getColor(R.color.orange_drawer));
    	 return convertView;
    }

    @Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
        super.onRowItemClick(parent, view, section, row, id);
        //TODO: add here code for onClick
    }

}
