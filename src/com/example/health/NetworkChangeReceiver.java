package com.example.health;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.HealthData;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;

/**
 * @author Administrator
 * 用来监听网络状态的改变
 *
 */
public class NetworkChangeReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(NetTool.isInternetConnect()) {			  			
			/*Toast.makeText(context, "network is available",
					Toast.LENGTH_SHORT).show();*/
			Intent intentService = new Intent(context, UpdataService.class);
			context.startService(intentService);
		}else{
		/*	Toast.makeText(context, "network is unavailable",
					Toast.LENGTH_SHORT).show();	*/						
		}
				
	}
}
