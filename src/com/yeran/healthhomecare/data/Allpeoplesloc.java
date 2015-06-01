package com.yeran.healthhomecare.data;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;

public interface Allpeoplesloc {
	public void setDeviceid(String deviceid);
	public String getDeviceid();
	public void setName(Context mcontext, String deviceid);
	public String getName();
	public void setLocation(double latitude,double longitude);
	public LatLng getLocation();
	public void saveLocation();
	public void setWeizhi(String weizhi);
	public String getWeizhi();
	public void setLength(double latitude1,double longitude1,double latitude2,double longitude2);
	public double getLength();
	public void setTime(String time);
	public String getTime();
	


}
