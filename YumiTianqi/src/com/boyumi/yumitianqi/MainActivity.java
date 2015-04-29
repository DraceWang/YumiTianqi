package com.boyumi.yumitianqi;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

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
		mTitle = tittlelist.get(number-1);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
				//初始化天气并获取天气信息，考虑改成百度天气
//				Weather y = new YahooWeather(rootView, "南京市");
//				Weather y = new BaiduWeather(rootView, "南京市");
				Weather y = new JuheWeather(rootView,NavigationDrawerFragment.getNavList().get(0));
				//玉米的显示考虑在哪里初始化,最好别在这
				Cron c = new Cron(y, rootView);
				c.bugsComing();
				freshWeatherView(rootView, y);
				return rootView;
			} else {
				View rootView = inflater.inflate(R.layout.fragment_othercity,
						container, false);
				Weather y = new JuheWeather(rootView,NavigationDrawerFragment.getNavList().get(pageNum));
				freshWeatherView(rootView, y);
				return rootView;
			}
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

		public void freshWeatherView(View view, Weather y) {
			TextView cityname = (TextView) view.findViewById(R.id.CityName);
			cityname.setText(y.getCityName());
			TextView temperatrue = (TextView) view
					.findViewById(R.id.currentTemperature);
			temperatrue.setText(y.getTemperature());
			ImageView weathericon = (ImageView) view
					.findViewById(R.id.WeatherIcon);
			weathericon.setImageResource(y.getWeatherIconNum());
			TextView weathercondition = (TextView) view.findViewById(R.id.WeatherCondition);
			weathercondition.setText(y.getWeatherCondition());
			TextView date = (TextView) view.findViewById(R.id.date);
			date.setText(y.getDate());
			ListView frocateListView = (ListView) view
					.findViewById(R.id.forecast);
			SimpleAdapter simpleAdapter = new SimpleAdapter(
					view.getContext(), y.getForecastList(),
					R.layout.forecast_list, new String[] { "day",
							"weather", "temprang" }, new int[] { R.id.day,
							R.id.weather, R.id.temperatrue_rang });
			frocateListView.setAdapter(simpleAdapter);
		}
	}
}
