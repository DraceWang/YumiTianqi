package com.boyumi.yumitianqi;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

public class AddCity extends Activity {
	
	MultiAutoCompleteTextView mauto;
	String[] CityNameHint = new String[] {};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcity);
		findCityName();
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, CityNameHint);
		mauto = (MultiAutoCompleteTextView) findViewById(R.id.MACTCityName);
		mauto.setAdapter(aa);
		mauto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}

	public void findCityName() {
		String res;
		try {
			InputStream in = getResources().getAssets().open("cityName.txt");
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			CityNameHint = res.split(";");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void clickHandle(View source){
		EditText cn = (EditText) findViewById(R.id.MACTCityName);
		MainActivity.CityName =String.valueOf(cn.getText());
		MainActivity.firstRun = false;
		Intent intent = new Intent(this,
				MainActivity.class);
		startActivity(intent);
		finish();
	}
}
