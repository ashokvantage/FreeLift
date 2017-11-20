package com.freelift;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JSONParser {
	int SDK_INT = android.os.Build.VERSION.SDK_INT;

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	@SuppressLint("NewApi")
	public JSONParser() {
		if (SDK_INT>8){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public JSONObject getJSONFromUrl(String url) {

		// Making HTTP request
		try {
			// Set verifier     
			if (SDK_INT>8){
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				}
//			DefaultHttpClient client = new DefaultHttpClient();
			// Example send http request
			HttpGet httpGet = new HttpGet(url);
			HttpParams httpParameters = new BasicHttpParams();
//			HttpResponse httpResponse = client.execute(httpGet);
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			httpClient.setParams(httpParameters);
			HttpResponse response = httpClient.execute(httpGet);


			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
	public JSONObject getLocationInfo(String address) {
		StringBuilder stringBuilder = new StringBuilder();
		try {

//			address = address.replaceAll(" ","%20");

			HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();


			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}
	
}
