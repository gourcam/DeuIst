package com.deu.istatistik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.deu.istatistik.R;
import com.flurry.android.FlurryAgent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Kutuphane {

	String flurryID = "K4DRB4T2CM8DY746HB5Q";
	SlidingMenu menu;

	public void getSolMenu(final Activity activity, Context context) {
		menu = new SlidingMenu(context);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.sol_menu);

		LinearLayout line = (LinearLayout) activity
				.findViewById(R.id.OrtHesapla_Click);
		line.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.deu.istatistik.ORTHESACTIVITY");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);

			}
		});

		LinearLayout hrt = (LinearLayout) activity
				.findViewById(R.id.HaritaClick);
		hrt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.deu.istatistik.HARITAACTIVITY");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}
		});

		LinearLayout tablo = (LinearLayout) activity
				.findViewById(R.id.TabloClick);
		tablo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.deu.istatistik.TABLOACTIVITY");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}
		});

		LinearLayout duyurular = (LinearLayout) activity
				.findViewById(R.id.DuyurularClick);
		duyurular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.deu.istatistik.ACILIS");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}
		});
		LinearLayout DescriptionStatsClick = (LinearLayout) activity
				.findViewById(R.id.DescriptionStatsClick);
		DescriptionStatsClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.aka.stat.DESCRIPTIONSTATS");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}
		});

		activity.findViewById(R.id.btn_settings).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View vi) {
//						Intent intent = new Intent(activity,
//								IletisimActivity.class);
//						// "com.deu.istatistik.ILETISIMACTIVITY");
//						activity.startActivity(intent);
					}
				});

	}

	public void showSlidingMenu() {
		menu.showMenu();
	}

	public void getAlertDialog(Context context, String baslik, String mesaj) {
		// AlertDialog.Builder ile AlertDialog nesnesini düzenle
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle(baslik);
		alertDialogBuilder
				.setMessage(mesaj)
				.setCancelable(false)
				.setPositiveButton("Tamam",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();

							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public ArrayList<String> getDosya(Context context, String dosyaadi) {
		ArrayList<String> dizi = new ArrayList<String>();

		try {
			InputStream dosya = context.getAssets().open(dosyaadi);

			// InputStream dss =
			// context.getResources().openRawResource(R.raw.qwerty);

			BufferedReader qq = new BufferedReader(new InputStreamReader(dosya));
			String receivestring = qq.readLine();

			while (receivestring != null) {

				dizi.add(receivestring);

				receivestring = qq.readLine();
			}

			qq.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return dizi;

	}

	public String TemizString(String deger) {
		String yenideger = deger.trim().replace(".", "").replace(" ", "")
				.replace("ö", "o").replace("ü", "u").replace(",", "")
				.replace("ý", "i");
		return "_" + yenideger;
	}

	// android.permission.ACCESS_NETWORK_STATE iznini almayý unutma
	public static boolean isConnectionInternet(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo durum = con.getActiveNetworkInfo();

		if (durum.isConnectedOrConnecting() && durum != null) {
			return true;

		} else {
			return false;
		}

	}

	public boolean internetErisimi(Context context) {

		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public String TagTemizle(String deger) {

		String sonuc = deger.trim();
		sonuc = Html.fromHtml(sonuc).toString();
		while (sonuc.contains("<")) {

			StringBuilder strbuild = new StringBuilder(sonuc);

			int tagBaslangicIndis = strbuild.indexOf("<");
			int tagBitisIndis = strbuild.indexOf(">", tagBaslangicIndis);

			strbuild.delete(tagBaslangicIndis, tagBitisIndis + 1);

			sonuc = strbuild.toString();

		}
		return sonuc;
	}

	public void startFlurry(Context context) {
		FlurryAgent.onStartSession(context, flurryID);

	}

	public void stopFlurry(Context context) {
		FlurryAgent.onEndSession(context);

	}

	public void sharedPreferencesEdit(Context context, String key, String value) {

		SharedPreferences shr = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = shr.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public String getsharedPreference(Context context, String key) {
		SharedPreferences shr = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		return shr.getString(key, null);

	}

	public String getPackageVersionName(Context context) {
		try {
			String versionname = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName.toString();
			return versionname;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public int getPackageVersionCode(Context context) {
		try {
			int versioncode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
			return versioncode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	public int getAndroidSdkVersionCode() {
		int androidverisonsdkint = android.os.Build.VERSION.SDK_INT;
		return androidverisonsdkint;
	}

	public String getAndroidSdkVersionName() {
		String androidverisonsdkint = android.os.Build.VERSION.CODENAME;
		return androidverisonsdkint;
	}

}
