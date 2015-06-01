package com.example.health.bo;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;



public class TabAdapterBO extends  FragmentPagerAdapter
{


	public static final String[] TITLES = new String[] { "血氧测量", "手动输入"};

	private List<Fragment> bo_fragments;

	public TabAdapterBO(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		bo_fragments=fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return bo_fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bo_fragments.size();
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



