package com.example.health.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.bp.ItemListActivity;

public class DataModel {
	/**
	 * @param currentTime 一开始以为要用到当前时间，查看最近7天的数据，可是如果最近7天没有测量，应该
	 * 取最后一天测量倒数7天的值
	 * function  数据库添加是按时间先后顺序的，最后一次添加的数据肯定会取到对应 mLatestTime
	 * mAvailableTime 取数据库的第一组数据，根据和最新数据之间的时间差值和space缩小数据范围
	 * 
	 * @return  
	 * 
	 */
	public int[] getGraphicalDataIndex(int space)  {
		BloodPre bloodpre = new BloodPre();
    	List<BloodPre> data = (ArrayList<BloodPre>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodpre,MainActivity.userID);
	    Date mCurrentTime, mLatestTime = null, mAvailableTime = null, mNextAvailableTime;	
	    String mCurrentTimeDay, mLatestTimeDay, mAvailableTimeDay = null, mNextAvailableTimeDay = null;
	    int size = data.size();
	    int[] graphicalIndex= new int[size];
	    LogUtil.i("data.size()",size+"");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfDay = new SimpleDateFormat("dd");
		if(size > 0) {
			try {
				mLatestTime = df.parse((String) data.get(size-1).getTime());
				mLatestTimeDay = dfDay.format(mLatestTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		if(size==0) {
			Toast.makeText(MyApplication.getContext(), "还没有数据呢", Toast.LENGTH_SHORT).show();
		}
		else if(size == 1) {
			graphicalIndex[0] = 1;
		}
		else {
			
			for(int i=0; i<size; i++) {

				   try {
					   mAvailableTime = df.parse((String) data.get(i).getTime());
					   mAvailableTimeDay = dfDay.format(mAvailableTime);
					   if(i+1 < size) {
						   mNextAvailableTime = df.parse((String) data.get(i+1).getTime());
						   mNextAvailableTimeDay = dfDay.format(mNextAvailableTime);
					   }
					   
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   
		           long l=mLatestTime.getTime()-mAvailableTime.getTime();
		           long day=l/(24*60*60*1000);
		           /*long hour=(l/(60*60*1000)-day*24);
		           long min=((l/(60*1000))-day*24*60-hour*60);
		           long s=(l/1000-day*24*60*60-hour*60*60-min*60);
				   System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");*/
		           if(day>space) {
		        	   continue;
		           }
		           else {
		        	   /*
		        	    * 天数相同有可能是不同月，同一天只取最新的一组数据
		        	   */
		        	   if(mAvailableTimeDay.equals(mNextAvailableTimeDay)) {
		        		   if(l>0) {
		        			   continue;
		        		   }
		        		   else {
		        			   graphicalIndex[i] = 1;
		        		   }
		        	   }
		        	   else {
		        		   graphicalIndex[i] = 1;
		        	   }
		        	   	        	  
		           }
		     }
        

	    }
		return graphicalIndex;			
	}
	
	public  List<BloodPre> getGraphicalData(int space)  {
		BloodPre bloodpre = new BloodPre();
    	List<BloodPre> data = (ArrayList<BloodPre>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodpre,MainActivity.userID);
		List<BloodPre>  GraphicalData = new ArrayList<BloodPre>();
		int[] index;
		int length;
		index = getGraphicalDataIndex(space);
		length = index.length;

		for(int i=0; i<length; i++) {
			if(index[i] == 1) {
				GraphicalData.add(data.get(i));
			}	
		}	
		LogUtil.i("GraphicalData.size()",GraphicalData.size()+"");
		return GraphicalData;		
	}
	
	
	/**
	 * 字符串各个元素转为int数组,这个方法只能按一位解析，例如3910，只能解析出 3，9，1，0 而不是3，9，10
	 * 
	 */
	static int[] str2IntArray(String str) {
		int len = str.length();
		int[] a = new int[len];
		char[] c = str.toCharArray();
		for(int i=0; i<len; i++) {
			a[i] = c[i]-'0';
		}	
		return a;	
	}

}
	

