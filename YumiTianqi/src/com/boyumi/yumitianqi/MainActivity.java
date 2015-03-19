package com.boyumi.yumitianqi;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	LocationManager lm;
	String Longitude;
	String Latitude;
	String CityName;
	
	String CityCode;
	String WeatherCondition = "水生火热(→_→)";
	String Temperature = "  爆表啦(--)#";
	String Date;
	int WeatherCode;
	int weather;
	int[] icons;
	
	ImageView c;
	
	Yumi a = new Yumi();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		show();
		showcron();
		OnClickImage();
		bugsComing();
	}
	
	public void showcron(){
		c = (ImageView) findViewById(R.id.cron);
//		c.setImageResource(a.cronImage[a.getCronIconNum()]);
		c.setImageBitmap(HandleImage());
		ProgressBar gv = (ProgressBar) findViewById(R.id.progressBar_gv);
		gv.setProgress(a.grownValue);
		ProgressBar wv = (ProgressBar) findViewById(R.id.progressBar_wv);
		wv.setProgress(a.waterValue);
		ProgressBar hv = (ProgressBar) findViewById(R.id.progressBar_hv);
		hv.setProgress(a.healthyValue);
	}
	
	public void bugsComing(){
		int p = new Random().nextInt(11);
		if (p < 10)a.stauteChange(0, -3, -10);
		showcron();
	}
	public Bitmap HandleImage(){
		Resources res = getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(res,a.cronImage[a.getCronIconNum()]);
        Matrix matrix = new Matrix();  
        float scale = (float) (40+a.grownValue*0.3)/100;
        matrix.setScale(scale, scale); 
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),   
        		bitmap.getHeight(),matrix,true);
		return resizeBmp;
	}
	
	public void show(){
		getGPSInfo();
		getCityName();
		getWeather(CityName);

		int i = comfirmWeatherIcon();
		ImageButton wi = (ImageButton) findViewById(R.id.WeatherIcon);
		wi.setBackgroundResource(icons[i]);
		System.out.println("finish");
		TextView cn = (TextView) findViewById(R.id.CityName);
		cn.setText(CityName);
		TextView wc = (TextView) findViewById(R.id.WeatherCondition);
		wc.setText(WeatherCondition);
		TextView t = (TextView) findViewById(R.id.Temperature);
		t.setText(Temperature);
		TextView d = (TextView) findViewById(R.id.Date);
		d.setText(Date);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Refresh) {
			getWeather(CityName);
			System.out.println("Done refresh!");
			return true;
		}
		if (id == R.id.About) {
			System.out.println("about");
			return true;
		}
		if (id == R.id.Exit) {
			System.exit(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	public void getGPSInfo() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location locationGPS = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		System.out.println("GPS:"+locationGPS);
		Location locationPassive = lm
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		System.out.println("passive:"+locationPassive);
		Location locationNetwork = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		System.out.println("network:"+locationNetwork);
		if(locationGPS!=null)updateLocation(locationGPS);
		if(locationPassive!=null)updateLocation(locationPassive);
		if(locationGPS==null&&locationPassive==null&&locationNetwork==null){
			System.out.println("无法获取定位信息");
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
						System.out.println(location);					}
				});
	}

	// get cityName from baiduAPI
	public void getCityName() {
		
//		final String baiduAPI_url = "http://api.map.baidu.com/geocoder?output=json&location="
//				+ Latitude+ "," + Longitude  + "&key=eZnlziqWIf9hYIokB4a4D7Bo";
		final String baiduAPI_url = "http://api.map.baidu.com/geocoder?output=json&location=31.9377,118.6386&key=eZnlziqWIf9hYIokB4a4D7Bo";
		
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
//					Log.i("cat", ">>>>>>" + strResult);
					CityName = new JSONObject(strResult)
							.getJSONObject("result")
							.getJSONObject("addressComponent")
							.getString("city");
					System.out.println(CityName);
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
	}

	

	public void updateLocation(Location newLocation) {
		if (newLocation != null) {
			Longitude = String.valueOf(newLocation.getLongitude());
			Latitude = String.valueOf(newLocation.getLatitude());
		} else {

		}
	}
	
	// get weather from yahoo
			public void getWeather(String CityName) {
				CityCode = getcitycode("cityCode.txt", CityName);
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
//										WeatherCondition = parser.getAttributeValue(
//												null, "text");
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
				
			}

			public String getcitycode(String fileName, String cn) {
				String citywhere = "你正在穿越虫洞？！";
				try {

					InputStream in = getResources().getAssets().open(fileName);
//					FileInputStream in = openFileInput(fileName);
					InputStreamReader read = new InputStreamReader(in, "UTF-8");
					BufferedReader br = new BufferedReader(read);
					String tempLine;
					while ((tempLine = br.readLine()) != null) {
						String[] tmpC = tempLine.split("\\|");
						String[] tmpcn = tmpC[0].split(";");
						if (cn.equals(tmpcn[0]) | cn.equals(tmpcn[1])) {
							String citycode = tmpC[1];
							System.out.println(citycode+"find it!");				
							return citycode;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return citywhere;
			}
			public int comfirmWeatherIcon(){
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
		            WeatherCondition = "风暴";
		            weather = 5;
		            return 0;
		        case 3:case 4:case 37:case 38:case 39:case 45:case 46:case 47:
		        	WeatherCondition = "雷阵雨";
		        	weather = 3;
		        	return 1;
		        case 5:case 6:case 7:case 35:case 17:
		        	WeatherCondition = "雨夹雪";
		        	weather = 4;
		        	return 2;
		        case 8:case 9:case 10:case 11:case 12:case 18:case 40:
		        	WeatherCondition = "阵雨";
		        	weather = 3;
		        	return 3;
		        case 13:case 14:case 15:case 16:case 41:case 42:case 43:
		        	WeatherCondition = "雪";
		        	weather = 2;
		        	return 4;
		        case 19:case 20:case 21:case 22:
		        	WeatherCondition = "雾";
		        	weather = 1;
		        	return 5;
		        case 23:case 24:case 25:
		        	WeatherCondition = "风";
		        	weather = 1;
		        	return 6;
		        case 26:case 27:case 28:case 29:case 30:case 44:
		        	WeatherCondition = "局部多云";
		        	weather = 1;
		        	return 7;
		        case 31:case 32:case 33:case 34:case 36:
		        	WeatherCondition = "晴";
		        	weather = 0;
		        	return 8;
		        default:
		        	weather = 2;
		        	return 9;
				}
			}
			
			public void OnClickImage(){
				ImageView wi = (ImageView) findViewById(R.id.WeatherIcon);
				wi.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						getWeather(CityName);
					}
				});
				ImageButton water = (ImageButton) findViewById(R.id.imageButton_water);
				water.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						a.stauteChange(0, 10, 0);
						showcron();
					}
				});
				ImageButton bug = (ImageButton) findViewById(R.id.imageButton_bug);
				bug.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						a.stauteChange(0, 0, 10);
						showcron();
					}
				});
				ImageButton healty = (ImageButton) findViewById(R.id.imageButton_feiliao);
				healty.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						a.stauteChange(10, 0, 0);
						showcron();
					}
				});
			}
			
//			public void OnTouchImage(){
//				ImageView wi = (ImageView) findViewById(R.id.WeatherIcon);
//				wi.setOnTouchListener(new OnTouchListener() {
//					
//					@Override
//					public boolean onTouch(View arg0, MotionEvent arg1) {
//						// TODO Auto-generated method stub
//						getWeather(CityName);
//						return false;
//					}
//				});
//				ImageButton water = (ImageButton) findViewById(R.id.imageButton_water);
//			}
}
