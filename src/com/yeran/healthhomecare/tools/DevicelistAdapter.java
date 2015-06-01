package com.yeran.healthhomecare.tools;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.health.R;
import com.yeran.healthhomecare.data.ShuaidaoDeviceinfor;

public class DevicelistAdapter extends BaseAdapter{
	private ArrayList<ShuaidaoDeviceinfor> mList;
	private Context mContext;

	
	
	
	public DevicelistAdapter(Context pContext, ArrayList<ShuaidaoDeviceinfor> pList) {
		this.mContext = pContext;
		this.mList = pList;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(com.example.health.R.layout.map_devicelistitem, null);
        TextView deviceid=null,peopname = null,createtime = null;
  //      View toolbar=null;
  //      ImageButton treate=null;
        if(convertView!=null)
        {
        	deviceid=(TextView)convertView.findViewById(R.id.deviceid);
        	peopname=(TextView)convertView.findViewById(R.id.peopname);
        	createtime=(TextView)convertView.findViewById(R.id.createtime);
//        	treate=(ImageButton)convertView.findViewById(R.id.treate);
//        	 toolbar=convertView.findViewById(R.id.toolbar);
//        	 
//        	treate.setTag(position);
//        	 ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
//     		toolbar.setVisibility(View.GONE);
        }
        deviceid.setText(mList.get(position).deviceid);
        peopname.setText(mList.get(position).username);
        createtime.setText(mList.get(position).createtime);
        
       
       

        return convertView;
    }
	


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}


	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}


	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}  
}
