package com.deu.deuistatistik.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class aykutasilcomJson {

	public static JSONObject jsonYazilim = null;

	public static String getJsonFromUrl(String strUrl) {
		String strJson = null;
		InputStream is = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(strUrl);
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			strJson = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			strJson = null;
		}
		return "{veriler:" + strJson + "}";
	}

	public static class UrlTask extends AsyncTask<String, Void, Void> {
		String strJson = null;

		@Override
		protected Void doInBackground(String... url) {
			strJson = getJsonFromUrl(url[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				jsonYazilim = new JSONObject(strJson);
				init(jsonYazilim);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static List<tb_Yazilim> init(JSONObject json) {
		// TODO Auto-generated method stub
		List<tb_Yazilim> liste = getList_tbYazilim(json);

		return liste;
		
//		for (tb_Yazilim book : liste) {
//			System.out.println("Id -> " + book.getYazilim_ID() + " | Konu -> "
//					+ book.getYazilim_konu());
//		}
	}
	
	public static List<tb_Yazilim> getList_tbYazilim(JSONObject jsonYazilim2) {
		// TODO Auto-generated method stub
		List<tb_Yazilim> listem = new ArrayList<tb_Yazilim>();

		try {
			JSONArray veriler = jsonYazilim2.getJSONArray("veriler");

			for (int q = 0; q < veriler.length(); q++) {
				JSONObject veri = veriler.getJSONObject(q);

				tb_Yazilim tb = new tb_Yazilim();
				tb.yazilim_ID = veri.getString("yazilim_ID");
				tb.yazilim_konu = veri.getString("yazilim_konu");

				listem.add(tb);
			}

			return listem;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static class tb_Yazilim implements Serializable {
		private static final long serialVersionUID = 1L;

		private String yazilim_ID;
		private String yazilim_kategoriID;
		private String yazilim_konu;

		public String getYazilim_ID() {
			return yazilim_ID;
		}

		public void setYazilim_ID(String yazilim_ID) {
			this.yazilim_ID = yazilim_ID;
		}

		public String getYazilim_kategoriID() {
			return yazilim_kategoriID;
		}

		public void setYazilim_kategoriID(String yazilim_kategoriID) {
			this.yazilim_kategoriID = yazilim_kategoriID;
		}

		public String getYazilim_konu() {
			return yazilim_konu;
		}

		public void setYazilim_konu(String yazilim_konu) {
			this.yazilim_konu = yazilim_konu;
		}

	}
}
