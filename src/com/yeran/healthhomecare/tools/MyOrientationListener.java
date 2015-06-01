package com.yeran.healthhomecare.tools;

import android.content.Context;  
import android.hardware.Sensor;  
import android.hardware.SensorEvent;  
import android.hardware.SensorEventListener;  
import android.hardware.SensorManager; 

public class MyOrientationListener implements SensorEventListener {
	private Context context;  
    private SensorManager sensorManager;  
    private Sensor sensor;       
    private float lastX ;   
      
    private OnOrientationListener onOrientationListener ;
    public MyOrientationListener(Context context)  
    {  
        this.context = context;  
    }  
    
 // 锟斤拷始  
    @SuppressWarnings("deprecation")
	public void start()  
    {  
        // 锟斤拷么锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟� 
        sensorManager = (SensorManager) context  
                .getSystemService(Context.SENSOR_SERVICE);  
        if (sensorManager != null)  
        {  
            // 锟斤拷梅锟斤拷虼锟斤拷锟� 
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);  
        }  
        // 注锟斤拷  
        if (sensor != null)  
        {//SensorManager.SENSOR_DELAY_UI  
//            sensorManager.registerListener(this, sensor,  
//                    SensorManager.SENSOR_DELAY_UI);  
        	sensorManager.registerListener(this, sensor, 0);  
        }  
  
    }  

    public void stop()  
    {  
      sensorManager.unregisterListener(this);  
    }  
    
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)    
        {    

            float x = event.values[SensorManager.DATA_X];    
              
            if( Math.abs(x- lastX) > 1.0 )  
            {  
                onOrientationListener.onOrientationChanged(x);  
            }
            lastX = x ;   
              
        }    
	}
	
	 public void setOnOrientationListener(OnOrientationListener onOrientationListener)  
	    {  
	        this.onOrientationListener = onOrientationListener ;  
	    }  
	      
	      
	    public interface OnOrientationListener   
	    {  
	        void onOrientationChanged(float x);  
	    }  

}
