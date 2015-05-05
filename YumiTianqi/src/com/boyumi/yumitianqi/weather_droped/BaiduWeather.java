package com.boyumi.yumitianqi.weather_droped;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.View;

import com.boyumi.yumitianqi.Weather;

public class BaiduWeather implements Weather {

	private String CityName;
	private String WeatherCondition;
	private String Temperature;
	private String Date;
	private Context context;
	private List<Map<String, Object>> ForecastList = new ArrayList<Map<String, Object>>();
//	private int[] weatherIcon = new int[] { R.drawable.windrain,
//			R.drawable.thunderstorm, R.drawable.snowrain, R.drawable.rain,
//			R.drawable.snow, R.drawable.fog, R.drawable.wind,
//			R.drawable.cloudy, R.drawable.sunny, };

	public BaiduWeather(View view, String cityname) {
		setContext(view.getContext());
		setCityName(cityname);
		getWeather();
	}

	@Override
	public void getWeather() {
		// TODO Auto-generated method stub
		if (CityName == null) {
			System.out.println("error:" + "didn't get a cityname!!");
			return;
		}
		final String baidu_url = "http://api.map.baidu.com/telematics/v3/weather?location="
				+ getCityName()
				+ "&output=xml&ak=EN4ewH1NIOaaQcivD4d7UeL7&mcode=54:97:14:2A:47:69:A5:FB:2E:77:65:D5:A7:CB:53:A4:5A:66:D6:67";
		Thread gw = new Thread() {
			@Override
			public void run() {
				try {
					URLConnection con = (URLConnection) (new URL(baidu_url))
							.openConnection();
					con.setRequestProperty("accept", "*/*");
					con.setRequestProperty("connection", "Keep-Alive");
					con.setRequestProperty("user-agent",
							"Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
					con.connect();
					System.out.println("connect to the baidu");
					XmlPullParser parser = XmlPullParserFactory.newInstance()
							.newPullParser();
					parser.setInput(new InputStreamReader(con.getInputStream()));
					
					while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
						if (parser.getEventType() == XmlResourceParser.START_TAG) {
							String tagName = parser.getName();
							if (tagName.equals("yweather:condition")) {
								System.out.println("getting weather info");
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		gw.start();
		try {
			gw.join();
			System.out.println("out of baidu Weahter");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * geter and seter
	 */
	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getWeatherCondition() {
		return WeatherCondition;
	}

	public void setWeatherCondition(String weatherCondition) {
		WeatherCondition = weatherCondition;
	}

	public String getTemperature() {
		return Temperature;
	}

	public void setTemperature(String temperature) {
		Temperature = temperature;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Map<String, Object>> getForecastList() {
		return ForecastList;
	}

	public void setForecastList(List<Map<String, Object>> forecastList) {
		ForecastList = forecastList;
	}

	@Override
	public int getWeatherIconNum() {
		// TODO Auto-generated method stub
		return 0;
	}
}