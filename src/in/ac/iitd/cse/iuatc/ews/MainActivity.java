package in.ac.iitd.cse.iuatc.ews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	@Override
	protected void onStart() {
		Log.v(TAG,"onStart");
		super.onStart();
	}

	private static final String TAG = "MAIN";
	
	@Override
	protected void onStop() {
		Log.v(TAG,"onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.v(TAG,"onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.v(TAG,"onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.v(TAG,"onResume");
		super.onResume();
	}


	private static String[] tabTitles;

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG,"onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabTitles = new String[] { this.getString(R.string.warning) };

		this.mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getSupportActionBar();

		actionBar.setHomeButtonEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		this.mViewPager = (ViewPager) findViewById(R.id.pager);
		this.mViewPager.setAdapter(this.mAppSectionsPagerAdapter);
		this.mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < this.mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(this.mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		this.mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			default:
				return new AlertListFragment();
			}
		}

		@Override
		public int getCount() {
			return tabTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabTitles[position];
		}
	}

}
