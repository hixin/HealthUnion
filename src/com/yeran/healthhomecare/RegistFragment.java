package com.yeran.healthhomecare;


import java.lang.reflect.Field;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.health.R;
import com.yeran.healthhomecare.tools.CustomViewpage;
import com.yeran.healthhomecare.tools.FragmentAdapter;

public class RegistFragment extends Fragment implements OnClickListener {
	View contactsLayout;
	private TextView register, devicelist, registerchoose, devicelistchoose;

	private CustomViewpage mpager;
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();
	private FragmentAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("Rsgisterfagmet", "---oncreate---");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contactsLayout = inflater.inflate(R.layout.map_fragment_item_detail,
				container, false);

		mpager = (CustomViewpage) contactsLayout.findViewById(R.id.pager);

		Registerfragmentpage1 refragmentpage = new Registerfragmentpage1();
		Devicelistfragmentpage delistfragmentpage = new Devicelistfragmentpage();

		pagerItemList.add(refragmentpage);
		pagerItemList.add(delistfragmentpage);

		mAdapter = new FragmentAdapter(getChildFragmentManager(), pagerItemList);
		mpager.setAdapter(mAdapter);

		

		
		mpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@SuppressLint("NewApi")
			@Override
			public void onPageSelected(int position) {

				if (myPageChangeListener != null)
					myPageChangeListener.onPageSelected(position);
				if (position == 0) {
					// registerchoose.setBackground(getResources().getDrawable(R.drawable.login_bg));
					registerchoose.setBackgroundColor(Color.BLUE);
					devicelistchoose.setBackgroundColor(Color.WHITE);
				} else {
					// devicelistchoose.setBackground(getResources().getDrawable(R.drawable.login_bg));
					devicelistchoose.setBackgroundColor(Color.BLUE);
					registerchoose.setBackgroundColor(Color.WHITE);

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});
		Initview();
		return contactsLayout;
	}

	public boolean isFirst() {
		if (mpager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mpager.getCurrentItem() == pagerItemList.size() - 1)
			return true;
		else
			return false;
	}

	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {

		myPageChangeListener = l;

	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}

	private void Initview() {
		register = (TextView) contactsLayout.findViewById(R.id.register);
		registerchoose = (TextView) contactsLayout
				.findViewById(R.id.chooseregister);

		devicelist = (TextView) contactsLayout.findViewById(R.id.btn_list);
		devicelistchoose = (TextView) contactsLayout
				.findViewById(R.id.choosedevice);

		register.setOnClickListener(this);
		devicelist.setOnClickListener(this);

	}

	/**
	 * 点击事件定义
	 * 
	 * */
	@SuppressLint("NewApi")
	@Override
	public void onClick(View clickitem) {
		// TODO Auto-generated method stub
		switch (clickitem.getId()) {
		// 展开设备、用户列表
		case R.id.register:
			// registerchoose.setBackground(getResources().getDrawable(R.drawable.login_bg));

			registerchoose.setBackgroundColor(Color.BLUE);
			devicelistchoose.setBackgroundColor(Color.WHITE);
			// myPageChangeListener.onPageSelected(0);

			mpager.setCurrentItem(0);


			break;
		case R.id.btn_list:
			// devicelistchoose.setBackground(getResources().getDrawable(R.drawable.login_bg));
			devicelistchoose.setBackgroundColor(Color.BLUE);
			registerchoose.setBackgroundColor(Color.WHITE);
			mpager.setCurrentItem(1);
			break;
		}
	}
	

    @Override
	public void onDetach() {
		super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}


}
