package com.boyumi.yumitianqi.weather_droped;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.View;
import android.widget.Toast;

import com.boyumi.yumitianqi.R;
import com.boyumi.yumitianqi.Weather;

public class YahooWeather implements Weather{


	private String Woeid;
	private String WeatherCondition;
	private String Temperature;
	private String Date;
	private String CityName;
	private int WeatherCode;
	private Context context;
	private List<Map<String, Object>> ForecastList = new ArrayList<Map<String, Object>>();
	private int[] weatherIcon = new int[]{
			R.drawable.windrain,
			R.drawable.thunderstorm,
			R.drawable.snowrain,
			R.drawable.rain,
			R.drawable.snow,
			R.drawable.fog,
			R.drawable.wind,
			R.drawable.cloudy,
			R.drawable.sunny,
	};
	private int wiNum;
	
	

	public YahooWeather(View view, String cityname) {
		// TODO Auto-generated constructor stub
		setContext(view.getContext());
		setCityName(cityname);
		getWeather();
	}
	
	
	
	public int getWeatherIcon(int i) {
		return weatherIcon[i];
	}

	public int getWiNum() {
		return wiNum;
	}

	public void setWiNum(int wiNum) {
		this.wiNum = wiNum;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getWoeid() {
		return Woeid;
	}

	public void setWoeid(String woeid) {
		Woeid = woeid;
	}

	public String getWeatherCondition() {
		return WeatherCondition;
	}

	public void setWeatherCondition(String weatherCondition) {
		WeatherCondition = weatherCondition;
	}
	@Override
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
	
	@Override
	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public int getWeatherCode() {
		return WeatherCode;
	}

	public void setWeatherCode(int weatherCode) {
		WeatherCode = weatherCode;
	}
	@Override
	public List<Map<String, Object>> getForecastList() {
		return ForecastList;
	}
	
	public void setForecastList(List<Map<String, Object>> forecastList) {
		ForecastList = forecastList;
	}

	@SuppressLint("ShowToast")
	@Override
	public void getWeather() {
		if (CityName == null) {
			setWeatherCode(99);
			System.out.println("error:" + "didn't get a cityname!!");
			return;
		}
		getWoeid(CityName, context);
		final String yahoo_url = "http://weather.yahooapis.com/forecastrss?w="
				+ Woeid + "&u=c";
		Thread gw = new Thread() {
			@Override
			public void run() {
				try {
					URLConnection con = (URLConnection) (new URL(yahoo_url))
							.openConnection();
					con.setRequestProperty("accept", "*/*");
					con.setRequestProperty("connection", "Keep-Alive");
					con.setRequestProperty("user-agent",
							"Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
					con.connect();
					System.out.println("connect to the Yahoo");
					XmlPullParser parser = XmlPullParserFactory.newInstance()
							.newPullParser();
					parser.setInput(new InputStreamReader(con.getInputStream()));

					while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
						// begin to analysis the return data
						if (parser.getEventType() == XmlResourceParser.START_TAG) {
							String tagName = parser.getName();
							// current weather
							if (tagName.equals("yweather:condition")) {
								System.out.println("pulling the weatherIfno");

								String temp = parser.getAttributeValue(null,
										"temp");
								temp += ("\u2103");
								setTemperature(temp);
								setWeatherCode(Integer.valueOf(parser
										.getAttributeValue(null, "code")));
								setWeatherCondition(transfer_weather_display(WeatherCode));
								setDate(parser.getAttributeValue(null, "date"));
								Map<String, Object> listItem = new HashMap<String, Object>();
								listItem.put("day", "今天");
								listItem.put("weather", getWeatherCondition());
								listItem.put("temprang", temp);
								ForecastList.add(listItem);
							}

							// forecast weather
							if (tagName.equals("yweather:forecast")) {
								String day = transfer_day_display(parser
										.getAttributeValue(null, "day"));
								String weather = transfer_weather_display(Integer
										.valueOf(parser.getAttributeValue(null,
												"code")));
								String temprang = parser.getAttributeValue(
										null, "low")
										+ "-"
										+ parser.getAttributeValue(null, "high")+("\u2103");
								Map<String, Object> listItem = new HashMap<String, Object>();
								listItem.put("day", day);
								listItem.put("weather", weather);
								listItem.put("temprang", temprang);
								ForecastList.add(listItem);
							}
						}
						parser.next();
					}
					System.out.println(ForecastList.toString());
				} catch (Exception e) {
					e.printStackTrace();
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
		
		if (Date == null) {
			Toast.makeText(context, "未得到天气信息", 5000);
			return;
		}
		System.out.println("pulling Weather -----Done");
	}

	public void getWoeid(String cityname, Context context) {
		try {
			InputStream in = context.getResources().getAssets()
					.open("woeid.txt");
			InputStreamReader read = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(read);
			String tempLine;
			while ((tempLine = br.readLine()) != null) {
				String[] tmp = tempLine.split("\\|");
				if (cityname.equals(tmp[0])) {
					setWoeid(tmp[1]);
					System.out.println(cityname + ": find it's woeid------"
							+ Woeid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String transfer_day_display(String day) {
		switch (day) {
		case "Mon":
			return "星期一";
		case "Tue":
			return "星期二";
		case "Wed":
			return "星期三";
		case "Thu":
			return "星期四";
		case "Fri":
			return "星期五";
		case "Sat":
			return "星期六";
		case "Sun":
			return "星期天";
		default:
			return null;
		}
	}

	public String transfer_weather_display(int WeatherCode) {
		switch (WeatherCode) {
		case 0:
		case 1:
		case 2:
			setWiNum(0);
			return "风暴";
		case 3:
		case 4:
		case 37:
		case 38:
		case 39:
		case 45:
		case 46:
		case 47:
			setWiNum(1);
			return "雷阵雨";
		case 5:
		case 6:
		case 7:
		case 35:
		case 17:
			setWiNum(2);
			return "雨夹雪";
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 18:
		case 40:
			setWiNum(3);
			return "阵雨";
		case 13:
		case 14:
		case 15:
		case 16:
		case 41:
		case 42:
		case 43:
			setWiNum(4);
			return "";
		case 19:
		case 20:
		case 22:
			setWiNum(5);
			return "";
		case 23:
		case 24:
		case 25:
			setWiNum(6);
			return "";
		case 26:
		case 27:
		case 29:
		case 30:
		case 44:
			setWiNum(7);
			return "多云";
		case 31:
		case 32:
		case 33:
		case 34:
		case 36:
		case 28:
		case 21:
			setWiNum(8);
			return "";
		default:
			return null;
		}
	}



	@Override
	public int getWeatherIconNum() {
		// TODO Auto-generated method stub
		return weatherIcon[getWiNum()];
	}



	@Override
	public Map<String, String> getAirQualityMap() {
		// TODO Auto-generated method stub
		return null;
	}
}
