package com.yeran.healthhomecare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.example.health.MyApplication;
import com.example.health.R;
import com.yeran.healthhomecare.connectinternet.Deviceaddorchange;
import com.yeran.healthhomecare.connectinternet.ThreadFactory;
import com.yeran.healthhomecare.data.ShuaidaoDeviceinfor;
import com.yeran.healthhomecare.data.peoplelist;
import com.yeran.healthhomecare.tools.DevicelistAdapter;
import com.yeran.healthhomecare.tools.numorstring;

public class Devicelistfragmentpage extends Fragment implements OnClickListener {
	public static ArrayList<ShuaidaoDeviceinfor> devicelist = new ArrayList<ShuaidaoDeviceinfor>();
	private static SwipeMenuListView mListView;
	// private ListView mListView;
	private static DevicelistAdapter delistadapter;
	private RelativeLayout changeview;
	private ImageView cancelchange;
	private Button changesave;
	private View view;
	private LinearLayout list123;
	private EditText name, deviceid, oldphone, shuaidaophone, secret,
			newsecret;
	private ShuaidaoDeviceinfor item = null;
	public peoplelist plist;
	String oldnames, olddeviceid;
	private int posi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 进行获取设备列表，由工厂获取加载类
		// ThreadFactory threadFactory = new ThreadFactory();
		// Deviceaddorchange decivechange = (Deviceaddorchange) threadFactory
		// .getThread(2);
		// try {
		// devicelist = decivechange.getDevicelist();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(getTag(), "devilist----oncreateview");
		plist = peoplelist.getInstance();
		devicelist = plist.getDevicelist(getActivity());

		view = inflater.inflate(R.layout.map_fragment_devicelist, null);
		Initview();
		// 添加列表适配器
		mListView = (SwipeMenuListView) view
				.findViewById(R.id.swipeMenuListView);
		// mListView = (ListView) view.findViewById(R.id.swipeMenuListView);
		delistadapter = new DevicelistAdapter(getActivity(), devicelist);

		mListView.setAdapter(delistadapter);
		delistadapter.notifyDataSetChanged();
		// // step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Change");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				posi = position;
				item = devicelist.get(position);
				switch (index) {
				case 0:
					// open
					// open(item);
					// 显示修改界面
					Initdata(item);
					oldnames = item.username;
					olddeviceid = item.deviceid;
					list123.setVisibility(View.GONE);
					changeview.setVisibility(View.VISIBLE);
					break;
				case 1:
					// delete
					// delete(item);
					// 执行远程数据库的删除
					Updatedatabase("Del");

					break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		return view;
	}

	void Initview() {
		changeview = (RelativeLayout) view.findViewById(R.id.changeview);
		cancelchange = (ImageView) view.findViewById(R.id.cancel);
		changesave = (Button) view.findViewById(R.id.btn_save2);
		list123 = (LinearLayout) view.findViewById(R.id.list123);

		name = (EditText) view.findViewById(R.id.et_peopname2);
		deviceid = (EditText) view.findViewById(R.id.et_deviceNo2);
		oldphone = (EditText) view.findViewById(R.id.old_mobileNo2);
		shuaidaophone = (EditText) view.findViewById(R.id.et_mobileNo2);
		secret = (EditText) view.findViewById(R.id.oldpassword2);
		newsecret = (EditText) view.findViewById(R.id.newpassword2);

		cancelchange.setOnClickListener(this);
		changesave.setOnClickListener(this);
	}

	private void Initdata(ShuaidaoDeviceinfor item) {
		name.setText(item.username);
		deviceid.setText(item.deviceid);
		oldphone.setText(item.oldphone);
		shuaidaophone.setText(item.shuaidaophone);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.cancel:
			list123.setVisibility(View.VISIBLE);
			changeview.setVisibility(View.GONE);

			break;
		case R.id.btn_save2:
			numorstring numors = new numorstring();
			if ((numors.isMobileNO((oldphone.getText().toString())))
					&& (name.getText().toString() != "")
					&& ((name.getText().toString().length()) < 4)
					&& ((secret.getText().toString().length()) > 0)
					&& ((secret.getText().toString().length()) < 9)
					&& ((newsecret.getText().toString().length()) < 9)
					&& (numors.isPwnum(deviceid.getText().toString()))
					&& (numors.isMobileNO(shuaidaophone.getText().toString()))) {
				// 进行数据的更新
				ThreadFactory threadfactory = new ThreadFactory();
				threadfactory.setContext(getActivity());

				// 如果本地验证密码正确，则进入修改
				if (secret.getText().toString().equals(item.secret)) {
					// 进行本地数据库密码的验证，符合要求执行修改的网络更新和本地数据库更新
					String username = name.getText().toString();
					String deviceid2 = deviceid.getText().toString();
					String oldphone2 = oldphone.getText().toString();
					String shuaidaophone2 = shuaidaophone.getText().toString();
					String newsecret2 = newsecret.getText().toString();
					item.username = username;
					item.deviceid = deviceid2;
					item.oldphone = oldphone2;
					item.shuaidaophone = shuaidaophone2;
					if (newsecret2.length() > 0) {
						item.secret = newsecret2;
					}
					devicelist.set(posi, item);
					delistadapter = new DevicelistAdapter(getActivity(),
							devicelist);
					mListView.setAdapter(delistadapter);
					// delistadapter.notifyDataSetChanged();
					// 远程数据库的更新
					Updatedatabase("Chg");

				} else {
					Toast.makeText(getActivity(), "原始密码不正确！",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(getActivity(), "请填写正确的格式！", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
	}

	/**
	 * 远程数据库操作
	 * */
	@SuppressLint("SimpleDateFormat")
	private void Updatedatabase(String treatway) {
		String secret123 = secret.getText().toString();
		if (newsecret.getText().length() > 0) {
			secret123 = newsecret.getText().toString();

		}
		
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String createtime = sDateFormat.format(new java.util.Date());
		
		// 远程数据库更新操作
		ThreadFactory threadfactory = new ThreadFactory();
		threadfactory.setContext(getActivity());
		Deviceaddorchange deviceclass = (Deviceaddorchange) threadfactory
				.getThread(2);
		if(treatway.equals("Chg")){
		deviceclass.setparams(deviceid.getText().toString(),
				MyApplication.homeid, name.getText().toString(), oldphone
						.getText().toString(), shuaidaophone.getText()
						.toString(), secret.getText().toString(),secret123,createtime, treatway);
		}else if(treatway.equals("Del")){
			deviceclass.setparams(item.deviceid,MyApplication.homeid,
						item.username, item.oldphone, item.shuaidaophone,
						item.secret,item.secret, createtime,treatway );
		}
		if (deviceclass.startthread()) {
			if (treatway.equals("Chg")) {
				plist.updateDevicelist(new ShuaidaoDeviceinfor(item.deviceid,
						item.username, item.oldphone, item.shuaidaophone,
						item.secret, null), oldnames, olddeviceid,
						getActivity());

				list123.setVisibility(View.VISIBLE);
				changeview.setVisibility(View.GONE);
			} else if(treatway.equals("Del")){
				devicelist.remove(posi);
				delistadapter.notifyDataSetChanged();
				// 本地数据库删除
				plist.deleteDevice(item, getActivity());
			}
		}
	}

	/**
	 * 刷新列表
	 * */
	public static void refresh(FragmentActivity activity) {
		// TODO Auto-generated method stub
		delistadapter = new DevicelistAdapter(activity, devicelist);
		mListView.setAdapter(delistadapter);
	}

	private int dp2px(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,
				getResources().getDisplayMetrics());
	}
}
