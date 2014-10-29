package com.wazapps.familybox.profiles;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wazapps.familybox.R;

public class EditProfileFamilyListAdapter extends AbstractFamilyListAdapter
		implements OnClickListener {

	private static final int POSITION = R.string.position;
	private static final int VIEW_TYPE = R.string.type;
	private static final String VIEW_ITEM = "view item";
	private static final Object IMAGE_ITEM = "round image item";
	private View focusedView;

	public EditProfileFamilyListAdapter(FragmentActivity activity,
			FamilyMemberDetails[] familyMembersList) {
		super(activity, familyMembersList);

	}

	@Override
	public View getInflatedView(ViewGroup parent) {

		return linearInflater.inflate(R.layout.edit_family_members_list_item,
				parent, false);
	}

	@Override
	public void initMemberView(int position, View v) {
		// TODO: add profile picture image handling
		FamilyMemberDetails member = this.familyMembersList[position];
		TextView role = (TextView) v
				.findViewById(R.id.tv_edit_close_family_role);

		TextView name = (TextView) v
				.findViewById(R.id.tv_close_family_member_name);

		name.setText(member.getName());
		role.setText(member.getRole());
		v.setTag(POSITION, position);
		v.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		final int position = (Integer) v.getTag(POSITION);
		final TextView role = (TextView) v
				.findViewById(R.id.tv_edit_close_family_role);
		final EditText editRole = (EditText) v
				.findViewById(R.id.et_edit_close_family_role);
		ImageView image = (ImageView) v
				.findViewById(R.id.iv_edit_profile_aprove);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				role.setText(editRole.getText().toString());
				//TODO change this in the db!!!
				familyMembersList[position].setRole(editRole.getText()
						.toString());
				restoreView();
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editRole.getWindowToken(), 0);
			}
		});
		editRole.setText(familyMembersList[position].getRole());

		restoreView();
		focusedView = v;
		editRole.setFocusableInTouchMode(true);
		editRole.requestFocus();
		image.setVisibility(View.VISIBLE);
		editRole.setVisibility(View.VISIBLE);
		role.setVisibility(View.INVISIBLE);

	}

	private void restoreView() {
		if (focusedView != null) {
			TextView role = (TextView) focusedView
					.findViewById(R.id.tv_edit_close_family_role);
			role.setVisibility(View.VISIBLE);
			EditText editRole = (EditText) focusedView
					.findViewById(R.id.et_edit_close_family_role);
			ImageView image = (ImageView) focusedView
					.findViewById(R.id.iv_edit_profile_aprove);
			image.setVisibility(View.INVISIBLE);
			editRole.clearFocus();
			focusedView.clearFocus();
			editRole.setVisibility(View.INVISIBLE);
		}

	}

}
