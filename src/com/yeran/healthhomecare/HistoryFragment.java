package com.yeran.healthhomecare;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.health.R;
import com.yeran.healthhomecare.data.Normaloldlocations;
import com.yeran.healthhomecare.data.Shuaidaoinfors;
import com.yeran.healthhomecare.data.oldsloctions;
import com.yeran.healthhomecare.data.peopinfor;
import com.yeran.healthhomecare.tools.FormDataUDP;
import com.yeran.healthhomecare.tools.PeoplelistAdapter;
import com.yeran.healthhomecare.tools.ShuaidaolistAdapter;

public class HistoryFragment extends Fragment {
	/**
	 * 历史记录控件声明
	 * */
	Spinner mspinner;
	SwipeMenuListView mshuaidaolist;
	TextView haveornot;
	View rootView = null;
	private List<peopinfor> peoplist;
	private PeoplelistAdapter padapter;
	private ShuaidaolistAdapter sladpter;
	public static peopinfor item = null;
	Shuaidaoinfors pelist = new Shuaidaoinfors(getActivity());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("goin--history---oncreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.map_fragment_item_detailhistory,
				container, false);
		Inithistory();
		return rootView;
	}

	/**
	 * 历史记录界面初始化
	 * */
	private void Inithistory() {
		mspinner = (Spinner) rootView.findViewById(R.id.peopchoose);
		mshuaidaolist = (SwipeMenuListView) rootView
				.findViewById(R.id.shuaidaolist);
		haveornot = (TextView) rootView.findViewById(R.id.haveornot);

		peoplist = new ArrayList<peopinfor>();

		// 设备信息集合peoplist2
		ArrayList<oldsloctions> peoplist2 = new ArrayList<oldsloctions>();
		// 获取所有设备信息绑定到spinner上
		Normaloldlocations nrmalold = new Normaloldlocations();
		oldsloctions old123 = new oldsloctions();
		old123.setName2("全部");
		old123.setDeviceid("000000");
		peoplist2.add(old123);
		for (oldsloctions oldsloctions : nrmalold.getDevices(getActivity())) {
			peoplist2.add(oldsloctions);
		}
		for (oldsloctions peop : peoplist2) {
			Log.v(getTag(), peop.getName() + peop.getDeviceid());
		}

		padapter = new PeoplelistAdapter(getActivity(), peoplist2);
		mspinner.setAdapter(padapter);

		mspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				oldsloctions str = (oldsloctions) parent
						.getItemAtPosition(position);
				String deviceid = str.getDeviceid();
				peoplist = pelist.getAllshuaidaoinfor(deviceid, getActivity());
				if (peoplist.size() == 0) {
					mshuaidaolist.setVisibility(View.GONE);
					haveornot.setVisibility(View.VISIBLE);
				} else {
					mshuaidaolist.setVisibility(View.VISIBLE);
					haveornot.setVisibility(View.GONE);
					sladpter = new ShuaidaolistAdapter(getActivity(), peoplist);
					mshuaidaolist.setAdapter(sladpter);
				}

				SwipeMenuCreator creator = new SwipeMenuCreator() {

					@Override
					public void create(SwipeMenu menu) {
						// create "open" item
						SwipeMenuItem openItem = new SwipeMenuItem(
								getActivity());
						// set item background
						openItem.setBackground(new ColorDrawable(Color.rgb(
								0xC9, 0xC9, 0xCE)));
						// set item width
						openItem.setBackground(new ColorDrawable(Color.GREEN));
						// openItem.setTitle("Map");
						// openItem.setTitleColor(Color.BLACK);
						// openItem.setTitleSize(18);
						// set item width
						openItem.setWidth(dp2px(90));
						// set a icon
						openItem.setIcon(R.drawable.gotomap);
						// add to menu
						menu.addMenuItem(openItem);
						// create "delete" item
						SwipeMenuItem deleteItem = new SwipeMenuItem(
								getActivity());
						// set item background
						deleteItem.setBackground(new ColorDrawable(Color.rgb(
								0xF9, 0x3F, 0x25)));
						// set item width
						deleteItem.setWidth(dp2px(90));
						// set a icon
						deleteItem.setIcon(R.drawable.ic_delete);
						// add to menu
						menu.addMenuItem(deleteItem);
					}
				};
				// set creator
				mshuaidaolist.setMenuCreator(creator);

				// step 2. listener item click event
				mshuaidaolist
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(int position,
									SwipeMenu menu, int index) {
								item = peoplist.get(position);
								switch (index) {
								case 0:
									// open(item);
									// 在地图页面显示,跳转到地图fragment。
									((ItemListActivity) getActivity())
											.onItemSelected("1");
									((ItemListFragment) ((ItemListActivity) getActivity())
											.getSupportFragmentManager()
											.findFragmentById(R.id.item_list))
											.setActivatedPosition(0);

									break;
								case 1:
									// delete
									// delete(item);
									peoplist.remove(position);
									sladpter.notifyDataSetChanged();
									// 本地数据库删除
									Shuaidaoinfors sd = new Shuaidaoinfors();
									sd.deletesdinfor(item, getActivity());
									item = null;
									break;
								/*
								 * 测试，现在已经无用 Intent intent = new
								 * Intent("UdpService"); // 请求位置的数据格式 String
								 * askwords = new
								 * FormDataUDP().getAskwords("Askforlocation",
								 * "100001"); intent.putExtra("askwords",
								 * askwords);
								 * getActivity().startService(intent);
								 */

								}
								return false;
							}
						});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	private int dp2px(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,
				getResources().getDisplayMetrics());
	}
}
