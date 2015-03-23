package com.boyumi.yumitianqi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;

public class YahooWeather extends Activity {
	
	String CityCode;
	String WeatherCondition = "ˮ������(��_��)";
	String Temperature = "  ������(--)#";
	String Date;
	String CityName;
	int WeatherCode;
	
	
	// get weather from yahoo
		public void getWeather(Context context,String CityName) {
			CityCode = getcitycode(context,"cityCode.txt", CityName);
			final String temp_url = "http://weather.yahooapis.com/forecastrss?w="
					+ CityCode + "&u=c";
			Thread gw = new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						URLConnection con = (URLConnection) (new URL(temp_url))
								.openConnection();
						con.setRequestProperty("accept", "*/*");
						con.setRequestProperty("connection", "Keep-Alive");
						con.setRequestProperty("user-agent",
								"Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
						con.connect();
						System.out.println("connected");
						XmlPullParser parser = XmlPullParserFactory.newInstance()
								.newPullParser();
						parser.setInput(new InputStreamReader(con.getInputStream()));

						while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
							if (parser.getEventType() == XmlResourceParser.START_TAG) {
								String tagName = parser.getName();

								if (tagName.equals("yweather:condition")) {
									System.out.println("pulling the weatherIfno");
//									WeatherCondition = parser.getAttributeValue(
//											null, "text");
									Temperature = parser.getAttributeValue(null,
											"temp");
									Temperature += ("\u2103");
									WeatherCode = Integer.valueOf(parser.getAttributeValue(null,
											"code"));
									Date = parser.getAttributeValue(null, "date");
								}
								
							}
							parser.next();
						}
					} catch (Exception e) {
						System.out.println("error:" + e);
					}
				}
			};
			gw.start();
			try {
				gw.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainActivity.isUpdate = true;
		}

		public String getcitycode(Context context,String fileName, String cn) {
			try {
				InputStream in =  context.getResources().getAssets().open(fileName);
				InputStreamReader read = new InputStreamReader(in, "UTF-8");
				BufferedReader br = new BufferedReader(read);
				String tempLine;
				while ((tempLine = br.readLine()) != null) {
					String[] tmpC = tempLine.split("\\|");
					String[] tmpcn = tmpC[0].split(";");
					if (cn.equals(tmpcn[0]) | cn.equals(tmpcn[1])) {
						String citycode = tmpC[1];
						System.out.println(citycode+"find it!");
						CityName = tmpcn[0];
						return citycode;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
}
