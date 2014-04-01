package com.deu.istatistik;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DosyaIslemleri {

	public String DosyaOku(String path) throws IOException {

		FileInputStream fileinput;

		fileinput = new FileInputStream(path);

		BufferedReader bufread = new BufferedReader(new InputStreamReader(
				fileinput));
		StringBuilder build = new StringBuilder();

		String satir;
		try {
			while ((satir = bufread.readLine()) != null) {

				build.append(satir);

			}
		} finally {
			bufread.close();
		}

		return build.toString();

	}
}
