package com.example.health.bp;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.bpdummy.DummyContent;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.bpchart.*;
/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentDetailTwo extends Fragment {
   
	private GraphicalView mChartView7;
	private GraphicalView mChartView30;
	private GraphicalView mChartViewAll;
    private LinearLayout layout;
    private LinearLayout layoutListTitle;
    private ListView  listView;
    private static final String TAG ="FragmentDetailTwo";
	/**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDetailTwo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));     
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.bp_fragment_item_detail2, container, false);
    	 LogUtil.i("position",getArguments().getString(ARG_ITEM_ID));
        if (mItem != null) {
 
         
        	layout = (LinearLayout)rootView.findViewById(R.id.detailfragment2); 
        	layoutListTitle = (LinearLayout)rootView.findViewById(R.id.list_title);
        	BloodPre bloodpre = new BloodPre();
        	List<BloodPre> data = (ArrayList<BloodPre>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodpre,MainActivity.userID);
        	
//        	mChartView7 = new AverageTemperatureChart().getView(getActivity(),7);
        	mChartView7 = new SalesStackedBarChart().getView(getActivity(),7);
        	mChartView30 = new SalesStackedBarChart().getView(getActivity(),30);
        	mChartViewAll = new SalesStackedBarChart().getView(getActivity());
        	LogUtil.i("2",TAG);
        	listView = (ListView) rootView.findViewById(R.id.data_list);
        	listView.setAdapter(new ListViewAdapter(data, getActivity()));
        	
            RadioGroup group1 = (RadioGroup)rootView.findViewById(R.id.radioGroup1);
            int radioButtonId = group1.getCheckedRadioButtonId();
            if(radioButtonId == R.id.radio0) {
            	
            	layout.removeView(mChartView7);
				layout.removeView(mChartView30);
				layout.removeView(mChartViewAll);
            	listView.setVisibility(View.GONE);
            	layoutListTitle.setVisibility(View.GONE);
            	layout.addView(mChartView7);
            }
            else if(radioButtonId == R.id.radio1) {
            	
            	layout.removeView(mChartView7);
				layout.removeView(mChartView30);
				layout.removeView(mChartViewAll);
            	listView.setVisibility(View.GONE);
            	layoutListTitle.setVisibility(View.GONE);
            	layout.addView(mChartView30);
            }
          
            else if(radioButtonId == R.id.radio3) {
            	
            	layout.removeView(mChartView7);
				layout.removeView(mChartView30);
				layout.removeView(mChartViewAll);
            	listView.setVisibility(View.GONE);
            	layoutListTitle.setVisibility(View.GONE);
            	layout.addView(mChartViewAll);
            }
            else if(radioButtonId == R.id.radio4)
            {
            	layout.removeView(mChartView7);
				layout.removeView(mChartView30);
				layout.removeView(mChartViewAll);
				layoutListTitle.setVisibility(View.VISIBLE);
            	listView.setVisibility(View.VISIBLE);  	
            }
            
            group1.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
    			@Override
    			public void onCheckedChanged(RadioGroup group, int checkedId) {
    				
    				/*RadioButton rb = (RadioButton)getActivity().findViewById(radioButtonId);
    				Toast.makeText(MyApplication.getContext(), rb.getText(), Toast.LENGTH_SHORT).show();*/
    				if(checkedId == R.id.radio4) {
    					layout.removeView(mChartView7);
    					layout.removeView(mChartView30);
    					layout.removeView(mChartViewAll);
    					layoutListTitle.setVisibility(View.VISIBLE);
    					listView.setVisibility(View.VISIBLE);
    				}
    				else if (checkedId == R.id.radio0) {
    					
    					layout.removeView(mChartView7);
    					layout.removeView(mChartView30);
    					layout.removeView(mChartViewAll);
    					listView.setVisibility(View.GONE);
    					layoutListTitle.setVisibility(View.GONE);
    					/*
    					 * 添加钱先要执行删除操作
    					 * 04-24 11:22:48.754: E/AndroidRuntime(24004): java.lang.IllegalStateException: 
    					 * The specified child already has a parent. You must call removeView() on the child's parent first.
    					*/
    					layout.addView(mChartView7);
    				}
    				else if (checkedId == R.id.radio1) {
    					
    					layout.removeView(mChartView7);
    					layout.removeView(mChartView30);
    					layout.removeView(mChartViewAll);
    					listView.setVisibility(View.GONE);
    					layoutListTitle.setVisibility(View.GONE);
    					/*
    					 * 添加钱先要执行删除操作
    					 * 04-24 11:22:48.754: E/AndroidRuntime(24004): java.lang.IllegalStateException: 
    					 * The specified child already has a parent. You must call removeView() on the child's parent first.
    					*/
    					layout.addView(mChartView30);
    				} 			
    				else if (checkedId == R.id.radio3) {
    					layout.removeView(mChartView7);
    					layout.removeView(mChartView30);
    		
    					layout.removeView(mChartViewAll);
    					listView.setVisibility(View.GONE);
    					layoutListTitle.setVisibility(View.GONE);
    					/*
    					 * 添加钱先要执行删除操作
    					 * 04-24 11:22:48.754: E/AndroidRuntime(24004): java.lang.IllegalStateException: 
    					 * The specified child already has a parent. You must call removeView() on the child's parent first.
    					*/
    					layout.addView(mChartViewAll);
    				}
    				
    			}
    		});
        }
 
        return rootView;
        
    }
    
    
    class ListViewAdapter extends BaseAdapter{
    	LayoutInflater inflater;
    	List<BloodPre> list;
    	
    	public ListViewAdapter(List<BloodPre> list,Context context){
    		this.list = list;
    		inflater = LayoutInflater.from(context);
    	}
    	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
				
			 View view = inflater.inflate(R.layout.bp_data_list, null);
			 
             TextView tvDate = (TextView)view.findViewById(R.id.item_date);
             tvDate.setText((CharSequence) list.get(position).getTime());
             
             TextView tvPre = (TextView)view.findViewById(R.id.item_pressure);
             tvPre.setText(list.get(position).getHighp()+"/"+list.get(position).getLowp());
             
             TextView tvPulse = (TextView)view.findViewById(R.id.item_pulse);
             tvPulse.setText(list.get(position).getPulse()+"");
         return view;
		}
    	
    }
    
    
  
   
}
