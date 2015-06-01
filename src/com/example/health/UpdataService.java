package com.example.health;

import java.util.List;

import com.example.health.util.BloodOx;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Administrator
 *广播监听到网络状态变化后，启动该服务，上传本地那些未上传的数据
 */
public class UpdataService extends IntentService{
	//IntentService集开启线程和自动停止于一身
	public UpdataService() {
		super("UpdataService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {		
		   SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", 0); 
	       int tempxueya = sharedPreferences.getInt("unSavedBP", 0);
	       String userid1 = sharedPreferences.getString("userIDBP", "");
	       int tempxueyang = sharedPreferences.getInt("unSavedBO", 0);
	       String userid2 = sharedPreferences.getString("userIDBO", "");
	       LogUtil.i("unsaved",tempxueya+":"+userid1);
	       if(tempxueya>0) {
	    	   BloodPre bloodpre = new BloodPre();
	    	   List<BloodPre> list1= (List<BloodPre>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodpre,userid1,tempxueya);
	    	   LogUtil.i("list.size",list1.size()+list1.get(0).toString());
	    	   
	    	   
	    	   for(int i = list1.size(); i>0; i--){
	    		   new NetTool().uploadData(list1.get(i-1));
	    	   }
	    	   
		        Editor editor = sharedPreferences.edit();
		        editor.putInt("unSavedBP", 0);
		        editor.commit(); 
	    	    MyApplication.unSavedBP = 0;
	       }
	       if(tempxueyang>0) {
	    	   BloodOx bloodox = new BloodOx();
	    	   List<BloodOx> list2= (List<BloodOx>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodox,userid2,tempxueyang);
	    	   LogUtil.i("list.size",list2.size()+list2.get(0).toString());
	    	   
	    	   
	    	   for(int j = list2.size(); j>0; j--){
	    		   new NetTool().uploadData(list2.get(j-1));
	    	   }
	    	   
		        Editor editor = sharedPreferences.edit();
		        editor.putInt("unSavedBO", 0);
		        editor.commit(); 
	    	    MyApplication.unSavedBO = 0;
	       }
	      
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i("UpdataService", "onDestroy executed");
	}

}
