package com.pribas.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonProccess {

	public Map<String, String> getJsonFromURL(String jsonURL, String auth) throws IOException {
		byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
		String authStringEnc = new String(authEncBytes);
		URL url = new URL(jsonURL);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
		InputStream is = urlConnection.getInputStream();

		Map<String, String> map = new Gson().fromJson(new InputStreamReader(is, "UTF-8"),
				new TypeToken<Map<String, String>>() {
				}.getType());
		return map;
	}

}
