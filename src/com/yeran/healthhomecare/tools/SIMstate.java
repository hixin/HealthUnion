package com.yeran.healthhomecare.tools;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMstate {
	private int SIMstate1;
	private TelephonyManager  telemanger;
	private static SIMstate sim=null;
	private String TELEPHONY_SERVICE="phone";
	
	private SIMstate(){}
	
	public static SIMstate getInstance(){
		if (sim==null) {
			sim=new SIMstate();
		}
		return sim;
	}
	
	public int getSIMstate(Context context){
		telemanger=(TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
		if (telemanger.getSimState() == TelephonyManager.SIM_STATE_READY) {  
			SIMstate1=1;
        } else if (telemanger.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {  
        	SIMstate1=0; 
        } else {  
        	SIMstate1=-1; 
        }  
		return SIMstate1;
	}

}
