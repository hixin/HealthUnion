package com.yeran.healthhomecare.tools;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.health.R;
import com.yeran.healthhomecare.data.peopinfor;

public class ShuaidaolistAdapter extends BaseAdapter {
	private List<peopinfor> mList;
	private Context mContext;

	public ShuaidaolistAdapter(Context pContext, List<peopinfor> peoplist) {
		this.mContext = pContext;
		this.mList = peoplist;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		TextView Id = null, peoplename = null, peopweizhi = null, peoptime = null;
		if (convertView == null) {
			convertView = _LayoutInflater.inflate(R.layout.map_historyitemlayout, null);
			
			// 使用缓存的view,节约内存
			// 当listview的item过多时，拖动会遮住一部分item，被遮住的item的view就是convertView保存着。
			// 当滚动条回到之前被遮住的item时，直接使用convertView，而不必再去new view()

			// 摔倒数据加载
			
		}
		Id = (TextView) convertView.findViewById(R.id.id);
		peoplename = (TextView) convertView.findViewById(R.id.peopname);
		peopweizhi = (TextView) convertView.findViewById(R.id.peopweizhi);
		peoptime = (TextView) convertView.findViewById(R.id.peoptime);
		
		Id.setText(position+""); 
		peoplename.setText(mList.get(position).getName());
		peopweizhi.setText(mList.get(position).getWeizhi());
		peoptime.setText(mList.get(position).getTime());		

		int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };// RGB颜色

		convertView.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同

		return convertView;
	}

}
