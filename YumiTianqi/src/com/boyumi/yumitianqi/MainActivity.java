package com.boyumi.yumitianqi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
	
	GPS g = new GPS();
	
	
	static String CityName = null;
	Weather w = new Weather();
	static boolean isUpdate;
	
	ImageView c;
	
	Yumi a = new Yumi();
	
	ProgressDialog pd;
	static boolean firstRun = true;
	
	Context context;
	Context GPScontext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GPScontext = this;
		if(firstRun&&(CityName==null)){
			loadLastInfo();
		}
		showcron();
		OnClickImage();
		bugsComing();
	}

	
	
	public void waitingDialog(){
		
		pd= new ProgressDialog(this);
//		pd.show(this, "请稍等", "正在从服务器获取天气信息",false,true);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle("请稍等");
		pd.setMessage("正在从服务器获取天气信息");
		pd.show();
		new Thread(){
			public void run (){
				while (!isUpdate){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				pd.dismiss();
			}
		}.start();
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
	@Override
	protected void onStart(){
		context = this;
		super.onStart();
		waitingDialog();
		if (CityName==null){
			g.getGPSInfo(GPScontext);
			CityName = g.getCityName();
			w.getYahooWeather(context,CityName);
		}
		System.out.println(w.WeatherCondition);
		
		refreshWeather();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		writeWeatherInfo();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		if(firstRun){
			loadLastInfo();
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		writeWeatherInfo();
	}
	
	public void writeWeatherInfo(){
		try{
			deleteFile("lastWeatherInfo.txt");
			FileOutputStream fos = openFileOutput("lastWeatherInfo.txt", MODE_APPEND);
			PrintStream ps = new PrintStream(fos);
			ps.println("CityName:"+CityName);
			ps.println("Temperature:"+w.Temperature);
			ps.println("WeatherCode:"+w.WeatherCode);
			ps.println("WeatherCondition:"+w.WeatherCondition);
			ps.println("Date:"+w.Date);
			System.out.println("WeatherInfo saved!");
			ps.close();
			firstRun = false;
			System.out.println("WeatherInfo saved!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadLastInfo(){
		System.out.println("loading last WeatherInfo");
		try{
			FileInputStream fis = openFileInput("lastWeatherInfo.txt");
			InputStreamReader read = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(read);
			String[] tmp;
			tmp = br.readLine().split(":");
			System.out.println(tmp[0]);
			System.out.println(tmp[1]);
			CityName = tmp[1];
			tmp = br.readLine().split(":");
			w.Temperature = tmp[1];
			tmp = br.readLine().split(":");
			w.WeatherCode = Integer.valueOf(tmp[1]);
			tmp = br.readLine().split(":");
			w.WeatherCondition = tmp[1];
			tmp = br.readLine().split(":");
			w.Date = tmp[1];
			br.close();
			fis.close();
			System.out.println("Done!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		refreshWeather();
	}
	
	public void refreshWeather(){
		int i = w.comfireYahooWeatherIcon();
		ImageButton wi = (ImageButton) findViewById(R.id.WeatherIcon);
		wi.setBackgroundResource(w.icons[i]);
		System.out.println("finish");
		TextView cn = (TextView) findViewById(R.id.CityName);
		cn.setText(CityName);
		TextView wc = (TextView) findViewById(R.id.WeatherCondition);
		wc.setText(w.WeatherCondition);
		TextView t = (TextView) findViewById(R.id.Temperature);
		t.setText(w.Temperature);
		TextView d = (TextView) findViewById(R.id.Date);
		d.setText(w.Date);
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
		if (id == R.id.Addcity) {
			Intent intent = new Intent(MainActivity.this,AddCity.class);
			startActivity(intent);
			w.getYahooWeather(context,CityName);

			return true;
		}
		if (id == R.id.Refresh) {
			
			w.getYahooWeather(context,CityName);
			System.out.println("Done refresh!");
			return true;
		}
		if (id == R.id.About) {
			Intent intent = new Intent(MainActivity.this,About.class);
			startActivity(intent);
//			finish();
			
			System.out.println("about");
			return true;
		}
		if (id == R.id.Exit) {
			System.exit(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
			public void OnClickImage(){
				ImageView wi = (ImageView) findViewById(R.id.WeatherIcon);
				wi.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						w.getYahooWeather(context,CityName);
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
			
			
}
