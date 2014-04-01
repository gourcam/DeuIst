package com.deu.istatistik;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.deu.istatistik.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HaritaActivity extends FragmentActivity implements
		LocationListener {

	private LocationManager locman;
	private GoogleMap googleHarita;
	private String provider;
	private double _latitude;
	private double _longitude;

	Kutuphane kutuphane = new Kutuphane();
	ArrayList<Konumlar> listkonum = new ArrayList<Konumlar>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.harita);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Dokuz Eylül Üniversitesi");
		actionBar.setTitle("Konum");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		kutuphane.startFlurry(this);
		getKonumParcala();

		harita_spinnerYerListesiDoldur();

		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setCostAllowed(true);

		provider = locman.getBestProvider(criteria, false);
		Location location = locman.getLastKnownLocation(provider);

		if (location != null) {
			onLocationChanged(location);
		} else {
			List<String> providerNames = locman.getAllProviders();
			for (String backupProvider : providerNames) {
				if (locman.isProviderEnabled(backupProvider))
					provider = backupProvider;
			}

			locman.requestLocationUpdates(provider, 1000, 50, this);
		}

	}

	private void harita_spinnerYerListesiDoldur() {

		ArrayList<String> yerisimleri = new ArrayList<String>();

		for (int a = 0; a < listkonum.size(); a++) {

			yerisimleri.add(listkonum.get(a).getKonumadi());

		}

		Spinner harita_spinnerYerListesi = (Spinner) findViewById(R.id.harita_spinnerYerListesi);
		ArrayAdapter<String> adap = new ArrayAdapter<String>(this,
				R.layout.spinnernotharf, yerisimleri);
		harita_spinnerYerListesi.setAdapter(adap);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locman.requestLocationUpdates(provider, 1000, 50, this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locman.removeUpdates(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		kutuphane.stopFlurry(this);
	}

	private void HaritaGoster(LatLng latlng) {

		googleHarita = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.haritafragment)).getMap();
		if (googleHarita != null) {
			LatLng Koordinat = latlng;

			googleHarita.clear();
			googleHarita.setMyLocationEnabled(true);
			// googleHarita.addMarker(new MarkerOptions().position(Koordinat)
			// .title("Burdasýn"));
			googleHarita.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(android.R.drawable.ic_input_add))
					.title("Burdasýn").position(Koordinat));

			getKonumParcala();
			HaritayaKonumEkle();
			// googleHarita.moveCamera(CameraUpdateFactory.newLatLngZoom(
			// Koordinat, 16));

			// googleHarita.addMarker(new MarkerOptions()
			// .icon(BitmapDescriptorFactory
			// .fromResource(R.drawable.ic_launcher))
			// .position(new LatLng(35, 23)).flat(true).rotation(245));

			// CameraPosition cameraPosition = CameraPosition.builder()
			// .target(Koordinat).zoom(13).bearing(90).build();

			// Animate the change in camera view over 2 seconds
			// googleHarita.animateCamera(
			// CameraUpdateFactory.newCameraPosition(cameraPosition),
			// 2000, null);

		}

	}

	public void HaritayaKonumEkle() {

		for (int a = 0; a < listkonum.size(); a++) {
			Konumlar konumum = listkonum.get(a);

			LatLng Koordinat = new LatLng(Double.parseDouble(konumum
					.getKonum_latitude()), Double.parseDouble(konumum
					.getKonum_longitude()));

			googleHarita.addMarker(new MarkerOptions().position(Koordinat)
					.title(konumum.getKonumadi()));
		}

	}

	public void btn_Nerdeyim_Click(View vi) {

		LatLng Koordinat = new LatLng(_latitude, _longitude);

		googleHarita.moveCamera(CameraUpdateFactory
				.newLatLngZoom(Koordinat, 16));

		CameraPosition cameraPosition = CameraPosition.builder()
				.target(Koordinat).zoom(13).bearing(30).build();
		googleHarita.animateCamera(
				CameraUpdateFactory.newCameraPosition(cameraPosition), 2000,
				null);
		getKonumParcala();
		HaritayaKonumEkle();

	}

	private class Konumlar {
		private String konumadi;
		private String konum_latitude;
		private String konum_longitude;

		public Konumlar(String konumadi, String konumlat, String konumlong) {
			this.konumadi = konumadi;
			this.konum_latitude = konumlat;
			this.konum_longitude = konumlong;
		}

		public String getKonumadi() {
			return konumadi;
		}

		public void setKonumadi(String konumadi) {
			this.konumadi = konumadi;
		}

		public String getKonum_latitude() {
			return konum_latitude;
		}

		public void setKonum_latitude(String konum_latitude) {
			this.konum_latitude = konum_latitude;
		}

		public String getKonum_longitude() {
			return konum_longitude;
		}

		public void setKonum_longitude(String konum_longitude) {
			this.konum_longitude = konum_longitude;
		}

	}

	private void getKonumParcala() {
		String konumlar[] = getResources().getStringArray(R.array.konumlar);

		Konumlar knm;
		String yer_isim;
		String latitude;
		String longitude;
		for (int a = 0; a < konumlar.length; a++) {
			int yerisimkonum = konumlar[a].indexOf(",");
			int arakonum = konumlar[a].indexOf("+");

			yer_isim = konumlar[a].substring(0, yerisimkonum);
			latitude = konumlar[a].substring(yerisimkonum + 1, arakonum);
			longitude = konumlar[a].substring(arakonum + 1);

			knm = new Konumlar(yer_isim, latitude, longitude);
			listkonum.add(knm);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		_latitude = latitude;
		_longitude = longitude;

		// latitude = 38.384634;
		// longitude = 27.180361;

		LatLng ltlng = new LatLng(latitude, longitude);
		HaritaGoster(ltlng);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:

			Intent intent = new Intent(this, Acilis.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}