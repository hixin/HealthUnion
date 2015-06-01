package com.yeran.healthhomecare.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.health.R;
import com.yeran.healthhomecare.data.oldsloctions;

public class PeoplelistAdapter extends BaseAdapter{
	
	private List<oldsloctions> mList;
    private Context mContext;
 
    public PeoplelistAdapter(Context pContext, ArrayList<oldsloctions> peoplist2) {
        this.mContext = pContext;
        this.mList = peoplist2;
    }
 
    @Override
    public int getCount() {
        return mList.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.map_historyitem, null);
        if(convertView!=null)
        {
            TextView _TextView1=(TextView)convertView.findViewById(R.id.spinneritem);
          
            _TextView1.setText(mList.get(position).getName());
        //    _TextView2.setText(mList.get(position).getPersonAddress());
        }
        return convertView;
    }
}
