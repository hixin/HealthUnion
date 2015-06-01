package com.yeran.healthhomecare.data;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.yeran.healthhomecare.tools.LLtolength;

/**
 * 正常模式下的老人位置信息类
 * */
public class oldsloctions implements Allpeoplesloc {

	private double latitude, longitude;
	private LatLng ll;
	private String weizhi, name, time;
	private double length;
	private String deviceid, phone;
	public String heathstate = "健康";

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

	// 进行本地保存操作
	@Override
	public void saveLocation() {
		// TODO Auto-generated method stub

	}



	@Override
	public String getWeizhi() {
		// TODO Auto-generated method stub
		return weizhi;
	}
	public void setName2(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public void setName(Context mcontext,String deviceid) {
		// TODO Auto-generated method stub
		this.name = peoplelist.getInstance().getCertaindevicename(mcontext,
				deviceid);;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public void setLength(double latitude1, double longitude1,
			double latitude2, double longitude2) {
		// TODO Auto-generated method stub
		LLtolength getlen = new LLtolength();
		this.length = getlen.getLength(latitude1, longitude1, latitude2,
				longitude2);
	}

	@Override
	public double getLength() {
		// TODO Auto-generated method stub
		return length;
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
	public void setTime(String time) {
		// TODO Auto-generated method stub
		this.time = time;
	}

	@Override
	public String getTime() {
		// TODO Auto-generated method stub
		return this.time;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone(){
		return this.phone;
	}

	@Override
	public void setWeizhi(String weizhi) {
		// TODO Auto-generated method stub
		this.weizhi=weizhi;
	}
}
