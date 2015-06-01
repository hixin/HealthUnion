package com.example.health;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.health.bo.FragmentDetailOneItemLeftBO;
import com.example.health.bo.FragmentDetailOneItemRightBO;
import com.example.health.bp.FragmentDetailOneItemLeft;
import com.example.health.bp.FragmentDetailOneItemRight;
import com.example.health.util.User;
import com.wiipu.voice.domain.VoiceConstant;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

public class MyApplication extends Application{

	private  static  Context context;
    public static List<Fragment> fragments=new ArrayList<Fragment>();
    public static List<Fragment> bo_fragments=new ArrayList<Fragment>();
    public List<User>  userList = new ArrayList<User>();
    public static String sData;
    public static String sAdduser;
    public static String sAdduserToast;
    public static String sDownBloodpre;
    public static String sDownBloodox;
    public boolean isFirstRun = true;
    public static String zhongduan_id;
    public static int unSavedBP;
    public static int unSavedBO;
    
	public static String centerurl = "219.245.64.1";
//	public static String centerurl = "219.245.64.94";
	public static String homeid = "11223344";
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
		SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", 0); 
		unSavedBP = sharedPreferences.getInt("unSavedBP", 0);
		zhongduan_id = sharedPreferences.getString("userID", "000000");
		fragments.add(new FragmentDetailOneItemLeft());
		fragments.add(new FragmentDetailOneItemRight()); 
		bo_fragments.add(new FragmentDetailOneItemLeftBO());
		bo_fragments.add(new FragmentDetailOneItemRightBO()); 
		
	}
	
	public static Context getContext() {
		return context;
	}

}

