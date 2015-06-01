package com.yeran.healthhomecare.tools;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> fragmentpagelist;
	 public static final String[] TITLES = new String[] { "注册", "设备列表" };

	public FragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragmentpagelist) {
		super(fm);
		this.fragmentpagelist=fragmentpagelist;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		if (position < fragmentpagelist.size())
			fragment = fragmentpagelist.get(position);
		else
			fragment = fragmentpagelist.get(0);

		return fragment;
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentpagelist.size();
	}
	
//	@Override  
//    public CharSequence getPageTitle(int position)  
//    {  
//        return TITLES[position % TITLES.length];  
//    }  

}
