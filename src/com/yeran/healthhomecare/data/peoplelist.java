package com.yeran.healthhomecare.data;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.yeran.healthhomecare.localdb.LocalDB;

/**
 * 摔打设备控制类，添加等操作
 * */
public class peoplelist {

	private static peoplelist mpeoplist = null;

	private peoplelist() {
	}

	public static peoplelist getInstance() {
		if (mpeoplist == null) {
			mpeoplist = new peoplelist();
		}
		return mpeoplist;
	}

	/**
	 * 摔倒设备增加，本地数据库保存
	 * */
	public void addDevice(ShuaidaoDeviceinfor deviceinfor, Context mcontext) {
		// shuaidaolist.add(deviceinfor);
		// 本地数据库插入
		LocalDB dbService = new LocalDB(mcontext);
		String sql = "insert into sddevices(name,deviceid,oldphone,shuaidaophone,secret,createtime) values(?,?,?,?,?,?)";
		Object[] args = new Object[] { deviceinfor.username,
				deviceinfor.deviceid, deviceinfor.oldphone,
				deviceinfor.shuaidaophone, deviceinfor.secret,
				deviceinfor.createtime };
		dbService.execSQL(sql, args);
		if (dbService != null) {
			dbService.close();
		}
	}
	
	/**
	 * 获取针对设备号的使用者名称
	 * */
	public String getCertaindevicename(Context mcontext,String deviceid){
	
		LocalDB dbService = new LocalDB(mcontext);
		Cursor cursor = dbService.query("select * from sddevices where deviceid ='"+deviceid+"'", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			int nameColumn = cursor.getColumnIndex("name");
			String name = cursor.getString(nameColumn);		
			return name;
		}
		if (cursor != null) {
			cursor.close();
		}
		if (dbService != null) {
			dbService.close();
		}
		return null;	
	}

	/**
	 * 读取本地数据库获取摔倒设备信息
	 * */
	public ArrayList<ShuaidaoDeviceinfor> getDevicelist(Context mcontext) {
		ArrayList<ShuaidaoDeviceinfor> shuaidaolist2 = new ArrayList<ShuaidaoDeviceinfor>();
		LocalDB dbService = new LocalDB(mcontext);
		Cursor cursor = dbService.query("select * from sddevices", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			int nameColumn = cursor.getColumnIndex("name");
			String name = cursor.getString(nameColumn);
			String deviceid = cursor.getString(cursor
					.getColumnIndex("deviceid"));
			String oldphone = cursor.getString(cursor
					.getColumnIndex("oldphone"));
			String shuaidaophone = cursor.getString(cursor
					.getColumnIndex("shuaidaophone"));
			String secret = cursor.getString(cursor.getColumnIndex("secret"));
			String createtime = cursor.getString(cursor
					.getColumnIndex("createtime"));
			// Arraylist增加
			shuaidaolist2.add(new ShuaidaoDeviceinfor(deviceid, name, oldphone,
					shuaidaophone, secret, createtime));
		}
		

		if (cursor != null) {
			cursor.close();
		}
		if (dbService != null) {
			dbService.close();
		}

		return shuaidaolist2;
	}

	/**
	 * 修改摔倒资料
	 * */
	public void updateDevicelist(ShuaidaoDeviceinfor newdeviceinfor,
			String username, String deviceid, Context mcontext) {
		LocalDB dbService = new LocalDB(mcontext);
		String sql = "update sddevices set name='" + newdeviceinfor.username
				+ "',deviceid='" + newdeviceinfor.deviceid + "',oldphone='"
				+ newdeviceinfor.oldphone + "',shuaidaophone='"
				+ newdeviceinfor.shuaidaophone + "',secret='"
				+ newdeviceinfor.secret + "' where name='" + username
				+ "'and deviceid='" + deviceid + "'";
		dbService.execSQL(sql);
		if (dbService != null) {
			dbService.close();
		}
	}

	/**
	 * 删除指定设备
	 * */
	public void deleteDevice(ShuaidaoDeviceinfor newdeviceinfor,
			Context mcontext) {
		LocalDB dbService = new LocalDB(mcontext);
		String sql = "delete from sddevices where name = '"
				+ newdeviceinfor.username + "' and deviceid='"
				+ newdeviceinfor.deviceid + "'";
		dbService.execSQL(sql);
		if (dbService != null) {
			dbService.close();
		}
	}
}
