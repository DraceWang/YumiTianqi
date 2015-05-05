package com.boyumi.yumitianqi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddCity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_city_page);
		
	}
	
	
	public void addcity(View view){
		
		Toast.makeText(view.getContext(), "click the button", 3000).show();	
//		NavigationDrawerFragment.getmDrawerLayout().closeDrawer(view);
		finish();
	}
}
