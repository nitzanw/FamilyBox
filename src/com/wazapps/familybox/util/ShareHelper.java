//package com.wazapps.familybox.util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ComponentName;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//
//
//public class ShareHelper {
//		FragmentActivity mActivity;
//	String subject;
//	String body;
//	Facebook facebook;
//	RecipeObj mRecipe;
//	private ShareFactory mShareFactory;
//	ArrayList<Intent> mTargetedShareIntents;
//
//	public ShareHelper(SherlockFragmentActivity activity, RecipeObj recipe) {
//		this.mActivity = activity;
//		mRecipe = recipe;
//		facebook = null;
//		mShareFactory = ShareFactory.getSFInstance();
//		mShareFactory.setActivity(mActivity);
//
//	}
//
//	private List<ResolveInfo> getIntentList() {
//
//		boolean twitResolved = false;
//		ResolveInfo twitinfo = null;
//
//		mTargetedShareIntents = new ArrayList<Intent>();
//		List<ResolveInfo> riList = new ArrayList<ResolveInfo>();
//
//		Intent share = new Intent(android.content.Intent.ACTION_SEND);
//		share.setType("text/plain");
//		List<ResolveInfo> resInfo = mActivity.getPackageManager()
//				.queryIntentActivities(share, 0);
//		if (!resInfo.isEmpty()) {
//			for (ResolveInfo info : resInfo) {
//				Intent targetedShare = new Intent(
//						android.content.Intent.ACTION_SEND);
//				targetedShare.setComponent(new ComponentName(
//						info.activityInfo.packageName, info.activityInfo.name));
//				targetedShare.setType("text/plain");
//				if (info.activityInfo.packageName.contains("android.gm")
//						|| info.activityInfo.name.contains("android.email")
//						|| info.activityInfo.packageName.contains("mail")) {
//					targetedShare.setType("message/rfc822"); // put here your
//																// mime type
//					targetedShare.putExtra(
//							Intent.EXTRA_SUBJECT,
//							mActivity.getResources().getString(
//									R.string.share_email_subject));
//					String mail = mShareFactory.getRecipeInHTMLforMail();
//					targetedShare.putExtra(Intent.EXTRA_TEXT,
//							Html.fromHtml(mail));
//					MyApplication vcb = (MyApplication) mActivity.getApplicationContext();
//					Uri media = vcb.getMediaStoreImageUri();
//					if(media != null){
//					targetedShare.putExtra(Intent.EXTRA_STREAM,
//							media);
//					}
//					riList.add(info);
//					mTargetedShareIntents.add(targetedShare);
//					// targetedShareIntents.add(targetedShare);
//				} else if (info.activityInfo.packageName.contains("mms")) {
//					targetedShare.putExtra(Intent.EXTRA_TEXT,
//							mShareFactory.getRecipeforSMS());
//					riList.add(info);
//					mTargetedShareIntents.add(targetedShare);
//				} else if (info.activityInfo.packageName
//						.contains("com.facebook.katana")) {
//					riList.add(info);
//
//				} else if (info.activityInfo.packageName.contains("twitter")) {
//					twitResolved = true;
//					twitinfo = info;
//
//				}
//			}
//		}
//		if (twitResolved) {
//			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//			intent.setComponent(new ComponentName(
//					twitinfo.activityInfo.packageName, twitinfo.activityInfo.name));
//			intent.setType("text/plain");
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            intent.putExtra(Intent.EXTRA_TEXT, mShareFactory.getTwitMsg());
//			mTargetedShareIntents.add(intent);
//			riList.add(twitinfo);
//		}
//		return riList;
//	}
//
//	public Facebook share() {
//		final List<ResolveInfo> riList = getIntentList();
//		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//		builder.setTitle(mActivity.getString(R.string.share_chooser_text));
//		final ShareIntentListAdapter adapter = new ShareIntentListAdapter(
//				 mActivity, R.layout.share_row, riList
//				.toArray());
//		
//		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			
//				ResolveInfo info = (ResolveInfo) adapter.getItem(which);
//				if (info.activityInfo.packageName.contains("facebook")) {
//					Bundle args = new Bundle();
//					args.putInt(FacebookMainActivity.FACEBOOK_CONNECT_MODE,
//							FacebookMainActivity.SHARE_FRAG);
//			//
//					Intent intent = new Intent(mActivity,
//							FacebookMainActivity.class);
//					 intent.putExtras(args);
//					 mActivity.startActivity(intent);
//				} else if(info.activityInfo.packageName.contains("twitter")){
//					
//					Intent intent = mTargetedShareIntents.get(mTargetedShareIntents.size()-1);
//					(mActivity).startActivity(intent);
//					updateUserShare();
//				}else {
//					
//					Intent intent = mTargetedShareIntents.get(which);
//					(mActivity).startActivity(intent);
//					updateUserShare();
//				}
//			}
//		});
//		builder.create().show();
//		return facebook;
//	}
//	private void updateUserShare(){
//		new UpdateUser((MyApplication) mActivity.getApplication(), 2, 0, 1, "", "", "", SplashActivity.mDeviceId, "")
//		.execute();
//	}
//	public class ShareIntentListAdapter extends ArrayAdapter<Object> {
//		Activity context;
//		Object[] items;
//		boolean[] arrows;
//		int layoutId;
//
//	
//		public ShareIntentListAdapter(Activity context, int layoutId,
//				Object[] items) {
//			super(context, layoutId, items);
//
//			this.context = context;
//			this.items = items;
//			this.layoutId = layoutId;
//		}
//
//		public View getView(int pos, View convertView, ViewGroup parent) {
//			LayoutInflater inflater = context.getLayoutInflater();
//			View row = inflater.inflate(layoutId, null);
//			TextView label = (TextView) row.findViewById(R.id.text1);
//			label.setText(((ResolveInfo) items[pos]).activityInfo.applicationInfo
//					.loadLabel(context.getPackageManager()).toString());
//			ImageView image = (ImageView) row.findViewById(R.id.logo);
//			image.setImageDrawable(((ResolveInfo) items[pos]).activityInfo.applicationInfo
//					.loadIcon(context.getPackageManager()));
//
//			return (row);
//		}
//	}
//}
//
