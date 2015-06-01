package com.example.health.util;

import android.widget.ImageView;

import java.io.Serializable;

public class User implements Serializable{
	private String shenfennum;
	private String name;
	private String regtime;
	private String address;
	private String birthdate;
	
	public User(){
		
	}
	
	
	public String getShenfennum() {
		return shenfennum;
	}

	public void setShenfennum(String shenfennum) {
		this.shenfennum = shenfennum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "User [shenfennum=" + shenfennum + ", name=" + name + ", regtime=" + regtime + ", address=" + address + ", birthdate=" + birthdate + "]";
	}
}








/*public class User {
	
	private boolean isOnLine;
	private String phoneNo;
	private String passward;
	private String userName;
	private ImageView userPhoto;
	private String sex;
	public User(){
	
	}
	
	public User(boolean isOnLine, String phoneNo, String passward) {

		this.isOnLine = isOnLine;
		this.phoneNo = phoneNo;
		this.passward = passward;
	}

	public boolean isOnLine() {
		return isOnLine;
	}

	public void setOnLine(boolean isOnLine) {
		this.isOnLine = isOnLine;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ImageView getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(ImageView userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPassward() {
		return passward;
	}

	public void setPassward(String passward) {
		this.passward = passward;
	}
	
}*/
