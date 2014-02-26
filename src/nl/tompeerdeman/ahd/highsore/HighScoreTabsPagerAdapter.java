/**
 * File: TabsPagerAdapter.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.highsore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tom Peerdeman
 * 
 */
public class HighScoreTabsPagerAdapter extends FragmentPagerAdapter {
	/**
	 * @param fm
	 */
	public HighScoreTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int fragment) {
		HighScoreFragment frag = new HighScoreFragment();
		
		/*
		 * Tabs are defined in reverse in comparison to the definition in the
		 * database.
		 * 
		 * Database:
		 * 0: Evil
		 * 1: Good
		 * 2. Combined
		 * 
		 * Tabs:
		 * 0: combined
		 * 1: Good
		 * 2: Evil
		 */
		frag.type = 2 - fragment;
		return frag;
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
