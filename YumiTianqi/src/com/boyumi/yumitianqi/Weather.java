package com.boyumi.yumitianqi;

import android.content.Context;

public class Weather {
	
	String CityName;
	String CityCode;
	String WeatherCondition = "ÀÆ…˙ª»»(°˙_°˙)";
	String Temperature = "  ±¨±Ì¿≤(--)#";
	String Date;
	int WeatherCode;
	
	
	int weather;
	int[] icons;
	
	public void getYahooWeather(Context context,String cityname){
		YahooWeather y = new YahooWeather();
		y.getWeather(context,cityname);
		CityCode = y.CityCode;
		CityName = y.CityName;
		WeatherCondition = y.WeatherCondition;
		Temperature = y.Temperature;
		Date = y.Date;
		WeatherCode = y.WeatherCode;
		
		System.out.println(CityName);
		System.out.println(CityCode);
		
		System.out.println(Temperature);
		System.out.println(Date);
	}
	
	public int comfireYahooWeatherIcon(){
		icons = new int[]{
				R.drawable.windrain,
				R.drawable.thunderstorm,
				R.drawable.snowrain,
				R.drawable.rain,
				R.drawable.snow,
				R.drawable.fog,
				R.drawable.wind,
				R.drawable.cloudy,
				R.drawable.sunny,
				R.drawable.doge,
		};
		switch(WeatherCode){
		case 0:case 1:case 2:
            WeatherCondition = "∑Á±©";
            weather = 5;
            return 0;
        case 3:case 4:case 37:case 38:case 39:case 45:case 46:case 47:
        	WeatherCondition = "¿◊’Û”Í";
        	weather = 3;
        	return 1;
        case 5:case 6:case 7:case 35:case 17:
        	WeatherCondition = "”Íº–—©";
        	weather = 4;
        	return 2;
        case 8:case 9:case 10:case 11:case 12:case 18:case 40:
        	WeatherCondition = "’Û”Í";
        	weather = 3;
        	return 3;
        case 13:case 14:case 15:case 16:case 41:case 42:case 43:
        	WeatherCondition = "—©";
        	weather = 2;
        	return 4;
        case 19:case 20:case 22:
        	WeatherCondition = "ŒÌ";
        	weather = 1;
        	return 5;
        case 23:case 24:case 25:
        	WeatherCondition = "∑Á";
        	weather = 1;
        	return 6;
        case 26:case 27:case 29:case 30:case 44:
        	WeatherCondition = "æ÷≤ø∂‡‘∆";
        	weather = 1;
        	return 7;
        case 31:case 32:case 33:case 34:case 36:case 28:case 21:
        	WeatherCondition = "«Á";
        	weather = 0;
        	return 8;
        default:
        	weather = 2;
        	return 9;
		}
	}
}
