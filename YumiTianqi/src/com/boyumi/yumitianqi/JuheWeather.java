package com.boyumi.yumitianqi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class JuheWeather implements Weather {

	private String CityName;
	private String WeatherCondition;
	private String Temperature;
	private String Date;
	private Context context;
	private int wid;
	private List<Map<String, Object>> ForecastList = new ArrayList<Map<String, Object>>();
	private int[] weatherIcon = new int[] { R.drawable.windrain,
			R.drawable.thunderstorm, R.drawable.snowrain, R.drawable.rain,
			R.drawable.snow, R.drawable.fog, R.drawable.wind,
			R.drawable.cloudy, R.drawable.sunny,R.drawable.themperature };

	private Map<String, String> AirQualityMap = new HashMap<String, String>(); 
	private String AQtime;
	/**
	 * 
	 * @param view
	 * @param cityname
	 */
	public JuheWeather(View view, String cityname) {
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
		final String juhe_url = "http://v.juhe.cn/weather/index?format=2&cityname="
				+ getCityName() + "&key=b4815115e391e0460400617affd166a1";
		Thread gw = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(juhe_url);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
//					 Log.i("Weather", ">>>>>>" + strResult);
					// current Weather
					setTemperature(new JSONObject(strResult)
							.getJSONObject("result").getJSONObject("sk")
							.getString("temp")
							+ ("\u2103"));
					setDate(new JSONObject(strResult).getJSONObject("result")
							.getJSONObject("today").getString("date_y")
							+ new JSONObject(strResult).getJSONObject("result")
									.getJSONObject("sk").getString("time"));
					setWeatherCondition(new JSONObject(strResult)
							.getJSONObject("result").getJSONObject("today")
							.getString("weather"));
					setWid(transfer(Integer.valueOf(new JSONObject(strResult).getJSONObject("result").getJSONObject("today").getJSONObject("weather_id").getString("fa"))));
//					System.out.println(CityName + Temperature + Date
//							+ WeatherCondition+getWid());

					// future weather
					JSONArray futuredata = new JSONObject(strResult)
							.getJSONObject("result").getJSONArray("future");
					for (int i = 0; i < 6; i++) {
						JSONObject temp = futuredata.getJSONObject(i);
						String day = temp.getString("week");
						String weather = temp.getString("weather");
						String temprang = temp.getString("temperature");
						Map<String, Object> listItem = new HashMap<String, Object>();
						listItem.put("day", day);
						listItem.put("weather", weather);
						listItem.put("temprang", temprang);
						ForecastList.add(listItem);
					}
					// System.out.println(ForecastList.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		gw.start();
		try {
			gw.join();
			if(isAQupdate()){
				getCityAQ(CityName);
			}else{
				getAQ();
				getCityAQ(CityName);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * get AirQuality info  API from pm25.in
	 */
	public void getAQ(){
		
		final String URL = "http://www.pm25.in/api/querys/all_cities.json?token=5j1znBVAsnSf5xQyNQyq";
		
		
		Thread gaq = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					URL url = new URL(URL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	                InputStream inStream = conn.getInputStream();
	                InputStreamReader read = new InputStreamReader(inStream, "UTF-8");
	     		    BufferedReader br = new BufferedReader(read);
	     			String strResult ="";
	     			String tempLine;
	     			while ((tempLine = br.readLine()) != null) {
	     				strResult += tempLine;
	     			}
	     			FileOutputStream out = getContext().openFileOutput("AitQuality.json", Context.MODE_PRIVATE);
	     			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
	     			writer.write(strResult);
	     			if(new JSONObject(strResult).has("error")){
	     				Toast.makeText(getContext(), "没有获取到空气质量信息", 3000);	
	     			}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		gaq.start();
		try {
			gaq.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getCityAQ(String cityname){
		FileInputStream in;
		try {
			in = getContext().openFileInput("AitQuality.json");
			BufferedReader ReaderIn = new BufferedReader(new InputStreamReader(in));
			String strResult ="";
			String tempLine;
			while ((tempLine = ReaderIn.readLine()) != null) {
				strResult += tempLine;
			}
			JSONArray AQArray = new JSONArray(strResult);
			for(int i=0;i<AQArray.length();i++){
				if(cityname.equals(AQArray.getJSONObject(i).getString("area"))){
					JSONObject ObjectC = AQArray.getJSONObject(i);
					AirQualityMap.clear();
					AirQualityMap.put("空气质量", ObjectC.getString("quality"));
					AirQualityMap.put("PM2.5", ObjectC.getString("pm2_5"));
					AirQualityMap.put("PM10", ObjectC.getString("pm10"));
					AirQualityMap.put("主要污染物", ObjectC.getString("primary_pollutant"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isAQupdate(){
		try {
			FileInputStream in = getContext().openFileInput("AitQuality.json");
			BufferedReader ReaderIn = new BufferedReader(new InputStreamReader(in));
			String strResult ="";
 			String tempLine;
 			while ((tempLine = ReaderIn.readLine()) != null) {
 				strResult += tempLine;
 			}
 			JSONArray AQArray = new JSONArray(strResult);
 			setAQtime(AQArray.getJSONObject(0).getString("time_point").split("T")[0]);
 			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 			if(getAQtime().equals(df.format(new java.util.Date()))){
 				return true;
 			}else{
 				return false;
 			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
	
	
	
	
	public int transfer(int wid) {
		switch (wid) {
		case 0:
			return 8;
		case 1:
			return 7;
		case 2:
			return 7;
		case 3:
			return 3;
		case 4:
			return 1;
		case 5:
			return 2;
		case 6:
			return 2;
		case 7:
			return 3;
		case 8:
			return 3;
		case 9:
			return 3;
		case 10:
			return 3;
		case 11:
			return 3;
		case 12:
			return 3;
		case 13:
			return 4;
		case 14:
			return 4;
		case 15:
		case 16:
		case 17:
			return 4;
		case 18:
			return 5;
		case 19:
			return 3;
		case 20:
			return 5;
		case 21:
			return 3;
		case 22:
			return 3;
		case 23:
			return 3;
		case 24:
			return 3;
		case 25:
			return 3;
		case 26:
			return 4;
		case 27:
			return 4;
		case 28:
			return 4;
		case 29:
		case 30:
		case 31:
		case 53:
			return 5;
		}
		return 9;
	}

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
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
	
	
	
	public int getWid() {
		return wid;
	}

	public void setWid(int wid) {
		this.wid = wid;
	}

	@Override
	public int getWeatherIconNum() {
		// TODO Auto-generated method stub
		return weatherIcon[getWid()];
	}


	@Override
	public Map<String, String> getAirQualityMap() {
		// TODO Auto-generated method stub
		return AirQualityMap;
	}

	public String getAQtime() {
		return AQtime;
	}

	public void setAQtime(String aQtime) {
		AQtime = aQtime;
	}
	
}
