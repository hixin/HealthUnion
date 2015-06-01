package com.yeran.healthhomecare;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;

import com.example.health.R;
import com.yeran.healthhomecare.data.oldsloctions;
import com.yeran.healthhomecare.data.peopinfor;
import com.yeran.healthhomecare.tools.Appcontext;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private RegistFragment registfragment;
	private ItemDetailFragment fragment;
	private FragmentManager fragmentManager;
	private HistoryFragment historyfragment;
	public static oldsloctions peop=null;
	public static peopinfor sdpeopinfor=null;
	
	/**
	 * 广播接收
	 * */
	private MyBroadcastRecever myBroadcastRecever;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_item_list);
		fragmentManager = this.getSupportFragmentManager();

		/*Display display = getWindowManager().getDefaultDisplay();
		if (display.getWidth() > display.getHeight()) {
			setContentView(R.layout.map_activity_item_twopane);
		} else {
			setContentView(R.layout.map_activity_item_list);
		}*/
		setContentView(R.layout.map_activity_item_list);
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);

			IntentFilter filter = new IntentFilter();
			myBroadcastRecever = new MyBroadcastRecever();
			// 设置接收广播的类型，这里要和Service里设置的类型匹配，还可以在AndroidManifest.xml文件中注册
			filter.addAction("com.example.broadcast.getlocations");
			filter.addAction("com.example.broadcast.getshuaidao");
			this.registerReceiver(myBroadcastRecever, filter);
		}

		// pop原来的事物
		// fragmentManager.popBackStack();
		// 进入直接进入地图界面
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		hideFragments(transaction);
		if (fragment == null) {
			fragment = new ItemDetailFragment();
			transaction.replace(R.id.item_detail_container, fragment);
		} else {
			// transaction.show(fragment);
			transaction.replace(R.id.item_detail_container, fragment);
		}
		// transaction.replace(R.id.item_detail_container, fragment);
		transaction.commit();
		 ActionBar actionBar = getActionBar();  
         actionBar.setDisplayShowHomeEnabled(false);
         actionBar.setDisplayHomeAsUpEnabled(true);
		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:return true;
		}
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			// fragmentManager.popBackStack();

			switch (Integer.parseInt(id)) {
			case 1:
				if (fragment == null) {
					fragment = new ItemDetailFragment();
				}
				if (!fragment.isAdded()) {
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.item_detail_container, fragment,
									"Detail").commit();
				}
				break;
			case 2:
				if (historyfragment == null) {
					historyfragment = new HistoryFragment();
				}

				if (!historyfragment.isAdded()) {
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.item_detail_container,
									historyfragment, "Detai2").commit();
				}

				break;
			case 3:
				if (registfragment == null) {
					registfragment = new RegistFragment();

				}
				if (!registfragment.isAdded()) {
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.item_detail_container,
									registfragment, "Detai3").commit();
				}

				break;
			}
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (fragment != null) {
			transaction.hide(fragment);
		}

		if (registfragment != null) {
			transaction.hide(registfragment);
		}
		if (historyfragment != null) {
			transaction.hide(historyfragment);
		}
	}

	class MyBroadcastRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 接收位置信息以及摔倒信息

			if (intent.getAction().equals("com.example.broadcast.getlocations")) {
				String name = intent.getStringExtra("name");
				String deviceid = intent.getStringExtra("deviceid");
				double latitude = Double.parseDouble(intent
						.getStringExtra("weidu"));
				double longitude = Double.parseDouble(intent
						.getStringExtra("jingdu"));
				String weizhi = intent.getStringExtra("weizhi");
				// 获得位置数据
				peop = new oldsloctions();
				peop.setName2(name);
				peop.setDeviceid(deviceid);
				peop.setLocation(latitude, longitude);
				peop.setWeizhi(weizhi);
				
				// 在地图页面显示,跳转到地图fragment。
				ItemListActivity.this.onItemSelected("1");
				((ItemListFragment)ItemListActivity.this
						.getSupportFragmentManager()
						.findFragmentById(R.id.item_list))
						.setActivatedPosition(0);

			} else if (intent.getAction().equals(
					"com.example.broadcast.getshuaidao")) {
				Log.v("Itemlistactivity", "接收到摔倒广播");
				String name = intent.getStringExtra("name");
				String deviceid = intent.getStringExtra("deviceid");
				double latitude = intent.getDoubleExtra("weidu", 0);
				double longitude = intent.getDoubleExtra("jingdu", 0);
				String weizhi = intent.getStringExtra("weizhi");
				String time = intent.getStringExtra("time");

				sdpeopinfor = new peopinfor();
				sdpeopinfor.setName2(name);
				sdpeopinfor.setDeviceid(deviceid);
				sdpeopinfor.setLocation(latitude, longitude);
				sdpeopinfor.setWeizhi(weizhi);
				sdpeopinfor.setTime(time);
				
				// 在地图页面显示,跳转到地图fragment。
				ItemListActivity.this.onItemSelected("1");				
				((ItemListFragment)ItemListActivity.this
						.getSupportFragmentManager()
						.findFragmentById(R.id.item_list))
						.setActivatedPosition(0);
			}
		}
	}
	
	


	@Override
	protected void onResume() {
		super.onResume();
		Appcontext.getInstance().setCurrentActivity(this);
	}

	@Override
	protected void onPause() {
		Appcontext.getInstance().setCurrentActivity(null);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		Appcontext.getInstance().setCurrentActivity(null);
		this.unregisterReceiver(myBroadcastRecever);
		super.onDestroy();
	}
}
