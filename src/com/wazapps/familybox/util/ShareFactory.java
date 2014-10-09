//package com.wazapps.familybox.util;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Environment;
//
//
//
//
//public class ShareFactory {
//	private static final String VEGAN_COOK_BOOK_FB_PAGE = "https://www.facebook.com/pages/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%98%D7%91%D7%A2%D7%95%D7%A0%D7%99%D7%99%D7%9D-Vegan-Cook-Book/270023029824076";
//
//	
//	FragmentActivity mActivity;
//	String mTinyUrl = "";
//	public Uri mMediaStoreImageUri = null;
//	private String NEW_LINE = "\n";
//	private int mWinPoints;
//	private static ShareFactory shareFactoryInstance;
//
//	private ShareFactory() {
//	}
//
//	// get the singeltone
//	public static ShareFactory getSFInstance() {
//		if (shareFactoryInstance == null) {
//			shareFactoryInstance = new ShareFactory();
//		}
//		return shareFactoryInstance;
//	}
//
//	public void initShareFactory(RecipeObj recipeObj) {
//		mRecipeData = recipeObj;
//
//		String uri = SearchActivity._recipeImageLocation
//				+ mRecipeData.getFields().getPhoto1();
//		new GetTinyUrlTask(mRecipeData.getFields().getRecipeUrl()).execute();
//		if (uri != null
//				&& Environment.MEDIA_MOUNTED.equals(Environment
//						.getExternalStorageState())) {
//			new GetRecipeImageTask(uri).execute();
//		}
//	}
//
//	public void setActivity(SherlockFragmentActivity activity) {
//		mActivity = activity;
//	}
//
//	public String getFacebookRecipeCaption() {
//		return mActivity.getResources().getString(R.string.app_name);
//	}
//
//	public String getAnimalPrizeFacebookCaption() {
//		return mActivity.getResources().getString(R.string.share_animal_prize);
//	}
//
//	public String getAnimalPrizeFacebookDescription() {
//		return mActivity.getResources().getString(
//				AchivmentsMap.getshareAnimalText(mWinPoints));
//	}
//
//	public String getFacebookRecipeImage() {
//		return SearchActivity._recipeImageLocation
//				+ mRecipeData.getFields().getPhoto1();
//	}
//
//	public String getFacebookAnimalPrizeImage() {
//		String tmp = SearchActivity._animalImageLocation // AchivmentsMap.getSmallAnimalDrawableName(winPoints);
//				+ AchivmentsMap.getAnimalShareDrawableName(mWinPoints);
//		return tmp;
//
//	}
//
//	public String getFacebookRecipeLink() {
//		if (mRecipeData != null) {
//			return mRecipeData.getFields().getRecipeUrl();
//		} else {
//			return VEGAN_COOK_BOOK_FB_PAGE;
//		}
//	}
//
//	public String getFacebookTitle() {
//		if (mRecipeData != null) {
//			return mRecipeData.getFields().getName()
//					+ " | "
//					+ mRecipeData.getFields().getUserProfile().getFields()
//							.getBloger();
//		}
//		return mActivity.getString(R.string.problem);
//	}
//
//	public String getRecipeforSMS() {
//		if (mRecipeData != null && !mTinyUrl.equals("")) {
//			String title = mRecipeData.getFields().getName() + NEW_LINE;
//			String bloger = mRecipeData.getFields().getUserProfile()
//					.getFields().getBloger();
//			String temp = mActivity.getResources().getString(
//					R.string.great_recipe_for)
//					+ title
//					+ " "
//					+ bloger
//					+ "# "
//					+ mActivity.getResources().getString(R.string.app_name)
//					+ "# " + mTinyUrl;
//			return temp;
//		}
//		return mActivity.getString(R.string.problem);
//	}
//
//	public String getRecipeInHTMLforMail() {
//		if(mRecipeData != null){
//		// set the title of the email:
//		String title = mRecipeData.getFields().getName() + NEW_LINE;
//
//		String fromBlog = mActivity.getString(R.string.from_blog)
//				+ " "
//				+ mRecipeData.getFields().getUserProfile().getFields()
//						.getBloger();
//		String ingTitle = mActivity.getString(R.string.ingrdients);
//		String ingListString = "";
//		TreeMap<Integer, HashMap<String, String>> ingMap = Utils
//				.orderIngredients(mRecipeData.getFields().getIngredients());
//		Iterator<Entry<Integer, HashMap<String, String>>> entries = ingMap
//				.entrySet().iterator();
//		while (entries.hasNext()) {
//			Entry<Integer, HashMap<String, String>> thisEntry = (Entry<Integer, HashMap<String, String>>) entries
//					.next();
//			HashMap<String, String> value = thisEntry.getValue();
//			String ingredientName = value.get(Utils.ING_NAME);
//			if (ingredientName.equals(mActivity.getString(R.string.other_ing))) {
//				ingListString += "<p>" + value.get(Utils.ING_OTHER);
//			} else {
//				ingListString += "<p>" + ingredientName;
//			}
//			// if there is no kind, remove the padding so it won't look
//			// too spaced
//			if (!value.get(Utils.ING_KIND).equals("")) {
//				ingListString += " " + value.get(Utils.ING_KIND);
//			}
//
//			if (!value.get(Utils.ING_QUANTITY).equals("")) {
//				ingListString += " - " + value.get(Utils.ING_QUANTITY);
//			}
//
//			// if there is no quntity give hypen to units
//			if (!value.get(Utils.ING_UNIT).equals("")) {
//				if (!value.get(Utils.ING_QUANTITY).equals("")) {
//					ingListString += " " + value.get(Utils.ING_UNIT);
//				} else {
//					ingListString += " - " + value.get(Utils.ING_UNIT);
//				}
//			}
//
//			// if there's a description. add it
//			if (!value.get(Utils.ING_DESC).equals("")) {
//				if (value.get(Utils.ING_QUANTITY).equals("")
//						|| value.get(Utils.ING_UNIT).equals("")) {
//					ingListString += " - " + value.get(Utils.ING_DESC);
//				} else {
//					ingListString += " " + value.get(Utils.ING_DESC);
//				}
//			}
//			ingListString += "</p><hr>";
//		}
//		String prepTitle = mActivity.getString(R.string.prepration);
//		String prepration = mRecipeData.getFields().getPrepDesc();
//
//		while (mTinyUrl.equals(""))
//			;
//		String temp = ("<!DOCTYPE html><html><body><h1>" + title
//				+ "</h1><hr><p>" + fromBlog + "</p>" + "<hr>" + NEW_LINE
//				+ "<h2>" + ingTitle + "</h2>" + NEW_LINE + "" + ingListString
//				+ "<hr>" + NEW_LINE + "<h2>" + prepTitle + "</h2>" + "<hr><p>"
//				+ prepration + "</p>" + "<hr>" + NEW_LINE + "<hr><a href="
//				+ mTinyUrl + ">" + mActivity.getString(R.string.link_to_recipe) + "</a></body></html>");
//		return temp;
//		}
//		return "";
//	}
//
//	// public Uri getImageToshare() {
//	//
//	// while (mMediaStoreImageUri.equals(null))
//	// ;
//	// return mMediaStoreImageUri;
//	// }
//
//	public class GetRecipeImageTask extends AsyncTask<String, Integer, Uri> {
//		String mUri;
//
//		public GetRecipeImageTask(String uri) {
//			mUri = uri;
//		}
//
//		@Override
//		protected Uri doInBackground(String... arg0) {
//			try {
//				// Download the icon...
//				URL iconUrl;
//				iconUrl = new URL(mUri);
//
//				HttpURLConnection connection = (HttpURLConnection) iconUrl
//						.openConnection();
//				connection.setConnectTimeout(30000);
//				connection.setReadTimeout(30000);
//				InputStream input = connection.getInputStream();
//				if (input != null) {
//					Bitmap immutableBpm = BitmapFactory.decodeStream(input);
//
//					// Save the downloaded icon to the pictures folder on the SD
//					// Card
//					File directory = Environment
//							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//					directory.mkdirs(); // Make sure the Pictures directory
//										// exists.
//					File destinationFile = new File(directory, "recipeImg.jpeg");
//					FileOutputStream out = new FileOutputStream(destinationFile);
//					if (immutableBpm != null) {
//						immutableBpm.compress(Bitmap.CompressFormat.JPEG, 90,
//								out);
//						return Uri.fromFile(destinationFile);
//					}
//					out.flush();
//					out.close();
//				}
//				// intent.putExtra(Intent.EXTRA_STREAM, mediaStoreImageUri);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Uri result) {
//			if (result != null) {
//				super.onPostExecute(result);
//				MyApplication vcb = (MyApplication) mActivity
//						.getApplicationContext();
//				vcb.setMediaStoreImageUri(result);
//			}
//		}
//
//	}
//
//	public class GetTinyUrlTask extends AsyncTask<String, Integer, Integer> {
//		String recipeUrl;
//
//		public GetTinyUrlTask(String url) {
//			recipeUrl = url;
//		}
//
//		@Override
//		protected Integer doInBackground(String... arg0) {
//			TinyUrl tiny = new TinyUrl();
//
//			mTinyUrl = tiny.getTinyUrl(recipeUrl);
//			return null;
//
//		}
//	}
//
//	public String getTwitMsg() {
//		String twit = mRecipeData.getFields().getName()
//				+ " "
//				+ mTinyUrl
//				+ " #"
//				+ mActivity.getResources().getString(R.string.app_name)
//				+ " #"
//				+ mRecipeData.getFields().getUserProfile().getFields()
//						.getBloger();
//		return twit;
//	}
//
//	public void setWinPoints(int winPoints) {
//		mWinPoints = winPoints;
//	}
//}
