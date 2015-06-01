package com.yeran.healthhomecare.data;

import com.baidu.mapapi.model.LatLng;

public class hospitalinfors {
	private String hosname;
	private String hosweizhi;
	private String hosphone;
	private LatLng hoslocation;

	
	public hospitalinfors(String hosname,String hosweizhi,String hosphone
			,LatLng hoslocation){
		this.hosname=hosname;
		this.hosweizhi=hosweizhi;
		this.hosphone=hosphone;
		this.hoslocation=hoslocation;

	}
	
	private void setHosname(String hosname){
		this.hosname=hosname;
	}
	private String getHosname(){
		return this.hosname;
	}

}
