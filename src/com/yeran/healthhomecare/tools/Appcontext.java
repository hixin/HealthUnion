package com.yeran.healthhomecare.tools;

import android.app.Activity;
import android.app.Application;

import com.example.health.MainActivity;
import com.yeran.healthhomecare.ItemListActivity;

/**
 * Activity 管理类
 * */
public class Appcontext extends Application {
	private Application parentApp;
	private Activity curActivity;

	public Application getAppContext() {
		return parentApp;
	}

	private static class LazyHolder {
		static Appcontext instance = new Appcontext();
	}

	public static Appcontext getInstance() {
		return LazyHolder.instance;
	}

	public void setCurrentActivity(Activity curActivity) {
		this.curActivity = curActivity;
	}

	public Activity getCurrentActivity() {
		return this.curActivity;
	}

	public boolean isStartActivity() {
		boolean isStartActivity = false;
		if (null != curActivity) {
			if (curActivity instanceof MainActivity) {
				isStartActivity = true;
			}
		}
		return isStartActivity;
	}

	public boolean isListActivity() {
		boolean isListActivity = false;
		if (null != curActivity) {
			if (curActivity instanceof ItemListActivity) {
				isListActivity = true;
			}
		}
		return isListActivity;
	}
}
