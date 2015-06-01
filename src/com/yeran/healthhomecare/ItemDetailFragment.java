package com.yeran.healthhomecare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.health.MyApplication;
import com.example.health.R;
import com.yeran.healthhomecare.connectinternet.ConnIntentdata;
import com.yeran.healthhomecare.connectinternet.ThreadFactory;
import com.yeran.healthhomecare.data.Normaloldlocations;
import com.yeran.healthhomecare.data.Shuaidaoinfors;
import com.yeran.healthhomecare.data.oldsloctions;
import com.yeran.healthhomecare.data.peopinfor;
import com.yeran.healthhomecare.dummy.DummyContent;
import com.yeran.healthhomecare.tools.FormDataUDP;
import com.yeran.healthhomecare.tools.MyOrientationListener;
import com.yeran.healthhomecare.tools.MyOrientationListener.OnOrientationListener;
import com.yeran.healthhomecare.tools.Poihospitalsearch;
import com.yeran.healthhomecare.tools.SIMstate;
import com.yeran.healthhomecare.tools.TTSpeaker;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment implements
		OnGetGeoCoderResultListener, OnClickListener,
		OnGetRoutePlanResultListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	/**
	 * 定义控件
	 * */
	View rootView = null;

	/**
	 * 地图控件声明
	 * */
	Button followbutton;
	ImageButton showallpeople;
	RelativeLayout relaypeop;
	ListView peoples;

	/**
	 * 定义声明
	 * */
	private GeoCoder mGeoCoder = null;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MyLocationListenner mylocationListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private MapStatusUpdate u;
	private boolean isFirstLoc = true;
	private double Latitude, Longitude;
	private LatLng ll;
	// private String weizhi, city;
	private int direction = 90;
	private float mCurrentAccracy;
	MyOrientationListener myOrientationListener;
	BitmapDescriptor mCurrentMarker;
	private ArrayList<oldsloctions> peoplist;

	/**
	 * 老人信息列表
	 * */
	private List<Map<String, Object>> mData = null;
	private MyAdapter adapter;
	private String getpeoplelist = "Get";
	private oldsloctions infors;
	private LatLng[] alloldllA2;
	private OverlayOptions[] overlayop;
	private BitmapDescriptor bitmaodescriptor;
	private Marker[] mMarker;
	private int peopid;
	private int h, oldclick = 1;

	// 摔倒信息
	/**
	 * 所有摔倒信息公用的一个列表，每次放置的时候需要清空一下。
	 * */
	private ArrayList<peopinfor> shuaidaoinfors = new ArrayList<peopinfor>();
	private LatLng[] shuaidaolls;
	private Marker[] shuaidaomarkers;
	private OverlayOptions[] shuaidlayop;

	/**
	 * 医院搜索
	 * */
	Poihospitalsearch poihossearch = null;
	PoiResult poiresult = null;
	PoiDetailResult poidetail = null;
	private Button button;

	/**
	 * 路线导航
	 * */
	private Button Goto;
	private LinearLayout gotomodels;
	private TextView drive, bus, walk, refresh;

	// RouteLine route = null;
	RoutePlanSearch mRouteSearch = null; // 搜索模块，也可去掉地图模块独立使用
	int nodeIndex = -1, haverote = 0;// 节点索引,供浏览节点时使用
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	DrivingRouteOverlay overlay;
	WalkingRouteOverlay overlay2;

	/**
	 * 导航起始点和终点定义
	 * */
	private LatLng StartLL, EndLL;

	/**
	 * 广播接收
	 * */
	private MyBroadcastRecever myBroadcastRecever;
	/**
	 * tts语音
	 * */
	private TTSpeaker tts = null;
	/**
	 * 历史路径数组
	 * */
	private ArrayList<LatLng> routearray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("goin--detailed---oncreate");
		SDKInitializer.initialize(this.getActivity().getApplicationContext());
		//
		// if (getArguments().containsKey(ARG_ITEM_ID)) {
		// // Load the dummy content specified by the fragment
		// // arguments. In a real-world scenario, use a Loader
		// // to load content from a content provider.
		// mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
		// ARG_ITEM_ID));
		// }

		IntentFilter filter = new IntentFilter();
		myBroadcastRecever = new MyBroadcastRecever();
		// 设置接收广播的类型，这里要和Service里设置的类型匹配，还可以在AndroidManifest.xml文件中注册
		filter.addAction("com.example.broadcast.getlocations");
		filter.addAction("com.example.broadcast.getshuaidao");
		getActivity().registerReceiver(myBroadcastRecever, filter);

		// 获取摔倒列表
		// getnowshuaidaoinfor();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		System.out.println("goin--detailed---oncreatview");

		rootView = inflater.inflate(R.layout.map_fragment_item_detailmap,
				container, false);

		Initdata();

		Initmap();

		Sdfromhistory();

		BCfromItemList();

		return rootView;
	}

	/**
	 * 判断是否从历史记录中跳转而来的一条摔倒信息
	 * */
	private void Sdfromhistory() {
		if (HistoryFragment.item != null) {
			peopinfor peop = HistoryFragment.item;
			if (shuaidaoinfors.size() > 0) {
				shuaidaoinfors.clear();
			}
			if (Latitude == 0 || Longitude == 0) {
				peop.setLength(0, 0, 0, 0);
			} else {
				peop.setLength(Latitude, Longitude,
						peop.getLocation().latitude,
						peop.getLocation().longitude);
			}
			shuaidaoinfors.add(peop);
			initShuaioverlay();
		}
	}

	/**
	 * 判断是否有Activity接收到的广播信息而来
	 * */
	private void BCfromItemList() {
		if (ItemListActivity.peop != null) {
			oldsloctions peop = ItemListActivity.peop;
			if (Latitude == 0 || Longitude == 0) {
				peop.setLength(0, 0, 0, 0);
			} else {
				peop.setLength(Latitude, Longitude,
						peop.getLocation().latitude,
						peop.getLocation().longitude);
			}
			// 暂时列表只保留一个当前请求的位置信息。
			peoplist.clear();
			peoplist.add(peop);

			for (oldsloctions pli : peoplist) {
				Log.v(getTag(), pli.getDeviceid());
			}

			mBaiduMap.clear();
			initOldOverlay();

			LatLng llp = peop.getLocation();
			System.out.println("location:" + llp);
			setCurrentMode(LocationMode.COMPASS);
			u = MapStatusUpdateFactory.newLatLng(llp);
			mBaiduMap.setMapStatus(u);

			// 定位老人位置的时候，把老人位置设为终点
			EndLL = llp;
		}
		if (ItemListActivity.sdpeopinfor != null) {
			peopinfor sdpeopinfor = ItemListActivity.sdpeopinfor;
			if (Latitude == 0 || Longitude == 0) {
				sdpeopinfor.setLength(0, 0, 0, 0);
			} else {
				sdpeopinfor.setLength(Latitude, Longitude,
						sdpeopinfor.getLocation().latitude,
						sdpeopinfor.getLocation().longitude);
			}
			if (shuaidaoinfors.size() > 0) {
				shuaidaoinfors.clear();
			}
			shuaidaoinfors.add(sdpeopinfor);
			mBaiduMap.clear();
			initShuaioverlay();

			tts = new TTSpeaker();
			tts.speak(
					getActivity(),
					sdpeopinfor.getName() + "。设备编号为："
							+ sdpeopinfor.getDeviceid() + "。在"
							+ sdpeopinfor.getWeizhi() + "遭遇摔倒。目前距离您"
							+ sdpeopinfor.getLength() + "米");
		}
	}

	/**
	 * 获取老人所有数据,应该定时刷新
	 * */
	private void Initdata() {
		// 获取所有摔倒设备的携带者信息，通过自定义添加设备和联系人方式进行设置
		// 从本地或远程服务器进行读取
		// 加载数据
		Normaloldlocations nold = new Normaloldlocations();
		mData = nold.getData(getActivity());
		// 初始化位置列表
		peoplist = new ArrayList<oldsloctions>();

		// 下载在线设备
		// ThreadFactory threadfactory = new ThreadFactory();
		// threadfactory.setContext(getActivity());
		// ConnIntentdata tif = (ConnIntentdata) threadfactory.getThread(1);
		// tif.setparams("100001");
		// peoplist=tif.getpeoplist();

	}

	/**
	 * 对地图的初始化操作
	 * */
	private void Initmap() {
		InitControler();

		mCurrentMode = LocationMode.FOLLOWING;
		// 地图初始化
		mMapView = (MapView) rootView.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		Log.i("mBaiduMap", mBaiduMap.toString());
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this.getActivity());
		mLocClient.registerLocationListener(mylocationListener);
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式
		option.setAddrType("all");
		option.setOpenGps(true);
		// 设置坐标类型
		option.setCoorType("bd09ll");
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();

		Initorientation();

		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(ItemDetailFragment.this);

		mRouteSearch = RoutePlanSearch.newInstance();
		mRouteSearch.setOnGetRoutePlanResultListener(ItemDetailFragment.this);

		if (shuaidaoinfors.size() > 0) {
			Goto.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 控件定义
	 * */
	private void InitControler() {
		followbutton = (Button) rootView.findViewById(R.id.follow);
		followbutton.getBackground().setAlpha(150);
		showallpeople = (ImageButton) rootView.findViewById(R.id.showallpeople);
		peoples = (ListView) rootView.findViewById(R.id.peoples);

		Goto = (Button) rootView.findViewById(R.id.gotoo);
		Goto.setTag("open");
		gotomodels = (LinearLayout) rootView.findViewById(R.id.daohang);
		walk = (TextView) rootView.findViewById(R.id.walk);
		drive = (TextView) rootView.findViewById(R.id.drive);
		bus = (TextView) rootView.findViewById(R.id.bus);
		refresh = (TextView) rootView.findViewById(R.id.refresh);

		showallpeople.setOnClickListener(this);
		followbutton.setOnClickListener(this);
		Goto.setOnClickListener(this);
		walk.setOnClickListener(this);
		bus.setOnClickListener(this);
		drive.setOnClickListener(this);
		refresh.setOnClickListener(this);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(direction).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			Latitude = location.getLatitude();
			Longitude = location.getLongitude();
			mCurrentAccracy = location.getRadius();
			ll = new LatLng(Latitude, Longitude);
			// weizhi = location.getAddrStr();
			// city = location.getCity();
			if (isFirstLoc) {
				isFirstLoc = false;

				if (shuaidaoinfors.size() > 0) {
					double la1 = shuaidaoinfors.get(shuaidaoinfors.size() - 1)
							.getLocation().latitude;
					double long2 = shuaidaoinfors
							.get(shuaidaoinfors.size() - 1).getLocation().longitude;
					shuaidaoinfors.get(shuaidaoinfors.size() - 1).setLength(
							Latitude, Longitude, la1, long2);
				} else {
					u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.setMapStatus(u);
					mBaiduMap.setMapStatus(MapStatusUpdateFactory
							.newMapStatus(new MapStatus.Builder().zoom(15)
									.build()));
				}
			}
			// 默认将本设备的位置作为导航起点
			StartLL = ll;
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 方向传感器的调用
	 * */
	private void Initorientation() {
		myOrientationListener = new MyOrientationListener(this.getActivity()
				.getApplicationContext());
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						direction = (int) x;
						System.out.println("direction" + direction);

						// 构造定位数据
						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mCurrentAccracy)
								// 此处设置开发者获取到的方向信息，顺时针0-360
								.direction(direction).latitude(Latitude)
								.longitude(Longitude).build();
						// 设置定位数据
						mBaiduMap.setMyLocationData(locData);
					}
				});
	}

	/**
	 * 点击事件定义
	 * 
	 * */
	@Override
	public void onClick(View clickitem) {
		// TODO Auto-generated method stub
		switch (clickitem.getId()) {
		// 展开设备、用户列表
		case R.id.showallpeople:
			if (getpeoplelist.equals("Get")) {

				adapter = new MyAdapter(this.getActivity());
				peoples.setAdapter(adapter);
				// 定义动画展开
				peoples.setVisibility(View.VISIBLE);
				Animation myAnimation = AnimationUtils.loadAnimation(
						this.getActivity(), R.anim.showpeop);
				peoples.startAnimation(myAnimation);

				// initOldOverlay();

				getpeoplelist = "close";
			} else {
				disappear(peoples);
			}

			break;

		case R.id.follow:
			setCurrentMode(mCurrentMode);
			break;
		case R.id.gotoo:
			if (Goto.getTag().equals("open")) {
				gotomodels.setVisibility(View.VISIBLE);
				Goto.setTag("close");
			} else {
				gotomodels.setVisibility(View.GONE);
				Goto.setTag("open");
			}
			break;
		case R.id.walk:
			RoutePlan("Walking", StartLL, EndLL);
			break;
		case R.id.bus:
			RoutePlan("Busing", StartLL, EndLL);
			break;
		case R.id.drive:
			RoutePlan("Driving", StartLL, EndLL);
			break;
		case R.id.refresh:
			// 刷新页面
			Refresh();
			break;

		}
	}

	/**
	 * 刷新页面
	 * */
	private void Refresh() {
		mBaiduMap.clear();
		gotomodels.setVisibility(View.GONE);
		Goto.setTag("open");
		initShuaioverlay();
	}

	/**
	 * 设置消失动画
	 * */
	private void disappear(final View view1) {
		Animation myAnimation = new ScaleAnimation(1f, 0f, 1f, 1f,
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
		myAnimation.setDuration(400);
		view1.startAnimation(myAnimation);

		myAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				System.out.println("endanimation");
				view1.clearAnimation();
				view1.setVisibility(View.GONE);
				getpeoplelist = "Get";
			}
		});
	}

	/**
	 * 设置当前定位模式
	 * */
	private void setCurrentMode(LocationMode mCurrentMode) {
		if (mCurrentMode == LocationMode.NORMAL) {
			this.mCurrentMode = LocationMode.FOLLOWING;
			mBaiduMap.setMapStatus(MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().zoom(15).build()));
			followbutton.setText("跟");
			mBaiduMap.clear();
		} else if (mCurrentMode == LocationMode.COMPASS) {
			this.mCurrentMode = LocationMode.NORMAL;
			followbutton.setText("普");
		} else {
			this.mCurrentMode = LocationMode.COMPASS;
			followbutton.setText("罗");
		}
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				this.mCurrentMode, true, mCurrentMarker));
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 老人列表适配器
	 * */
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int item) {
			// TODO Auto-generated method stub
			return item;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.map_peoplelist, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.Name);
				holder.info = (TextView) convertView.findViewById(R.id.info);

				holder.golocation = (ImageView) convertView
						.findViewById(R.id.location);
				holder.callpeople = (ImageView) convertView
						.findViewById(R.id.callphone);
				holder.searchlastsd = (ImageView) convertView
						.findViewById(R.id.lastsd);
				// 设置标记位置的值
				holder.golocation.setTag(position);
				holder.callpeople.setTag(position);
				holder.searchlastsd.setTag(position);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource((Integer) mData.get(position).get(
					"img"));
			holder.title.setText((String) mData.get(position).get("NAME"));
			holder.info.setText((String) mData.get(position).get("state"));
			infors = (oldsloctions) mData.get(position).get("infors");

			holder.golocation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					// 向服务器发送位置请求，通过udp协议发送，固定帧格式。在收到具体的位置信息后，在地图上显示，
					// 并且在本地数据库中进行存储本次的位置信息。本地数据库，在设备注册的时候建立
					// 一个新的位置表，针对每个设备，有1200条临时存储。在1200满了的时候进行覆盖。主要用于用户对近20小时的
					// 历史路线的查询。

					// 点击时候，直接接收位置显示。不从本地数据表读值。
					final int tagposition = (Integer) view.getTag();
					oldsloctions infors = (oldsloctions) mData.get(tagposition)
							.get("infors");
					askfornowlocation(infors.getDeviceid());
			//		Askforhisroute(infors.getDeviceid());
				}
			});
			holder.callpeople.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					int simstate = (int) SIMstate.getInstance().getSIMstate(
							getActivity());
					if (simstate == 1) {
						// 状态完好
						final int tagposition = (Integer) view.getTag();
						oldsloctions infors = (oldsloctions) mData.get(
								tagposition).get("infors");
						final String phone = infors.getPhone();

						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setIcon(R.drawable.ic_launcher);
						builder.setTitle("您好");
						builder.setMessage("是否拨打电话:" + phone);
						builder.setPositiveButton("拨打",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 带扩展，检测是否支持SIM卡和打电话
										Uri uri = Uri.parse("tel:" + phone);
										Intent intent = new Intent(
												Intent.ACTION_CALL, uri);
										startActivity(intent);
									}
								});
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						builder.show();
					} else if (simstate == 0) {
						Toast.makeText(getActivity(), "您的设备没有SIM卡！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "您的SIM卡状态不正常！",
								Toast.LENGTH_SHORT).show();
					}
				}
			});

			// 定位到本人最近一次摔倒的位置
			holder.searchlastsd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					final int tagposition = (Integer) view.getTag();
					oldsloctions infors = (oldsloctions) mData.get(tagposition)
							.get("infors");
					getcertainlastsd(infors.getDeviceid());

				}
			});

			return convertView;
		}

		public final class ViewHolder {
			public ImageView img;
			public TextView title;
			public TextView info;
			public ImageView golocation;
			private ImageView searchlastsd, callpeople;
		}
	}

	/**
	 * 自定义搜索出医院的贴标
	 * */
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// System.out.println("poi@@=" + poi.name + poi.uid);
			poidetail = poihossearch.getpoidetail(poi.uid);
			poidetail = poihossearch.getpoidetail(poi.uid);
			if (poidetail == null) {
				Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
			} else {

				// 点击了医院，将医院设置为导航终点
				EndLL = poi.location;
				u = MapStatusUpdateFactory.newLatLng(EndLL);
				mBaiduMap.setMapStatus(u);

				Toast.makeText(getActivity(), poi.name + ": " + poi.address,
						Toast.LENGTH_SHORT).show();

				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setMessage("是否前往医院的详细页面");
				builder.setTitle("医院：" + poi.name);
				builder.setPositiveButton("前往",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								Gotowebview(poidetail);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder.show();

			}
			return true;
		}
	}

	/**
	 * 导航至webview
	 * */
	private void Gotowebview(PoiDetailResult poidetail) {

		Uri uri = Uri.parse(poidetail.getDetailUrl());
		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		Intent intent = new Intent(getActivity(), Webview.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("HospUrl", uri.toString());
		startActivity(intent);
	}

	/**
	 * 定义老人的现在位置贴图
	 * */
	private void initOldOverlay() {
		// 搜索附近医院---POI搜索
		poihossearch = Poihospitalsearch.getInstance();

		int numb = adapter.getCount();
		alloldllA2 = new LatLng[numb];
		mMarker = new Marker[numb];
		overlayop = new OverlayOptions[numb];
		bitmaodescriptor = BitmapDescriptorFactory
				.fromResource(R.drawable.poi_1);

		peopid = 0;
		for (oldsloctions peop : peoplist) {
			overlayop[peopid] = new MarkerOptions()
					.position(peop.getLocation()).icon(bitmaodescriptor)
					.zIndex(9).draggable(true);
			mMarker[peopid] = (Marker) (mBaiduMap.addOverlay(overlayop[peopid]));

			if (peopid == 0) {
				poiresult = poihossearch.getpoiresult("医院", 0, 10,
						peop.getLocation());

			}
			peopid++;
		}
		if (peopid > 0) {
			Goto.setVisibility(View.VISIBLE);
		}
		// 点击贴图出老人信息效果
		Initmarketclick();
	}

	/**
	 * 定义摔倒位置贴图
	 * */
	private void initShuaioverlay() {
		poihossearch = Poihospitalsearch.getInstance();

		int sdnumb = shuaidaoinfors.size();
		System.out.println("shuaidao:" + sdnumb);
		shuaidaolls = new LatLng[sdnumb];
		shuaidaomarkers = new Marker[sdnumb];
		shuaidlayop = new OverlayOptions[sdnumb];
		BitmapDescriptor bitmaodescriptor = BitmapDescriptorFactory
				.fromResource(R.drawable.sdlocation);
		int peopid2 = 0;
		for (peopinfor peop : shuaidaoinfors) {
			shuaidlayop[peopid2] = new MarkerOptions()
					.position(peop.getLocation()).icon(bitmaodescriptor)
					.zIndex(9).draggable(true);
			shuaidaomarkers[peopid2] = (Marker) (mBaiduMap
					.addOverlay(shuaidlayop[peopid2]));

			if (peopid2 == 0) {
				poiresult = poihossearch.getpoiresult("医院", 0, 10,
						peop.getLocation());

			}
			peopid2++;
		}
		if (bitmaodescriptor != null) {
			bitmaodescriptor = null;
		}
		// 定位到最近一次的摔倒位置
		setCurrentMode(LocationMode.COMPASS);
		u = MapStatusUpdateFactory.newLatLng(shuaidaoinfors.get(peopid2 - 1)
				.getLocation());
		mBaiduMap.setMapStatus(u);

		Goto.setVisibility(View.VISIBLE);
		peopinfor sdi = shuaidaoinfors.get(peopid2 - 1);
		EndLL = sdi.getLocation();
		Initshuaidaomarketclick();

		// 将历史记录选中的摔倒记录设置为空。
		HistoryFragment.item = null;
		ItemListActivity.peop = null;
		ItemListActivity.sdpeopinfor = null;

	}

	/**
	 * 点击贴图出老人信息效果
	 * */
	void Initmarketclick() {
		OnMarkerClickListener onClickListener = new OnMarkerClickListener() {
			// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				if (oldclick == 1) {

					button = new Button(getActivity().getApplicationContext());
					button.setBackgroundResource(R.drawable.popup);
					final LatLng ll = marker.getPosition();
					Point p = mBaiduMap.getProjection().toScreenLocation(ll);
					p.y -= 47;

					LatLng llInfo = mBaiduMap.getProjection()
							.fromScreenLocation(p);
					OnInfoWindowClickListener listener = null;
					for (int i = 0; i < (mMarker.length); i++) {
						if (marker == mMarker[i])
							h = i;
					}
					// 点击之后聚焦到这个点上
					setCurrentMode(LocationMode.COMPASS);
					u = MapStatusUpdateFactory.newLatLng(peoplist.get(h)
							.getLocation());
					mBaiduMap.setMapStatus(u);

					button.setText(peoplist.get(h).getName() + "\r\n" + "老人位置:"
							+ peoplist.get(h).getWeizhi());
					button.setTextColor(Color.BLACK);
					System.out.println("name" + peoplist.get(h).getName());

					{
						listener = new OnInfoWindowClickListener() {
							public void onInfoWindowClick() {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setIcon(R.drawable.ic_launcher);
								builder.setTitle(peoplist.get(h).getName()
										+ "\n" + " 目前距离本机："
										+ (peoplist.get(h).getLength()) / 1000
										+ "Km");
								builder.setMessage("我的状态： "
										+ peoplist.get(h).heathstate);
								builder.setPositiveButton("行踪路线",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// 历史路线的查询

												Askforhisroute(peoplist.get(h)
														.getDeviceid());

											}
										});
								builder.setNegativeButton("附近医院",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// 搜索附近医院
												Searchhospital(peoplist.get(h)
														.getLocation());
											}
										});
								builder.show();
								mBaiduMap.hideInfoWindow();
								oldclick = 1;
							}
						};
					}
					InfoWindow mInfoWindow = new InfoWindow(button, llInfo,
							listener);
					mBaiduMap.showInfoWindow(mInfoWindow);

					EndLL = peoplist.get(h).getLocation();

					oldclick = 0;
				} else {
					mBaiduMap.hideInfoWindow();
					oldclick = 1;

				}
				return true;
			}
		};
		mBaiduMap.setOnMarkerClickListener(onClickListener);

	}

	/**
	 * 点击贴图摔倒位置
	 * */
	void Initshuaidaomarketclick() {
		OnMarkerClickListener onClickListener = new OnMarkerClickListener() {
			// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				if (oldclick == 1) {

					button = new Button(getActivity().getApplicationContext());
					button.setBackgroundResource(R.drawable.popup);
					final LatLng ll = marker.getPosition();
					Point p = mBaiduMap.getProjection().toScreenLocation(ll);
					p.y -= 47;

					LatLng llInfo = mBaiduMap.getProjection()
							.fromScreenLocation(p);
					OnInfoWindowClickListener listener = null;
					for (int i = 0; i < (shuaidaomarkers.length); i++) {
						if (marker == shuaidaomarkers[i])
							h = i;
					}
					// 点击之后聚焦到这个点上
					setCurrentMode(LocationMode.COMPASS);
					u = MapStatusUpdateFactory.newLatLng(shuaidaoinfors.get(h)
							.getLocation());
					mBaiduMap.setMapStatus(u);

					button.setText(shuaidaoinfors.get(h).getName() + "\r\n"
							+ "老人摔倒位置:" + shuaidaoinfors.get(h).getWeizhi());
					button.setTextColor(Color.BLACK);
					System.out
							.println("name" + shuaidaoinfors.get(h).getName());

					{
						listener = new OnInfoWindowClickListener() {
							public void onInfoWindowClick() {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setIcon(R.drawable.ic_launcher);
								builder.setTitle(shuaidaoinfors.get(h)
										.getName()
										+ "\n"
										+ "摔倒位置距离本机："
										+ (shuaidaoinfors.get(h).getLength())
										/ 1000 + "Km");
								builder.setMessage("我的状态：" + "摔倒！");
								builder.setPositiveButton("行踪路线",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// 历史路线的查询
												Toast.makeText(getActivity(),
														"暂未开放~",
														Toast.LENGTH_SHORT)
														.show();

											}
										});
								builder.setNegativeButton("附近医院",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// 搜索医院
												LatLng llp = shuaidaoinfors
														.get(h).getLocation();
												// poihossearch.Init();
												poiresult = poihossearch
														.getpoiresult("医院", 0,
																10, llp);

												if (poiresult != null) {
													disappear(peoples);

													PoiOverlay overlay = new MyPoiOverlay(
															mBaiduMap);
													mBaiduMap
															.setOnMarkerClickListener(overlay);
													overlay.setData(poiresult);
													overlay.addToMap();
													overlay.zoomToSpan();
												}

											}
										});

								builder.show();
								mBaiduMap.hideInfoWindow();
								oldclick = 1;
							}
						};
					}
					InfoWindow mInfoWindow = new InfoWindow(button, llInfo,
							listener);
					mBaiduMap.showInfoWindow(mInfoWindow);

					EndLL = shuaidaoinfors.get(h).getLocation();

					oldclick = 0;
				} else {
					mBaiduMap.hideInfoWindow();
					oldclick = 1;

				}
				return true;
			}
		};
		mBaiduMap.setOnMarkerClickListener(onClickListener);

	}

	@Override
	public void onStart() {
		super.onStart();
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this.getActivity());
		mLocClient.registerLocationListener(mylocationListener);
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式
		option.setAddrType("all");
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		myOrientationListener.start();
		if (!mLocClient.isStarted()) {
			mLocClient.start();
		}
		Log.v(getTag(), "onstart");
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		Log.v(getTag(), "onresume");
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
		if (tts != null)
			tts.destory();
		Log.v(getTag(), "onpause");
	}

	@Override
	public void onStop() {
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();
		myOrientationListener.stop();
		if (tts != null)
			tts.destory();
		super.onStop();
		Log.v(getTag(), "onstop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		myOrientationListener.stop();
		mLocClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mBaiduMap.clear();
		mMapView.onDestroy();
		mMapView = null;
		if (tts != null)
			tts.destory();
		getActivity().unregisterReceiver(myBroadcastRecever);
		Log.v(getTag(), "ondestory");
	}

	/**
	 * 路径规划以及规划清除
	 * 
	 * */
	private void RoutePlan(String mode, LatLng from, LatLng to) {
		PlanNode stNode = PlanNode.withLocation(from);
		PlanNode enNode = PlanNode.withLocation(to);
		if (mode.equals("Walking")) {
			mRouteSearch.walkingSearch((new WalkingRoutePlanOption()).from(
					stNode).to(enNode));
		} else if (mode.equals("Driving")) {
			mRouteSearch.drivingSearch((new DrivingRoutePlanOption()).from(
					stNode).to(enNode));
		} else if (mode.equals("Busing")) {
			mRouteSearch.transitSearch((new TransitRoutePlanOption()).from(
					stNode).to(enNode));
		} else {
			if (routeOverlay != null) {
				routeOverlay.removeFromMap();
			}
		}
		gotomodels.setVisibility(View.GONE);
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult routeresult) {
		// TODO Auto-generated method stub
		if (routeresult == null
				|| routeresult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到驾驶路线！", Toast.LENGTH_SHORT)
					.show();
		}
		if (routeresult.error == ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (routeresult.error == ERRORNO.NO_ERROR) {
			mBaiduMap.clear();

			nodeIndex = -1;
			// route = routeresult.getRouteLines().get(0);
			routeOverlay = new DrivingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(routeOverlay);
			((DrivingRouteOverlay) routeOverlay).setData(routeresult
					.getRouteLines().get(0));
			routeOverlay.addToMap();
			routeOverlay.zoomToSpan();
			// Initmarketclick();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult routeresult) {
		// TODO Auto-generated method stub
		if (routeresult == null
				|| routeresult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到公交路线！", Toast.LENGTH_SHORT)
					.show();
		}
		if (routeresult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (routeresult.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			nodeIndex = -1;
			// route = routeresult.getRouteLines().get(0);
			routeOverlay = new TransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(routeOverlay);
			((TransitRouteOverlay) routeOverlay).setData(routeresult
					.getRouteLines().get(0));
			routeOverlay.addToMap();
			routeOverlay.zoomToSpan();
			// Initmarketclick();
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult routeresult) {
		// TODO Auto-generated method stub
		if (routeresult == null
				|| routeresult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到步行路线！", Toast.LENGTH_SHORT)
					.show();
		}
		if (routeresult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (routeresult.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			nodeIndex = -1;
			// route = routeresult.getRouteLines().get(0);
			routeOverlay = new WalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(routeOverlay);
			((WalkingRouteOverlay) routeOverlay).setData(routeresult
					.getRouteLines().get(0));
			routeOverlay.addToMap();
			routeOverlay.zoomToSpan();
			// Initmarketclick();
		}
	}

	/**
	 * 获取摔倒列表,此方法已经失效
	 * */
	private void getnowshuaidaoinfor() {
		// 获取摔倒列表，如果有列表，则显示出摔倒位置，如果没有则不采取行动
		Shuaidaoinfors pelist = new Shuaidaoinfors(getActivity());
		shuaidaoinfors = pelist.getallshuaidaoinfors();
		System.out.println("size==" + shuaidaoinfors.size());
	}

	/**
	 * 与服务器udp通信，获取选中的设备的最近一次的位置信息。直接发送，接收通过广播机制来实现贴图等效果。 调用service进行发送数据
	 * */
	private void askfornowlocation(String deviceid) {
		Intent intent = new Intent("UdpService");
		// 请求位置的数据格式
		String askwords = new FormDataUDP().getAskwords("Askforlocation",
				deviceid);
		intent.putExtra("askwords", askwords);
		getActivity().startService(intent);
	}

	/**
	 * 从服务器获取近一天的行踪路线
	 * */
	private void Askforhisroute(String deviceid) {
		ThreadFactory tf = new ThreadFactory();
		tf.setContext(getActivity());
		ConnIntentdata cd = (ConnIntentdata) tf.getThread(1);
		cd.setparams(new String[] { MyApplication.homeid, deviceid });
		if (cd.startthread()) {
			Toast.makeText(getActivity(), "正在规划近24小时的历史行踪轨迹~",
					Toast.LENGTH_SHORT).show();

			String json = cd.getmessage().substring(7);
			Log.v(getTag(), json);
			routearray = new ArrayList<LatLng>();
			Log.v(getTag(), json);

			try {
				// json数据解析
				JSONArray jsarray = new JSONArray(json);
				for (int i = 0; i < jsarray.length(); i++) {
					JSONObject temp = (JSONObject) jsarray.get(i);
					routearray.add(new LatLng(temp.getDouble("weidu"), temp
							.getDouble("jingdu")));
					Log.v(getTag(), temp.getString("id"));

				}
				// 画线
				PolylineOptions pol = new PolylineOptions().points(routearray)
						.color(Color.RED).width(2);
				mBaiduMap.addOverlay(pol);
				// 在最后一个位置添加一个贴图
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.rounded_green);
				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions().position(
						routearray.get(jsarray.length() - 1)).icon(bitmap);
				// 在地图上添加Marker，并显示
				mBaiduMap.addOverlay(option);
				bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.login_bg);
				option = new MarkerOptions().position(
						routearray.get(0)).icon(bitmap);
				mBaiduMap.addOverlay(option);
				bitmap = null;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获取指定设备最近一次的摔倒位置，从本地数据库读取
	 * */
	private void getcertainlastsd(String deviceid) {
		Shuaidaoinfors sdinfor = new Shuaidaoinfors();
		peopinfor lastsdinfor = sdinfor.getlastsd(deviceid, getActivity());

		if (lastsdinfor != null) {
			if (shuaidaoinfors.size() > 0) {
				shuaidaoinfors.clear();
			}
			lastsdinfor.setLength(Latitude, Longitude,
					lastsdinfor.getLocation().latitude,
					lastsdinfor.getLocation().longitude);
			shuaidaoinfors.add(lastsdinfor);
			initShuaioverlay();
		} else {
			Toast.makeText(getActivity(), "没有摔倒记录哦~", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * 搜索附近医院
	 * */
	private void Searchhospital(LatLng llp) {
		poiresult = poihossearch.getpoiresult("医院", 0, 10, llp);

		if (poiresult != null) {
			disappear(peoples);

			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(poiresult);
			overlay.addToMap();
			overlay.zoomToSpan();

			setCurrentMode(LocationMode.COMPASS);
			u = MapStatusUpdateFactory.newLatLng(llp);
			mBaiduMap.setMapStatus(u);

		} else {
			Toast.makeText(getActivity(), "附近5千米没有医院！", Toast.LENGTH_SHORT)
					.show();
		}
	}

	class MyBroadcastRecever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODOAuto-generated
			// method stub
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
				oldsloctions peop = new oldsloctions();
				peop.setName2(name);
				peop.setDeviceid(deviceid);
				peop.setLocation(latitude, longitude);
				peop.setWeizhi(weizhi);
				peop.setLength(Latitude, Longitude, latitude, longitude);
				// 暂时列表只保留一个当前请求的位置信息。
				peoplist.clear();
				peoplist.add(peop);

				for (oldsloctions pli : peoplist) {
					Log.v(getTag(), pli.getDeviceid());
				}

				mBaiduMap.clear();
				initOldOverlay();

				LatLng llp = peop.getLocation();
				System.out.println("location:" + llp);
				setCurrentMode(LocationMode.COMPASS);
				u = MapStatusUpdateFactory.newLatLng(llp);
				mBaiduMap.setMapStatus(u);

				// 定位老人位置的时候，把老人位置设为终点
				EndLL = llp;
			} else if (intent.getAction().equals(
					"com.example.broadcast.getshuaidao")) {
				Log.v(getTag(), "接收到摔倒广播");
				String name = intent.getStringExtra("name");
				String deviceid = intent.getStringExtra("deviceid");
				double latitude = intent.getDoubleExtra("weidu", 0);
				double longitude = intent.getDoubleExtra("jingdu", 0);
				String weizhi = intent.getStringExtra("weizhi");
				String time = intent.getStringExtra("time");

				peopinfor sdpeopinfor = new peopinfor();
				sdpeopinfor.setName2(name);
				sdpeopinfor.setDeviceid(deviceid);
				sdpeopinfor.setLocation(latitude, longitude);
				sdpeopinfor.setWeizhi(weizhi);
				sdpeopinfor.setTime(time);
				if (Latitude == 0 || Longitude == 0) {
					sdpeopinfor.setLength(0, 0, 0, 0);
				} else {
					sdpeopinfor.setLength(Latitude, Longitude, latitude,
							longitude);
				}
				if (shuaidaoinfors.size() > 0) {
					shuaidaoinfors.clear();
				}
				shuaidaoinfors.add(sdpeopinfor);
				initShuaioverlay();
				// TTS语音发声
				tts = new TTSpeaker();
				tts.speak(
						getActivity(),
						sdpeopinfor.getName() + "。设备编号为："
								+ sdpeopinfor.getDeviceid() + "。在"
								+ sdpeopinfor.getWeizhi() + "遭遇摔倒。目前距离您"
								+ sdpeopinfor.getLength() + "米");

			}
		}
	}

}
