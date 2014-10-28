package com.wazapps.familybox.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wazapps.familybox.R;

public class MultiImageChooserActivity extends FragmentActivity implements
		OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = "Collage";
	public static final String COL_WIDTH_KEY = "COL_WIDTH";
	public static final String FLURRY_EVENT_ADD_MULTIPLE_IMAGES = "Add multiple images";

	// El tama��o por defecto es 100 porque los thumbnails MICRO_KIND son de
	// 96x96
	private static final int DEFAULT_COLUMN_WIDTH = 200;

	public static final int NOLIMIT = -1;
	public static final String MAX_IMAGES_KEY = "MAX_IMAGES";

	private ImageAdapter ia;
	Set<Integer> photoIdList = new HashSet<Integer>();;

	Cursor imagecursor;
	private Cursor actualimagecursor;
	int image_column_index;
	private int actual_image_column_index;
	int colWidth;

	private static final int CURSORLOADER_THUMBS = 0;
	private static final int CURSORLOADER_REAL = 1;
	private static final String TOTAL_FILES = "total files";
	public static final String MULTIPLE_FILE_IDS = "multiple file ids";
	private static final int ID = R.string.id;
	private Set<String> fileNames = new HashSet<String>();

	private SparseBooleanArray checkStatus = new SparseBooleanArray();

	private TextView freeLabel = null;
	private int maxImages;
	private boolean unlimitedImages = false;

	private GridView gridView;

	final ImageFetcher fetcher = new ImageFetcher();

	int selectedColor = Color.GREEN;
	boolean shouldRequestThumb = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// int theme = getIntent().getIntExtra("THEME", R.style.CollageTheme);
		// setTheme(theme);
		setContentView(R.layout.multiselectorgrid);
		fileNames.clear();
		photoIdList.clear();
		maxImages = getIntent().getIntExtra(MAX_IMAGES_KEY, NOLIMIT);

		unlimitedImages = maxImages == NOLIMIT;
		if (!unlimitedImages) {
			freeLabel = (TextView) findViewById(R.id.label_images_left);
			freeLabel.setVisibility(View.VISIBLE);
			updateLabel();
		}

		colWidth = getIntent().getIntExtra(COL_WIDTH_KEY, DEFAULT_COLUMN_WIDTH);

		Display display = getWindowManager().getDefaultDisplay();

		Point p = new Point();
		display.getSize(p);
		int width = p.x;

		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setColumnWidth(width / 2);
		gridView.setNumColumns(2);
		gridView.setOnItemClickListener(this);
		gridView.setOnScrollListener(new OnScrollListener() {

			private int lastFirstItem = 0;
			private long timestamp = System.currentTimeMillis();

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					Log.d(TAG, "IDLE - Reload!");
					shouldRequestThumb = true;
					ia.notifyDataSetChanged();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				float dt = System.currentTimeMillis() - timestamp;
				if (firstVisibleItem != lastFirstItem) {
					double speed = 1 / dt * 1000;
					lastFirstItem = firstVisibleItem;
					timestamp = System.currentTimeMillis();
					Log.d(TAG, "Speed: " + speed + " elements/second");

					// Limitarlo si vamos a m��s de una p��gina por segundo...
					shouldRequestThumb = speed < visibleItemCount;
				}
			}
		});
		selectedColor = getResources().getColor(R.color.orange_fb);
		// selectedColor = Color.RED;

		// gridView.setBackgroundColor(bgColor);
		// gridView.setBackgroundResource(R.drawable.grid_background);

		ia = new ImageAdapter(this);
		gridView.setAdapter(ia);

		LoaderManager.enableDebugLogging(false);
		getSupportLoaderManager().initLoader(CURSORLOADER_THUMBS, null, this);
		getSupportLoaderManager().initLoader(CURSORLOADER_REAL, null, this);
		setupHeader();
		updateAcceptButton();
	}

	private void setupHeader() {
		// From Roman Nurik's code
		// https://plus.google.com/113735310430199015092/posts/R49wVvcDoEW
		// Inflate a "Done/Discard" custom action bar view.
		LayoutInflater inflater = (LayoutInflater) getActionBar()
				.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		final View customActionBarView = inflater.inflate(
				R.layout.actionbar_custom_view_done_discard, null);
		customActionBarView.findViewById(R.id.actionbar_done)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// "Done"
						selectClicked(null);
					}
				});
		customActionBarView.findViewById(R.id.actionbar_cancel)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		// Show the custom action bar view and hide the normal Home icon and
		// title.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setCustomView(customActionBarView,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
	}

	private void updateLabel() {
		if (freeLabel != null) {
			String text = String.format(getString(R.string.free_version_label),
					maxImages);
			freeLabel.setText(text);
			if (maxImages == 0) {
				freeLabel.setTextColor(Color.RED);
			} else {
				freeLabel.setTextColor(0xff777777);
			}
		}
	}

	private String getImageName(int position) {
		actualimagecursor.moveToPosition(position);
		String name = null;

		try {
			name = actualimagecursor.getString(actual_image_column_index);
		} catch (Exception e) {
			return null;
		}
		return name;
	}

	private void setChecked(int position, boolean b) {
		checkStatus.put(position, b);
	}

	public boolean isChecked(int position) {
		boolean ret = checkStatus.get(position);
		return ret;
	}

	public void cancelClicked(View ignored) {
		setResult(RESULT_CANCELED);
		finish();
	}

	public void selectClicked(View ignored) {
		Intent data = new Intent();
		if (fileNames.isEmpty()) {
			this.setResult(RESULT_CANCELED);
		} else {

			// ArrayList<String> al = new ArrayList<String>();
			// al.addAll(fileNames);
			Bundle res = new Bundle();

			List<Integer> al = new ArrayList<Integer>(photoIdList);

			res.putIntegerArrayList(MULTIPLE_FILE_IDS, (ArrayList<Integer>) al);
			if (imagecursor != null) {
				res.putInt(TOTAL_FILES, imagecursor.getCount());
			}

			data.putExtras(res);
			this.setResult(RESULT_OK, data);
		}
		Log.d(TAG, "Returning " + fileNames.size() + " items");
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		String name = getImageName(position);

		if (name == null) {
			return;
		}
		boolean isChecked = !isChecked(position);
		// PhotoMix.Log("DAVID", "Posicion " + position + " isChecked: " +
		// isChecked);
		if (!unlimitedImages && maxImages == 0 && isChecked) {
			// PhotoMix.Log("DAVID", "Aqu�� no deber��a entrar...");
			isChecked = false;
		}

		if (isChecked) {
			// Solo se resta un slot si hemos introducido un
			// filename de verdad...
			if (fileNames.add(name)) {
				maxImages--;
				photoIdList.add((Integer) view.getTag(ID));
				view.setBackgroundColor(selectedColor);
			}
		} else {
			if (fileNames.remove(name)) {
				// Solo incrementa los slots libres si hemos
				// "liberado" uno...
				maxImages++;
				Integer photoId = (Integer) view.getTag(ID);
				if (photoIdList.contains(photoId)) {
					photoIdList.remove(photoId);
				}
				view.setBackgroundColor(Color.TRANSPARENT);
			}
		}

		setChecked(position, isChecked);
		updateAcceptButton();
		updateLabel();

	}

	private void updateAcceptButton() {
		((TextView) getActionBar().getCustomView().findViewById(R.id.tv_done))
				.setEnabled(fileNames.size() != 0);
		getActionBar().getCustomView().findViewById(R.id.actionbar_done)
				.setEnabled(fileNames.size() != 0);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int cursorID, Bundle arg1) {
		CursorLoader cl = null;

		ArrayList<String> img = new ArrayList<String>();
		switch (cursorID) {

		case CURSORLOADER_THUMBS:
			img.add(MediaStore.Images.Media._ID);
			break;
		case CURSORLOADER_REAL:
			img.add(MediaStore.Images.Thumbnails.DATA);
			break;
		default:
			break;
		}

		cl = new CursorLoader(MultiImageChooserActivity.this,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				img.toArray(new String[img.size()]), null, null, null);
		return cl;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor == null) {
			// NULL cursor. This usually means there's no image database yet....
			return;
		}

		switch (loader.getId()) {
		case CURSORLOADER_THUMBS:
			imagecursor = cursor;
			image_column_index = imagecursor
					.getColumnIndex(MediaStore.Images.Media._ID);
			ia.notifyDataSetChanged();
			break;
		case CURSORLOADER_REAL:
			actualimagecursor = cursor;
			actual_image_column_index = actualimagecursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == CURSORLOADER_THUMBS) {
			imagecursor = null;
		} else if (loader.getId() == CURSORLOADER_REAL) {
			actualimagecursor = null;
		}
	}

	public class ImageAdapter extends BaseAdapter {

		/**
		 * 
		 */
		private final Bitmap mPlaceHolderBitmap;
		private LayoutInflater inflater;

		public ImageAdapter(Context c) {

			if (photoIdList == null) {
				photoIdList = new HashSet<Integer>();
			} else {
				photoIdList.clear();
			}
			Bitmap tmpHolderBitmap = BitmapFactory.decodeResource(
					getResources(), R.drawable.stub);
			mPlaceHolderBitmap = Bitmap.createScaledBitmap(tmpHolderBitmap,
					colWidth, colWidth, false);
			if (tmpHolderBitmap != mPlaceHolderBitmap) {
				tmpHolderBitmap.recycle();
				tmpHolderBitmap = null;
			}
			inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			if (imagecursor != null) {
				return imagecursor.getCount();
			} else {
				return 0;
			}
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int pos, View vi, ViewGroup parent) {
			ImageView imageView = (ImageView) vi;

			if (imageView == null) {
				imageView = (ImageView) inflater.inflate(
						R.layout.image_picker_item, parent, false);

			}

			imageView.setImageBitmap(null);

			final int position = pos;

			if (!imagecursor.moveToPosition(position)) {
				return imageView;
			}

			if (image_column_index == -1) {
				return imageView;
			}

			final int id = imagecursor.getInt(image_column_index);
			imageView.setTag(ID, id);
			if (isChecked(pos)) {

				imageView.setBackgroundColor(selectedColor);
			} else {

				imageView.setBackgroundColor(Color.TRANSPARENT);
			}
			if (shouldRequestThumb) {
				fetcher.fetch(Integer.valueOf(id), imageView, colWidth);
			}

			return imageView;
		}
	}
}