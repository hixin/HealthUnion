package com.yeran.healthhomecare.connectinternet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.yeran.healthhomecare.ItemListActivity;
import com.yeran.healthhomecare.data.Shuaidaoinfors;
import com.yeran.healthhomecare.data.oldsloctions;
import com.yeran.healthhomecare.data.peopinfor;
import com.yeran.healthhomecare.data.peoplelist;
import com.yeran.healthhomecare.tools.Appcontext;

public class UdpService extends Service implements OnGetGeoCoderResultListener {

	Udphelperclass udphelper;
	WifiManager manager;
	Thread tReceived;
	DatagramSocket datagramSocket;
	public Boolean IsThreadDisable = false;// 指示监听线程是否终止
	private WifiManager.MulticastLock lock;
	private GeoCoder mGeoCoder;
	private String weizhi, askwords = "startwork";
	private peopinfor pinfor;
	private Thread sendthread, listenthread;
	private volatile boolean flag = false;
	private String FF;
	private oldsloctions normalinfor;
	// 广播类型
	private final String BROADCAST_ACTION_Location = "com.example.broadcast.getlocations";
	private final String BROADCAST_ACTION_Shuaidao = "com.example.broadcast.getshuaidao";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		SDKInitializer.initialize(this.getApplicationContext());
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(this);
		manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		this.lock = manager.createMulticastLock("UDPwifi");
		GetIpclass gic = new GetIpclass();
		String Ip = gic.getIp();
		// udphelper.setInetaddress(Ip);
		System.out.println("ip:" + Ip);
		try {
			datagramSocket = new DatagramSocket(9001);

		} catch (SocketException e) {
			e.printStackTrace();
		}

		sendthread = new Thread(new SendThread());
		listenthread = new Thread(new Listenthread());

	//	sendthread.start();
		listenthread.start();

	}

	/*
	 * 每次调用startService，将执行onStart
	 */

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// 传递WifiManager对象，以便在UDPHelper类里面使用MulticastLock
		// udphelper.addObserver(UdpService.this);
		int retVal = super.onStartCommand(intent, flags, startId);
		Log.d("service", "onStartCommand" + intent);
		if (intent.hasExtra("askwords")) {
			this.askwords = intent.getStringExtra("askwords");
			new Thread(runnable).start();
			// send(askwords);
		}

		return retVal;
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			send(askwords);
		}
	};

	/**
	 * udp通信线程
	 * */
	class SendThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			send(askwords);
		}
	}

	class Listenthread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			StartListen();
		}

	}

	/**
	 * 通过udp向服务器发送数据
	 * */
	public void send(String message) {
		message = (message == null ? "Hello IdeasAndroid!" : message);
		// 客户端端口号
		int server_port = 12235;
		Log.d("UDP Demo", "UDP发送数据:" + message);

		InetAddress local = null;
		try {
			local = InetAddress.getByName("219.245.64.1");
			System.out.println("local" + local.toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		int msg_length = message.length();
		byte[] messageByte = message.getBytes();
		DatagramPacket p = new DatagramPacket(messageByte, msg_length, local,
				server_port);
		try {

			datagramSocket.send(p);
			System.out.println("port:" + datagramSocket.getLocalPort());
			// datagramSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (flag) {
			// 摔倒信息的存储
			pinfor = new peopinfor();
			pinfor.setDeviceid("100001");
			pinfor.setName(getApplicationContext(), pinfor.getDeviceid());
			pinfor.setLocation(34.236971, 108.823456);
			pinfor.setTime("201505031232");
			Thread getw = new Thread(new MyThread());
			getw.start();
			try {
				getw.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flag = false;
		}

	}

	/**
	 * 监听服务器端消息
	 * */
	public void StartListen() {

		// 接收的字节大小，客户端发送的数据不能超过这个大小
		byte[] message = new byte[10240];
		try {
			// 建立Socket连接
			datagramSocket = new DatagramSocket(9001);
			System.out.println("datagramSocket" + datagramSocket.getLocalPort());
			datagramSocket.setBroadcast(true);
			DatagramPacket datagramPacket = new DatagramPacket(message,
					message.length);
			try {
				while (!IsThreadDisable) {
					// 准备接收数据
					Log.d("UDP Demo", "准备接受");
					this.lock.acquire();

					datagramSocket.receive(datagramPacket);
					String strMsg = new String(datagramPacket.getData()).trim();
					Log.d("UDP Demo",strMsg);
					// 摔倒通知，进入地图界面
					// 格式：SD:ID:100001:W:26.123456:J:109.123456:T:201504131234:ED(判断轻重程度)
					if (strMsg.startsWith("SD:") && strMsg.endsWith("ED")) {

						if(!Appcontext.getInstance().isListActivity()){
						// 跳转到摔倒程序
						Intent mRunPackageIntent = new Intent(this,
								ItemListActivity.class);
						mRunPackageIntent
								.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(mRunPackageIntent);
						}
						String deid = strMsg.substring(strMsg.indexOf("ID")+3, strMsg.indexOf("W")-1);
						String weidu = strMsg.substring(strMsg.indexOf("W")+2, strMsg.indexOf("J")-1);
						String jingdu = strMsg.substring(strMsg.indexOf("J")+2, strMsg.indexOf("T")-1);
						String time = strMsg.substring(strMsg.indexOf("T")+2, strMsg.indexOf("ED")-1);

						Log.v("Msgdeid", deid+"+"+weidu);
						// 根据deviceid查询本地存储的所有个人信息？或者放在服务器端进行处理，暂时按照放在服务器端进行处理。

						pinfor = new peopinfor();
						pinfor.setDeviceid((deid));
						pinfor.setName(getApplicationContext(), deid);
						pinfor.setLocation(Double.parseDouble(weidu),
								Double.parseDouble(jingdu));
						pinfor.setTime(time);
						FF = "sd";
						Thread getw = new Thread(new MyThread());
						getw.start();
						try {
							getw.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if(strMsg.startsWith("NL:") && strMsg.endsWith("ED")){
						// 发送正常位置信息广播
						String deid = strMsg.substring(strMsg.indexOf("D")+1, strMsg.indexOf("M")-1);
						String weidu = strMsg.substring(strMsg.indexOf("M")+1, strMsg.indexOf("J")-1);
						String jingdu = strMsg.substring(strMsg.indexOf("J")+1, strMsg.indexOf("T")-1);

						FF = "normal";
						normalinfor = new oldsloctions();
						normalinfor.setDeviceid(deid);
						normalinfor.setName(
								getApplicationContext(),
								peoplelist.getInstance().getCertaindevicename(
										getApplicationContext(), deid));
						normalinfor.setLocation(Double.parseDouble(weidu),
								Double.parseDouble(jingdu));

						Thread getw = new Thread(new MyThread());
						getw.start();
						try {
							getw.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			
					}else{
						
					}

					this.lock.release();
				}
			} catch (IOException e) {// IOException
				e.printStackTrace();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public void stopTask() {
		flag = false;
	}

	/**
	 * 得到具体的位置信息
	 * */
	class MyThread implements Runnable {
		@Override
		public void run() {
			if (FF == "sd") {
				getWeizhi(pinfor.getLocation());
			} else {
				getWeizhi(normalinfor.getLocation());
			}
		}
	}

	private void getWeizhi(LatLng ll) {
		// 发起反地理编码请求
		mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		weizhi = result.getAddress();
		if (FF == "sd") {
			pinfor.setWeizhi(weizhi);
			Log.v("weizhi:", weizhi);

			//摔倒信息本地存储
			Shuaidaoinfors plist = new Shuaidaoinfors(getApplicationContext());
			plist.saveshuaidaoinfor(pinfor, getApplicationContext());

			// 发送摔倒广播
			Intent intent = new Intent();
			intent.putExtra("name", pinfor.getName());
			intent.putExtra("deviceid", pinfor.getDeviceid());
			intent.putExtra("weidu", pinfor.getLocation().latitude);
			intent.putExtra("jingdu", pinfor.getLocation().longitude);
			intent.putExtra("weizhi", pinfor.getWeizhi());
			intent.putExtra("time", pinfor.getTime());
			intent.setAction(BROADCAST_ACTION_Shuaidao);
			this.sendBroadcast(intent);

			Log.v("Sendbroadcast", "Sendbroadcast");
		}else{
			normalinfor.setWeizhi(weizhi);
			
			Intent intent = new Intent();
			intent.putExtra(
					"name",
					peoplelist.getInstance().getCertaindevicename(
							getApplicationContext(), normalinfor.getDeviceid()));
			intent.putExtra("deviceid", normalinfor.getDeviceid());
			intent.putExtra("weidu", normalinfor.getLocation().latitude);
			intent.putExtra("jingdu", normalinfor.getLocation().longitude);
			intent.putExtra("weizhi",normalinfor.getWeizhi());
			
			intent.setAction(BROADCAST_ACTION_Location);
			this.sendBroadcast(intent);
		}

	}
}
