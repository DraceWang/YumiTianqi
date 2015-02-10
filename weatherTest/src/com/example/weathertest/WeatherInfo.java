package com.example.weathertest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherInfo {
	public void getYHWinfo(String cc) {
		final String temp_url = "http://weather.yahooapis.com/forecastrss?w="+cc+"&u=c";
//		new Thread() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				super.run();
				try {
					System.out.println("url set!");
					URLConnection con = (URLConnection) (new URL(temp_url))
							.openConnection();
					con.setRequestProperty("accept", "*/*");
					con.setRequestProperty("connection", "Keep-Alive");
					con.setRequestProperty("user-agent",
							"Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
					con.connect();
					System.out.println("connect");
					InputStream is = null;
					// Let's read the response
					StringBuffer buffer = new StringBuffer();
					is = con.getInputStream();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null)
						buffer.append(line + "\r\n");
					System.out.println(buffer);
					is.close();
					
					System.out.println("compelet");
				} catch (Exception e) {
					System.out.println("error:" + e);
				}
//				handler.sendEmptyMessage(0);
//			}
//		}.start();
//	}
	}	
}
