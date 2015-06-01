package com.yeran.healthhomecare.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.example.health.R;

/**
 * 正常模式下的老人列表以及位置信息获取类
 * */
public class Normaloldlocations {
	private List<Map<String, Object>> list;
	private ArrayList<oldsloctions> normalpeopleinfors = new ArrayList<oldsloctions>();

	/**
	 * 修正正常模式下的位置信息
	 * */
	public void setnormalLocations(oldsloctions onenormalpeopinfor) {

		for (oldsloctions normalitem : normalpeopleinfors) {
			if (normalitem.getDeviceid() == onenormalpeopinfor.getDeviceid()) {
				normalpeopleinfors.set(normalpeopleinfors.indexOf(normalitem),
						onenormalpeopinfor);
				break;
			}
		}

	}

	/**
	 * 获取摔倒设备
	 * */
	public ArrayList<oldsloctions> getDevices(Context mcontext) {
		ArrayList<oldsloctions> normalpeopleinfors2 = new ArrayList<oldsloctions>();

		peoplelist plist = peoplelist.getInstance();
		ArrayList<ShuaidaoDeviceinfor> devicelist = plist
				.getDevicelist(mcontext);
		oldsloctions peinfor;
		for (ShuaidaoDeviceinfor shdinfor : devicelist) {
			peinfor = new oldsloctions();
			peinfor.setName2(shdinfor.username);
			peinfor.setDeviceid(shdinfor.deviceid);
			peinfor.setPhone(shdinfor.oldphone);

			normalpeopleinfors2.add(peinfor);

		}
		for (oldsloctions peinfor2 : normalpeopleinfors2) {
			Log.v("devices=", peinfor2.getName());
		}
		return normalpeopleinfors2;
	}

	/**
	 * 获取地图页面所有的摔倒设备用户，目前是获取所有的，后期根据是否在线改变颜色。
	 * */
	public List<Map<String, Object>> getData(Context mcontext) {
		normalpeopleinfors.clear();
		peoplelist plist = peoplelist.getInstance();
		ArrayList<ShuaidaoDeviceinfor> devicelist = plist
				.getDevicelist(mcontext);
		oldsloctions peinfor;
		for (ShuaidaoDeviceinfor shdinfor : devicelist) {
			peinfor = new oldsloctions();
			peinfor.setName2(shdinfor.username);
			peinfor.setDeviceid(shdinfor.deviceid);
			peinfor.setPhone(shdinfor.oldphone);

			normalpeopleinfors.add(peinfor);
		}

		list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map;
		for (oldsloctions pepitem : normalpeopleinfors) {
			map = new HashMap<String, Object>();
			map.put("NAME", pepitem.getName());
			map.put("state", pepitem.heathstate);
			map.put("infors", pepitem);
			map.put("img", R.drawable.ic_launcher);
			list.add(map);
		}
		return list;

	}
}
