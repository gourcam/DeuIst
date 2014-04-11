package com.deu.istatistik;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.deu.istatistik.R;
import com.flurry.android.FlurryAgent;

public class TabloActivity extends Activity {

	Kutuphane kutuphane = new Kutuphane();

	ArrayList<String> degerler_kikare;
	ArrayList<String> degerler_student_t;
	ArrayList<String> degerler_tukey01;
	ArrayList<String> degerler_tukey05;
	ArrayList<String> degerler_spearmankorelasyon;
	// ////
	Spinner spin_tukey05;
	Spinner spin_tukey01;
	Spinner spin_kikare;
	Spinner spin_student_t;
	Spinner spin_spearmankorelasyon;

	private void SpinnerDoldur() {

		tDoldur();
		chiSquareDoldur();
		tukey01Doldur();
		tukey05Doldur();
		spearmanDoldur();
	}

	private void spearmanDoldur() {
		degerler_spearmankorelasyon = kutuphane.getDosya(this,
				"spearmankorelasyon.txt");
		String[] alfadegerler = degerler_spearmankorelasyon.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adapp = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler);

		spin_spearmankorelasyon = (Spinner) findViewById(R.id.spinner_spearmankorelasyon);
		spin_spearmankorelasyon.setAdapter(adapp);
	}

	private void zDoldur() {

	}

	private void chiSquareDoldur() {
		degerler_kikare = kutuphane.getDosya(this, "kikare.txt");
		String[] alfadegerler = degerler_kikare.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adapp = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler);

		spin_kikare = (Spinner) findViewById(R.id.spinner_kikare_alfa);
		spin_kikare.setAdapter(adapp);
	}

	private void fDoldor() {

	}

	private void tukey01Doldur() {
		degerler_tukey01 = kutuphane.getDosya(this, "tukeytesti01.txt");
		String[] alfadegerler2 = degerler_tukey01.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adaptukey = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler2);

		spin_tukey01 = (Spinner) findViewById(R.id.spinner_tukey01);
		spin_tukey01.setAdapter(adaptukey);
	}

	private void tukey05Doldur() {
		degerler_tukey05 = kutuphane.getDosya(this, "tukeytesti05.txt");
		String[] alfadegerler3 = degerler_tukey05.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adaptukey05 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler3);

		spin_tukey05 = (Spinner) findViewById(R.id.spinner_tukey05);
		spin_tukey05.setAdapter(adaptukey05);
	}

	private void tDoldur() {
		degerler_student_t = kutuphane.getDosya(this, "student_t.txt");
		String[] alfadegerler1 = degerler_student_t.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adap = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler1);

		spin_student_t = (Spinner) findViewById(R.id.spinner_student_alfa);
		spin_student_t.setAdapter(adap);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablo);

		kutuphane.getSolMenu(this, getApplicationContext());
		kutuphane.startFlurry(this);
		FlurryAgent.logEvent("Tablo Activity");

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Dokuz Eylül Üniversitesi");
		actionBar.setTitle("Tablolar");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		SpinnerDoldur();

		final String[] tablolar = getResources().getStringArray(
				R.array.Tablolar);
		ArrayAdapter<String> adap = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, tablolar);
		Spinner spinnerTablolar = (Spinner) findViewById(R.id.spinnerTablolar);
		spinnerTablolar.setAdapter(adap);

		spinnerTablolar.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View vi, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				VisibilityTable();
				int secilenindis = parent.getSelectedItemPosition();

				switch (secilenindis) {
				case 0:
					break;
				// Z
				case 1:
					LinearLayout lyt0 = (LinearLayout) findViewById(R.id.layout_z);
					lyt0.setVisibility(View.VISIBLE);
					break;
				// Student T
				case 2:
					LinearLayout lyt = (LinearLayout) findViewById(R.id.layout_student_t);
					lyt.setVisibility(View.VISIBLE);
					break;
				// Chi-Square
				case 3:
					LinearLayout lyt1 = (LinearLayout) findViewById(R.id.layout_kikare);
					lyt1.setVisibility(View.VISIBLE);
					break;
				case 4:
					LinearLayout lyt2 = (LinearLayout) findViewById(R.id.layout_tukey01);
					lyt2.setVisibility(View.VISIBLE);
					break;
				case 5:
					LinearLayout lyt3 = (LinearLayout) findViewById(R.id.layout_tukey05);
					lyt3.setVisibility(View.VISIBLE);
					break;
				case 6:
					LinearLayout lyt4 = (LinearLayout) findViewById(R.id.layout_spearmankorelasyon);
					lyt4.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void VisibilityTable() {
		LinearLayout layoutTablolar = (LinearLayout) findViewById(R.id.layoutTablolar);
		int a = layoutTablolar.getChildCount();

		for (int i = 0; i < a; i++) {
			LinearLayout lyt = (LinearLayout) layoutTablolar.getChildAt(i);
			lyt.setVisibility(View.GONE);

		}
	}

	public void btn_z_Click(View vi) {
		try {
			final String[] ztablo = getResources().getStringArray(
					R.array.ztablo);

			EditText KulGiris = (EditText) findViewById(R.id.editTxt_z);
			double x = Double.parseDouble(KulGiris.getText().toString());
			if (x < 3.5) {
				double z1 = Double.parseDouble(ztablo[(int) (x * 100)]
						.substring(0, 4)); // Düþük olan z deðeri deðer
				double z2 = Double.parseDouble(ztablo[(int) (x * 100) + 1]
						.substring(0, 4)); // Yüksek olan zdeðeri deðer;
				double y1 = Double.parseDouble(ztablo[(int) (x * 100)]
						.substring(ztablo[(int) (x * 100)].length() - 6,
								ztablo[(int) (x * 100)].length()));
				;
				double y2 = Double.parseDouble(ztablo[(int) (x * 100) + 1]
						.substring(ztablo[(int) (x * 100) + 1].length() - 6,
								ztablo[(int) (x * 100) + 1].length()));
				;
				double sonuc = (y2 - y1) * (x - z1) / (z2 - z1);

				kutuphane.getAlertDialog(this, "Sonuç : ",
						Double.toString(sonuc + y1));
				// KulSonuc.setText(String.valueOf(sonuc + y1));
			}

			else {
				kutuphane.getAlertDialog(this, "Sonuç : ",
						Double.toString(0.4999));
				// KulSonuc.setText("0.4999");
			}
		} catch (Exception e) {
			kutuphane.getAlertDialog(this, "Hata",
					"Lütfen doðru giriþ yaptýðýnýzdan emin olunuz.");
		}

	}

	public void btn_kikare_Click(View vi) {

		try {

			EditText alfadeger = (EditText) findViewById(R.id.editTxt_kikare_sd);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_kikare.get(deger_int).split(";");

				int sutun = spin_kikare.getSelectedItemPosition();

				String sonuc = satir[sutun];

				kutuphane.getAlertDialog(this, "Sonuç : ", sonuc);

			} else {
				kutuphane.getAlertDialog(this, "Aralýk Hatasý",
						"Lütfen 1 ile 30 arasýnda deðer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(this, "Giriþ Hatasý",
					"Tablo deðerleri dýþýnda deðer girdiniz.");
		}

	}

	public void btn_student_t_Click(View vi) {

		try {

			EditText alfadeger = (EditText) findViewById(R.id.editTxt_studentSd);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_student_t.get(deger_int).split(";");

				int sutun = spin_student_t.getSelectedItemPosition();

				String sonuc = satir[sutun];

				kutuphane.getAlertDialog(this, "Sonuç : ", sonuc);

			} else {
				kutuphane.getAlertDialog(this, "Aralýk Hatasý",
						"Lütfen 1 ile 30 arasýnda deðer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(this, "Giriþ Hatasý",
					"Tablo deðerleri dýþýnda deðer girdiniz.");
		}
	}

	public void btn_tukey01_Click(View vi) {

		try {

			EditText alfadeger = (EditText) findViewById(R.id.editTxt_tukey01);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_tukey01.get(deger_int).split(";");

				int sutun = spin_tukey01.getSelectedItemPosition();

				String sonuc = satir[sutun];

				kutuphane.getAlertDialog(this, "Sonuç : ", sonuc);

			} else {
				kutuphane.getAlertDialog(this, "Aralýk Hatasý",
						"Lütfen 1 ile 30 arasýnda deðer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(this, "Giriþ Hatasý",
					"Tablo deðerleri dýþýnda deðer girdiniz.");
		}

	}

	public void btn_tukey05_Click(View vi) {

		try {

			EditText alfadeger = (EditText) findViewById(R.id.editTxt_tukey05);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_tukey05.get(deger_int).split(";");

				int sutun = spin_tukey05.getSelectedItemPosition();

				String sonuc = satir[sutun];

				kutuphane.getAlertDialog(this, "Sonuç : ", sonuc);

			} else {
				kutuphane.getAlertDialog(this, "Aralýk Hatasý",
						"Lütfen 1 ile 30 arasýnda deðer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(this, "Giriþ Hatasý",
					"Tablo deðerleri dýþýnda deðer girdiniz.");
		}

	}

	public void btn_spearmankorelasyon_Click(View vi) {

		try {

			EditText alfadeger = (EditText) findViewById(R.id.editTxt_spearmankorelasyon);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_spearmankorelasyon.get(deger_int)
						.split(";");

				int sutun = spin_spearmankorelasyon.getSelectedItemPosition();

				String sonuc = satir[sutun];

				kutuphane.getAlertDialog(this, "Sonuç : ", sonuc);

			} else {
				kutuphane.getAlertDialog(this, "Aralýk Hatasý",
						"Lütfen 1 ile 30 arasýnda deðer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(this, "Giriþ Hatasý",
					"Lütfen 1 ile 30 arasýnda deðer giriniz");
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		kutuphane.stopFlurry(this);
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
