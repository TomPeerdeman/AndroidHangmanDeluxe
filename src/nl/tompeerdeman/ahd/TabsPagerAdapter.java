/**
 * File: TabsPagerAdapter.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tom Peerdeman
 * 
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
	/**
	 * @param fm
	 */
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int fragment) {
		return new HighScoreFragment();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return 3;
	}
}
