package com.boyumi.yumitianqi;

import java.util.List;
import java.util.Map;

public interface Weather {
	
	
	public void getWeather();

	public CharSequence getCityName();

	public CharSequence getTemperature();

	public int getWeatherIconNum();
	

	public List<? extends Map<String, ?>> getForecastList();

	public CharSequence getWeatherCondition();

	public CharSequence getDate();
	
	public Map<String, String> getAirQualityMap();
}
