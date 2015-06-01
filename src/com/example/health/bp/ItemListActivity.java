package com.example.health.bp;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.UserListActivity;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;
import com.example.health.util.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;


public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private FragmentDetailOne fragment1;
    private FragmentDetailTwo fragment2;
    private FragmentDetailThree fragment3;
    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 2:	//从服务器下载数据成功						
			        	progressDialog.dismiss();			      
						break;
				case 20://从服务器下载数据失败
					   progressDialog.dismiss();
					   Toast.makeText(MyApplication.getContext(), "加载数据失败,稍后重试", Toast.LENGTH_SHORT).show();
					   break;
				default:break;
			}
		}
		
	};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
       /* WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
       Display display = windowManager.getDefaultDisplay();

       int width = display.getWidth();
       int height = display.getHeight();

       //得到设备的大小

        if(width > height) {
        	setContentView(R.layout.bp_activity_item_twopane);
        }
        else {
        	setContentView(R.layout.bp_activity_item_list);
        }
        */
        setContentView(R.layout.bp_activity_item_list);
        if (findViewById(R.id.item_detail_container) != null) {
     
            mTwoPane = true;
            
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
     
        ActionBar actionBar = getActionBar();  
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
   /* @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();  
		inflater.inflate(R.menu.bp_main, menu);  
		  
		return super.onCreateOptionsMenu(menu);
	}*/
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bloodpre, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_add30:			
		case R.id.action_add90:		
		case R.id.action_addall:
			if(NetTool.isInternetConnect()) {
			 progressDialog = new ProgressDialog(ItemListActivity.this);					
			 progressDialog.setMessage("正在加载数据，请稍候...");
			 progressDialog.setCancelable(false);
			 progressDialog.show();		
			new Thread(){
				@Override
				public void run() {
					BloodPre obj1 = new BloodPre();
					obj1.setUserid(MainActivity.userID);
					if(new NetTool().downloadData(obj1, 1)) {
						Gson gson = new Gson();
				        Type type = new TypeToken<List<BloodPre>>(){}.getType();
				        List<BloodPre>  bloodPreList = new ArrayList<BloodPre>();
				        bloodPreList = gson.fromJson(MyApplication.sDownBloodpre, type);								       						       								        
				        //创建数据库，保存用户信息到本地
				        if(GeneralDbHelper.getInstance(MyApplication.getContext()).deleteBloodPreTable()){
				        	 for (BloodPre bp  :  bloodPreList) { 
				                  GeneralDbHelper.getInstance(MyApplication.getContext()).save(bp);
				               }				
					    }else{
					    	   LogUtil.i("deleteBloodPreTable", "fail");
					    }
											
						mHandler.sendEmptyMessage(2);
				       
					}else{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mHandler.sendEmptyMessage(20);	
					}
				}
				
			}.start();
			
		}else {
			 Toast.makeText(MyApplication.getContext(), "请保持网络连接", Toast.LENGTH_SHORT).show();
		}
		
		return true;			
		default:return true;
		}
	}
	
	
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {

            Bundle arguments = new Bundle();
          
            switch(Integer.parseInt(id)) {
            case 1: 
            	  arguments.putString(FragmentDetailOne.ARG_ITEM_ID, id);
            		if(fragment1 == null) {
            			fragment1 = new FragmentDetailOne();
            			fragment1.setArguments(arguments);
            		}
                 
                   if(!fragment1.isAdded()) {
	                   getSupportFragmentManager().beginTransaction()
	                           .replace(R.id.item_detail_container, fragment1,"Detail")
	                           .commit();
                   }
            	break;
            case 2:
            	  arguments.putString(FragmentDetailOne.ARG_ITEM_ID, id);
            	if(fragment2 == null) {
            		fragment2 = new FragmentDetailTwo();
            		fragment2.setArguments(arguments);
        		}
               
                
                if(!fragment2.isAdded()) {
	                getSupportFragmentManager().beginTransaction()
	                        .replace(R.id.item_detail_container, fragment2,"Detai2")
	                        .commit();
                }
            	
            	break;
            case 3:
            	arguments.putString(FragmentDetailOne.ARG_ITEM_ID, id);
            	if(fragment3 == null) {
            		fragment3 = new FragmentDetailThree();
            		fragment3.setArguments(arguments);
        		}
                if(!fragment3.isAdded()){
                	 getSupportFragmentManager().beginTransaction()
                     .replace(R.id.item_detail_container, fragment3,"Detai3")
                     .commit();
                }
               
            	break;
            }
         

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(FragmentDetailOne.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

}
