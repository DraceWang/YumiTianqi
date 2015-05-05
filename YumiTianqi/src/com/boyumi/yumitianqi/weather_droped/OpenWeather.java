package com.boyumi.yumitianqi.weather_droped;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

public class OpenWeather {
	
	String openweather_url = getInterfaceURL("101010100", "index");
	public void getweather(){
	Thread gcn = new Thread() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(openweather_url);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				 Log.i("cat", ">>>>>>" + strResult);
//				String temp = new JSONObject(strResult)
//						.getJSONObject("result")
//						.getJSONObject("addressComponent")
//						.getString("city");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	gcn.start();
	try {
		gcn.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * ��������API��url
	 */
	
	private final static String TAG = "GenerateURL";
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	private static final String appid = "0a3566f61ae9d021";
	private static final String private_key = "3a1da6_SmartWeatherAPI_0a0e519";
	private static final String url_header = "http://open.weather.com.cn/data/?";

	private static byte[] HmacSHA1Encrypt(String url, String privatekey)
			throws Exception {
		byte[] data = privatekey.getBytes(ENCODING);
		// ���ݸ������ֽ����鹹��һ����Կ,�ڶ�����ָ��һ����Կ�㷨������
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		// ����һ��ָ�� Mac �㷨 �� Mac ����
		Mac mac = Mac.getInstance(MAC_NAME);
		// �ø�����Կ��ʼ�� Mac ����
		mac.init(secretKey);
		byte[] text = url.getBytes(ENCODING);
		// ��� Mac ����
		return mac.doFinal(text);
	}

	private static String getKey(String url, String privatekey)
			throws Exception {
		byte[] key_bytes = HmacSHA1Encrypt(url, privatekey);
		String base64encoderStr = Base64.encodeToString(key_bytes,
				Base64.NO_WRAP);
		return URLEncoder.encode(base64encoderStr, ENCODING);
	}

	private static String getInterfaceURL(String areaid, String type,
			String date) throws Exception {
		String keyurl = url_header + "areaid=" + areaid + "&type=" + type
				+ "&date=" + date + "&appid=";
		String key = getKey(keyurl + appid, private_key);
		String appid6 = appid.substring(0, 6);

		return keyurl + appid6 + "&key=" + key;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getInterfaceURL(String areaid, String type) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = dateFormat.format(new Date());
		// String type="forecast3d";//"index";//"forecast3d";"observe"
		try {
			return getInterfaceURL(areaid, type, date);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e.fillInStackTrace());
		}
		return null;
	}

}