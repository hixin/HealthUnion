package com.example.health;

import java.util.List;

import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.User;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class WelComeActivity extends Activity {

	   private static final int GOTO_Login_ACTIVITY = 0;
	   private static final int GOTO_MAIN_ACTIVITY = 1;
	   private MyApplication app;
	    private Handler mHandler = new Handler(){
	        public void handleMessage(android.os.Message msg) {
	            switch (msg.what) {
	                case GOTO_Login_ACTIVITY:
	                    Intent intent1 = new Intent();
	                    intent1.setClass(WelComeActivity.this, LoginActivity.class);
	                    startActivity(intent1);
	                    finish();
	                    break;
	                case GOTO_MAIN_ACTIVITY:
	                    Intent intent = new Intent();
	                    intent.setClass(WelComeActivity.this, UserListActivity.class);
	                    startActivity(intent);
	                    finish();
	                    break;
	                default:
	                    break;
	            }
	        };
	    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcom);
        /*推测大头尺寸：1024*600 xdpi160 ydpi160  因为运行Android 4.0以上系统的平板电脑，
         * 不root不会隐藏屏幕下方的状态栏，所以得到的高度应该加上状态栏的高度
        05-13 09:46:20.830: I/MainActivity(25278): xwidth is 1024
        05-13 09:46:20.830: I/MainActivity(25278): ywidth is 552
        05-13 09:46:20.830: I/MainActivity(25278): xdpi is 159.5681
        05-13 09:46:20.830: I/MainActivity(25278): ydpi is 160.42105*/
       /* float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        int xpx = getResources().getDisplayMetrics().widthPixels;
        int ypx = getResources().getDisplayMetrics().heightPixels;
        LogUtil.i("MainActivity", "xwidth is " + xpx);
        LogUtil.i("MainActivity", "ywidth is " + ypx);
        LogUtil.i("MainActivity", "xdpi is " + xdpi);
        LogUtil.i("MainActivity", "ydpi is " + ydpi);*/
        app = (MyApplication) getApplication();
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", MODE_PRIVATE); 
        app.isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if(app.isFirstRun) {
        	mHandler.sendEmptyMessageDelayed(GOTO_Login_ACTIVITY, 2000);//2秒跳转
        }else {
        	 mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 2000);//2秒跳转
        	 //提前将数据从数据库中取出
        	 User user = new User();
        	 app.userList = (List<User>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(user);
        	
        }
       
    }
 
}