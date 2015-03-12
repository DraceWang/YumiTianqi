package com.example.weathertest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class MainActivity extends Activity {

	MultiAutoCompleteTextView mauto;
	String[] CityNameHint = new String[] {};
	String CityCode;
	
	
	public String readtogetcitycode(String fileName,String cn) {    
        try {  
            InputStream in = getResources().getAssets().open(fileName); 
            InputStreamReader read = new InputStreamReader (in,"UTF-8");  
            BufferedReader br = new BufferedReader(read);
            String tempLine;
            while ((tempLine= br.readLine()) != null) {
            	String[] tmpC=tempLine.split("\\|");
            	String[] tmpcn=tmpC[0].split(";");
            	System.out.println(tmpcn[0]);
            	System.out.println(tmpcn[1]);
            	if (cn.equals(tmpcn[0])|cn.equals(tmpcn[1])){
            		CityCode = tmpC[1];
            		return CityCode;
            	}
            }            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	
	public String readcityname(String fileName) {  
        String res = "";  
        try {  
            InputStream in = getResources().getAssets().open(fileName);  
            int length = in.available();  
            byte[] buffer = new byte[length];  
            in.read(buffer);  
            res = EncodingUtils.getString(buffer, "UTF-8");  
            CityNameHint=res.split(";");
            System.out.println(CityNameHint);
//            InputStreamReader read = new InputStreamReader (in,"UTF-8");  
//            BufferedReader br = new BufferedReader(read);
//            String tempLine;
//            while ((tempLine= br.readLine()) != null) {
//            	res = tempLine.split(";");
//            	CityNameHint = insert(CityNameHint,res[0]);
//            	
//            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		readcityname("cityName.txt");
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, CityNameHint);
		mauto = (MultiAutoCompleteTextView) findViewById(R.id.MACTCityName);
		mauto.setAdapter(aa);
		mauto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
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
		if (id == R.id.action_settings) {
			System.exit(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	StringBuilder www = new StringBuilder("");

	public void clickHandler(View source) {
		EditText mactv = (EditText) findViewById(R.id.MACTCityName);
		String cn = String.valueOf(mactv.getText());
		System.out.println(cn);
		getYHWinfo(readtogetcitycode("cityCode.txt",cn));
		// ############################################
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
					System.out.println("bbbb");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// handler.sendMessage(); //告诉主线程执行任务
			}
		}).start();
		TextView wc = (TextView) findViewById(R.id.weatherCondition);

		wc.setText(www.toString());
	}

	public void getYHWinfo(String cc) {
		final String temp_url = "http://weather.yahooapis.com/forecastrss?w="
				+ cc + "&u=c";
		new Thread() {
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
					XmlPullParser parser = XmlPullParserFactory.newInstance()
							.newPullParser();
					parser.setInput(new InputStreamReader(con.getInputStream()));

					while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
						if (parser.getEventType() == XmlResourceParser.START_TAG) {
							String tagName = parser.getName();
							if (tagName.equals("yweather:location")) {
								String cityName = parser.getAttributeValue(
										null, "city");
								String countryName = parser.getAttributeValue(
										null, "country");
								www.append("city: ");
								www.append(cityName);
								www.append(",");
								www.append(countryName);
								www.append("\n");
							}
							if (tagName.equals("pubDate")) {
								www.append("publishTime: ");
								www.append("\n");
								www.append(parser.nextText());
								www.append("\n");
							}
							if (tagName.equals("yweather:condition")) {
								String condition = parser.getAttributeValue(
										null, "text");
								String temperature = parser.getAttributeValue(
										null, "temp");
								String date = parser.getAttributeValue(null,
										"date");
								www.append("condition: ");
								www.append(condition);
								www.append("\n");
								www.append("temperature: ");
								www.append(temperature + "\u2103");
								www.append("\n");
								www.append("date: ");
								www.append(date);
							}
						}
						parser.next();
					}

					// ###############################################################################################

					System.out.println(www.toString());

					System.out.println("compelet");
				} catch (Exception e) {
					System.out.println("error:" + e);
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			}
		}
	};
}
