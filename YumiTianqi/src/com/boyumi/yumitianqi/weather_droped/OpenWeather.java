package com.boyumi.yumitianqi.weather_droped;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.boyumi.yumitianqi.Weather;

import android.util.Log;
import android.view.View;

public class OpenWeather implements Weather{
	
	
	
	
	public OpenWeather(View view, String cityname){
		getWeather();
	}
	
	
	
	/**
	 * 
	 */

	public void getWeather() {
		String citycode = "101020100";
		final String openweathreURL = "http://mobile.weather.com.cn/data/forecast/"+citycode+".html";
		Thread gw = new Thread() {
			public void run() {
				super.run();
				try {
//					DefaultHttpClient httpClient = new DefaultHttpClient();
//					HttpPost httpPost = new HttpPost(openweathreURL);
//					HttpResponse httpResponse = httpClient.execute(httpPost);
//					String strResult = EntityUtils.toString(httpResponse
//							.getEntity());
					URL url = new URL(openweathreURL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	                InputStream inStream = conn.getInputStream();
	                InputStreamReader read = new InputStreamReader(inStream, "UTF-8");
	     		    BufferedReader br = new BufferedReader(read);
	     			String strResult ="";
	     			String tempLine;
	     			while ((tempLine = br.readLine()) != null) {
	     				strResult += tempLine;
	     			}
	     			JSONObject jsonObject = new JSONObject(strResult);
					Log.i("cat", ">>>>>>" + jsonObject);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		gw.start();
	}
	@Override
	public CharSequence getCityName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CharSequence getTemperature() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getWeatherIconNum() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<? extends Map<String, ?>> getForecastList() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CharSequence getWeatherCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CharSequence getDate() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Map<String, String> getAirQualityMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
