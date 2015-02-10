package com.example.weathertest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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

import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		EditText cd = (EditText) findViewById(R.id.code);
		String cc = String.valueOf(cd.getText());
		getYHWinfo(cc);
		// ############################################
		TextView wc = (TextView) findViewById(R.id.weatherCondition);
		
		
		wc.setText(www.toString());
	}

	public void getYHWinfo(String cc) {
		final String temp_url = "http://weather.yahooapis.com/forecastrss?w="+cc+"&u=c";
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
					XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
					parser.setInput(new InputStreamReader(con.getInputStream()));
					
					while(parser.getEventType()!=XmlResourceParser.END_DOCUMENT){
						System.out.println("a");
						if (parser.getEventType() == XmlResourceParser.START_TAG){
							String tagName = parser.getName();
							if(tagName.equals("yweather:location")){
								String cityName = parser.getAttributeValue(null,"city");
								String countryName = parser.getAttributeValue(null,"country");
								www.append("city: ");
								www.append(cityName);
								www.append(",");
								www.append(countryName);
								www.append("\n");
							}
							if(tagName.equals("pubDate")){
								www.append("publishTime: ");
								www.append("\n");
								www.append(parser.nextText());
								www.append("\n");
							}
							if(tagName.equals("yweather:condition")){
								String condition = parser.getAttributeValue(null,"text");
								String temperature = parser.getAttributeValue(null,"temp");
								String date = parser.getAttributeValue(null,"date");
								www.append("condition: ");
								www.append(condition);
								www.append("\n");
								www.append("temperature: ");
								www.append(temperature);
								www.append("\n");
								www.append("date: ");
								www.append(date);
							}
						}
						parser.next();
					}
					
//###############################################################################################
					// Let's read the response
//					StringBuffer buffer = new StringBuffer();
//					is = con.getInputStream();
//					BufferedReader br = new BufferedReader(
//							new InputStreamReader(is));
//					String line = null;
//					while ((line = br.readLine()) != null)
//						buffer.append(line + "\r\n");
					System.out.println(www.toString());

					System.out.println("compelet");
				} catch (Exception e) {
					System.out.println("error:" + e);
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	public void xmlFig(InputStream is){
		
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
