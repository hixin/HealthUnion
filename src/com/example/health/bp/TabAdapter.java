package com.example.health.bp;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;



public class TabAdapter extends  FragmentPagerAdapter
{


	public static final String[] TITLES = new String[] { "血压计测量", "手动输入"};

	private List<Fragment> mFragments;

	public TabAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		mFragments=fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}
	
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		return TITLES[position % TITLES.length];
		
	}

	
	
	/*public TabAdapter(FragmentManager fm)
	{
		super(fm);
	}

	//它们只会在新生成 Fragment 对象时执行一遍
	@Override
	public Fragment getItem(int arg0)
	{
		FragmentDetailOneItem fragment = new FragmentDetailOneItem(arg0);
		LogUtil.i("2","FragmentDetailOneBO");
		
		return fragment;
		
	}




	@Override
	public int getCount()
	{
		return TITLES.length;
	}
*/
}



