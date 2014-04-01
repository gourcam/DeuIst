package com.deu.istatistik;

import static com.deu.deuistatistik.gcm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.deu.deuistatistik.gcm.CommonUtilities.EXTRA_MESSAGE;
import static com.deu.deuistatistik.gcm.CommonUtilities.SENDER_ID;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deu.deuistatistik.gcm.ServerUtilities;
import com.deu.deuistatistik.gcm.WakeLocker;
import com.deu.istatistik.estatXmlParser.Entry;
import com.flurry.android.Constants;
import com.flurry.android.FlurryAgent;
import com.google.android.gcm.GCMRegistrar;

public class Acilis extends Activity {

	Kutuphane kutuphane = new Kutuphane();
	DbHelper veritabani = new DbHelper(this, "tb_Acilis");

	private static String URL = "";
	// ///////////////////////
	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";
	private static final String estatURL = "http://estat.deu.edu.tr/syndication.php?m=duyuru";

	// Whether there is a Wi-Fi connection.
	private static boolean wifiConnected = false;
	// Whether there is a mobile connection.
	private static boolean mobileConnected = false;
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;

	// The user's current network preference setting.
	public static String sPref = null;

	// The BroadcastReceiver that tracks network connectivity changes.
	private NetworkReceiver receiver = new NetworkReceiver();

	// /////////////////
	SharedPreferences shr = null;

	// GcmRegistrar AsyncTask
	AsyncTask<Void, Void, Void> asy;

	// Progress Diaolog for download to acilisjSonVeriler
	ProgressDialog prgdialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		URL = getResources().getString(R.string.jsonUrl);

		// int androidverisonsdkint = kutuphane.getAndroidSdkVersionCode();
		// Toast.makeText(this, Integer.toString(androidverisonsdkint),
		// Toast.LENGTH_LONG).show();

		kutuphane.getSolMenu(this, getApplicationContext());
		kutuphane.startFlurry(this);

		FlurryAgent.logEvent("Inbox");
		FlurryAgent.setAge(25);
		FlurryAgent.setGender(Constants.MALE);
		FlurryAgent.setVersionName("1.0");
		Log.e("onCreate", "onCreate Çalýþtý");

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Dokuz Eylül Üniversitesi");
		actionBar.setTitle("Ýstatistik");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();
		// //////////
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);

		// /////////////
		if (kutuphane.internetErisimi(getApplicationContext())) {
			GcmKayitIslemleri();
		}

		AcilisListviewgetRow();

		// XmlParser();
		// Intent intent = new
		// Intent(Intent.ACTION_VIEW,Uri.parse("google.navigation:q=34.232323,35.454454"));
		// startActivity(intent);

		//
		// Intent intent = new
		// Intent("com.google.android.c2dm.intent.REGISTER");
		// intent.putExtra("sender", "AIzaSyCNBDgmcn4qXbDuCX6tmbPAElcEdaPcRjw");
		// intent.putExtra("app", PendingIntent.getBroadcast(this, 0, new
		// Intent(), 0));
		// startService(intent);

	}

	private void AcilisListviewgetRow() {

		String listviewsatirsayisi = kutuphane.getsharedPreference(
				getApplicationContext(), "listviewsatirsayisi");

		if (listviewsatirsayisi != null || listviewsatirsayisi == "") {

			Entry entry = new Entry();
			for (int i = 0; i < Integer.parseInt(listviewsatirsayisi); i++) {
				String sharedbaslik = "acilisliste_" + entry.getTitle() + "_"
						+ i;
				String sharedyazi = "acilisliste_" + entry.getDescription()
						+ "_" + i;
				String sharedhtmltitle = "acilisliste_" + entry.getHtml_title()
						+ "_" + i;
				String sharedhtmldescription = "acilisliste_"
						+ entry.getHtml_description() + "_" + i;

				String baslik = kutuphane.getsharedPreference(Acilis.this,
						sharedbaslik);
				String yazi = kutuphane.getsharedPreference(Acilis.this,
						sharedyazi);
				String htmltitle = kutuphane.getsharedPreference(Acilis.this,
						sharedhtmltitle);
				String htmldescription = kutuphane.getsharedPreference(
						Acilis.this, sharedhtmldescription);

				Entry entry1 = new Entry();

				entry1.setTitle(baslik);
				entry1.setDescription(yazi);
				entry1.setHtml_title(htmltitle);
				entry1.setHtml_description(htmldescription);
				listviewSatirDoldur(entry1);
			}

			ListView listview_Acilis = (ListView) findViewById(R.id.listview_Acilis);

			AcilisListViewAdapter adapp = new AcilisListViewAdapter(
					Acilis.this, entrylistem);
			listview_Acilis.setAdapter(adapp);
		} else {
			kutuphane
					.getAlertDialog(Acilis.this, "Uyarý",
							"Son duyurularý almak için lütfen internetiniz açýnýz ve sayfayý yenileyiniz");
		}
	}

	ArrayList<Entry> entrylistem = new ArrayList<Entry>();

	private void listviewSatirDoldur(Entry entry) {
		entrylistem.add(entry);
	}

	private class fillAcilisListView extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			try {
				entrylistem.clear();
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException e) {
				// return getResources().getString(R.string.connection_error);
				return e.getMessage();
			} catch (XmlPullParserException e) {
				// return getResources().getString(R.string.xml_error);
				return e.getMessage();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// setContentView(R.layout.activity_main);
			setContentView(R.layout.activity_main);
			// Displays the HTML string in the UI via a WebView
			// WebView myWebView = (WebView) findViewById(R.id.webview);
			// myWebView.loadData(result, "text/html; charset=utf-8", null);
			// super.onPostExecute(result);
			int acilislisterownumbers = getResources().getInteger(
					R.integer.AcilisListerownumbers);

			int sayac = 0;
			Entry ent = new Entry();

			for (Entry entry : entrylistem) {
				// listviewSatirDoldur(entry);

				if (sayac < acilislisterownumbers) {
					kutuphane.sharedPreferencesEdit(getApplicationContext(),
							"acilisliste_" + ent.getTitle() + "_" + sayac,
							entry.getTitle());
					kutuphane
							.sharedPreferencesEdit(getApplicationContext(),
									"acilisliste_" + ent.getDescription() + "_"
											+ sayac, entry.getDescription());
					kutuphane.sharedPreferencesEdit(getApplicationContext(),
							"acilisliste_" + ent.getHtml_title() + "_" + sayac,
							entry.getHtml_title());
					kutuphane.sharedPreferencesEdit(getApplicationContext(),
							"acilisliste_" + ent.getHtml_description() + "_"
									+ sayac, entry.getHtml_description());
					kutuphane.sharedPreferencesEdit(getApplicationContext(),
							"listviewsatirsayisi", Integer.toString(sayac + 1));

					sayac++;

				}
			}

			ListView listview = (ListView) findViewById(R.id.listview_Acilis);
			AcilisListViewAdapter adap = new AcilisListViewAdapter(Acilis.this,
					entrylistem);
			listview.setAdapter(adap);

			prgdialog.dismiss();
			kutuphane.getSolMenu(Acilis.this, getApplicationContext());
		}

	}

	private class AcilisListViewAdapter extends BaseAdapter implements
			OnClickListener {

		private Activity activity;
		private ArrayList<Entry> data;
		private LayoutInflater inflater = null;
		// public Resources res;
		Entry tempValues = null;
		int i = 0;

		public AcilisListViewAdapter(Activity a, ArrayList<Entry> d) {
			activity = a;
			data = d;

			/*********** Layout inflator to call external xml layout () ***********/
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			if (data.size() <= 0)
				return 1;
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public class ViewHolder {

			public TextView text;
			public TextView text1;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			ViewHolder holder;

			if (convertView == null) {

				/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
				vi = inflater.inflate(R.layout.acilisveriler, null);

				/****** View Holder Object to contain tabitem.xml file elements ******/

				holder = new ViewHolder();
				holder.text = (TextView) vi.findViewById(R.id.acilis_txtBaslik);
				holder.text1 = (TextView) vi.findViewById(R.id.acilis_txtYazi);

				/************ Set holder with LayoutInflater ************/
				vi.setTag(holder);
			} else
				holder = (ViewHolder) vi.getTag();

			if (data.size() <= 0) {
				holder.text.setText("No Data");

			} else {
				/***** Get each Model object from Arraylist ********/
				tempValues = null;
				tempValues = (Entry) data.get(position);

				/************ Set Model values in Holder elements ***********/

				holder.text.setText(tempValues.getTitle());
				holder.text1.setText(tempValues.getDescription());
				// holder.image.setImageResource(res.getIdentifier(
				// "com.androidexample.customlistview:drawable/"
				// + tempValues.getImage(), null, null));

				/******** Set Item Click Listner for LayoutInflater for each row *******/

				vi.setOnClickListener(new OnItemClickListener(position));
			}
			return vi;
		}

		@Override
		public void onClick(View v) {
			Log.v("CustomAdapter", "=====Row button clicked=====");
		}

		/********* Called when Item click in ListView ************/
		private class OnItemClickListener implements OnClickListener {
			private int mPosition;

			OnItemClickListener(int position) {
				mPosition = position;
			}

			@Override
			public void onClick(View arg0) {

				Acilis sct = (Acilis) activity;

				sct.onItemClick(mPosition);
			}
		}
	}

	public void onItemClick(int mPosition) {
		Entry tempValues = (Entry) entrylistem.get(mPosition);

		// SHOW ALERT

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toastcustomestat,
				(ViewGroup) findViewById(R.id.toast_custom_layout));

		WebView toast_custom_webview = (WebView) layout
				.findViewById(R.id.toast_custom_webview);
		toast_custom_webview.loadData(tempValues.getHtml_description(),
				"text/html; charset=utf-8", null);

		// Toast toast = new Toast(getApplicationContext());
		// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		// toast.setDuration(10000);
		// toast.setView(layout);
		// toast.show();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(tempValues.getTitle());
		alertDialogBuilder
				.setCancelable(false)
				.setView(layout)
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

		// Toast.makeText(this,
		// tempValues.getTitle() + "Image:" + tempValues.getDescription(),
		// Toast.LENGTH_LONG).show();
	}

	private void GcmKayitIslemleri() {
		// Gcm Registration
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id

		final String regId = GCMRegistrar.getRegistrationId(this);
		// ServerUtilities.unregister(this, regId);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.cfc

				Log.i("GcmDurum", "Zaten kayýt edilmiþ");
				// Toast.makeText(getApplicationContext(),
				// "Already registered with GCM", Toast.LENGTH_LONG)
				// .show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				asy = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user

						String packagename = getPackageName();
						String versionname = kutuphane
								.getPackageVersionName(Acilis.this);
						String versioncode = Integer.toString(kutuphane
								.getPackageVersionCode(Acilis.this));
						String androidversioncode = Integer.toString(kutuphane
								.getAndroidSdkVersionCode());
						ServerUtilities.register(context, "name", "email",
								regId, packagename, versionname, versioncode,
								androidversioncode);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						asy = null;
					}

				};
				asy.execute(null, null, null);
			}
		}

	}

	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(),
			// "- " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	public void gcmregister_Click(View view) {
		Intent intent = new Intent("com.deu.deuistatistik.gcm.REGISTER");
		startActivity(intent);
	}

	public void clear_Click(View vi) {

		GCMRegistrar.unregister(getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuacilis, menu);

		return true;
	}

	private void updateConnectedFlags() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
			mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
		} else {
			wifiConnected = false;
			mobileConnected = false;
		}
	}

	private void loadPage() {

		if (kutuphane.internetErisimi(getApplicationContext())) {
			new fillAcilisListView().execute(estatURL);
		} else {
			showErrorPage();
		}
		// if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
		// || ((sPref.equals(WIFI)) && (wifiConnected))) {
		// // AsyncTask subclass
		// // new DownloadXmlTask().execute(estatURL);
		// new fillAcilisListView().execute(estatURL);
		// } else {
		// showErrorPage();
		// }
	}

	private void showErrorPage() {

		kutuphane.getAlertDialog(this, "Hata", "Baðlantý hatasý.");

	}

	// Uploads XML from stackoverflow.com, parses it, and combines it with
	// HTML markup. Returns HTML string.
	private String loadXmlFromNetwork(String urlString)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		estatXmlParser estatxmlparser = new estatXmlParser();
		List<Entry> entries = null;
		String title = null;
		String url = null;
		String summary = null;
		Calendar rightNow = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

		// Checks whether the user set the preference to include summary text
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean pref = sharedPrefs.getBoolean("summaryPref", false);

		// StringBuilder htmlString = new StringBuilder();
		// htmlString.append("<h3>"
		// + getResources().getString(R.string.page_title) + "</h3>");
		// htmlString.append("<em>" + getResources().getString(R.string.updated)
		// + " " + formatter.format(rightNow.getTime()) + "</em>");

		try {
			stream = downloadUrl(urlString);
			entries = estatxmlparser.parse(stream);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (stream != null) {
				stream.close();
			}
		}

		for (Entry ent : entries) {

			// AcilisClass acilisclas = new AcilisClass();
			Entry entry = new Entry();
			entry.setTitle(kutuphane.TagTemizle(ent.getTitle()));
			entry.setDescription(kutuphane.TagTemizle(ent.getDescription()));
			entry.setHtml_title(ent.getTitle());
			entry.setHtml_description(ent.getDescription());
			entrylistem.add(entry);

			// htmlString.append("<p><a href='");
			// htmlString.append(entry.link);
			// htmlString.append("'>" + entry.title + "</a></p>");
			// // If the user set the preference to include summary text,
			// // adds it to the display.
			// // if (pref) {
			// htmlString.append(entry.description);
			// }
		}

		// ListView listview = (ListView) findViewById(R.id.listview_Acilis);
		// AcilisListViewAdapter adap = new AcilisListViewAdapter(this, listem);
		// listview.setAdapter(adap);

		// return htmlString.toString();
		return "TRUE";
	}

	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	private InputStream downloadUrl(String urlString) throws IOException {
		// URL url = new URL(urlString);
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setReadTimeout(10000 /* milliseconds */);
		// conn.setConnectTimeout(15000 /* milliseconds */);
		// conn.setRequestMethod("GET");
		// conn.setDoInput(true);
		// // Starts the query
		// conn.connect();
		//
		// InputStream stream = conn.getInputStream();

		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(urlString);

		HttpResponse response = httpClient.execute(httpPost);

		HttpEntity entity = response.getEntity();
		String resultsStringg = EntityUtils.toString(entity, "ISO-8859-9");
		InputStream stream = new ByteArrayInputStream(
				resultsStringg.getBytes("ISO-8859-9"));

		return stream;
	}

	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			// Checks the user prefs and the network connection. Based on the
			// result, decides
			// whether
			// to refresh the display or keep the current display.
			// If the userpref is Wi-Fi only, checks to see if the device has a
			// Wi-Fi connection.
			if (WIFI.equals(sPref) && networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// If device has its Wi-Fi connection, sets refreshDisplay
				// to true. This causes the display to be refreshed when the
				// user
				// returns to the app.
				refreshDisplay = true;
				// Toast.makeText(context, R.string.wifi_connected,
				// Toast.LENGTH_SHORT).show();

				// If the setting is ANY network and there is a network
				// connection
				// (which by process of elimination would be mobile), sets
				// refreshDisplay to true.
			} else if (ANY.equals(sPref) && networkInfo != null) {
				refreshDisplay = true;

				// Otherwise, the app can't download content--either because
				// there is no network
				// connection (mobile or Wi-Fi), or because the pref setting is
				// WIFI, and there
				// is no Wi-Fi connection.
				// Sets refreshDisplay to false.
			} else {
				refreshDisplay = false;
				// Toast.makeText(context, R.string.lost_connection,
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.i("onStart", "onStart Çalýþtý");
		// Gets the user's network preference settings
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// Retrieves a string value for the preferences. The second parameter
		// is the default value to use if a preference value is not found.
		sPref = sharedPrefs.getString("listPref", "Wi-Fi");

		updateConnectedFlags();

		if (refreshDisplay) {
			// loadPage();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.d("onPause", "onPause Çalýþtý");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.d("onResume", "onResume Çalýþtý");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		kutuphane.stopFlurry(this);

		Log.d("onStop", "onStop Çalýþtý");

	}

	@Override
	protected void onDestroy() {

		if (asy != null) {
			asy.cancel(true);
		}
		// gcm.UnRegisterReceiverGcm();
		try {
			unregisterReceiver(mHandleMessageReceiver);
			unregisterReceiver(receiver);
			GCMRegistrar.onDestroy(getApplicationContext());

			Log.d("onDestroy",
					"Receiver silindi.GcmRegistrar.onDestroy Çalýþtý.");
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_yenile:

			if (kutuphane.internetErisimi(getApplicationContext())) {
				prgdialog = ProgressDialog.show(this, "Lütfen Bekleyiniz...",
						"Yükleniyor.", true);
				loadPage();
			} else {
				kutuphane.getAlertDialog(this, "Uyarý",
						"Güncellemek için Lütfen internetinizi açýnýz.");
			}
			return true;
		case R.id.action_btnOyla:
			// SharedPrfGoster();
			// loadPage();
			return true;

		case android.R.id.home:
			// kutuphane.getAlertDialog(this, "Uyarý",
			// "Güncellemek için Lütfen internetinizi açýnýz.");
			kutuphane.showSlidingMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
