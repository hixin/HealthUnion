package com.yeran.healthhomecare.data;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.yeran.healthhomecare.tools.LLtolength;

/**
 * 用户摔倒信息类
 * */
public class peopinfor implements Allpeoplesloc {
	private String deviceid;
	private String name;
	public String phone;
	private double latitude;
	private double longitude;
	private LatLng ll;
	private String weizhi = null;
	public String healthstate = "健康";
	private double length;
	private String time;

	public void setName2(String name) {
		this.name = name;
	}

	@Override
	public void setName(Context mcontext, String deviceid) {
		// TODO Auto-generated method stub
		// 查找匹配的姓名
		this.name = peoplelist.getInstance().getCertaindevicename(mcontext,
				deviceid);
	}

	@Override
	public void setLocation(double latitude, double longitude) {
		// TODO Auto-generated method stub
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public LatLng getLocation() {
		// TODO Auto-generated method stub
		ll = new LatLng(latitude, longitude);
		return ll;
	}

	@Override
	public void saveLocation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWeizhi(String weizhi) {
		// TODO Auto-generated method stub
		this.weizhi = weizhi;
	}

	@Override
	public String getWeizhi() {
		// TODO Auto-generated method stub
		return this.weizhi;
	}

	@Override
	public void setLength(double latitude1, double longitude1,
			double latitude2, double longitude2) {
		// TODO Auto-generated method stub

		this.length = new LLtolength().getLength(latitude1, longitude1,
				latitude2, longitude2);
	}

	@Override
	public double getLength() {
		// TODO Auto-generated method stub
		return this.length;
	}

	@Override
	public void setDeviceid(String deviceid) {
		// TODO Auto-generated method stub
		this.deviceid = deviceid;
	}

	@Override
	public String getDeviceid() {
		// TODO Auto-generated method stub
		return this.deviceid;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub
		this.time = time;
	}

	@Override
	public String getTime() {
		// TODO Auto-generated method stub
		return this.time;
	}

}
