package com.boyumi.yumitianqi;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GPS {

	LocationManager lm;
	String Longitude;
	String Latitude;
	String GpsCityName;
	static Location GPSlocation = null;
	
	// GPS location
	@SuppressLint("ShowToast")
	public void getGPSInfo(Context context) {
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location locationGPS = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		GPSlocation = locationGPS;
		System.out.println("GPS:" + locationGPS);
		Location locationPassive = lm
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		System.out.println("passive:" + locationPassive);
		Location locationNetwork = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		System.out.println("network:" + locationNetwork);
		if (locationGPS != null)
			updateLocation(locationGPS);
		if (locationPassive != null)
			updateLocation(locationPassive);
		if (locationGPS == null && locationPassive == null
				&& locationNetwork == null) {
			Toast.makeText(context, "�޷���λ������λ��", 50000).show();

		}
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 8,
				new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						updateLocation(lm.getLastKnownLocation(provider));
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						updateLocation(null);
					}

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						updateLocation(location);
					}
				});
//		System.out.println(GPSlocation);
	}

	public void updateLocation(Location newLocation) {
		if (newLocation != null) {
			Longitude = String.valueOf(newLocation.getLongitude());
			Latitude = String.valueOf(newLocation.getLatitude());
			System.out.println("getting longitude and latitude!---------Done!");
		}
	}

	// get cityName from baiduAPI
	public String getCityName() {
		if(GPSlocation==null)return null;
		final String baiduAPI_url ="http://api.map.baidu.com/geocoder?output=json&location="+ Latitude+ "," + Longitude + "&key=eZnlziqWIf9hYIokB4a4D7Bo";
//		final String baiduAPI_url = "http://api.map.baidu.com/geocoder?output=json&location=31.9377,118.6386&key=eZnlziqWIf9hYIokB4a4D7Bo";

		Thread gcn = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(baiduAPI_url);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					// Log.i("cat", ">>>>>>" + strResult);
					String temp = new JSONObject(strResult)
							.getJSONObject("result")
							.getJSONObject("addressComponent")
							.getString("city");
					String[] temp2 = temp.split("��");
					GpsCityName = temp2[0];
					System.out.println("GPS_CityName:"+GpsCityName);
				} catch (JSONException e) {
					e.printStackTrace();
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
		return GpsCityName;
	}
	
}