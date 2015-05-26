package com.boyumi.yumitianqi;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCity extends Activity{
	MultiAutoCompleteTextView mauto;
	String[] CityNameHint = new String[] {};
	String cityname = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_city_page);
		
		findCityName();
		TextView GCN = (TextView) findViewById(R.id.GpsCityName);
		GCN.setText(MainActivity.GpsCityName);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, CityNameHint);
		mauto = (MultiAutoCompleteTextView) findViewById(R.id.cityname_add);
		mauto.setAdapter(aa);
		mauto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		Button btn_ac = (Button) findViewById(R.id.btn_addcity);
		btn_ac.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				String newCity = mauto.getText().toString();
				if (newCity==null){
					Toast.makeText(view.getContext(), "未输入任何城市", 3000).show();
				}else{
					cityname = getCity_zh(newCity);
					Intent data = new Intent();
					data.putExtra("AddNewCity", cityname);
					setResult(0, data);
//					NavigationDrawerFragment.getmDrawerLayout().closeDrawer(view);
					Log.d("addcity",cityname);
					finish();
				}
			}
		});
	}
	
	public void findCityName() {
		String res;
		try {
			InputStream in = getResources().getAssets().open("cityname.txt");
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			CityNameHint = res.split(";");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getCity_zh(String name){
		name = name.split(",")[0];
		for(int i=0;i<CityNameHint.length;i+=2){
			if (name.equals(CityNameHint[i])){
				return CityNameHint[i+1];
			}
		}
		return cityname;
	}

		

}
