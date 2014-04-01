package com.deu.istatistik;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbHelper extends SQLiteOpenHelper{
	private static final String db_name = "db_Depo";
	private static final int version = 1;

	Context context;
	private static String TABLO_ADI = null;
	
	public DbHelper(Context context,String tablo_adi) {
		super(context, db_name, null, version);
		// TODO Auto-generated constructor stub
		this.context = context;
		DbHelper.TABLO_ADI = tablo_adi;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			String sorgu = "CREATE TABLE "
					+ TABLO_ADI
					+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ad TEXT,soyad TEXT)";
			db.execSQL(sorgu);
			Toast.makeText(context, "Tablo oluþturuldu", Toast.LENGTH_LONG)
					.show();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "Tablo Oluþturmada Hata",
					Toast.LENGTH_LONG).show();

		}
	}

	
	private void creteTables(SQLiteDatabase db)
	{
		Toast.makeText(context, "Tablo Oluþturmada Hata",
				Toast.LENGTH_LONG).show();

	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sorgu = "DROP TABLE " + TABLO_ADI + "";
		db.execSQL(sorgu);
		onCreate(db);

	}

//	public final String tb_Deneme = "tb_Deneme";

	public void KayitEkle(String ad, String soyad, String tablo_adi) {
		SQLiteDatabase dat = getWritableDatabase();

		ContentValues content = new ContentValues();
		content.put("ad", ad);
		content.put("soyad", soyad);

		dat.insertOrThrow(tablo_adi, null, content);

		dat.close();

	}

	public ArrayList<String> KayitGetir() {
		SQLiteDatabase dat = getReadableDatabase();

		Cursor crs = dat.query(TABLO_ADI, new String[] { "ID", "ad",
				"soyad" }, null, null, null, null, null);

		ArrayList<String> adlar = new ArrayList<String>();
		while (crs.moveToNext()) {
			adlar.add(crs.getString(1));

		}
		dat.close();
		return adlar;

	}
}
