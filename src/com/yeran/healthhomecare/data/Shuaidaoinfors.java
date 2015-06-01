package com.yeran.healthhomecare.data;

import java.util.ArrayList;

import com.yeran.healthhomecare.localdb.LocalDB;

import android.content.Context;
import android.database.Cursor;

public class Shuaidaoinfors {

	private static ArrayList<peopinfor> shuaidaopeopinfors = new ArrayList<peopinfor>();

	public Shuaidaoinfors(Context mcontext) {

	}

	public Shuaidaoinfors() {
	}

	/**
	 * 增加摔倒信息
	 * */
	public void addshuaidaoinfor(peopinfor onepeopinfor) {
		shuaidaopeopinfors.add(onepeopinfor);
	}

	/**
	 * 获取所有人的摔倒位置信息
	 * */
	public ArrayList<peopinfor> getallshuaidaoinfors() {
		return shuaidaopeopinfors;
	}

	/**
	 * 获取指定摔倒信息
	 * */
	public peopinfor getonesdaoinfor(String deviceid) {
		peopinfor infor = null;
		for (peopinfor peinfor : shuaidaopeopinfors) {
			if (peinfor.getDeviceid() == deviceid) {
				infor = peinfor;
				break;
			}
		}
		return infor;
	}

	/**
	 * 获取最近的一次的摔倒记录
	 * */
	public peopinfor getlastshuaidaoinfor() {
		peopinfor infor = shuaidaopeopinfors.get(shuaidaopeopinfors.size() - 1);
		return infor;
	}

	/**
	 * 摔倒信息的本地存储
	 * */
	public void saveshuaidaoinfor(peopinfor onepeopinfor, Context mcontext) {
		LocalDB dbService = new LocalDB(mcontext);

		String sql = "insert into sdinfors(name,deviceid,weidu,jingdu,weizhi,time) values(?,?,?,?,?,?)";
		Object[] args = new Object[] { onepeopinfor.getName(),
				onepeopinfor.getDeviceid(),
				onepeopinfor.getLocation().latitude,
				onepeopinfor.getLocation().longitude, onepeopinfor.getWeizhi(),
				onepeopinfor.getTime() };
		dbService.execSQL(sql, args);

		if (dbService != null) {
			dbService.close();
		}

		shuaidaopeopinfors.add(onepeopinfor);
	}

	/**
	 * 通过读取本地数据库获取相应的摔倒记录
	 * */
	public ArrayList<peopinfor> getAllshuaidaoinfor(String deviceid,
			Context mcontext) {
		ArrayList<peopinfor> allsdinfors = new ArrayList<peopinfor>();
		peopinfor oneinfor = new peopinfor();

		String sql = null;
		// 全部查询
		if (deviceid == "000000") {
			sql = "select * from sdinfors";
		} else {
			sql = "select * from sdinfors where deviceid='" + deviceid + "'";
		}
		LocalDB dbService = new LocalDB(mcontext);

		Cursor cursor = dbService.query(sql, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			int nameColumn = cursor.getColumnIndex("name");
			oneinfor.setName2(cursor.getString(nameColumn));
			oneinfor.setDeviceid(cursor.getString(cursor
					.getColumnIndex("deviceid")));
			oneinfor.setLocation(
					cursor.getDouble(cursor.getColumnIndex("weidu")),
					cursor.getDouble(cursor.getColumnIndex("jingdu")));
			oneinfor.setWeizhi(cursor.getString(cursor.getColumnIndex("weizhi")));
			oneinfor.setTime(cursor.getString(cursor.getColumnIndex("time")));

			allsdinfors.add(oneinfor);
		}

		if (cursor != null) {
			cursor.close();
		}
		if (dbService != null) {
			dbService.close();
		}
		return allsdinfors;
	}

	/**
	 * 获取指定设备号的最近一次摔倒历史
	 * */
	public peopinfor getlastsd(String deviceid, Context mcontext) {

		peopinfor oneinfor = new peopinfor();

		String sql = null;
		// 全部查询
		if (deviceid != null) {
			sql = "select * from sdinfors where deviceid='" + deviceid + "'";
			LocalDB dbService = new LocalDB(mcontext);
			Cursor cursor = dbService.query(sql, null);
			cursor.moveToLast();
			if (cursor.getCount() > 0) {
				int nameColumn = cursor.getColumnIndex("name");
				oneinfor.setName2(cursor.getString(nameColumn));
				oneinfor.setDeviceid(cursor.getString(cursor
						.getColumnIndex("deviceid")));
				oneinfor.setLocation(
						cursor.getDouble(cursor.getColumnIndex("weidu")),
						cursor.getDouble(cursor.getColumnIndex("jingdu")));
				oneinfor.setWeizhi(cursor.getString(cursor
						.getColumnIndex("weizhi")));
				oneinfor.setTime(cursor.getString(cursor.getColumnIndex("time")));

				if (cursor != null) {
					cursor.close();
				}
				if (dbService != null) {
					dbService.close();
				}
				return oneinfor;
			} else {
				return null;
			}

		}
		return null;
	}

	/**
	 * 删除指定的摔倒记录
	 * */
	public void deletesdinfor(peopinfor sdinfor, Context mcontext) {
		LocalDB dbService = new LocalDB(mcontext);
		String sql = "delete from sdinfors where deviceid='"
				+ sdinfor.getDeviceid() + "' and time='" + sdinfor.getTime()
				+ "'";
		dbService.execSQL(sql);
		if (dbService != null) {
			dbService.close();
		}
	}
}
