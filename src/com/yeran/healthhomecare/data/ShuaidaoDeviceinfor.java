package com.yeran.healthhomecare.data;

/**
 * 摔倒信息类
 * */
public class ShuaidaoDeviceinfor {
	public String deviceid;
	public String username;
	public String oldphone;
	public String shuaidaophone;
	public String secret;
	public String createtime;


	public ShuaidaoDeviceinfor(String deviceid, String username,
			String oldphone, String shuaidaophone, String secret,String createtime) {
		this.deviceid = deviceid;
		this.username = username;
		this.oldphone = oldphone;
		this.shuaidaophone = shuaidaophone;
		this.secret = secret;
		this.createtime=createtime;
	}
	
	
	

}
