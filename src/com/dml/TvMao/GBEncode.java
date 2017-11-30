package com.dml.TvMao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GBEncode { 

	public static String gbEncoding(final String gbString) {
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "//u" + hexB;
		}
		System.out.println("unicodeBytes is: " + unicodeBytes);
		return unicodeBytes;
	}

	public static String formatUrl(String url)
			throws UnsupportedEncodingException {
		String[] dir = url.split("/");
		StringBuffer tempPath = new StringBuffer("");
		for (int i = 0; i < dir.length; i++) {
			tempPath.append(URLEncoder.encode(dir[i], "UTF-8"));
			tempPath.append("/");
		}
		return tempPath.toString().substring(0, tempPath.length() - 1);
	}
}
