package com.boyumi.yumitianqi;

import java.util.ArrayList;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 *
	 */
	static Cron c = new Cron();
	static String GpsCityName;
	String CurrentCityName;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		ImageButton addcity = (ImageButton) findViewById(R.id.addCity);
		addcity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(getApplicationContext(),
						AddCity.class), 0);
			}
		});

		// test
		// OpenWeather.test();

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		ArrayList<String> tittlelist = NavigationDrawerFragment.getNavList();
		mTitle = tittlelist.get(number - 1);
	}

	@SuppressWarnings("deprecation")
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// SharedPreferences.Editor editor =
		// getSharedPreferences("nav_citylist", 0).edit();
		// for (int i=0;i<NavigationDrawerFragment.getNavList().size();i++){
		// editor.putString("navlist_"+i,
		// NavigationDrawerFragment.getNavList().get(i));
		// }
		// editor.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.about) {
			startActivity(new Intent(MainActivity.this,AboutUs.class));
			return true;
		}
		if (id == R.id.exit) {
			System.exit(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		// view used later
		private static View viewTopass;
		static Map<String, String> AQ;
		static int ikey = 0;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// take the argument from bundle , it's represent section number
			int pageNum = (Integer) getArguments().get(ARG_SECTION_NUMBER) - 1;
			// I need two different pages
			if (pageNum == 0) {
				View rootView = inflater.inflate(R.layout.fragment_main,
						container, false);
				// 初始化天气并获取天气信息
				// Weather y = new YahooWeather(rootView, "南京市");
				// Weather y = new OpenWeather(rootView,
				// NavigationDrawerFragment.getNavList().get(0));
				Weather y = new JuheWeather(rootView, NavigationDrawerFragment
						.getNavList().get(0));

				// 玉米的显示
				c.bugsComing(y, rootView);
				freshWeatherView(rootView, y);
				setViewTopass(rootView);
				return rootView;
			} else {
				View rootView = inflater.inflate(R.layout.fragment_othercity,
						container, false);
				// Weather y = new
				// BaiduWeather(rootView,NavigationDrawerFragment.getNavList().get(pageNum));
				Weather y = new JuheWeather(rootView, NavigationDrawerFragment
						.getNavList().get(pageNum));
				freshWeatherView(rootView, y);
				setViewTopass(rootView);
				return rootView;
			}
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

		public static void freshWeatherView(View view, Weather y) {
			TextView cityname = (TextView) view.findViewById(R.id.CityName);
			cityname.setText(y.getCityName());
			TextView temperatrue = (TextView) view
					.findViewById(R.id.currentTemperature);
			temperatrue.setText(y.getTemperature());
			ImageView weathericon = (ImageView) view
					.findViewById(R.id.WeatherIcon);
			weathericon.setImageResource(y.getWeatherIconNum());
			TextView weathercondition = (TextView) view
					.findViewById(R.id.WeatherCondition);
			weathercondition.setText(y.getWeatherCondition());
			TextView date = (TextView) view.findViewById(R.id.date);
			date.setText(y.getDate());
			ListView frocateListView = (ListView) view
					.findViewById(R.id.forecast);
			SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(),
					y.getForecastList(), R.layout.forecast_list, new String[] {
							"day", "weather", "temprang" }, new int[] {
							R.id.day, R.id.weather, R.id.temperatrue_rang });
			frocateListView.setAdapter(simpleAdapter);
			AQ = y.getAirQualityMap();
			if (AQ.isEmpty()) {

			} else {
				View btn_AQ = (View) view.findViewById(R.id.btn_AQ);
				TextView AQ_t = (TextView) view.findViewById(R.id.AQ_title);
				TextView AQ_c = (TextView) view.findViewById(R.id.AQ_content);
				AQ_t.setText("空气质量");
				switch (y.getAirQualityMap().get("空气质量")) {
				case "优":
					AQ_t.setBackgroundColor(view.getResources().getColor(
							R.color.AQ_A));
					AQ_c.setText("优");
					break;
				case "良":
					AQ_t.setBackgroundColor(view.getResources().getColor(
							R.color.AQ_B));
					AQ_c.setText("良");
					break;
				case "轻度污染":
					AQ_t.setBackgroundColor(view.getResources().getColor(
							R.color.AQ_C));
					AQ_c.setText("轻度污染");
					break;
				case "中度污染":
					AQ_t.setBackgroundColor(view.getResources().getColor(
							R.color.AQ_D));
					AQ_c.setText("中度污染");
					break;
				case "重度污染":
					AQ_t.setBackgroundColor(view.getResources().getColor(
							R.color.AQ_E));
					AQ_c.setText("重度污染");
					break;
				case "严重污染":
					AQ_t.setBackgroundColor(view.getResources().getColor(
							R.color.AQ_F));
					AQ_c.setText("严重污染");
					break;
				default:
					break;
				}
				btn_AQ.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ikey++;
						if (ikey >= AQ.keySet().size()) {
							ikey = 0;
						}
						String key = AQ.keySet().toArray()[ikey].toString();
						TextView AQt = (TextView) v.findViewById(R.id.AQ_title);
						AQt.setText(key);
						TextView AQc = (TextView) v
								.findViewById(R.id.AQ_content);
						AQc.setText(AQ.get(key));
					}

				});
			}
		}

		/**
		 * 
		 * getter and setter
		 */
		public static View getViewTopass() {
			return viewTopass;
		}

		public static void setViewTopass(View viewTopass) {
			PlaceholderFragment.viewTopass = viewTopass;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data == null) {
			return;
		}
		switch (requestCode) {
		case 0:
			if (resultCode == 0) {
				CurrentCityName = data.getStringExtra("AddNewCity");
				NavigationDrawerFragment.getNavList().add(CurrentCityName);
				Log.d("addCity", "现在添加了一个城市：" + CurrentCityName);
			}
		}
	}
}
