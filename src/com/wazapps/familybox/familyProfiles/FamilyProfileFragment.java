package com.wazapps.familybox.familyProfiles;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wazapps.familybox.R;
import com.wazapps.familybox.handlers.FamilyHandler;
import com.wazapps.familybox.handlers.InputHandler;
import com.wazapps.familybox.handlers.PhotoHandler;
import com.wazapps.familybox.handlers.UserHandler;
import com.wazapps.familybox.handlers.UserHandler.FamilyMembersFetchCallback;
import com.wazapps.familybox.photos.Album;
import com.wazapps.familybox.photos.PhotoAlbumScreenActivity;
import com.wazapps.familybox.photos.PhotoGridFragment;
import com.wazapps.familybox.photos.ShareAlbum;
import com.wazapps.familybox.profiles.ProfileFragment;
import com.wazapps.familybox.profiles.ProfileFragment.AddProfileFragmentListener;
import com.wazapps.familybox.profiles.UserData;
import com.wazapps.familybox.util.LogUtils;
import com.wazapps.familybox.util.RoundedImageView;

public class FamilyProfileFragment extends Fragment implements OnClickListener {
	private static final int ITEM_TYPE = R.string.type;
	private static final int ITEM_POS = R.string.position;
	private static final int FAMILY_MEMBER_TYPE = R.string.family;

	public static final String USER_FAMILY = "user family";

	private static final String FAMILY_MEMBER_CHILD_TYPE = "child";
	private static final String FAMILY_MEMBER_PARENT_TYPE = "parent";
	private static final String FAMILY_MEMBER_SINGLE_TYPE = "single";
	private static final String FAMILY_MEMBER_ITEM_TYPE = "family member item";
	public static final String FAMILY_PROFILE_DATA = "family profile data";
	public static final String FAMILY_PROFILE_FRAG = "family profile fragment";
	public static final String FAMILY_ID = "family id";
	public static final String FAMILY_NAME = "family name";

	// fragment views
	private View root;
	private LinearLayout mParentLayoutRight, mParentLayoutLeft,
			mParentLayoutCenter;
	private LinearLayout mPhotoAlbumsHolder, mChildrenHolder;
	private LinearLayout mChildrenSpinner, mParentsSpinner;
	private RelativeLayout mSingleMember;
	private TextView mFamilyTitle, mFamilyParentsTitle, mFamilyChildrenTitle;
	private RoundedImageView mSingleMemberPhoto;
	private TextView mSingleMemberName;
	private ImageView noChildrenCover;

	// adapters and data handlers
	private FamilyProfileParentAdapter mParentAdapter;
	private FamilyProfileChildAdapter mChildrenAdapter;
	private FamilyProfileAlbumAdapter mAlbumsAdapter;
	private AddProfileFragmentListener profileFragmentAdder;

	// family profile data
	private ArrayList<ParseUser> mFamilyMembers = null;
	private ArrayList<UserData> mFamilyMembersData = null,
			mChildrenList = null, mParentsList = null;
	private UserHandler mUserhandler = null;
	private boolean mIsUserFamily = false;
	private ParseUser mLoggedUser = null;
	private ParseObject mCurrentFamily = null;
	private String mFamilyId, mFamilyName;
	private ImageView noParentsCover;
	private Animation textJump;
	protected int ALBUM_ITEM = R.string.album_title;
	private ProgressBar mAlbumProgress;
	protected FamilyProfileSharedAlbumAdapter mShareAlbumsAdapter;

	public interface AddFamilyProfileFragmentListener {
		public void addFamilyProfileFragment(Bundle args);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			profileFragmentAdder = (AddProfileFragmentListener) getActivity();
		}

		catch (ClassCastException e) {
			Log.e(getTag(), "the activity does not implement "
					+ "AddProfileFragmentListener interface");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserhandler = new UserHandler();
		mFamilyMembers = new ArrayList<ParseUser>();
		mFamilyMembersData = new ArrayList<UserData>();
		mLoggedUser = ParseUser.getCurrentUser();

		if (mLoggedUser == null) {
			// TODO: handle error
		}

		Bundle args = getArguments();
		if (args != null) {
			mIsUserFamily = args.getBoolean(USER_FAMILY);

			mFamilyId = args.getString(FAMILY_ID,
					mLoggedUser.getString(UserHandler.FAMILY_KEY));

			if (args.containsKey(FAMILY_NAME)) {
				mFamilyName = args.getString(FAMILY_NAME);
			}

			if (mIsUserFamily) {
				mFamilyId = mLoggedUser.getString(UserHandler.FAMILY_KEY);
				mFamilyName = mLoggedUser.getString(UserHandler.LAST_NAME_KEY);
			}

		}

		else {
			LogUtils.logWarning(getTag(),
					"family profile arguments did not pass");
		}

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_family_profile, container,
				false);
		mFamilyTitle = (TextView) root
				.findViewById(R.id.tv_family_profile_title);

		mParentLayoutRight = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_right);

		mParentLayoutLeft = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_left);

		mParentLayoutCenter = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parent_center);

		mChildrenHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_children_holder);

		mPhotoAlbumsHolder = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_album_holder);
		mAlbumProgress = (ProgressBar) root.findViewById(R.id.pb_album_spinner);

		mSingleMember = (RelativeLayout) root
				.findViewById(R.id.rl_family_profile_single_member);

		mSingleMemberPhoto = (RoundedImageView) root
				.findViewById(R.id.riv_family_profile_single_member_pic);

		mSingleMemberName = (TextView) root
				.findViewById(R.id.tv_family_profile_single_member_name);

		mFamilyParentsTitle = (TextView) root
				.findViewById(R.id.tv_family_profile_parents);
		mFamilyParentsTitle.setVisibility(View.GONE);

		mFamilyChildrenTitle = (TextView) root
				.findViewById(R.id.tv_family_profile_children);
		mFamilyChildrenTitle.setVisibility(View.GONE);

		noChildrenCover = (ImageView) root
				.findViewById(R.id.iv_family_profile_no_children);

		noParentsCover = (ImageView) root
				.findViewById(R.id.iv_family_profile_no_parents);

		mChildrenSpinner = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_children_load_spinner);

		mParentsSpinner = (LinearLayout) root
				.findViewById(R.id.ll_family_profile_parents_load_spinner);

		mFamilyName = InputHandler.capitalizeName(mFamilyName);
		mFamilyTitle.setText(mFamilyName + " Family");

		FamilyHandler.getFamilyById(mFamilyId, mIsUserFamily,
				new GetCallback<ParseObject>() {
					FamilyProfileFragment frag;

					@Override
					public void done(ParseObject family, ParseException e) {
						if (e != null) {
							LogUtils.logWarning("ROFL", e.getMessage());
							return;
						}

						frag.mCurrentFamily = family;
						if (mIsUserFamily) {
							frag.mUserhandler.fetchFamilyMembersLocally(
									mFamilyMembers, mFamilyMembersData,
									mCurrentFamily, mLoggedUser.getObjectId(),
									true, new FamilyMembersFetchCallback() {

										@Override
										public void done(ParseException e) {
											if (e != null) {
												// TODO: handle error
											} else {
												frag.initMembersViews();
											}
										}
									});
						}

						else {
							frag.mUserhandler.fetchFamilyMembers(
									mFamilyMembers, mFamilyMembersData,
									mCurrentFamily, mLoggedUser.getObjectId(),
									true, new FamilyMembersFetchCallback() {

										@Override
										public void done(ParseException e) {
											if (e != null) {
												// TODO: handle error
											} else {
												frag.initMembersViews();
											}
										}
									});
						}
					}

					GetCallback<ParseObject> init(FamilyProfileFragment frag) {
						this.frag = frag;
						return this;
					}
				}.init(this));

		initAnimations();
		initAlbumsListView();
		return root;
	}

	private void initAnimations() {
		textJump = AnimationUtils.loadAnimation(getActivity(),
				R.anim.pulse_once);
		textJump.setInterpolator(new AccelerateDecelerateInterpolator());
	}

	private void initMembersViews() {
		// init the data
		mParentsList = new ArrayList<UserData>();
		mChildrenList = new ArrayList<UserData>();

		if (mFamilyMembersData.size() == 1) {
			initSingleMemberView();
		}

		else {
			new AsyncTask<Void, Void, Void>() {
				FamilyProfileFragment frag;

				@Override
				protected Void doInBackground(Void... params) {
					for (UserData member : mFamilyMembersData) {
						if (member.getRole().equals(UserData.ROLE_PARENT)) {
							mParentsList.add(member);
						} else {
							mChildrenList.add(member);
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					initParentsLevelView();
					initChildrenLevelView();
				}

				private AsyncTask<Void, Void, Void> init(
						FamilyProfileFragment frag) {
					this.frag = frag;
					return this;
				}
			}.init(this).execute();
		}
	}

	private void initSingleMemberView() {
		UserData member = mFamilyMembersData.get(0);
		Bitmap photo = member.getprofilePhoto();
		String name = InputHandler.capitalizeName(member.getName());
		String msg = (mLoggedUser.getObjectId().equals(member.getUserId())) ? "You are the only member of this family"
				: name + " is the only member of this family";

		mSingleMemberName.setText(msg);

		if (photo != null) {
			mSingleMemberPhoto.setImageBitmap(photo);
			mSingleMemberPhoto.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
		}

		mChildrenSpinner.setVisibility(View.GONE);
		mParentsSpinner.setVisibility(View.GONE);
		mSingleMember.setVisibility(View.VISIBLE);
		mSingleMemberPhoto.setOnClickListener(this);
		mSingleMemberPhoto.setTag(ITEM_TYPE, FAMILY_MEMBER_ITEM_TYPE);
		mSingleMemberPhoto
				.setTag(FAMILY_MEMBER_TYPE, FAMILY_MEMBER_SINGLE_TYPE);
		mSingleMemberPhoto.setTag(ITEM_POS, 0);
	}

	private void initParentsLevelView() {
		new AsyncTask<Void, Object, int[]>() {
			class ParentViewData {
				public LinearLayout holder;
				public View v;

				public ParentViewData(LinearLayout holder, View v) {
					this.holder = holder;
					this.v = v;
				}
			}

			FamilyProfileFragment frag;

			@Override
			protected int[] doInBackground(Void... params) {
				frag.mParentAdapter = new FamilyProfileParentAdapter(
						getActivity(),
						frag.mParentsList
								.toArray(new UserData[frag.mParentsList.size()]));
				int mParentsListSize = frag.mParentsList.size();
				int[] size = { mParentsListSize };

				if (mParentsListSize == 0) {
					return size;
				}

				else if (mParentsListSize == 1) {
					View v = initParentView(0);
					ParentViewData data = new ParentViewData(
							frag.mParentLayoutCenter, v);

					publishProgress(data);
				}

				else {
					View parentView1 = initParentView(0);
					View parentView2 = initParentView(1);

					ParentViewData data1 = new ParentViewData(
							frag.mParentLayoutRight, parentView1);
					publishProgress(data1);

					ParentViewData data2 = new ParentViewData(
							frag.mParentLayoutLeft, parentView2);
					publishProgress(data2);
				}

				return size;
			}

			@Override
			protected void onProgressUpdate(Object... viewData) {
				super.onProgressUpdate(viewData);
				ParentViewData data = (ParentViewData) viewData[0];
				data.holder.addView(data.v);
			}

			@Override
			protected void onPostExecute(int[] result) {
				super.onPostExecute(result);
				int parentsNum = result[0];
				frag.mParentsSpinner.setVisibility(View.GONE);
				if (parentsNum == 0) {
					frag.noParentsCover.setVisibility(View.VISIBLE);
				}

				else if (parentsNum == 1) {
					frag.mFamilyParentsTitle.setVisibility(View.VISIBLE);
					frag.mParentLayoutCenter.setVisibility(View.VISIBLE);
					frag.mParentLayoutLeft.setVisibility(View.GONE);
					frag.mParentLayoutRight.setVisibility(View.GONE);
				}

				else {
					frag.mFamilyParentsTitle.setVisibility(View.VISIBLE);
				}
			}

			private AsyncTask<Void, Object, int[]> init(
					FamilyProfileFragment frag) {
				this.frag = frag;
				return this;
			}

			private View initParentView(int position) {
				View v = mParentAdapter.getView(position, null,
						(ViewGroup) getView());
				v.setTag(ITEM_TYPE, FAMILY_MEMBER_ITEM_TYPE);
				v.setTag(FAMILY_MEMBER_TYPE, FAMILY_MEMBER_PARENT_TYPE);
				v.setTag(ITEM_POS, position);
				v.setOnClickListener(frag);
				return v;
			}
		}.init(this).execute();
	}

	private void initChildrenLevelView() {
		// if the family does not have any child members
		mChildrenSpinner.setVisibility(View.GONE);
		if (mChildrenList.size() == 0) {
			mFamilyChildrenTitle.setVisibility(View.GONE);
			noChildrenCover.setVisibility(View.VISIBLE);
			return;
		}

		else {
			mFamilyChildrenTitle.setVisibility(View.VISIBLE);
			mFamilyChildrenTitle.startAnimation(textJump);
		}

		// init the children list view
		mChildrenAdapter = new FamilyProfileChildAdapter(getActivity(),
				mChildrenList.toArray(new UserData[mChildrenList.size()]));

		for (int i = 0; i < mChildrenList.size(); i++) {
			View v = mChildrenAdapter.getView(i, null, (ViewGroup) getView());
			v.setTag(ITEM_TYPE, FAMILY_MEMBER_ITEM_TYPE);
			v.setTag(FAMILY_MEMBER_TYPE, FAMILY_MEMBER_CHILD_TYPE);
			v.setTag(ITEM_POS, i);
			v.setOnClickListener(this);
			mChildrenHolder.addView(v);
		}
	}

	/**
	 * Initialize the albums list view
	 */
	private void initAlbumsListView() {
		if (mFamilyId.equals(mLoggedUser.getString(UserHandler.FAMILY_KEY))) {
			userFamilyAlbumSectionInit();
		} else {
			ParseQuery<ShareAlbum> query = ParseQuery.getQuery("ShareAlbum");
			query.whereEqualTo("albumOwnerId", mFamilyId);
			query.whereEqualTo("sharedWithId",
					ParseUser.getCurrentUser().get(UserHandler.FAMILY_KEY));
			query.orderByDescending("createdAt");
			query.findInBackground(new FindCallback<ShareAlbum>() {

				@Override
				public void done(List<ShareAlbum> objects, ParseException e) {
					mAlbumProgress.setVisibility(View.INVISIBLE);
					mShareAlbumsAdapter = new FamilyProfileSharedAlbumAdapter(
							getActivity(), objects);
					for (int i = 0; i < objects.size(); i++) {
						View v = mShareAlbumsAdapter.getView(i, null,
								(ViewGroup) getView());
						ImageButton album = ((ImageButton) v
								.findViewById(R.id.ib_album_image));

						objects.get(i).fetchIfNeededInBackground(
								new GetCallback<ShareAlbum>() {

									private ImageButton albumButton;

									@Override
									public void done(ShareAlbum object,
											ParseException e) {
										if (e == null) {

											ParseQuery<Album> query = ParseQuery
													.getQuery("Album");
											query.getInBackground(
													object.getAlbumId(),
													new GetCallback<Album>() {

														@Override
														public void done(
																Album album,
																ParseException e) {
															album.fetchIfNeededInBackground(new GetCallback<Album>() {

																@Override
																public void done(
																		Album object,
																		ParseException e) {
																	albumButton
																			.setTag(ALBUM_ITEM,
																					object);
																}
															});

														}

													});

										}
									}

									GetCallback<ShareAlbum> init(
											ImageButton albumButton) {
										this.albumButton = albumButton;
										return this;

									}

								}.init(album));

						mPhotoAlbumsHolder.addView(v);
						album.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(getActivity(),
										PhotoAlbumScreenActivity.class);
								Bundle args = new Bundle();
								Album album = (Album) v.getTag(ALBUM_ITEM);
								args.putInt(
										PhotoGridFragment.ALBUM_PHOTO_COUNT,
										album.getAlbumPhotoCount());
								args.putString(PhotoGridFragment.ALBUM_ITEM_ID,
										album.getObjectId());
								args.putString(PhotoGridFragment.ALBUM_SRC,
										FAMILY_PROFILE_FRAG);
								args.putString(PhotoGridFragment.ALBUM_NAME,
										album.getAlbumName());
								i.putExtra(PhotoGridFragment.ALBUM_ITEM, args);

								getActivity().startActivity(i);

							}
						});
					}
				}
			});
		}
	}

	private void userFamilyAlbumSectionInit() {
		ParseQuery<Album> query = ParseQuery.getQuery("Album");
		query.whereEqualTo(PhotoHandler.ALBUM_FAMILY_KEY, mFamilyId);

		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<Album>() {

			@Override
			public void done(List<Album> objects, ParseException e) {
				mAlbumProgress.setVisibility(View.INVISIBLE);
				mAlbumsAdapter = new FamilyProfileAlbumAdapter(getActivity(),
						objects);

				for (int i = 0; i < objects.size(); i++) {
					View v = mAlbumsAdapter.getView(i, null,
							(ViewGroup) getView());
					ImageButton album = ((ImageButton) v
							.findViewById(R.id.ib_album_image));
					objects.get(i).fetchIfNeededInBackground(
							new GetCallback<Album>() {

								private ImageButton album;

								@Override
								public void done(Album object, ParseException e) {
									if (e == null) {

										album.setTag(ALBUM_ITEM, object);
									}
								}

								GetCallback<Album> init(ImageButton album) {
									this.album = album;
									return this;

								}

							}.init(album));

					mPhotoAlbumsHolder.addView(v);
					album.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = new Intent(getActivity(),
									PhotoAlbumScreenActivity.class);
							Bundle args = new Bundle();
							Album album = (Album) v.getTag(ALBUM_ITEM);
							args.putInt(PhotoGridFragment.ALBUM_PHOTO_COUNT,
									album.getAlbumPhotoCount());
							args.putString(PhotoGridFragment.ALBUM_ITEM_ID,
									album.getObjectId());
							args.putString(PhotoGridFragment.ALBUM_SRC,
									FAMILY_PROFILE_FRAG);
							args.putString(PhotoGridFragment.ALBUM_NAME,
									album.getAlbumName());
							i.putExtra(PhotoGridFragment.ALBUM_ITEM, args);

							getActivity().startActivity(i);

						}
					});
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// if the currently clicked item is a family member profile
		if (FAMILY_MEMBER_ITEM_TYPE.equals(v.getTag(ITEM_TYPE))) {
			Bundle args = new Bundle();
			int pos = (Integer) v.getTag(ITEM_POS);
			UserData clickedMember = getMemberItem(
					(String) v.getTag(FAMILY_MEMBER_TYPE), pos);

			// if the user clicked is the currently logged user
			if (clickedMember.getUserId().equals(mLoggedUser.getObjectId())) {
				args.putBoolean(ProfileFragment.USER_PROFILE, true);
			}

			else {
				args.putBoolean(ProfileFragment.USER_PROFILE, false);
				args.putParcelable(ProfileFragment.MEMBER_ITEM, clickedMember);
			}

			profileFragmentAdder.addProfileFragment(args);
		}
	}

	private UserData getMemberItem(String type, int pos) {
		if (FAMILY_MEMBER_SINGLE_TYPE.equals(type)) {
			return mFamilyMembersData.get(pos);
		}

		if (FAMILY_MEMBER_PARENT_TYPE.equals(type)) {
			return mParentsList.get(pos);
		}

		else if (FAMILY_MEMBER_CHILD_TYPE.equals(type)) {
			return mChildrenList.get(pos);
		}

		else {
			LogUtils.logWarning(getTag(), "bad family member type entered");
			return null;
		}
	}
}
