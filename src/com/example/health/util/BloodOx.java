package com.example.health.util;

public class BloodOx {
	 private String userid;  
	 private String time;
	 private int ox;  
	 private int pulse;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getOx() {
		return ox;
	}
	public void setOx(int ox) {
		this.ox = ox;
	}
	public int getPulse() {
		return pulse;
	}
	public void setPulse(int pulse) {
		this.pulse = pulse;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\'"+userid+"\',\'"+time+"\',\'"+ox+"\',\'"+pulse+"\'";
	}
	 
}
