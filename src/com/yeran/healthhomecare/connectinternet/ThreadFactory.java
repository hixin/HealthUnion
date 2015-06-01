package com.yeran.healthhomecare.connectinternet;

import android.content.Context;

public class ThreadFactory {
	
	private  Context mcontext;
	
	public  void setContext(Context context){
		this.mcontext = context;
	}
	
	public  Threadinterface getThread(int condition){
		Threadinterface tif = null;
		if(condition==1){
			tif= new ConnIntentdata(mcontext);
		}if(condition==2){
			tif = new Deviceaddorchange(mcontext);
		}
		return tif;
	}

}
