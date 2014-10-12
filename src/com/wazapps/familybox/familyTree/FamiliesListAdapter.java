package com.wazapps.familybox.familyTree;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wazapps.familybox.R;
import com.wazapps.familybox.photos.AlbumGridScreenActivity;
import com.wazapps.familybox.photos.AlbumItem;
import com.wazapps.familybox.photos.PhotoGridFragment;
import com.wazapps.familybox.photos.PhotoItem;
import com.wazapps.familybox.profiles.FamilyProfileActivity;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.SectionAdapter;

public class FamiliesListAdapter extends SectionAdapter {
	private enum LetterTypes {
		a, b, c, d, e, f, g, h, i, j, k, l,
		m, n, o, p, q, r, s, t, u, v, w, x,
		y, z
	}
	
	Activity activity;
	ArrayList<FamiliesListItem> familiesList;
	ArrayList<String> sectionLetters;
	ArrayList<Integer> sectionIndexes, sectionSizes;
	ArrayList<Drawable> sectionDrawables;
	String currLetter;
	int numberOfSections, currSectionSize;
	
	public FamiliesListAdapter(Activity activity, ArrayList<FamiliesListItem> familiesList) {
		this.activity = activity;
		this.familiesList = familiesList;
		this.sectionDrawables = new ArrayList<Drawable>();
		this.sectionLetters = new ArrayList<String>();
		this.sectionIndexes = new ArrayList<Integer>();
		this.sectionSizes = new ArrayList<Integer>();
		this.currLetter = "";
		this.numberOfSections = 0;
		this.currSectionSize = 0;
		Collections.sort(this.familiesList);
		
		parseData();
		loadLetterSeparators();
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
            convertView = (LinearLayout) this.activity.getLayoutInflater()
            		.inflate(this.activity.getResources()
            		.getLayout(R.layout.families_list_item), null);
        }
        
        FamiliesListItem currItem = (FamiliesListItem)getRowItem(section, row);
        TextView familyName = (TextView) convertView.findViewById(R.id.tv_families_list_item_name);
        familyName.setText(currItem.getFamilyName());
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
    		convertView = (LinearLayout) this.activity.getLayoutInflater()
    				.inflate(this.activity.getResources()
    				.getLayout(R.layout.families_list_header_item), null);
    	}
    	
    	ImageView currHeader = (ImageView) 
    			convertView.findViewById(R.id.iv_families_list_header_image);
    	Drawable currHeaderSrc = this.sectionDrawables.get(section);
    	currHeader.setImageDrawable(currHeaderSrc);
    	return convertView;
    }

    @Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
        super.onRowItemClick(parent, view, section, row, id);
        Intent familyAlbumsIntent = new Intent(activity, AlbumGridScreenActivity.class);
        AlbumItem[] albumList = { null, null, null, null, null, null };
        Bundle args = new Bundle();
        args.putParcelableArray("test1", albumList);
        familyAlbumsIntent.putExtra("test2", args);
        activity.startActivity(familyAlbumsIntent);
    }
    
    /**
     * Loads relevant section drawables into an arraylist for the purpose of loading
     * it into the list
     */
    private void loadLetterSeparators() {
    	Drawable currDrawable;
    	for (int k = 0; k < this.sectionLetters.size(); k++) {
    		String currLetter = this.sectionLetters.get(k);
    		LetterTypes currType = LetterTypes.valueOf(currLetter);
    		switch (currType) {
			case a:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_a);
				break;
				
			case b:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_b);
				break;
				
			case c:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_c);
				break;
				
			case d:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_d);
				break;
				
			case e:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_e);
				break;
				
			case f:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_f);
				break;
				
			case g:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_g);
				break;
				
			case h:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_h);
				break;
				
			case i:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_i);
				break;
				
			case j:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_j);
				break;
				
			case k:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_k);
				break;
				
			case l:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_l);
				break;
				
			case m:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_m);
				break;
			
			case n:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_n);
				break;
				
			case o:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_o);
				break;
				
			case p:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_p);
				break;
				
			case q:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_q);
				break;
				
			case r:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_r);
				break;
				
			case s:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_s);
				break;
				
			case t:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_t);
				break;
				
			case u:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_u);
				break;
				
			case v:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_v);
				break;
				
			case w:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_w);
				break;
				
			case x:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_x);
				break;
				
			case y:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_y);
				break;
				
			case z:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_z);
				break;

			default:
				currDrawable = activity.getResources()
				.getDrawable(R.drawable.letter_none);
				break;
			}
    		
    		this.sectionDrawables.add(currDrawable);
    	}
    }
    
    /**
     * Reads the family list and extracts relevant section information from it
     */
    private void parseData() {
		for (int i = 0; i < this.familiesList.size(); i++) {
			String familyLetter = this.familiesList.get(i).getFamilyName().substring(0,1);
			//TODO: if we want to support other languages we should change the comparison method 
			//right now we're using 'toLowerCase' and we can use 'toLowerCase(locale)'
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
}
